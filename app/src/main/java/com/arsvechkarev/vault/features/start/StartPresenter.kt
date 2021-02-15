package com.arsvechkarev.vault.features.start

import com.arsvechkarev.vault.core.BasePresenter
import com.arsvechkarev.vault.core.Threader
import com.arsvechkarev.vault.cryptography.MasterPasswordChecker
import com.arsvechkarev.vault.cryptography.MasterPasswordHolder

class StartPresenter(
  private val masterPasswordChecker: MasterPasswordChecker,
  threader: Threader,
) : BasePresenter<StartView>(threader) {
  
  fun onEnteredPassword(password: String) {
    if (password.isBlank()) return
    viewState.showLoading()
    threader.onBackgroundThread {
      val isCorrect = masterPasswordChecker.isCorrect(password)
      if (isCorrect) {
        MasterPasswordHolder.setMasterPassword(password)
        updateViewState { showSuccess() }
      } else {
        updateViewState { showError() }
      }
    }
  }
}