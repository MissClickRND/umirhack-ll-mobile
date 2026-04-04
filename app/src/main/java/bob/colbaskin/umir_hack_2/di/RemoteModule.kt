package bob.colbaskin.umir_hack_2.di

import android.content.Context
import android.util.Log
import bob.colbaskin.umir_hack_2.BuildConfig
import bob.colbaskin.umir_hack_2.auth.domain.AuthRepository
import bob.colbaskin.umir_hack_2.di.token.TokenAuthenticator
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        authRepository: Provider<AuthRepository>
    ): TokenAuthenticator {
        return TokenAuthenticator(authRepository = authRepository)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient {
        val cookieJar = PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(context)
        )

        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .authenticator(tokenAuthenticator)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("Cookies", "Sending cookies: ${request.headers["Cookie"]}")
                val response = chain.proceed(request)
                Log.d("Cookies", "Received cookies: ${response.headers["Set-Cookie"]}")
                response
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val jsonConfig = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
