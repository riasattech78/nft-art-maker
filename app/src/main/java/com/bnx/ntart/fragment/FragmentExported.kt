package com.bnx.ntart.fragment

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bnx.ntart.ActivityHome
import com.bnx.ntart.R
import com.bnx.ntart.adapter.AdapterExported
import com.bnx.ntart.creator.utility.FileHelperUtilities
import com.bnx.ntart.data.SharedPref
import com.bnx.ntart.data.StringConstants
import com.bnx.ntart.databinding.FragmentExportedBinding
import com.bnx.ntart.utils.PermissionUtil
import com.bnx.ntart.utils.Tools
import java.io.File


class FragmentExported : Fragment() {

    lateinit var binding: FragmentExportedBinding
    lateinit var sharedPref: SharedPref

    companion object {
        @JvmStatic
        fun instance() = FragmentExported()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExportedBinding.inflate(inflater, container, false)
        sharedPref = SharedPref(requireContext())

        initComponent()

        return binding.root
    }

    private fun initComponent() {
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.setHasFixedSize(true)
        }
        requestAction()
    }

    private fun requestAction() {
        if (!PermissionUtil().isStorageGranted(requireActivity())) {
            if (sharedPref.getNeverAskAgain(PermissionUtil().STORAGE)) {
                PermissionUtil().showDialog(requireActivity() as ActivityHome)
            } else {
                requestPermissions(PermissionUtil().PERMISSION_STORAGE, 500)
            }
            return
        }

//        showNoItemView(false)
        showProgress(true)
        Handler(Looper.getMainLooper()).postDelayed({ loadFiles() }, 1000)
    }

    private fun loadFiles() {

        val filePaths: MutableList<String> = ArrayList()
        var projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.ImageColumns.RELATIVE_PATH else MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.MIME_TYPE,
        )

        var selection = MediaStore.Images.Media.DATA + " like ? "
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            selection = MediaStore.Images.Media.RELATIVE_PATH + " like ? "
        }
        val selectionArgs = arrayOf("%" + getString(R.string.app_name) + "%")

        val cursor: Cursor? = requireActivity().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        )

        if (cursor != null) {
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                if (isSupportedFile(name)) {
                    filePaths.add(contentUri.toString())
                }
            }
        }
        displayResult(filePaths)
    }

    private fun displayResult(items: List<String>) {
        binding.recyclerView.adapter =
            AdapterExported(items, object : AdapterExported.AdapterListener {
                override fun onClicked(obj: String, pos: Int) {
                    try {
                        if ((activity as ActivityHome).showInterstitialAd()) return
                    } catch (e: Exception) {
                    }
                    Tools.openImage(requireContext(), obj)
                }

                override fun onLongClicked(obj: String, pos: Int) {
                    onCreationLongTappedOverride(obj, pos)
                }

            })
        showProgress(false)
        sharedPref.setExported(false)
    }

    private fun onCreationLongTappedOverride(obj: String, pos: Int) {
        (binding.recyclerView.adapter as AdapterExported).userHasLongPressed = true
        var file = File(obj)
        Tools.showDialog(
            activity,
            StringConstants.DIALOG_DELETE_EXPORTED_TITLE,
            StringConstants.DIALOG_DELETE_EXPORTED_MESSAGE + file.name + "?",
            StringConstants.DIALOG_POSITIVE_BUTTON_TEXT,
            { _, _ ->
                FileHelperUtilities(requireContext()).deleteFile(file)
                requestAction()
            }, StringConstants.DIALOG_NEGATIVE_BUTTON_TEXT, { _, _ -> }, null
        )

    }

    private fun isSupportedFile(filename: String): Boolean {
        return filename.lowercase().endsWith(".jpg") || filename.lowercase().endsWith(".png")
    }

    private fun showProgress(show: Boolean) {
        binding.progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (sharedPref.getExported()) requestAction()
    }
}