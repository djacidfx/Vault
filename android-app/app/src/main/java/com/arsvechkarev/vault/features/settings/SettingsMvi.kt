package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.features.settings.EnterPasswordDialogState.HIDDEN

sealed interface SettingsEvent {
  class ShowUsernamesReceived(val showUsernames: Boolean) : SettingsEvent
  object MasterPasswordChanged : SettingsEvent
}

sealed interface SettingsUiEvent : SettingsEvent {
  object FetchShowUsernamesChecked : SettingsUiEvent
  object OnHideEnterPasswordDialog : SettingsUiEvent
  object OnChangeMasterPasswordClicked : SettingsUiEvent
  object OnEnteredPasswordToChangeMasterPassword : SettingsUiEvent
  class OnShowUsernamesChanged(val showUsernames: Boolean) : SettingsUiEvent
  object OnBackPressed : SettingsUiEvent
}

sealed interface SettingsCommand {
  
  object GetShowUsernames : SettingsCommand
  class ChangeShowUsernames(val show: Boolean) : SettingsCommand
  
  sealed interface RouterCommand : SettingsCommand {
    object GoBack : RouterCommand
    object GoToMasterPasswordScreen : RouterCommand
  }
}

sealed interface SettingsNews {
  class SetShowUsernames(val showUsernames: Boolean) : SettingsNews
  object ShowMasterPasswordChanged : SettingsNews
}

data class SettingsState(
  val showUsernames: Boolean = false,
  val enterPasswordDialogState: EnterPasswordDialogState = HIDDEN
)

enum class EnterPasswordDialogState {
  SHOWN, HIDDEN, HIDDEN_KEEPING_KEYBOARD
}