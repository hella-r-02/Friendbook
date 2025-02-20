package com.src.book.di

import android.content.Context
import com.src.book.data.remote.NetworkInterceptor
import com.src.book.data.remote.dataSource.author.AuthorDataSource
import com.src.book.data.remote.dataSource.author.AuthorDataSourceImpl
import com.src.book.data.remote.dataSource.book.BookDataSource
import com.src.book.data.remote.dataSource.book.BookDataSourceImpl
import com.src.book.data.remote.dataSource.friend.FriendDataSource
import com.src.book.data.remote.dataSource.friend.FriendDataSourceImpl
import com.src.book.data.remote.dataSource.login.LoginDataSource
import com.src.book.data.remote.dataSource.login.LoginDataSourceImpl
import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.data.remote.dataSource.user.UserDataSourceImpl
import com.src.book.data.remote.model.author.author.AuthorMapper
import com.src.book.data.remote.model.author.authorList.AuthorListMapper
import com.src.book.data.remote.model.book.book.BookMapper
import com.src.book.data.remote.model.book.bookList.BookListMapper
import com.src.book.data.remote.model.friend.friend.FriendMapper
import com.src.book.data.remote.model.friend.request.FriendRequestMapper
import com.src.book.data.remote.model.genre.GenreMapper
import com.src.book.data.remote.model.login.login.LoginMapper
import com.src.book.data.remote.model.tag.TagMapper
import com.src.book.data.remote.model.user.userProfile.UserProfileMapper
import com.src.book.data.remote.service.*
import com.src.book.data.remote.session.SessionStorage
import com.src.book.data.remote.session.SessionStorageImpl
import com.src.book.data.remote.utils.TokenInterceptor
import com.src.book.utils.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideNetworkInterceptor(context: Context): NetworkInterceptor {
        return NetworkInterceptor(context)
    }

    @Singleton
    @Provides
    @Named(NAME_OKHTTP_WITHOUT_TOKEN)
    fun provideOkHttpClient(
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addNetworkInterceptor(networkInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

    @Singleton
    @Provides
    @Named(NAME_RETROFIT_WITHOUT_TOKEN)
    fun provideRetrofit(@Named(NAME_OKHTTP_WITHOUT_TOKEN) okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    @Named(NAME_OKHTTP_WITH_TOKEN)
    fun provideOkHttpClientToken(
        networkInterceptor: NetworkInterceptor,
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addNetworkInterceptor(networkInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

    @Singleton
    @Provides
    @Named(NAME_RETROFIT_WITH_TOKEN)
    fun provideRetrofitToken(@Named(NAME_OKHTTP_WITH_TOKEN) okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideBookService(@Named(NAME_RETROFIT_WITHOUT_TOKEN) retrofit: Retrofit): BookService {
        return retrofit.create(BookService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthorService(@Named(NAME_RETROFIT_WITHOUT_TOKEN) retrofit: Retrofit): AuthorService {
        return retrofit.create(AuthorService::class.java)
    }

    @Singleton
    @Provides
    fun provideReviewService(@Named(NAME_RETROFIT_WITHOUT_TOKEN) retrofit: Retrofit): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(@Named(NAME_RETROFIT_WITH_TOKEN) retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginService(@Named(NAME_RETROFIT_WITHOUT_TOKEN) retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Singleton
    @Provides
    fun provideSessionService(@Named(NAME_RETROFIT_WITHOUT_TOKEN) retrofit: Retrofit): SessionService {
        return retrofit.create(SessionService::class.java)
    }

    @Singleton
    @Provides
    fun provideFriendService(@Named(NAME_RETROFIT_WITH_TOKEN) retrofit: Retrofit): FriendService {
        return retrofit.create(FriendService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthorDataSource(
        authorService: AuthorService,
        authorMapper: AuthorMapper,
        authorListMapper: AuthorListMapper
    ): AuthorDataSource {
        return AuthorDataSourceImpl(
            authorService = authorService,
            authorMapper = authorMapper,
            authorListMapper = authorListMapper
        )
    }

    @Singleton
    @Provides
    fun provideBookDataSource(
        bookService: BookService,
        bookMapper: BookMapper,
        bookListMapper: BookListMapper,
        genreMapper: GenreMapper,
        tagMapper: TagMapper,
        sessionService: SessionService,
        sessionStorage: SessionStorage
    ): BookDataSource {
        return BookDataSourceImpl(
            bookService = bookService,
            bookMapper = bookMapper,
            bookListMapper = bookListMapper,
            genreMapper = genreMapper,
            tagMapper = tagMapper,
            sessionService = sessionService,
            sessionStorage = sessionStorage
        )
    }

    @Singleton
    @Provides
    fun provideSessionStorage(context: Context): SessionStorage {
        return SessionStorageImpl(context)
    }

    @Singleton
    @Provides
    fun provideLoginDataSource(
        loginService: LoginService,
        sessionStorage: SessionStorage,
        loginMapper: LoginMapper
    ): LoginDataSource {
        return LoginDataSourceImpl(
            loginService = loginService,
            sessionStorage = sessionStorage,
            loginMapper = loginMapper
        )
    }

    @Singleton
    @Provides
    fun provideUserDataSource(
        userService: UserService,
        sessionStorage: SessionStorage,
        sessionService: SessionService,
        userProfileMapper: UserProfileMapper
    ): UserDataSource {
        return UserDataSourceImpl(
            userService = userService,
            sessionStorage = sessionStorage,
            sessionService = sessionService,
            userProfileMapper = userProfileMapper
        )
    }

    @Singleton
    @Provides
    fun provideFriendDataSource(
        friendService: FriendService,
        friendRequestMapper: FriendRequestMapper,
        friendMapper: FriendMapper
    ): FriendDataSource {
        return FriendDataSourceImpl(
            friendService = friendService,
            friendRequestMapper = friendRequestMapper,
            friendMapper = friendMapper
        )
    }
}

