package com.bnx.ntart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bnx.ntart.ActivityIntro
import com.bnx.ntart.R
import com.bnx.ntart.creator.utility.FileHelperUtilities
import com.bnx.ntart.data.SharedPref
import com.bnx.ntart.databinding.FragmentSettingBinding
import com.bnx.ntart.utils.Tools

class FragmentSetting : Fragment() {

    lateinit var binding: FragmentSettingBinding
    lateinit var sharedPref: SharedPref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        sharedPref = SharedPref(requireContext())
        return binding.root
    }

    companion object {
        @JvmStatic
        fun instance() = FragmentSetting()
    }

    override fun onResume() {
        super.onResume()
        initComponent()
    }

    private fun initComponent() {
        binding.include.folderLocation.text = FileHelperUtilities(requireContext()).getDirectory()?.absolutePath
        binding.include.buildVersion.text = Tools.getVersionName(requireContext())

        binding.include.lytFolderLocation.setOnClickListener {
            Tools.openFolder(requireContext())
        }
        binding.include.lytIntroduction.setOnClickListener {
            ActivityIntro().navigate(requireActivity())
        }
        binding.include.lytAbout.setOnClickListener {
            Tools().showDialogProject(requireActivity())
        }
        binding.include.lytTnc.setOnClickListener {
            Tools.directLinkToBrowser(requireActivity(), getString(R.string.term_and_condition_url))
        }
        binding.include.lytRateApp.setOnClickListener {
            Tools.rateAction(requireActivity())
        }
        binding.include.lytMoreApps.setOnClickListener {
            Tools.directLinkToBrowser(requireActivity(), getString(R.string.more_app_url))
        }
        binding.include.lytContactUs.setOnClickListener {
            Tools.directLinkToBrowser(requireActivity(), getString(R.string.contact_us_url))
        }
    }
}