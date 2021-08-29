package com.example.google_files_app.Fragments

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.google_files_app.R
import com.example.google_files_app.SideActivityOperation
import com.example.google_files_app.storage_folder_fragment.FragmentFolder
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.android.synthetic.main.fragment_clean.*
import java.io.File
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.roundToLong


class CleanFragment : Fragment(R.layout.fragment_clean) {
    private lateinit var fragmentManager2: FragmentManager
    private lateinit var selectedSideActivityOperation: SideActivityOperation

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = context as Activity
        selectedSideActivityOperation = activity as SideActivityOperation
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvInternalStorageUsage.text = busyMemory().toString() + " GB used"
        tvInternalStorageDetails.text = totalMemory().toString() + " GB total • Internal"
        progressBar.setProgress(usedMemoryPercentage(), false)
        cleanInternal.setOnClickListener {
            selectedSideActivityOperation.onSideActivitySelected("Internal Storage")
            fragmentManager2 = requireActivity().supportFragmentManager
            launchFirstFragment()
        }

    }

    private fun launchFirstFragment() {
        val fragmentTransaction = fragmentManager2.beginTransaction()
        val fragmentFolder = FragmentFolder()
        fragmentTransaction.replace(R.id.container, fragmentFolder, "tag")
            .addToBackStack("LaunchFolderFragment").commit()

    }


    fun totalMemory(): Long {

        val statFs = StatFs(Environment.getRootDirectory().absolutePath)
        return abs(statFs.blockCount * statFs.blockSize/(1024*1024*100)).toLong()
    }

    fun usedMemoryPercentage(): Int {
        val statFs = StatFs(Environment.getRootDirectory().absolutePath)
        val total = abs(statFs.blockCount * statFs.blockSize/(1024*1024*100)).toLong()
        val free =  (statFs.availableBlocksLong * statFs.blockSize)/(1024*1024*100).toLong()
        return (((total.toDouble()-free.toDouble())*100/total.toDouble()).roundToInt())
    }
    fun busyMemory(): Long {

        val statFs = StatFs(Environment.getRootDirectory().absolutePath)
        val total = abs(statFs.blockCount * statFs.blockSize/(1024*1024*100)).toLong()
        val free =  (statFs.availableBlocksLong * statFs.blockSize)/(1024*1024*100).toLong()
        return (total - free)
    }

}