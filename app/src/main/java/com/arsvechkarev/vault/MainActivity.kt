package com.arsvechkarev.vault

import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import com.arsvechkarev.vault.core.BaseActivity
import com.arsvechkarev.vault.core.Singletons.userAuthSaver
import com.arsvechkarev.vault.core.extensions.bundle
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.core.navigation.Navigator
import com.arsvechkarev.vault.core.navigation.NavigatorView
import com.arsvechkarev.vault.core.navigation.Options
import com.arsvechkarev.vault.features.creating_master_password.CreateMasterPasswordScreen
import com.arsvechkarev.vault.features.creating_service.CreatingServiceScreen
import com.arsvechkarev.vault.features.info.InfoScreen
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.SERVICE_INFO
import com.arsvechkarev.vault.features.passwords_list.PasswordsListScreen
import com.arsvechkarev.vault.features.start.StartScreen
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewdsl.Densities
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.size

class MainActivity : BaseActivity(), Navigator {
  
  private val navigator get() = viewAs<NavigatorView>()
  
  private val mainActivityLayout
    get() = NavigatorView(this).apply {
      classNameTag()
      size(MatchParent, MatchParent)
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Densities.init(resources)
    Colors.init(this)
    window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
        or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    setContentView(mainActivityLayout)
    if (userAuthSaver.isUserAuthorized()) {
      navigator.navigate(StartScreen::class)
    } else {
      navigator.navigate(CreateMasterPasswordScreen::class)
    }
  }
  
  override fun onBackPressed() {
    if (!navigator.handleGoBack()) {
      super.onBackPressed()
    }
  }
  
  override fun goToPasswordsListScreen() {
    navigator.navigate(PasswordsListScreen::class, options = Options(removeCurrentScreen = true))
  }
  
  override fun goToNewServiceScreen() {
    navigator.navigate(CreatingServiceScreen::class, options = Options(removeWhenBackClicked = true))
  }
  
  override fun goToSavedServiceInfoScreen(serviceInfo: ServiceInfo) {
    navigator.navigate(InfoScreen::class, options = Options(
      arguments = bundle(SERVICE_INFO to serviceInfo)
    ))
  }
  
  override fun popCurrentScreen() {
    navigator.handleGoBack()
  }
}