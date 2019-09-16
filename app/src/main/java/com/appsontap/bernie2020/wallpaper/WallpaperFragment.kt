package com.appsontap.bernie2020.wallpaper

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.fragment.app.FragmentActivity
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.models.WallpaperItem
import com.appsontap.bernie2020.plan_details.CategoryDetailsFragment
import kotlinx.android.synthetic.main.fragment_wallpaper.*
import kotlinx.android.synthetic.main.grid_wallpaper.*


/**
 * Feel the Bern
 */
class WallpaperFragment : BaseFragment() {

    private val viewModel: WallpaperViewModel by lazy {
        ViewModelProviders.of(this).get(WallpaperViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.wallpapers)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallpaper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.WallpaperReady()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val wallpaperAdapter = adapter_wallpaper(activity!!, it)
                    gridview.adapter = wallpaperAdapter
                    gridview.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                        try {
                            val args = Bundle()
                            args.putString("wpTag", it.get(position).wallpaper_resource)
                            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.fragment_container,
                                    viewWallpaperFragment.newInstance(args),
                                    CategoryDetailsFragment.TAG
                                )
                                .addToBackStack(CategoryDetailsFragment.TAG)
                                .commit()
                        } catch (e: Exception) {
                            Log.e(TAG, "Cannot set image as wallpaper", e)
                        }
                    })
                },
                onError = {
                    Log.e(TAG, "Couldn't display Wallpaper ${it.message}", it)
                }).into(bin)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bin.clear()
    }

    inner class adapter_wallpaper(
        private val mContext: Context,
        private val wallpapers: List<WallpaperItem>
    ) : BaseAdapter() {

        override fun getCount(): Int {
            return wallpapers.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        //Sets the thumbnails within the gridView
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val thumbnail = wallpapers.get(position)
            var wallpaperView =  LayoutInflater.from(mContext).inflate(R.layout.grid_wallpaper, null)
            val wpThumbnailView = wallpaperView!!.findViewById(R.id.imageview_wallpaper)as ImageView
            val id = resources.getIdentifier(thumbnail.wallpaper_resource, "drawable", activity!!.packageName)
            wpThumbnailView.setImageResource(id)
            return wallpaperView
        }
    }

    companion object {
        fun newInstance(): WallpaperFragment {
            return WallpaperFragment()
        }
    }
}


