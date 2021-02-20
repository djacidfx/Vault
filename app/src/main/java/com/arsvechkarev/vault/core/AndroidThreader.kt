package com.arsvechkarev.vault.core

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

object AndroidThreader : Threader {
  
  private val backgroundWorker = Executors.newSingleThreadExecutor()
  private val ioWorker = Executors.newFixedThreadPool(4)
  
  private val handler = Handler(Looper.getMainLooper())
  
  override fun onBackgroundThread(block: () -> Unit) {
    backgroundWorker.submit(block)
  }
  
  override fun onIoThread(block: () -> Unit) {
    ioWorker.submit(block)
  }
  
  override fun onMainThread(block: () -> Unit) {
    handler.post(block)
  }
}