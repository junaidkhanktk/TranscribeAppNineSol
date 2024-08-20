package com.example.transcribeapp.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.transcribeapp.R
import com.example.transcribeapp.adapter.ViewPagerAdapter
import com.example.transcribeapp.databinding.FragmentRecordingBinding
import com.example.transcribeapp.databinding.TabItemBinding
import com.example.transcribeapp.extension.log
import com.example.transcribeapp.recorder.AudioRecorderManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.vosk.Model
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService


class RecordingFragment :
    BaseFragment<FragmentRecordingBinding>(FragmentRecordingBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragments: List<Fragment>
        val destination = arguments?.getString("destination")

        fragments = when (destination) {
            "sourceA" -> listOf(SummaryFragment(), ConversationFragment(), AiChatFragmentInner())
             else -> listOf(SummaryFragment(), ConversationFragment(), AiChatFragmentInner())
        }



        binding?.apply {

            viewPager.adapter = ViewPagerAdapter(this@RecordingFragment, fragments)

            if (destination == "sourceB") {
                viewPager.setCurrentItem(1, false) // Load PlayerFragment by default
            } else {
                viewPager.setCurrentItem(1, false) // Load ConversationFragment by default
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.customView = createTabView(position)
            }.attach()

            // Initial setup to highlight the first tab
            highlightSelectedTab(1)

            // Add a tab selected listener to update tab views
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    highlightSelectedTab(tab.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    resetTabView(tab)
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    // No action needed
                }
            })

        }

    }

    private fun createTabView(position: Int): View {
        val tabBinding = TabItemBinding.inflate(LayoutInflater.from(requireContext()))
        tabBinding.tabTitle.text = when (position) {
            0 -> "Summary"
            1 -> "Conversation"
            2 -> "AI Chat"
            else -> ""
        }
        return tabBinding.root
    }

    private fun highlightSelectedTab(position: Int) {
        val tab = binding?.tabLayout?.getTabAt(position)
        val tabBinding = TabItemBinding.bind(tab?.customView!!)
        tabBinding.tabTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        tabBinding.backgroundTab.setBackgroundResource(R.drawable.bg_recording_selected)
    }

    private fun resetTabView(tab: TabLayout.Tab) {
        val tabBinding = TabItemBinding.bind(tab.customView!!)
        tabBinding.tabTitle.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textColor
            )
        )
        tabBinding.tabTitle.setTypeface(null, Typeface.NORMAL)
        tabBinding.backgroundTab.setBackgroundResource(R.drawable.bg_main_gradient)
    }


}
