package com.sophia.firebasestoragetest

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.core.Context
import com.sophia.firebasestoragetest.databinding.ImageItemBinding

class ImageAdapter: ListAdapter<Upload, ImageAdapter.MyViewHolder>(

    object : DiffUtil.ItemCallback<Upload>() {
        override fun areItemsTheSame(oldItem: Upload, newItem: Upload): Boolean =
           oldItem == newItem


        override fun areContentsTheSame(oldItem: Upload, newItem: Upload): Boolean =
            oldItem.name == newItem.name && oldItem.ImageUri == newItem.ImageUri

    }

) {

    private lateinit var mcontext: Context
    private lateinit var muploads: List<Upload>

    fun ImageAdapters(context: Context, uploads: List<Upload>) {
        mcontext = context
        muploads = uploads
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
       MyViewHolder(
           ImageItemBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val uploadCurrent: Upload = muploads.get(position)
        holder.textview.setText(uploadCurrent.name)
        Glide.with(Activity())
            .load(uploadCurrent.ImageUri)
            .fitCenter()
            .centerCrop()
            .into(holder.imageview)

    }

    inner class MyViewHolder(
        private val binding: ImageItemBinding,
    ):RecyclerView.ViewHolder(binding.root) {

        val textview = binding.itemName
        val imageview = binding.itemImage

//        fun bind(upload: Upload) {
//            binding.itemName.text = upload.name
//            binding.itemImage
//            Glide.with(Activity())
//                .load(upload.ImageUri)
//                .centerCrop()
//                .fitCenter()
//                .into(binding.itemImage)
//        }
    }
}


