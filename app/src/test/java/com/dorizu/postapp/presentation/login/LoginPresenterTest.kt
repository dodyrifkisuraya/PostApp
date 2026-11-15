package com.dorizu.postapp.presentation.login

import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class LoginPresenterTest {

    private val view: LoginContract.View = mock()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var presenter: LoginPresenter

    @Before
    fun setUp() {
        presenter = LoginPresenter(view, testDispatcher)
    }

    @Test
    fun login_emptyUsername_showsUsernameError() {
        presenter.login("", "password123")

        verify(view).clearErrors()
        verify(view).showUsernameError("Username tidak boleh kosong")
    }

    @Test
    fun login_emptyPassword_showsPasswordError() {
        presenter.login("dody", "")

        verify(view).clearErrors()
        verify(view).showPasswordError("Password tidak boleh kosong")
    }

    @Test
    fun login_validCredentials_navigatesToDashboard() = runTest(testDispatcher) {
        presenter.login("dody", "password123")

        verify(view).clearErrors()
        verify(view).showLoading()

        testScheduler.advanceTimeBy(601)

        val inOrder = inOrder(view)
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).navigateToDashboard("dody")
    }
}
