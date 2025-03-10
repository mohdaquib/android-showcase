package com.igorwojda.showcase.feature.album.presentation.albumlist.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.igorwojda.showcase.base.common.delegate.observer
import com.igorwojda.showcase.base.presentation.ext.hide
import com.igorwojda.showcase.base.presentation.ext.setOnDebouncedClickListener
import com.igorwojda.showcase.base.presentation.ext.show
import com.igorwojda.showcase.feature.album.R
import com.igorwojda.showcase.feature.album.databinding.ItemAlbumListBinding
import com.igorwojda.showcase.feature.album.domain.model.Album

internal class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    var albums: List<Album> by observer(listOf()) {
        notifyDataSetChanged()
    }

    private var onDebouncedClickListener: ((album: Album) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlbumListBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    fun setOnDebouncedClickListener(listener: (album: Album) -> Unit) {
        onDebouncedClickListener = listener
    }

    internal inner class ViewHolder(private val binding: ItemAlbumListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var url by observer<String?>(null) {
            binding.coverErrorImageView.hide()

            if (it == null) {
                setDefaultImage()
            } else {
                binding.coverImageView.load(it) {
                    crossfade(true)
                    error(R.drawable.ic_image)
                    @Suppress("detekt.MagicNumber")
                    transformations(RoundedCornersTransformation(10F))
                }
            }
        }

        fun bind(album: Album) {
            itemView.setOnDebouncedClickListener { onDebouncedClickListener?.invoke(album) }
            url = album.getDefaultImageUrl()
        }

        private fun setDefaultImage() {
            binding.coverErrorImageView.show()
        }
    }
}
