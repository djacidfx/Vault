package com.arsvechkarev.vault.features.login

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.login.actors.LoginActor
import com.arsvechkarev.vault.features.login.actors.LoginRouterActor

fun LoginStore(
  coreComponent: CoreComponent
): TeaStore<LoginState, LoginUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      LoginActor(coreComponent.masterPasswordChecker),
      LoginRouterActor(coreComponent.router)
    ),
    reducer = LoginReducer(),
    initialState = LoginState()
  )
}