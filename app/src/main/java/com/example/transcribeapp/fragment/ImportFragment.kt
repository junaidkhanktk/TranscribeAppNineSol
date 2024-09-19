package com.example.transcribeapp.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.transcribeapp.databinding.FragmentImportBinding
import com.example.transcribeapp.extension.uriToFile

class ImportFragment : BaseFragment<FragmentImportBinding>(FragmentImportBinding::inflate) {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { fileUri ->
                val file = fileUri.let { it1 -> requireActivity().uriToFile(it1) }
                file?.let {
                    importVieModel.sendRequest(it)
                }

            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.apply {
            importFile.setOnClickListener {
                launcher.launch("*/*")
            }
        }


    }


}