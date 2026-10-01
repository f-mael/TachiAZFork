package eu.kanade.tachiyomi.ui.manga.info

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.data.glide.GlideApp
import eu.kanade.tachiyomi.data.glide.toMangaThumbnail
import eu.kanade.tachiyomi.databinding.EditMangaDialogBinding
import eu.kanade.tachiyomi.ui.base.controller.DialogController

class EditMangaDialog(bundle: Bundle? = null) : DialogController(bundle) {

    private lateinit var manga: Manga

    constructor(target: MangaInfoController, manga: Manga) : this() {
        targetController = target
        this.manga = manga
    }

    override fun onCreateDialog(savedViewState: Bundle?): Dialog {
        val activity = activity!!
        val target = targetController as? MangaInfoController
        if (!::manga.isInitialized) {
            val targetManga = target?.presenter?.manga
            if (targetManga != null) {
                manga = targetManga
            } else {
                return MaterialDialog(activity)
            }
        }
        val binding = EditMangaDialogBinding.inflate(LayoutInflater.from(activity))

        // Set initial values
        binding.mangaTitleEdit.setText(manga.title)
        binding.mangaAuthorEdit.setText(manga.author)
        binding.mangaArtistEdit.setText(manga.artist)
        binding.mangaDescriptionEdit.setText(manga.description)

        // Load cover preview
        GlideApp.with(activity)
            .load(manga.toMangaThumbnail())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .centerCrop()
            .into(binding.mangaCoverPreview)

        val hasCustomCover = target?.hasCustomCover() == true
        binding.btnResetCover.isVisible = hasCustomCover

        binding.btnChangeCover.setOnClickListener {
            target?.openMangaCoverPicker(manga)
            dismissDialog()
        }

        binding.btnResetCover.setOnClickListener {
            target?.deleteMangaCover(manga)
            binding.btnResetCover.isVisible = false
            GlideApp.with(activity)
                .load(manga.toMangaThumbnail())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .into(binding.mangaCoverPreview)
        }

        val hasCustomOverrides = (target?.hasCustomInfo() == true) || hasCustomCover

        val dialog = MaterialDialog(activity)
            .title(R.string.action_edit_manga)
            .customView(view = binding.root, scrollable = true)
            .positiveButton(R.string.action_save) {
                val newTitle = binding.mangaTitleEdit.text?.toString()?.trim()
                val newAuthor = binding.mangaAuthorEdit.text?.toString()?.trim()
                val newArtist = binding.mangaArtistEdit.text?.toString()?.trim()
                val newDescription = binding.mangaDescriptionEdit.text?.toString()?.trim()

                target?.onMangaInfoEdited(newTitle, newAuthor, newArtist, newDescription)
            }
            .negativeButton(android.R.string.cancel)

        if (hasCustomOverrides) {
            dialog.neutralButton(R.string.action_reset_info) {
                target?.onResetMangaInfo()
            }
        }

        return dialog
    }
}
