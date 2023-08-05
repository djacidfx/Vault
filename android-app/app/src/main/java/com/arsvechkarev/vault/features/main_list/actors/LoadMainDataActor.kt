package com.arsvechkarev.vault.features.main_list.actors

import com.arsvechkarev.vault.core.ScreenState
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedEntriesStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.main_list.MainListCommand
import com.arsvechkarev.vault.features.main_list.MainListCommand.LoadData
import com.arsvechkarev.vault.features.main_list.MainListEvent
import com.arsvechkarev.vault.features.main_list.domain.EntriesMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class LoadMainDataActor(
  private val entriesStorage: ListenableCachedEntriesStorage,
  private val masterPasswordProvider: MasterPasswordProvider,
  private val entriesMapper: EntriesMapper,
) : Actor<MainListCommand, MainListEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<MainListCommand>): Flow<MainListEvent> {
    return commands
        .filterIsInstance<LoadData>()
        .mapLatest {
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val entries = entriesStorage.getEntries(masterPassword)
          val entriesItems = entriesMapper.mapEntries(entries)
          val state = if (entriesItems.isNotEmpty()) {
            ScreenState.success(entriesItems)
          } else {
            ScreenState.empty()
          }
          MainListEvent.UpdateData(state)
        }
  }
}
