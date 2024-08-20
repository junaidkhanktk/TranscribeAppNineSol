package com.example.transcribeapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.transcribeapp.R
import com.example.transcribeapp.databinding.FragmentImportBinding
import com.example.transcribeapp.extension.uriToFile

class ImportFragment : BaseFragment<FragmentImportBinding>(FragmentImportBinding::inflate) {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { fileUri ->


                val file = uri.let { it1 -> requireActivity().uriToFile(it1) }

                file?.let {
                    importVieModel.sendRequest(it)
                }


                val contentResolver = requireActivity().applicationContext.contentResolver
                // Check MIME type if needed
                val mimeType = contentResolver.getType(fileUri)
                // Open an InputStream (might require permissions)
                contentResolver.openInputStream(fileUri)?.use { inputStream ->


                    //val summary = processFileWithModel(inputStream)

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