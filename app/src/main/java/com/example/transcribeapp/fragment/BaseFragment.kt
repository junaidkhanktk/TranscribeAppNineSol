package com.example.transcribeapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.example.transcribeapp.authorization.dataLogicLayer.AuthViewModel
import com.example.transcribeapp.authorization.google.GoogleSignInViewModel
import com.example.transcribeapp.history.mvvm.HistoryViewModel
import com.example.transcribeapp.importAllFile.ImportViewModel
import com.example.transcribeapp.summary.SummaryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {
        protected val historyViewModel by viewModel<HistoryViewModel>()
    protected val summaryViewModel by viewModel<SummaryViewModel>()
    protected val importVieModel by viewModel<ImportViewModel>()
    protected val authViewModel by viewModel<AuthViewModel>()
    protected val googleSignInViewModel by viewModel<GoogleSignInViewModel>()

    private var _binding: VB? = null
    val binding get() = _binding
    private var drawerListener: DrawerLayout.DrawerListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DrawerLayout.DrawerListener) {
            drawerListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (drawerListener != null) {
            drawerListener = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding?.root
    }

    fun backPress(callback: () -> Unit) {
        isFragmentVisible {
            requireActivity().onBackPressedDispatcher.addCallback(this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        callback()
                    }
                })
        }
    }


    fun isFragmentVisible(doWork: () -> Unit) {
        if (isAdded && !isDetached)
            doWork.invoke()
    }

}