package com.appsontap.bernie2020.util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager
import com.appsontap.bernie2020.Constants
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.models.Plan
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.math.min

fun Disposable.into(compositeDisposable: CompositeDisposable?) {
    compositeDisposable?.let {
        if (it.isDisposed) {
            throw IllegalStateException(
                "attempted to add Disposable into CompositeDisposable that is already disposed"
            )
        } else {
            it.add(this)
        }
    }
}

fun FragmentManager.getTopFragmentEntry(): FragmentManager.BackStackEntry =
    getBackStackEntryAt(backStackEntryCount - 1)

fun Context.addFavoriteToSharedPrefs(id: String) {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    val idSet = HashSet<String>(pref.getStringSet(Constants.PREFS_FAVORITES, setOf()))

    if (!(idSet.contains(id))) {
        idSet.add(id)
    }

    Log.d(TAG, idSet.toString())
    pref.edit {
        putStringSet(Constants.PREFS_FAVORITES, idSet)
    }
}

fun Context.removeFavoriteFromSharedPrefs(id: String) {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    val idSet = HashSet<String>(pref.getStringSet(Constants.PREFS_FAVORITES, setOf()))
    if (idSet.contains(id)) {
        idSet.remove(id)
        Log.d(TAG, idSet.toString())
        pref.edit {
            putStringSet(Constants.PREFS_FAVORITES, idSet)
        }
    }
}

fun Context.loadFavoritesFromSharedPrefs(): Set<String> {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    return HashSet<String>(pref?.getStringSet(Constants.PREFS_FAVORITES, setOf()))
}

fun Context.savePlansScrollStateToSharedPrefs(position: Int) {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    pref?.edit {
        putInt(Constants.PREFS_PLANS_SCROLL, position)
    }
}

fun Context.loadPlansScrollStateFromSharedPrefs(): Int {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    return pref?.getInt(Constants.PREFS_PLANS_SCROLL, 0) ?: 0
}

fun Context.saveLegislationScrollStateToSharedPrefs(position: Int) {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    pref?.edit {
        putInt(Constants.PREFS_LEGISLATION_SCROLL, position)
    }
}

fun Context.loadLegislationScrollStateFromSharedPrefs(): Int {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    return pref?.getInt(Constants.PREFS_LEGISLATION_SCROLL, 0) ?: 0
}

fun Plan.getPlanStringForTwitter(context: Context): Uri {
    val link = this.links?.split(" ")?.get(0)

    val hashtag = context.getString(R.string.twitter_share_hashtag)
    var charOffset = hashtag.length
    var hasLink = false
    if (link != null && link.isNotEmpty()) {
        charOffset += min(Constants.TWITTER_URL_LENGTH, link.length)
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

    val remainingChars = Constants.TWITTER_MAX_CHARS - charOffset
    val planName = this.name as String
    message += getMessage(planName, remainingChars)

    uriBuilder.appendQueryParameter("text", message)

    return uriBuilder.build()
}

fun Legislation.getLegislationStringForTwitter(context: Context): Uri {
    val link = this.url
    val preamble = context.getString(R.string.twitter_share_leg_preamble)
    var charOffset = preamble.length + 1 // 1 is for the space
    var hasLink = false
    if (link != null && link.isNotEmpty()) {
        charOffset += min(Constants.TWITTER_URL_LENGTH, link.length)
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

    val remainingChars = Constants.TWITTER_MAX_CHARS - charOffset
    val legTitle = this.name as String
    message += getMessage(legTitle, remainingChars)

    uriBuilder.appendQueryParameter("text", message)

    return uriBuilder.build()
}

fun getMessage(planName: String, remainingChars: Int): String {
    return if (planName.length <= remainingChars) {
        " $planName"
    } else {
        // makes it so the message fits within the 280 twitter character limit
        val excess = (planName.length + 3) - remainingChars
        " " + planName.substring(0, planName.length - excess - 1) + "..."
    }
}

fun Context.savePlansSearchStateToSharedPrefs(searchText: String) {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    pref?.edit {
        putString(Constants.PREFS_PLANS_SEARCH, searchText)
    }
}

fun Context.loadPlansSearchStateFromSharedPrefs(): String? {
    val pref = this.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    return pref?.getString(Constants.PREFS_PLANS_SEARCH, "")
}