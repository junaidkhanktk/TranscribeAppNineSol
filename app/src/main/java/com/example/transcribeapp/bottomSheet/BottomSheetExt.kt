package com.example.transcribeapp.bottomSheet

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.transcribeapp.R
import com.example.transcribeapp.adapter.ViewPagerAdapter
import com.example.transcribeapp.databinding.DialogDateTimePickerBottomSheetBinding
import com.example.transcribeapp.databinding.RecordingBottomSheetBinding
import com.example.transcribeapp.databinding.TabItemBinding
import com.example.transcribeapp.fragment.AiChatFragmentInner
import com.example.transcribeapp.fragment.ConversationFragment
import com.example.transcribeapp.fragment.SummaryFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


fun setupFullHeight(bottomSheet: View) {
    val layoutParams = bottomSheet.layoutParams
    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    bottomSheet.layoutParams = layoutParams
}

/*fun fullHeightDialog(bottomSheet: BottomSheetDialog) {
    val parentLayout =
        bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
    parentLayout?.let { it ->
        val behaviour = BottomSheetBehavior.from(it)
        setupFullHeight(it)
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }
}*/

fun FragmentActivity.recordingBottomSheet(showFragment: Fragment) {
    val binding by lazy {
        RecordingBottomSheetBinding.inflate(layoutInflater)
    }

    val dialog = BottomSheetDialog(this)
    dialog.setContentView(binding.root)

   // fullHeightDialog(dialog)

    val fragments =
        listOf(SummaryFragment(), ConversationFragment.cancelDialog(dialog), AiChatFragmentInner())

    binding.apply {

        cancelDialog.setOnClickListener {
            dialog.dismiss()
        }

        viewPager.adapter = ViewPagerAdapter(showFragment, fragments)
        viewPager.setCurrentItem(1, false)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.customView = createTabView(position)
        }.attach()
        highlightSelectedTab(1, tabLayout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                highlightSelectedTab(tab.position, tabLayout)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                resetTabView(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // No action needed
            }
        })
    }

    dialog.show()


}


fun FragmentActivity.createTabView(position: Int): View {
    val tabBinding = TabItemBinding.inflate(LayoutInflater.from(this))
    tabBinding.tabTitle.text = when (position) {
        0 -> "Summary"
        1 -> "Conversation"
        2 -> "AI Chat"
        else -> ""
    }
    return tabBinding.root
}


fun FragmentActivity.highlightSelectedTab(position: Int, tabLayout: TabLayout) {
    val tab = tabLayout.getTabAt(position)
    val tabBinding = TabItemBinding.bind(tab?.customView!!)
    tabBinding.tabTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
    tabBinding.backgroundTab.setBackgroundResource(R.drawable.bg_recording_selected)
}

fun FragmentActivity.resetTabView(tab: TabLayout.Tab) {
    val tabBinding = TabItemBinding.bind(tab.customView!!)
    tabBinding.tabTitle.setTextColor(
        ContextCompat.getColor(
            this,
            R.color.textColor
        )
    )
    tabBinding.tabTitle.setTypeface(null, Typeface.NORMAL)
    tabBinding.backgroundTab.setBackgroundResource(R.drawable.bg_main_gradient)
}


fun FragmentActivity.timeDatePicker() {
    val binding by lazy {
        DialogDateTimePickerBottomSheetBinding.inflate(layoutInflater)
    }
    val dialog = BottomSheetDialog(this)
    dialog.setContentView(binding.root)
    binding.apply {
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnOk.setOnClickListener {

        }
        singleDayPicker.addOnDateChangedListener { label, date ->
            // Handle the selected label and date
            Log.d("DateSelected", "Label: $label, Date: $date")
        }


    }

    dialog.show()


}



