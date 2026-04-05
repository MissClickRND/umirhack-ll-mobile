package bob.colbaskin.umir_hack_2.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bob.colbaskin.umir_hack_2.auth.domain.AuthRepository
import bob.colbaskin.umir_hack_2.common.ApiResult
import bob.colbaskin.umir_hack_2.common.toUiState
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.models.User
import bob.colbaskin.umir_hack_2.profile.domain.ProfileRepository
import bob.colbaskin.umir_hack_2.profile.domain.models.*
import bob.colbaskin.umir_hack_2.profile.presentation.components.share_link.ShareLinkBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val shareLinkBuilder: ShareLinkBuilder
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private val effectsChannel = Channel<ProfileEffect>(Channel.BUFFERED)
    val effects = effectsChannel.receiveAsFlow()

    private data class PendingToken(
        val tempId: String,
        val diplomaId: Long,
        val duration: DiplomaShareDuration,
        val tokenString: String,
        val createdAt: Instant
    )

    private val pendingByTempId = mutableMapOf<String, PendingToken>()
    private val tokenIdToTokenString = mutableMapOf<Long, String>()

    private var currentUser: User? = null
    private var loadJob: Job? = null
    private var generateJob: Job? = null

    init {
        loadAll()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.LoadUser -> loadAll()
            ProfileAction.RefreshAll -> loadAll()
            ProfileAction.Logout -> logout()

            ProfileAction.SelectMyDiplomasTab -> state = state.copy(selectedTab = ProfileTab.MyDiplomas)
            ProfileAction.SelectActiveLinksTab -> state = state.copy(selectedTab = ProfileTab.ActiveLinks)
            ProfileAction.SelectHistoryTab -> state = state.copy(selectedTab = ProfileTab.History)
            is ProfileAction.PagerPageChanged -> onPagerChanged(action.page)

            is ProfileAction.OpenShareSheet -> openShareSheet(action.diplomaId)
            ProfileAction.CloseShareSheet -> closeShareSheet()
            is ProfileAction.SelectShareDuration -> selectDuration(action.duration)
            ProfileAction.GenerateShareLink -> generateShareLink()
            ProfileAction.ConfirmShareSheet -> confirmShareSheet()

            is ProfileAction.CopyLink -> copyLink(action.value)
            is ProfileAction.ShareLink -> shareLink(action.value)

            is ProfileAction.ShowQrFullscreen -> state = state.copy(fullscreenQrLink = action.link)
            ProfileAction.HideQrFullscreen -> state = state.copy(fullscreenQrLink = null)

            is ProfileAction.RevokeLink -> revokeToken(action.tokenId)

            is ProfileAction.UpdateAttachDiplomaId ->
                state = state.copy(attachDiplomaIdText = action.text)

            ProfileAction.AttachDiplomaToMe -> attachDiplomaToMe()
        }
    }

    private fun loadAll() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            state = state.copy(isLoadingContent = true)

            when (val userRes = profileRepository.getUser()) {
                is ApiResult.Success -> {
                    currentUser = userRes.data
                    state = state.copy(userState = userRes.toUiState())
                }

                is ApiResult.Error -> {
                    state = state.copy(
                        userState = userRes.toUiState(),
                        isLoadingContent = false
                    )
                    return@launch
                }
            }

            val user = currentUser ?: run {
                state = state.copy(isLoadingContent = false)
                return@launch
            }

            val userId = user.id.toLong()

            val diplomas = when (val diplomasRes = profileRepository.getUserDiplomas(userId)) {
                is ApiResult.Success -> diplomasRes.data.map { it.toProfileDiplomaUi() }
                is ApiResult.Error -> state.diplomas
            }

            val tokens = when (val tokensRes = profileRepository.getUserTokens(userId)) {
                is ApiResult.Success -> tokensRes.data
                is ApiResult.Error -> emptyList()
            }

            val rebuilt = rebuildLinksAndHistory(
                diplomas = diplomas,
                tokenGroups = tokens
            )

            state = state.copy(
                isLoadingContent = false,
                diplomas = diplomas,
                activeLinks = rebuilt.activeLinks,
                history = rebuilt.history
            )
        }
    }

    private fun onPagerChanged(page: Int) {
        val tab = when (page) {
            0 -> ProfileTab.MyDiplomas
            1 -> ProfileTab.ActiveLinks
            2 -> ProfileTab.History
            else -> return
        }
        if (state.selectedTab != tab) state = state.copy(selectedTab = tab)
    }

    private fun openShareSheet(diplomaId: Long) {
        generateJob?.cancel()
        generateJob = null

        state = state.copy(
            isShareSheetVisible = true,
            selectedDiplomaIdForSharing = diplomaId,
            selectedDuration = DiplomaShareDuration.DAYS_30,
            isGeneratingLink = false,
            generatedLink = null
        )
    }

    private fun closeShareSheet() {
        generateJob?.cancel()
        generateJob = null
        state = state.copy(
            isShareSheetVisible = false,
            selectedDiplomaIdForSharing = null,
            isGeneratingLink = false,
            generatedLink = null
        )
    }

    private fun selectDuration(duration: DiplomaShareDuration) {
        state = state.copy(
            selectedDuration = duration,
            generatedLink = null
        )
    }

    private fun generateShareLink() {
        val user = currentUser ?: run {
            sendEffect(ProfileEffect.ShowSnackbar("Нет пользователя"))
            return
        }
        val diplomaId = state.selectedDiplomaIdForSharing ?: return
        val duration = state.selectedDuration
        val userId = user.id.toLong()

        generateJob?.cancel()
        generateJob = viewModelScope.launch {
            state = state.copy(isGeneratingLink = true, generatedLink = null)

            val tokenString = when (
                val createRes = profileRepository.createQrToken(
                    diplomaId = diplomaId,
                    type = duration.toQrTokenType()
                )
            ) {
                is ApiResult.Success -> createRes.data
                is ApiResult.Error -> {
                    state = state.copy(isGeneratingLink = false)
                    sendEffect(ProfileEffect.ShowSnackbar("Не удалось создать ссылку"))
                    return@launch
                }
            }

            val now = Instant.now()
            val shareUrl = shareLinkBuilder.build(tokenString)

            state = state.copy(
                isGeneratingLink = false,
                generatedLink = DiplomaShareLink(
                    tokenId = -1L,
                    diplomaId = diplomaId,
                    tokenString = tokenString,
                    shareUrl = shareUrl,
                    createdAt = now,
                    expiresAt = duration.estimateExpiresAt(now),
                    revokedAt = null,
                    isOneTime = (duration == DiplomaShareDuration.ONETIME),
                    lastUsedAt = null
                )
            )

            val fetch = fetchTokensUntilContainsToken(
                userId = userId,
                tokenString = tokenString,
                maxRetries = 3,
                baseDelayMs = 250L
            )

            fetch?.tokens?.let { tokens ->
                val rebuilt = rebuildLinksAndHistory(state.diplomas, tokens)
                state = state.copy(activeLinks = rebuilt.activeLinks, history = rebuilt.history)
            }

            val matched = fetch?.matched
            if (matched == null) {
                sendEffect(
                    ProfileEffect.ShowSnackbar(
                        "Токен создан, но ещё не появился в списке. Попробуйте обновить профиль."
                    )
                )
                return@launch
            }

            state.generatedLink?.let { gl ->
                if (gl.tokenString == tokenString) {
                    state = state.copy(
                        generatedLink = gl.copy(
                            tokenId = matched.tokenMeta.tokenId,
                            createdAt = matched.tokenMeta.createdAt,
                            expiresAt = matched.tokenMeta.expiresAt,
                            revokedAt = matched.tokenMeta.revokedAt,
                            isOneTime = matched.tokenMeta.isOneTime,
                            lastUsedAt = matched.tokenMeta.lastUsedAt
                        )
                    )
                }
            }

            state = state.copy(
                selectedTab = ProfileTab.ActiveLinks,
                isShareSheetVisible = false,
                selectedDiplomaIdForSharing = null
            )
        }
    }

    private suspend fun fetchTokensUntilContainsToken(
        userId: Long,
        tokenString: String,
        maxRetries: Int,
        baseDelayMs: Long
    ): TokensFetchResult? {
        var attempt = 0
        var delayMs = baseDelayMs
        var lastTokens: List<DiplomaTokens>? = null

        while (attempt <= maxRetries) {
            val tokens = when (val res = profileRepository.getUserTokens(userId)) {
                is ApiResult.Success -> res.data
                is ApiResult.Error -> null
            }

            if (tokens != null) {
                lastTokens = tokens
                val matched = tokens.firstOrNull { it.token == tokenString }
                if (matched != null) return TokensFetchResult(tokens = tokens, matched = matched)
            }

            if (attempt == maxRetries) break
            delay(delayMs)
            delayMs *= 2
            attempt++
        }

        return lastTokens?.let { TokensFetchResult(tokens = it, matched = null) }
    }

    private suspend fun refreshTokensOnly(userId: Long) {
        val diplomas = state.diplomas
        val tokensGroups = when (val res = profileRepository.getUserTokens(userId)) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> {
                sendEffect(ProfileEffect.ShowSnackbar("Не удалось обновить токены"))
                return
            }
        }
        val rebuilt = rebuildLinksAndHistory(diplomas, tokensGroups)
        state = state.copy(activeLinks = rebuilt.activeLinks, history = rebuilt.history)
    }

    private fun confirmShareSheet() {
        state = state.copy(
            selectedTab = ProfileTab.ActiveLinks,
            isShareSheetVisible = false,
            selectedDiplomaIdForSharing = null,
            generatedLink = null
        )
    }

    private fun revokeToken(tokenId: Long) {
        val user = currentUser ?: return
        val userId = user.id.toLong()

        viewModelScope.launch {
            when (val res = profileRepository.revokeQrToken(tokenId)) {
                is ApiResult.Success -> {
                    sendEffect(ProfileEffect.ShowSnackbar("Ссылка отозвана"))
                    refreshTokensOnly(userId)
                }
                is ApiResult.Error -> {
                    sendEffect(ProfileEffect.ShowSnackbar("Не удалось отозвать ссылку"))
                }
            }
        }
    }

    private fun copyLink(value: String) {
        sendEffect(ProfileEffect.CopyToClipboard(label = "Ссылка на диплом", value = value))
        sendEffect(ProfileEffect.ShowSnackbar("Скопировано"))
    }

    private fun shareLink(value: String) {
        sendEffect(ProfileEffect.ShareText(value))
    }

    private fun attachDiplomaToMe() {
        val user = currentUser ?: return

        val diplomaId = state.attachDiplomaIdText.toLongOrNull()
        if (diplomaId == null) {
            sendEffect(ProfileEffect.ShowSnackbar("Введите корректный ID диплома"))
            return
        }

        viewModelScope.launch {
            state = state.copy(isAttachingDiploma = true)
            when (val res = profileRepository.attachDiplomaToMe(diplomaId)) {
                is ApiResult.Success -> {
                    sendEffect(ProfileEffect.ShowSnackbar("Диплом привязан"))
                    state = state.copy(isAttachingDiploma = false, attachDiplomaIdText = "")
                    loadAll()
                }
                is ApiResult.Error -> {
                    state = state.copy(isAttachingDiploma = false)
                    sendEffect(ProfileEffect.ShowSnackbar("Не удалось привязать диплом"))
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            when (val res = authRepository.logout()) {
                is ApiResult.Success -> {
                    pendingByTempId.clear()
                    tokenIdToTokenString.clear()
                    sendEffect(ProfileEffect.NavigateToDiplomaCheck)
                }
                is ApiResult.Error -> {
                    pendingByTempId.clear()
                    tokenIdToTokenString.clear()
                    sendEffect(ProfileEffect.ShowSnackbar(
                        "Logout не подтвердился сервером, но вы вышли локально"
                    ))
                    sendEffect(ProfileEffect.NavigateToDiplomaCheck)
                }
            }
        }
    }

    private data class RebuiltLists(
        val activeLinks: List<DiplomaShareLink>,
        val history: List<DiplomaLinkHistoryItem>
    )

    private fun rebuildLinksAndHistory(
        diplomas: List<ProfileDiploma>,
        tokenGroups: List<DiplomaTokens>
    ): RebuiltLists {
        val titleByDiplomaId = diplomas.associateBy({ it.id }, { it.title })

        val active = mutableListOf<DiplomaShareLink>()
        val history = mutableListOf<DiplomaLinkHistoryItem>()

        tokenGroups.forEach { item ->
            val meta = item.tokenMeta
            val diplomaId = item.diploma.id
            val diplomaTitle = titleByDiplomaId[diplomaId] ?: item.diploma.toProfileDiplomaUi().title

            val tokenString = item.token
            val shareUrl = shareLinkBuilder.build(tokenString)

            val link = DiplomaShareLink(
                tokenId = meta.tokenId,
                diplomaId = diplomaId,
                tokenString = tokenString,
                shareUrl = shareUrl,
                createdAt = meta.createdAt,
                expiresAt = meta.expiresAt,
                revokedAt = meta.revokedAt,
                isOneTime = meta.isOneTime,
                lastUsedAt = meta.lastUsedAt
            )

            if (link.isActive) {
                active += link
            } else {
                val status = when {
                    meta.revokedAt != null -> HistoryStatus.REVOKED
                    meta.isOneTime && meta.lastUsedAt != null -> HistoryStatus.USED_ONETIME
                    else -> HistoryStatus.EXPIRED
                }
                val endedAt = meta.revokedAt ?: meta.lastUsedAt ?: meta.expiresAt
                history += DiplomaLinkHistoryItem(
                    tokenId = meta.tokenId,
                    diplomaId = diplomaId,
                    diplomaTitle = diplomaTitle,
                    shareUrl = shareUrl,
                    createdAt = meta.createdAt,
                    endedAt = endedAt,
                    status = status
                )
            }
        }

        return RebuiltLists(
            activeLinks = active.sortedByDescending { it.createdAt },
            history = history.sortedByDescending { it.createdAt }
        )
    }

    private suspend fun matchTokenIdWithRetry(
        userId: Long,
        pending: PendingToken,
        maxRetries: Int,
        baseDelayMs: Long
    ): QrTokenMeta? {
        var attempt = 0
        var delayMs = baseDelayMs

        while (attempt <= maxRetries) {
            val tokensGroups = when (val res = profileRepository.getUserTokens(userId)) {
                is ApiResult.Success -> res.data
                is ApiResult.Error -> null
            }

            if (tokensGroups != null) {
                val candidateMeta = findBestMatch(tokensGroups, pending)
                if (candidateMeta != null) return candidateMeta
            }

            if (attempt == maxRetries) break
            delay(delayMs)
            delayMs *= 2
            attempt++
        }

        return null
    }

    private fun findBestMatch(
        tokenGroups: List<DiplomaTokens>,
        pending: PendingToken
    ): QrTokenMeta? {
        val candidates = tokenGroups
            .filter { item ->
                item.diploma.id == pending.diplomaId &&
                        item.tokenMeta.revokedAt == null
            }
            .map { item -> item.tokenMeta }

        val expected = pending.duration

        val filtered = candidates.filter { meta ->
            when (expected) {
                DiplomaShareDuration.ONETIME ->
                    meta.isOneTime && meta.expiresAt == null

                DiplomaShareDuration.INFINITE ->
                    !meta.isOneTime && meta.expiresAt == null

                DiplomaShareDuration.DAYS_7 ->
                    !meta.isOneTime && meta.expiresAt != null && meta.isRoughlyDays(7)

                DiplomaShareDuration.DAYS_30 ->
                    !meta.isOneTime && meta.expiresAt != null && meta.isRoughlyDays(30)
            }
        }

        return filtered.minByOrNull { meta ->
            abs(meta.createdAt.toEpochMilli() - pending.createdAt.toEpochMilli())
        }
    }

    private fun QrTokenMeta.isRoughlyDays(days: Long): Boolean {
        val exp = expiresAt ?: return false
        val d = Duration.between(createdAt, exp).toDays()
        return d == days
    }

    private fun DiplomaShareDuration.estimateExpiresAt(createdAt: Instant): Instant? {
        return when (this) {
            DiplomaShareDuration.ONETIME -> null
            DiplomaShareDuration.INFINITE -> null
            DiplomaShareDuration.DAYS_7 -> createdAt.plusSeconds(7L * 24 * 60 * 60)
            DiplomaShareDuration.DAYS_30 -> createdAt.plusSeconds(30L * 24 * 60 * 60)
        }
    }

    private fun sendEffect(effect: ProfileEffect) {
        viewModelScope.launch { effectsChannel.send(effect) }
    }

    private fun DiplomaShareDuration.toQrTokenType(): QrTokenType {
        return when (this) {
            DiplomaShareDuration.ONETIME -> QrTokenType.ONETIME
            DiplomaShareDuration.DAYS_7 -> QrTokenType.DAYS_7
            DiplomaShareDuration.DAYS_30 -> QrTokenType.DAYS_30
            DiplomaShareDuration.INFINITE -> QrTokenType.INFINITE
        }
    }
}

private data class TokensFetchResult(
    val tokens: List<DiplomaTokens>,
    val matched: DiplomaTokens?
)
