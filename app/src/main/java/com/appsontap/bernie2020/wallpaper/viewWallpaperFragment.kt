package com.appsontap.bernie2020.wallpaper2

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.BaseFragment
import kotlinx.android.synthetic.main.fragment_wallpaper_view.*


/**
 * Feel the Bern
 */
class viewWallpaperFragment : BaseFragment() {

    private var wallpaperId: String? = null

    private val viewModel: WallpaperViewModel by lazy {
        ViewModelProviders.of(this).get(WallpaperViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.wallpapers)
        arguments?.let {
            it.run {
               wallpaperId = getString("wpTag")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallpaper_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wallpaperImageView.setImageResource(resources.getIdentifier(wallpaperId, "drawable", activity!!.packageName))
        buttonWallpaper.setOnClickListener{
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Wallpapers")
            builder.setMessage("Are you want to change your phones wallpaper?")
            builder.setPositiveButton("YES") { dialog, which ->
                changeBackground(resources.getIdentifier(wallpaperId, "drawable", activity!!.packageName))
            }
            builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        bin.clear()
    }
    fun changeBackground( id: Int) {

        val metrics = DisplayMetrics()
        val windowManager =
            activity!!.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        //Also need to take the navigation bar into account to properly resize the image
        var navigationBarHeight = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        val bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, id), width, height + navigationBarHeight, true
        )
        val wallpaperManager = WallpaperManager.getInstance(activity!!.applicationContext)
        wallpaperManager.setBitmap(bitmap)
    }

    companion object {
        fun newInstance(args: Bundle): viewWallpaperFragment {
            return viewWallpaperFragment().apply{
                this.arguments = args
            }
        }
    }
}


