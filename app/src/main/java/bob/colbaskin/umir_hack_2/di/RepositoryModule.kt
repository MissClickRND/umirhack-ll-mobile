package bob.colbaskin.umir_hack_2.di

import bob.colbaskin.umir_hack_2.auth.data.AuthRepositoryImpl
import bob.colbaskin.umir_hack_2.auth.domain.AuthApiService
import bob.colbaskin.umir_hack_2.auth.domain.AuthRepository
import bob.colbaskin.umir_hack_2.common.user_prefs.data.UserPreferencesRepositoryImpl
import bob.colbaskin.umir_hack_2.common.user_prefs.data.datastore.UserDataStore
import bob.colbaskin.umir_hack_2.common.user_prefs.domain.UserPreferencesRepository
import bob.colbaskin.umir_hack_2.profile.data.ProfileRepositoryImpl
import bob.colbaskin.umir_hack_2.profile.domain.ProfileRepository
import bob.colbaskin.umir_hack_2.profile.domain.ProfileService
import bob.colbaskin.umir_hack_2.scanner.data.remote.DocumentApi
import bob.colbaskin.umir_hack_2.scanner.data.remote.ScannerRepositoryImpl
import bob.colbaskin.umir_hack_2.scanner.domain.ScannerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: UserDataStore): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApiService,
        userPreferences: UserPreferencesRepository
    ): AuthRepository {
        return AuthRepositoryImpl(
            authApi = authApi,
            userPreferences = userPreferences
        )
    }

    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository (profileApi: ProfileService): ProfileRepository {
        return ProfileRepositoryImpl(profileApi = profileApi)
    }

    @Provides
    @Singleton
    fun provideDocumentApi(retrofit: Retrofit): DocumentApi {
        return retrofit.create(DocumentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideScannerRepository (
        documentApi: DocumentApi
    ): ScannerRepository {
        return ScannerRepositoryImpl(
            documentApi = documentApi
        )
    }
}
