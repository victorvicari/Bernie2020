package com.appsontap.bernie2020

import android.content.Context
import android.R.id.edit
import android.content.SharedPreferences
import android.util.Log


class IOHelper {


    companion object{
        val PREFS_FAVORITES = "favorites"

        @JvmStatic
        fun addFavoriteToSharedPrefs(context: Context, id: String) {
            val pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val idSet = HashSet<String>(pref.getStringSet(PREFS_FAVORITES, null))

            if(!(idSet.contains(id))) {
                idSet.add(id)
            }

            Log.d(TAG, idSet.toString())
            val editor = pref.edit()
            editor.putStringSet(PREFS_FAVORITES, idSet)
            editor.commit()
        }

        @JvmStatic
        fun removeFavoriteFromSharedPrefs(context: Context, id: String) {
            val pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val idSet = HashSet<String>(pref.getStringSet(PREFS_FAVORITES, null))
            if(idSet.contains(id)) {
                idSet.remove(id)
                Log.d(TAG, idSet.toString())
                val editor = pref.edit()
                editor.putStringSet(PREFS_FAVORITES, idSet)
                editor.commit()
            }
        }

        @JvmStatic
        fun loadFavoritesFromSharedPrefs(context: Context?) : Set<String> {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            return HashSet<String>(pref?.getStringSet(PREFS_FAVORITES, null))
        }
    }
}