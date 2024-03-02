package com.hrithikvish.ecommerce.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hrithikvish.ecommerce.R
import com.hrithikvish.ecommerce.databinding.ActivityMainBinding
import com.hrithikvish.ecommerce.models.Attributes
import java.io.ByteArrayOutputStream
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

class ColorsListRVAdapter(var context: Context, val binding: ActivityMainBinding) : RecyclerView.Adapter<ColorsListRVAdapter.colorViewHolder>() {

    private lateinit var colorsList : List<Attributes>

    fun setColorsList(colorsList: List<Attributes>) {
        this.colorsList = colorsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): colorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_colors_item, parent, false)
        return colorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return colorsList.size
    }

    //setting the default clicked position as 0 i.e. first item
    var previouslyClickedPosition = 0
    //flag so that it runs only the first time when flag is false
    var flag : Boolean = false;
    override fun onBindViewHolder(holder: colorViewHolder, position: Int) {
        val attribute : Attributes = colorsList.get(position)

        binding.colorsRvProgressBar.visibility = View.GONE
        Glide.with(context)
            .load(attribute.swatch_url)
            .apply(RequestOptions().override(200, 200))
            .into(holder.colorImage)

        //making the first item selected by default
        if(position == 0 && !flag) {
            holder.itemView.elevation = 8F
            flag = true
        }

        holder.colorImage.setOnClickListener {
            //based on item click updating the viewpager
            lateinit var viewPagerAdapter : ViewPagerAdapter
            val imagesList : ArrayList<String> = ArrayList()
            imagesList.add(attribute.images.get(0))
            imagesList.add(attribute.images.get(1))
            viewPagerAdapter = ViewPagerAdapter(context, imagesList)
            binding.viewpager.adapter = viewPagerAdapter
            binding.dotsIndicator.attachTo(binding.viewpager)

            //~unselecting the previously selected item
            if (previouslyClickedPosition != -1) {
                val rv = binding.root.findViewById<RecyclerView>(R.id.colors_rv)
                (rv as RecyclerView).findViewHolderForAdapterPosition(previouslyClickedPosition)?.itemView?.elevation = 0F
            }
            previouslyClickedPosition = position
            holder.itemView.elevation = 8F
        }
    }

    class colorViewHolder(view : View) : ViewHolder(view) {
        val colorImage : ImageView = view.findViewById(R.id.colorimg)
    }

    private fun compressImage(imageUri: Uri): Bitmap {
        val bytes: ByteArray
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, 100, 100, true)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        bytes = byteArrayOutputStream.toByteArray()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

}