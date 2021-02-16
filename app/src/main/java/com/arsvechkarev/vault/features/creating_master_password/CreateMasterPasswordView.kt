package com.arsvechkarev.vault.features.creating_master_password

import com.arsvechkarev.vault.cryptography.PasswordStatus
import com.arsvechkarev.vault.cryptography.PasswordStrength
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreateMasterPasswordView : MvpView {
  
  fun showPasswordProblem(passwordStatus: PasswordStatus)
  
  fun showPasswordStrength(strength: PasswordStrength?)
  
  fun switchToEnterPasswordState()
  
  fun switchToRepeatPasswordState()
  
  fun showPasswordsDontMatch()
  
  fun showFinishingAuthorization()
  
  fun goToPasswordsList()
}