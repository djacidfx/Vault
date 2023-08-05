package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.context
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.hasTextColorInt
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.core.stub.StubActivityResultSubstitutor
import com.arsvechkarev.vault.test.core.stub.StubExternalFileReader
import com.arsvechkarev.vault.test.screens.KImportPasswordsScreen
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.viewbuilding.Colors
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class ImportPasswordsTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val stubFileReader = StubExternalFileReader(
    uriToMatch = "content://myfolder/myfile.png",
    bytesToRead = { context.assets.open("file_two_passwords").readBytes() }
  )
  
  @Test
  fun importingPasswordsFromMainMenuTest() = init {
    runBlocking {
      CoreComponentHolder.initialize(
        application = ApplicationProvider.getApplicationContext(),
        activityResultSubstitutor = StubActivityResultSubstitutor(
          stubGetFileUri = "content://myfolder/myfile.png"
        ),
        externalFileReader = stubFileReader
      )
      writeVaultFileFromAssets("file_one_password")
      rule.launchActivity()
    }
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      
      KMainListScreen {
        recycler {
          hasSize(2)
          childAt<PasswordItem>(1) {
            text.hasText("aaa")
          }
        }
        
        menu {
          open()
          importPasswordsMenuItem.click()
        }
        
        KImportPasswordsScreen {
          currentScreenIs(ImportPasswordsScreen::class)
          iconBack.click()
        }
        
        currentScreenIs(MainListScreen::class)
        
        menu {
          open()
          importPasswordsMenuItem.click()
        }
        
        KImportPasswordsScreen {
          titleSelectFile {
            hasText("File")
            hasTextColorInt(Colors.Accent)
          }
          textSelectFile.hasText("Select file")
          
          buttonImportPasswords.click()
          
          titleSelectFile {
            hasText("You have not selected file")
            hasTextColorInt(Colors.Error)
          }
          
          layoutSelectFile.click()
          
          textSelectFile.hasText("myfolder/myfile.png")
          titleSelectFile {
            hasText("File")
            hasTextColorInt(Colors.Accent)
          }
          
          buttonImportPasswords.click()
          
          infoDialog {
            isDisplayed()
            title.hasText("Confirmation")
            message.hasText(R.string.text_confirm_import_passwords_message)
            action1.hasText("CANCEL")
            action2.hasText("CONFIRM")
          }
          
          infoDialog.action1.click()
          
          infoDialog.isNotDisplayed()
          currentScreenIs(ImportPasswordsScreen::class)
          
          buttonImportPasswords.click()
          
          infoDialog.isDisplayed()
          
          pressBack()
          
          infoDialog.isNotDisplayed()
          
          buttonImportPasswords.click()
          infoDialog.action2.click()
          
          enterPasswordDialog {
            isDisplayed()
            title.hasText("Enter password to decipher the file with")
            textError.hasEmptyText()
          }
          
          enterPasswordDialog.iconCross.click()
          
          enterPasswordDialog.isNotDisplayed()
          
          buttonImportPasswords.click()
          infoDialog.action2.click()
          
          enterPasswordDialog {
            editText.replaceText("abc")
            buttonContinue.click()
          }
          
          infoDialog {
            isDisplayed()
            title.hasText("Error")
            message.hasText(R.string.text_error_import_message)
            action1.isNotDisplayed()
            action2.hasText("OK")
          }
          
          infoDialog.action2.click()
          
          infoDialog.isNotDisplayed()
          
          enterPasswordDialog.isDisplayed()
          
          enterPasswordDialog {
            editText.replaceText("qwetu1233")
            buttonContinue.click()
          }
          
          enterPasswordDialog.isNotDisplayed()
          
          infoDialog {
            isDisplayed()
            title.hasText("Success!")
            message.hasText("Imported passwords successfully")
            action1.isNotDisplayed()
            action2.hasText("OK")
          }
          
          infoDialog.action2.click()
        }
        
        currentScreenIs(MainListScreen::class)
        
        recycler {
          hasSize(3)
          childAt<PasswordItem>(1) {
            text.hasText("google")
            icon.hasDrawable(R.drawable.icon_google)
          }
          childAt<PasswordItem>(2) {
            text.hasText("test.com")
            icon.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
      }
    }
  }
  
  @Test
  fun importingPasswordsFromInitialScreenTest() = init {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      activityResultSubstitutor = StubActivityResultSubstitutor(
        stubGetFileUri = "content://myfolder/myfile.png"
      ),
      externalFileReader = stubFileReader
    )
    rule.launchActivity()
  }.run {
    KInitialScreen {
      buttonImportPasswords.click()
      KImportPasswordsScreen {
        currentScreenIs(ImportPasswordsScreen::class)
        layoutSelectFile.click()
        buttonImportPasswords.click()
        infoDialog.action2.click()
        enterPasswordDialog {
          editText.replaceText("qwetu1233")
          buttonContinue.click()
        }
        infoDialog.action2.click()
        KMainListScreen {
          currentScreenIs(MainListScreen::class)
          recycler {
            hasSize(3)
            childAt<PasswordItem>(1) {
              text.hasText("google")
              icon.hasDrawable(R.drawable.icon_google)
            }
            childAt<PasswordItem>(2) {
              text.hasText("test.com")
              icon.hasDrawable(LetterInCircleDrawable("t"))
            }
          }
        }
      }
    }
  }
}