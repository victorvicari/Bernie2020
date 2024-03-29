package com.appsontap.bernie2020.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.models.Plan
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.min


class IOHelper {


    companion object {
        private const val PREFS_PLANS_SCROLL = "plans scroll state"
        private const val PREFS_FAVORITES = "favorites"
        private const val PREFS_LEGISLATION_SCROLL = "leg scroll state"
        private const val TWITTER_URL_LENGTH = 24 // shortens to 23 + a space
        private const val TWITTER_MAX_CHARS = 280
        private const val PREFS_PLANS_SEARCH = "plans search state"

        fun addFavoriteToSharedPrefs(context: Context, id: String) {
            val pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val idSet = HashSet<String>(pref.getStringSet(PREFS_FAVORITES, setOf()))

            if (!(idSet.contains(id))) {
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
            if (idSet.contains(id)) {
                idSet.remove(id)
                Log.d(TAG, idSet.toString())
                val editor = pref.edit()
                editor.putStringSet(PREFS_FAVORITES, idSet)
                editor.apply()
            }
        }

        fun loadFavoritesFromSharedPrefs(context: Context?): Set<String> {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            return HashSet<String>(pref?.getStringSet(PREFS_FAVORITES, setOf()))
        }

        fun savePlansScrollStateToSharedPrefs(context: Context?, position: Int) {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val editor = pref?.edit()
            editor?.putInt(PREFS_PLANS_SCROLL, position)
            editor?.apply()
        }

        fun loadPlansScrollStateFromSharedPrefs(context: Context?): Int {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            return pref?.getInt(PREFS_PLANS_SCROLL, 0) ?: 0
        }

        fun saveLegislationScrollStateToSharedPrefs(context: Context?, position: Int) {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val editor = pref?.edit()
            editor?.putInt(PREFS_LEGISLATION_SCROLL, position)
            editor?.apply()
        }

        fun loadLegislationScrollStateFromSharedPrefs(context: Context?): Int {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            return pref?.getInt(PREFS_LEGISLATION_SCROLL, 0) ?: 0
        }

        fun getPlanStringForTwitter(context: Context, plan: Plan): Uri {
            val link = plan.links?.split(" ")?.get(0)
            //val hashtag = URLEncoder.encode(context.getString(R.string.twitter_share_hashtag), "utf-8")
            val hashtag = context.getString(R.string.twitter_share_hashtag)
            var charOffset = hashtag.length
            var hasLink = false
            if (link != null && link.isNotEmpty()) {
                charOffset += min(TWITTER_URL_LENGTH, link.length)
                hasLink = true
            }
            val uriBuilder = Uri.Builder()
            uriBuilder.scheme("https")
                .authority("twitter.com")
                .appendPath("intent")
                .appendPath("tweet")

            if (hasLink) {
                uriBuilder.appendQueryParameter("url", link)
            }

            var message = hashtag

            val remainingChars = TWITTER_MAX_CHARS - charOffset
            val planName = plan.name as String
            if (planName.length <= remainingChars) {
                message += " " + planName
            } else {
                // makes it so the message fits within the 280 twitter character limit
                val excess = (planName.length + 3) - remainingChars
                message += " " + planName.substring(0, planName.length - excess - 1) + "..."
            }

            uriBuilder.appendQueryParameter("text", message)

            return uriBuilder.build()
        }

        fun getLegislationStringForTwitter(context: Context, leg: Legislation): Uri {
            var link = leg.url
            var preamble = context.getString(R.string.twitter_share_leg_preamble)
            var charOffset = preamble.length + 1 // 1 is for the space
            var hasLink = false
            if (link != null && link.isNotEmpty()) {
                charOffset += min(TWITTER_URL_LENGTH, link.length)
                hasLink = true
            }
            val uriBuilder = Uri.Builder()
            uriBuilder.scheme("https")
                .authority("twitter.com")
                .appendPath("intent")
                .appendPath("tweet")

            if (hasLink) {
                uriBuilder.appendQueryParameter("url", link)
            }

            var message = preamble

            val remainingChars = TWITTER_MAX_CHARS - charOffset
            val legTitle = leg.name as String
            if (legTitle.length <= remainingChars) {
                message += " " + legTitle
            } else {
                // makes it so the message fits within the 280 twitter character limit
                val excess = (legTitle.length + 3) - remainingChars
                message += " " + legTitle.substring(0, legTitle.length - excess - 1) + "..."
            }

            uriBuilder.appendQueryParameter("text", message)

            return uriBuilder.build()
        }

        fun savePlansSearchStateToSharedPrefs(context: Context?, searchText: String) {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            val editor = pref?.edit()
            editor?.putString(PREFS_PLANS_SEARCH, searchText)
            editor?.apply()
        }

        fun loadPlansSearchStateFromSharedPrefs(context: Context?): String? {
            val pref = context?.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            return pref?.getString(PREFS_PLANS_SEARCH, "")
        }
    }

}
