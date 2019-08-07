package com.appsontap.bernie2020.legislation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.models.Legislation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_legislation.*
import kotlinx.android.synthetic.main.item_legislation.*
import kotlinx.android.synthetic.main.item_legislation.view.*
import kotlinx.android.synthetic.main.item_plan.*
import kotlinx.android.synthetic.main.item_plan.view.*

/**
 * Feel the Bern
 */
class LegislationFragment : BaseFragment(){
    val repo = LegislationRepo()
    private lateinit var favorites: Set<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.drawer_legislation)
        favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_legislation, container, false)
    }

    override fun onStart() {
        super.onStart()
        repo
            .getLegislation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onSuccess = { legislation ->
                    recycler_view.adapter = LegislationAdapter(legislation)
                },
                onError = {
                    Log.e(TAG, "${it.message}", it)
                }
            ).into(bin)
    }

    override fun onStop() {
        super.onStop()
        bin.clear()
    }

    companion object{
        fun newInstance() : LegislationFragment{
            return LegislationFragment()
        }
    }
    
    inner class LegislationAdapter(val items : List<Legislation>) : RecyclerView.Adapter<LegislationViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegislationViewHolder {
            return LegislationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_legislation, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: LegislationViewHolder, position: Int) {
            holder.bind(items[position])
        }
    }
    
    inner class LegislationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init{
            itemView.setOnClickListener { 
                
            }
        }
        fun bind(legislation: Legislation){
            itemView.textview_legislation_name.text = legislation.name
            itemView.checkbox_legislation_favorite?.isChecked = favorites.contains(legislation.id)

            itemView.checkbox_legislation_favorite?.setOnClickListener {
                if(itemView.checkbox_legislation_favorite.isChecked) {
                    context?.let { theContext -> IOHelper.addFavoriteToSharedPrefs(theContext, legislation.id) }
                } else {
                    context?.let { theContext -> IOHelper.removeFavoriteFromSharedPrefs(theContext, legislation.id) }
                }
                favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
                Log.d(TAG, "FAVORITES: $favorites")
            }
        }
    }
}