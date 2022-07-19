package com.arsvechkarev.vault.features.creating_master_password

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.animation.AnimationUtils
import android.widget.ViewSwitcher
import buisnesslogic.MIN_PASSWORD_LENGTH
import buisnesslogic.PasswordStatus
import buisnesslogic.PasswordStatus.EMPTY
import buisnesslogic.PasswordStatus.OK
import buisnesslogic.PasswordStatus.TOO_SHORT
import buisnesslogic.PasswordStatus.TOO_WEAK
import buisnesslogic.PasswordStrength.MEDIUM
import buisnesslogic.PasswordStrength.STRONG
import buisnesslogic.PasswordStrength.VERY_STRONG
import buisnesslogic.PasswordStrength.WEAK
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.appComponent
import com.arsvechkarev.vault.core.extensions.moxyStore
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordNews.FinishingAuthorization
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnBackButtonClicked
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnInitialPasswordTyping
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.OnRepeatPasswordTyping
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.RequestHidePasswordStrengthDialog
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordUiEvent.RequestShowPasswordStrengthDialog
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.INITIAL
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.REPEATING
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.PasswordStrengthMeterHeight
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.PasswordStrengthMeter
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.PasswordStrengthDialog.Companion.PasswordStrengthDialog
import com.arsvechkarev.vault.views.dialogs.PasswordStrengthDialog.Companion.passwordStrengthDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.classNameTag
import viewdsl.clearOnClick
import viewdsl.drawablePadding
import viewdsl.drawables
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.showKeyboard
import viewdsl.tag
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.visible
import viewdsl.withViewBuilder

class CreatingMasterPasswordScreen : BaseScreen(),
  MviView<CreatingMasterPasswordState, CreatingMasterPasswordNews> {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout(MatchParent, MatchParent) {
      HorizontalLayout(MatchParent, WrapContent) {
        tag(RepeatPasswordLayout)
        invisible()
        margins(top = MarginNormal + StatusBarHeight, start = MarginNormal, end = MarginNormal)
        ImageView(WrapContent, WrapContent, style = ImageBack) {
          onClick { store.dispatch(OnBackButtonClicked) }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(CENTER)
          text(R.string.text_repeat_password)
          margins(start = MarginLarge, end = MarginLarge)
          gravity(CENTER)
          textSize(TextSizes.H1)
        }
      }
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        tag(TextTitle)
        text(R.string.text_create_master_password)
        margins(top = MarginNormal + StatusBarHeight, start = MarginNormal, end = MarginNormal)
        gravity(CENTER)
        textSize(TextSizes.H1)
      }
      VerticalLayout(MatchParent, WrapContent) {
        layoutGravity(CENTER)
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(TextPasswordStrength)
          margins(start = MarginNormal)
        }
        child<PasswordStrengthMeter>(MatchParent, IntSize(PasswordStrengthMeterHeight)) {
          classNameTag()
          margins(top = MarginNormal, start = MarginNormal,
            end = MarginNormal, bottom = MarginLarge)
        }
        child<ViewSwitcher>(MatchParent, WrapContent) {
          classNameTag()
          inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
          outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
          child<EditTextPassword>(MatchParent, WrapContent) {
            tag(EditTextEnterPassword)
            marginHorizontal(MarginNormal)
            setHint(R.string.hint_enter_password)
            onTextChanged { text ->
              store.dispatch(OnInitialPasswordTyping(text))
            }
          }
          child<EditTextPassword>(MatchParent, WrapContent) {
            tag(EditTextRepeatPassword)
            marginHorizontal(MarginNormal)
            setHint(R.string.hint_repeat_password)
            onTextChanged { text ->
              store.dispatch(OnRepeatPasswordTyping(text))
            }
          }
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(TextError)
          gravity(CENTER)
          drawablePadding(MarginNormal)
          drawables(end = R.drawable.ic_question, color = Colors.Background)
          textColor(Colors.Error)
          margins(start = MarginNormal, end = MarginNormal, top = MarginNormal)
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        tag(TextContinue)
        layoutGravity(Gravity.BOTTOM)
        text(R.string.text_continue)
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
        onClick { store.dispatch(OnContinueClicked) }
      }
      LoadingDialog()
      PasswordStrengthDialog {
        onHide = { store.dispatch(RequestHidePasswordStrengthDialog) }
        onGotItClicked { store.dispatch(RequestHidePasswordStrengthDialog) }
      }
    }
  }
  
  private var passwordEnteringState = INITIAL
  
  private val store by moxyStore { CreatingMasterPasswordStore(appComponent) }
  
  override fun onAppearedOnScreenAfterAnimation() {
    contextNonNull.showKeyboard()
    viewAs<EditTextPassword>(EditTextEnterPassword).requestEditTextFocus()
  }
  
  override fun render(state: CreatingMasterPasswordState) {
    if (passwordEnteringState != state.passwordEnteringState) {
      passwordEnteringState = state.passwordEnteringState
      when (passwordEnteringState) {
        INITIAL -> switchToEnterPasswordState()
        REPEATING -> switchToRepeatPasswordState()
      }
    }
    if (state.showPasswordStrengthDialog) {
      passwordStrengthDialog.show()
    } else {
      passwordStrengthDialog.hide()
    }
    if (state.showErrorText) {
      state.passwordStatus?.let(::showPasswordStatus)
      if (state.passwordsMatch == false) {
        textView(TextError).text(R.string.text_passwords_dont_match)
      }
    } else {
      textView(TextError).text("")
      textView(TextError).drawables(end = R.drawable.ic_question, color = Colors.Background)
    }
    showPasswordStrength(state)
  }
  
  override fun handleNews(event: CreatingMasterPasswordNews) {
    if (event is FinishingAuthorization) {
      contextNonNull.hideKeyboard()
      loadingDialog.show()
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.dispatch(OnBackPressed)
    return true
  }
  
  private fun showPasswordStatus(passwordStatus: PasswordStatus) {
    Log.d("CMPScreen", "showing password status $passwordStatus")
    val text = when (passwordStatus) {
      EMPTY -> contextNonNull.getString(R.string.text_password_cannot_be_empty)
      TOO_SHORT -> contextNonNull.getString(R.string.text_password_min_length, MIN_PASSWORD_LENGTH)
      TOO_WEAK -> contextNonNull.getString(R.string.text_password_is_too_weak)
      OK -> contextNonNull.getString(R.string.text_empty)
    }
    if (passwordStatus == TOO_WEAK) {
      textView(TextError).drawables(end = R.drawable.ic_question, color = Colors.Error)
      textView(TextError).onClick { store.dispatch(RequestShowPasswordStrengthDialog) }
    } else {
      textView(TextError).drawables(end = R.drawable.ic_question, color = Colors.Background)
      textView(TextError).clearOnClick()
    }
    textView(TextError).text(text)
  }
  
  private fun showPasswordStrength(state: CreatingMasterPasswordState) {
    if (state.passwordEnteringState == REPEATING) {
      textView(TextPasswordStrength).text("")
      return
    }
    viewAs<PasswordStrengthMeter>().setStrength(state.passwordStrength)
    val textResId = when (state.passwordStrength) {
      WEAK -> R.string.text_weak
      MEDIUM -> R.string.text_medium
      STRONG -> R.string.text_strong
      VERY_STRONG -> R.string.text_secure
      null -> R.string.text_empty
    }
    textView(TextPasswordStrength).text(textResId)
  }
  
  private fun switchToEnterPasswordState() {
    viewAs<PasswordStrengthMeter>().setStrength(null, animate = false)
    textView(TextError).text("")
    viewAs<PasswordStrengthMeter>().visible()
    view(TextTitle).animateVisible()
    view(RepeatPasswordLayout).animateInvisible()
    textView(TextPasswordStrength).animateVisible()
    viewAs<ViewSwitcher>().apply {
      inAnimation = AnimationUtils.loadAnimation(contextNonNull, android.R.anim.slide_in_left)
      outAnimation = AnimationUtils.loadAnimation(contextNonNull, android.R.anim.slide_out_right)
      showPrevious()
    }
  }
  
  private fun switchToRepeatPasswordState() {
    viewAs<EditTextPassword>(EditTextRepeatPassword).text("")
    textView(TextError).text("")
    textView(TextPasswordStrength).animateInvisible()
    view(TextTitle).animateInvisible()
    view(RepeatPasswordLayout).animateVisible()
    viewAs<PasswordStrengthMeter>().invisible()
    viewAs<ViewSwitcher>().apply {
      inAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_out_left)
      outAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_in_right)
      showNext()
    }
  }
  
  companion object {
    
    const val TextPasswordStrength = "TextPasswordStrength"
    const val TextError = "TextError"
    const val TextTitle = "TextTitle"
    const val TextContinue = "TextContinue"
    const val RepeatPasswordLayout = "RepeatPasswordLayout"
    const val EditTextEnterPassword = "EditTextEnterPassword"
    const val EditTextRepeatPassword = "EditTextRepeatPassword"
  }
}