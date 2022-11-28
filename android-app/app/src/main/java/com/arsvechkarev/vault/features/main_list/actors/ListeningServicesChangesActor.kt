package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedPasswordsStorage
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListeningServicesChangesActor(
  private val passwordStorage: ListenableCachedPasswordsStorage,
) : Actor<MainListCommand, MainListEvent> {
  
  override fun handle(commands: Flow<MainListCommand>): Flow<MainListEvent> {
    return passwordStorage.passwords.map { passwords ->
      val state = if (passwords.isNotEmpty()) ScreenState.success(passwords) else ScreenState.empty()
      MainListEvent.UpdateData(state)
    }
  }
}