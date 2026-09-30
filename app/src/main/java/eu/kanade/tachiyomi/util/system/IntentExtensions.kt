package eu.kanade.tachiyomi.util.system

import android.app.PendingIntent
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.IntentCompat
import eu.kanade.tachiyomi.R
import java.io.Serializable

val FLAG_IMMUTABLE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    PendingIntent.FLAG_IMMUTABLE
} else {
    0
}

val FLAG_MUTABLE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    PendingIntent.FLAG_MUTABLE
} else {
    0
}

fun Uri.toShareIntent(context: Context, type: String = "image/*", message: String? = null): Intent {
    val uri = this

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        when (uri.scheme) {
            "http", "https" -> {
                putExtra(Intent.EXTRA_TEXT, uri.toString())
            }
            "content" -> {
                message?.let { putExtra(Intent.EXTRA_TEXT, it) }
                putExtra(Intent.EXTRA_STREAM, uri)
            }
        }
        clipData = ClipData.newRawUri(null, uri)
        setType(type)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    return Intent.createChooser(shareIntent, context.getString(R.string.action_share)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
}

inline fun <reified T> Intent.getParcelableExtraCompat(name: String): T? {
    return IntentCompat.getParcelableExtra(this, name, T::class.java)
}

inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(name) as? T
    }
}
