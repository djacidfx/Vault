package com.arsvechkarev.vault.features.creating_service

import android.graphics.drawable.Drawable
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreatingServiceView : MvpView {
  
  fun showServiceNameCannotBeEmpty()
  
  fun showPasswordCreatingDialog()
  
  fun showLoadingCreation()
  
  fun showDialogSavePassword()
  
  fun hidePasswordEditingDialog()
  
  fun hideSavePasswordDialog()
  
  fun showLetterInCircleIcon(letter: String)
  
  fun showIconFromResources(icon: Drawable)
  
  fun hideLetterInCircleIcon()
  
  fun showExit()
}