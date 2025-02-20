package com.src.book.presentation.registration

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.src.book.R
import com.src.book.app.App
import com.src.book.presentation.registration.first_registration.RegistrationFragment
import com.src.book.presentation.registration.first_registration.viewModel.RegistrationViewModel
import com.src.book.presentation.registration.first_registration.viewModel.RegistrationViewModelFactory
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecovery.PasswordRecoveryViewModel
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecovery.PasswordRecoveryViewModelFactory
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryCode.PasswordRecoveryCodeViewModel
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryCode.PasswordRecoveryCodeViewModelFactory
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryEmail.PasswordRecoveryEmailViewModel
import com.src.book.presentation.registration.password_recovery.viewModel.passwordRecoveryEmail.PasswordRecoveryEmailViewModelFactory
import com.src.book.presentation.registration.sign_in.viewModel.SignInViewModel
import com.src.book.presentation.registration.sign_in.viewModel.SignInViewModelFactory
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var signInViewModelFactory: SignInViewModelFactory

    @Inject
    lateinit var registrationViewModelFactory: RegistrationViewModelFactory

    @Inject
    lateinit var passwordRecoveryEmailViewModelFactory: PasswordRecoveryEmailViewModelFactory

    @Inject
    lateinit var passwordRecoveryCodeViewModelFactory: PasswordRecoveryCodeViewModelFactory

    @Inject
    lateinit var passwordRecoveryViewModelFactory: PasswordRecoveryViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as App).appComponent.inject(this)
        setContentView(R.layout.activity_login)
        replaceFragment(RegistrationFragment())
//        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},
//            REQUEST_PERMISSION);
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
        );
    }

  fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null).commit()
    }

    fun getSignInViewModel(): SignInViewModel =
        ViewModelProvider(this, signInViewModelFactory)[SignInViewModel::class.java]

    fun getRegistrationViewModel(): RegistrationViewModel =
        ViewModelProvider(this, registrationViewModelFactory)[RegistrationViewModel::class.java]

    fun getPasswordRecoveryEmailViewModel(): PasswordRecoveryEmailViewModel = ViewModelProvider(
        this, passwordRecoveryEmailViewModelFactory
    )[PasswordRecoveryEmailViewModel::class.java]

    fun getPasswordRecoveryCodeViewModel(): PasswordRecoveryCodeViewModel = ViewModelProvider(
        this, passwordRecoveryCodeViewModelFactory
    )[PasswordRecoveryCodeViewModel::class.java]

    fun getPasswordRecoveryViewModel(): PasswordRecoveryViewModel = ViewModelProvider(
        this, passwordRecoveryViewModelFactory
    )[PasswordRecoveryViewModel::class.java]
}