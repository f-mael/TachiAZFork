package eu.kanade.tachiyomi.data.custom

import android.content.Context
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.util.storage.DiskUtil
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.io.File

@Serializable
data class CustomMangaInfo(
    val id: Long,
    val title: String? = null,
    val author: String? = null,
    val artist: String? = null,
    val description: String? = null
)

class CustomMangaManager(
    val context: Context,
    private val json: Json = Injekt.get()
) {
    private val customInfoDir = File(context.filesDir, "custom_manga_info").apply {
        if (!exists()) mkdirs()
    }

    private fun getCustomInfoFile(mangaId: Long): File {
        return File(customInfoDir, "${DiskUtil.hashKeyForDisk(mangaId.toString())}.json")
    }

    fun hasCustomInfo(manga: Manga): Boolean {
        val id = manga.id ?: return false
        return getCustomInfoFile(id).exists()
    }

    fun getCustomInfo(manga: Manga): CustomMangaInfo? {
        val id = manga.id ?: return null
        val file = getCustomInfoFile(id)
        if (!file.exists()) return null
        return try {
            json.decodeFromString<CustomMangaInfo>(file.readText())
        } catch (e: Exception) {
            Timber.e(e, "Failed to read custom manga info for id=$id")
            null
        }
    }

    fun saveCustomInfo(customInfo: CustomMangaInfo) {
        val file = getCustomInfoFile(customInfo.id)
        try {
            file.writeText(json.encodeToString(customInfo))
        } catch (e: Exception) {
            Timber.e(e, "Failed to save custom manga info for id=${customInfo.id}")
        }
    }

    fun deleteCustomInfo(manga: Manga): Boolean {
        val id = manga.id ?: return false
        val file = getCustomInfoFile(id)
        return file.exists() && file.delete()
    }

    fun applyCustomInfo(manga: Manga) {
        val customInfo = getCustomInfo(manga) ?: return
        customInfo.title?.let { if (it.isNotBlank()) manga.title = it }
        customInfo.author?.let { manga.author = it }
        customInfo.artist?.let { manga.artist = it }
        customInfo.description?.let { manga.description = it }
    }
}
