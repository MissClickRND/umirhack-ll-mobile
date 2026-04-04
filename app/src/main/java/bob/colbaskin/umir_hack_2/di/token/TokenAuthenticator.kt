package bob.colbaskin.umir_hack_2.di.token

import android.util.Log
import bob.colbaskin.umir_hack_2.auth.domain.AuthRepository
import bob.colbaskin.umir_hack_2.common.ApiResult
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

private const val TAG = "Auth"

class TokenAuthenticator @Inject constructor(
    private val authRepository: Provider<AuthRepository>,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val path = response.request.url.encodedPath

        if (
            path.contains("/auth/login") ||
            path.contains("/auth/register") ||
            path.contains("/auth/refresh")
        ) {
            Log.d(TAG, "Skip auth refresh for $path")
            return null
        }

        if (responseCount(response) >= 2) {
            Log.d(TAG, "Too many retries for $path")
            return null
        }

        Log.d(TAG, "Unauthorized. Attempting to refresh token")

        val refreshResult = runBlocking {
            authRepository.get().refresh()
        }

        return if (refreshResult is ApiResult.Success<*>) {
            Log.d(TAG, "Refresh successful. Retrying original request")
            response.request.newBuilder().build()
        } else {
            Log.d(TAG, "Refresh failed")
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}

