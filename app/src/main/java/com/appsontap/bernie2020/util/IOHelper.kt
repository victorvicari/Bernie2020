package com.appsontap.bernie2020.util

import android.content.Context
import android.util.Log


class IOHelper {


    companion object{
        private const val PREFS_FAVORITES = "favorites"

        fun addFavoriteToSharedPrefs(context: Context, id: String) {
            val pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val idSet = HashSet<String>(pref.getStringSet(PREFS_FAVORITES, setOf()))

            if(!(idSet.contains(id))) {
                idSet.add(id)
            }

            Log.d(TAG, idSet.toString())
            val editor = pref.edit()
            editor.putStringSet(PREFS_FAVORITES, idSet)
            editor.apply()
        }

        fun removeFavoriteFromSharedPrefs(context: Context, id: String) {
            val pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val idSet = HashSet<String>(pref.getStringSet(PREFS_FAVORITES, setOf()))
            if(idSet.contains(id)) {
                idSet.remove(id)
                Log.d(TAG, idSet.toString())
                val editor = pref.edit()
                editor.putStringSet(PREFS_FAVORITES, idSet)
                editor.apply()
            }
        }

        fun loadFavoritesFromSharedPrefs(context: Context?) : Set<String> {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            return HashSet<String>(pref?.getStringSet(PREFS_FAVORITES, setOf()))
        }
    }
}