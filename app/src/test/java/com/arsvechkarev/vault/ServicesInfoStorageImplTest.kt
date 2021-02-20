package com.arsvechkarev.vault

import com.arsvechkarev.vault.common.GsonJsonConverter
import com.arsvechkarev.vault.common.TestFileSaver
import com.arsvechkarev.vault.core.model.Service
import com.arsvechkarev.vault.cryptography.Cryptography
import com.arsvechkarev.vault.cryptography.JavaBase64Coder
import com.arsvechkarev.vault.cryptography.SeedRandomGeneratorImpl
import com.arsvechkarev.vault.cryptography.ServicesInfoStorageImpl
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ServicesInfoStorageImplTest {
  
  private val testFileSaver = TestFileSaver()
  private val testFileName = "testFileName"
  private val testPassword = "pAsSw0rd"
  
  private val cryptography = Cryptography(JavaBase64Coder, SeedRandomGeneratorImpl)
  private val storage = ServicesInfoStorageImpl(cryptography, testFileSaver, GsonJsonConverter)
  
  @Before
  fun setUp() {
    val encryptedText = cryptography.encryptForTheFirstTime(testPassword, "")
    testFileSaver.saveTextToFile(testFileName, encryptedText)
  }
  
  @After
  fun tearDown() {
    testFileSaver.saveTextToFile(testFileName, "")
  }
  
  @Test
  fun `Getting services list for the first time`() {
    val servicesList = storage.getServices(testPassword)
    assertTrue(servicesList.isEmpty())
  }
  
  @Test
  fun `Saving new services`() {
    val service1 = Service("id", "google", "pro", "", "po39,x//2")
    val service2 = Service("id2", "netflix", "lol", "", "wsald0k")
    
    storage.saveService(testPassword, service1)
    storage.saveService(testPassword, service2)
    val services = storage.getServices(testPassword)
    
    assertTrue(services.size == 2)
    assertTrue(services.contains(service1))
    assertTrue(services.contains(service2))
  }
  
  @Test
  fun `Updating services`() {
    val service1 = Service("id", "google", "pro", "", "po39,x//2")
    val service2 = Service("id2", "netflix", "lol", "", "wasp")
    val service2Updated = Service("id2", "netflix", "newUser", "", "wasp")
    
    storage.saveService(testPassword, service1)
    storage.saveService(testPassword, service2)
    storage.updateService(testPassword, service2Updated)
    val services = storage.getServices(testPassword)
    
    assertTrue(services.size == 2)
    assertTrue(services.contains(service1))
    assertTrue(services.contains(service2Updated))
  }
  
  @Test
  fun `Deleting services`() {
    val service1 = Service("id", "google", "pro", "", "po39,x//2")
    val service2 = Service("id2", "netflix", "lol", "", "wasp")
    
    storage.saveService(testPassword, service1)
    storage.saveService(testPassword, service2)
    storage.deleteService(testPassword, service2)
    val services = storage.getServices(testPassword)
    
    assertTrue(services.size == 1)
    assertTrue(services.contains(service1))
  }
}