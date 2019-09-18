package com.appsontap.bernie2020.legislation_details

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.appsontap.bernie2020.App
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.util.TAG
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * Parse the markup json from legislation.json
 * Convert it into a SpannabeStringBuilder
 * TODO make this return SpannableAttributedTextImpl instead so we can writes tests for this class.
 */
class MarkupParser {
    fun parse(markupString: String): SpannableStringBuilder {
        val markupArray = Gson().fromJson(markupString, JsonArray::class.java)
        val builder = SpannableStringBuilder()
        markupArray.forEach { element ->
            val markup = element as JsonObject
            val keys = markup.keySet()
            val values = markup.entrySet()
            keys.forEachIndexed { index, key ->
                when (key) {
                    "pText" -> {
                        var value = values.elementAt(index).value.asString
                        value = value.replace("\\n", "\n\n")
                        builder.append(value)
                    }
                    "h2Text" -> {
                        var value = values.elementAt(index).value.asString
                        value = value.replace("\\n", "\n\n")
                        builder.append(value, AbsoluteSizeSpan(54), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        var start = builder.length - value.length - 1
                        if (start < 0) {
                            start = 0
                        }
                        builder.setSpan(
                            ForegroundColorSpan(App.get().getColor(R.color.colorPrimary)),
                            start,
                            builder.length,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                    }
                    "bulletList" -> {
                        val bulletList = values.elementAt(index).value as JsonArray
                        bulletList.forEach { bulletText: JsonElement ->
                            builder.append(
                                bulletText.asString + "\n",
                                BulletSpan(8, App.get().getColor(R.color.colorAccent)),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                        }
                        builder.append("\n")
                    }
                    else -> {
                        Log.e(TAG, "Unknown markup item")
                    }
                }

            }
        }

        return builder
    }
}