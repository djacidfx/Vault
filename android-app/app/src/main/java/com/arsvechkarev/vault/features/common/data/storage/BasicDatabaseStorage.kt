package com.arsvechkarev.vault.features.common.data.storage

import app.keemobile.kotpass.database.KeePassDatabase
import buisnesslogic.DatabaseFileSaver
import buisnesslogic.DatabaseStorage
import buisnesslogic.Password

class BasicDatabaseStorage(
  private val databaseFileSaver: DatabaseFileSaver,
) : DatabaseStorage {
  
  override suspend fun getDatabase(masterPassword: Password): KeePassDatabase {
    return checkNotNull(databaseFileSaver.read(masterPassword))
  }
  
  override suspend fun saveDatabase(database: KeePassDatabase) {
    databaseFileSaver.save(database)
  }
}