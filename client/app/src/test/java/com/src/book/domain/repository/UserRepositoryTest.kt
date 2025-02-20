package com.src.book.domain.repository

import com.src.book.PASSWORD
import com.src.book.data.remote.dataSource.user.UserDataSource
import com.src.book.data.repository.UserRepositoryImpl
import com.src.book.domain.utils.BasicState
import com.src.book.domain.utils.ChangePasswordState
import com.src.book.domain.utils.EditProfileState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryTest {
    @get:Rule
    val rule = MockKRule(this)

    @MockK
    private lateinit var userDataSource: UserDataSource
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(userDataSource)
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun testChangePasswordSuccessful() = runTest {
        coEvery {
            userDataSource.changePassword(
                any(),
                any()
            )
        } returns ChangePasswordState.SuccessState
        Assert.assertEquals(
            ChangePasswordState.SuccessState,
            userRepository.changePassword(PASSWORD, PASSWORD)
        )
    }

    @Test
    fun testChangePasswordError() = runTest {
        coEvery {
            userDataSource.changePassword(
                any(),
                any()
            )
        } returns ChangePasswordState.ErrorState
        Assert.assertEquals(
            ChangePasswordState.ErrorState,
            userRepository.changePassword(PASSWORD, PASSWORD)
        )
    }

    @Test
    fun testChangePasswordWrong() = runTest {
        coEvery {
            userDataSource.changePassword(
                any(),
                any()
            )
        } returns ChangePasswordState.WrongPasswordState
        Assert.assertEquals(
            ChangePasswordState.WrongPasswordState,
            userRepository.changePassword(PASSWORD, PASSWORD)
        )
    }

    @Test
    fun testLogoutSuccessful() = runTest {
        coEvery { userDataSource.logout() } returns BasicState.SuccessState(Unit)
        Assert.assertTrue( userRepository.logout() is BasicState.SuccessState)
    }

    @Test
    fun testLogoutError() = runTest {
        coEvery { userDataSource.logout() } returns BasicState.ErrorState()
        Assert.assertTrue( userRepository.logout() is BasicState.ErrorState)
    }

    @Test
    fun testEditProfileSuccessful() = runTest {
        coEvery { userDataSource.editProfile(any(), any()) } returns EditProfileState.SuccessState
        Assert.assertTrue(userRepository.editProfile("data", null) is EditProfileState.SuccessState)
    }

    @Test
    fun testEditProfileLoginAlreadyExistsError() = runTest {
        coEvery {
            userDataSource.editProfile(
                any(),
                any()
            )
        } returns EditProfileState.LoginAlreadyExistsState
        Assert.assertTrue(
            userRepository.editProfile(
                "data",
                null
            ) is EditProfileState.LoginAlreadyExistsState
        )
    }

    @Test
    fun testEditProfileError() = runTest {
        coEvery { userDataSource.editProfile(any(), any()) } returns EditProfileState.ErrorState
        Assert.assertTrue(userRepository.editProfile("data", null) is EditProfileState.ErrorState)
    }
}