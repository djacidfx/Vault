package com.arsvechkarev.vault.features.main_list.recycler

import com.arsvechkarev.vault.recycler.DifferentiableItem

object Loading : DifferentiableItem {
  
  override val id: String = javaClass.name
  
  override fun equals(other: Any?): Boolean {
    return other is Loading
  }
}
