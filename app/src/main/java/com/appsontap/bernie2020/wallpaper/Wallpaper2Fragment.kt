package com.appsontap.bernie2020.wallpaper2

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.util.into
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.models.WallpaperItem
import kotlinx.android.synthetic.main.fragment_wallpaper.*
import android.graphics.drawable.BitmapDrawable




/**
 * Feel the Bern
 */
class Wallpaper2Fragment : BaseFragment() {

    private val viewModel: Wallpaper2ViewModel by lazy {
        ViewModelProviders.of(this).get(Wallpaper2ViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.wallpapers)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallpaper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.Wallpaper2Ready()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val wallpaperAdapter = adapter_wallpaper2(activity!!, it)
                    gridview.adapter= wallpaperAdapter
                    gridview.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                        try {
                            val builder = AlertDialog.Builder(activity)
                            builder.setTitle("Wallpapers")
                            builder.setMessage("Are you want to change your phones wallpaper?")
                            builder.setPositiveButton("YES"){dialog, which ->
                                changeBackground(it,position)
                            }
                            builder.setNegativeButton("No"){dialog, which ->dialog.cancel() }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()

                        } catch (e: Exception) {
                            Log.e(TAG, "Cannot set image as wallpaper", e)
                        }

                    })

                },
                onError = {
                    Log.e(TAG, "Couldn't display Wallpaper2 ${it.message}", it)
                }).into(bin)
    }

    //Rescales the chosen bitmap to the size of the phone screen and sets it as the current wallpaper
    fun changeBackground(wp :Wallpaper2b, position: Int ){
        val metrics = DisplayMetrics()
        val windowManager = activity!!.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        val height = metrics.heightPixels
        val width = metrics.widthPixels

        val current_wp = wp.getItemAtPosition(position) as WallpaperItem
        val id = resources.getIdentifier(current_wp.wallpaper_resource, "drawable", activity!!.packageName)
        val bm = BitmapFactory.decodeResource(resources, id)
        val w = bm.width
        val h = bm.height


        Log.d("Hellos1", Integer.toString(width))
        Log.d("Hellos1", Integer.toString(height))
        Log.d("Hellos1", Integer.toString(w))
        Log.d("Hellos1", Integer.toString(h))


        val bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, id), width, height, true)
        val wallpaperManager =  WallpaperManager.getInstance(activity!!.applicationContext)
        wallpaperManager.setBitmap(bitmap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bin.clear()
    }

   inner class adapter_wallpaper2(private val mContext: Context, private val wallpapers: Wallpaper2b) : BaseAdapter() {

        override fun getCount(): Int {
            return wallpapers.totalItemCount()
        }

       override fun getItemId(position: Int): Long {
           return position.toLong()
       }

       override fun getItem(position: Int): Any? {
           return null
       }

       //Sets the thumbnails within the gridView
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val thumbnail = wallpapers.getItemAtPosition(position) as WallpaperItem
            if (convertView == null) {
                val layoutInflater = LayoutInflater.from(mContext)
                convertView = layoutInflater.inflate(R.layout.grid_wallpaper, null)
            }

            val imageView = convertView!!.findViewById(R.id.imageview_wallpaper) as ImageView
            val id = resources.getIdentifier(thumbnail.wallpaper_resource, "drawable", activity!!.packageName)
            imageView.setImageResource(id)
            return convertView
        }
    }

    companion object {
        fun newInstance(): Wallpaper2Fragment {
            return Wallpaper2Fragment()
        }
    }
}


