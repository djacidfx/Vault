package com.arsvechkarev.vault.features.main_list

import android.view.Gravity.CENTER
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.features.common.setWebsiteIcon
import com.arsvechkarev.vault.recycler.BaseListAdapter
import com.arsvechkarev.vault.recycler.delegate
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.HorizontalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.ItemServiceInfoImageSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.onClick
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.rippleBackground
import viewdsl.tag
import viewdsl.text
import viewdsl.viewAs

class MainListAdapter(
  private val onItemClick: (PasswordInfoItem) -> Unit,
) : BaseListAdapter() {
  
  init {
    addDelegates(
      delegate<PasswordInfoItem> {
        buildView {
          RootHorizontalLayout(MatchParent, WrapContent) {
            rippleBackground(Colors.Ripple)
            paddingHorizontal(HorizontalMarginSmall)
            paddingVertical(MarginSmall)
            ImageView(ItemServiceInfoImageSize, ItemServiceInfoImageSize) {
              tag(ItemServiceInfoImage)
              layoutGravity(CENTER)
              margins(start = MarginNormal, end = MarginNormal)
            }
            TextView(WrapContent, WrapContent, style = BoldTextView) {
              tag(ItemServiceInfoTextServiceName)
              layoutGravity(CENTER)
            }
          }
        }
        onInitViewHolder {
          itemView.onClick { onItemClick(item) }
        }
        onBind {
          itemView.viewAs<ImageView>(ItemServiceInfoImage).setWebsiteIcon(item.websiteName)
          itemView.viewAs<TextView>(ItemServiceInfoTextServiceName).text(item.websiteName)
        }
      }
    )
  }
  
  private companion object {
    
    const val ItemServiceInfoImage = "ItemServiceInfoImage"
    const val ItemServiceInfoTextServiceName = "ItemServiceInfoTextServiceName"
  }
}