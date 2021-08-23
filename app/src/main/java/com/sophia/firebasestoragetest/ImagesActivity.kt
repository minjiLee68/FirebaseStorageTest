package com.sophia.firebasestoragetest

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.core.Context
import com.sophia.firebasestoragetest.databinding.ActivityImageBinding

class ImagesActivity: AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding
    private var adapter = ImageAdapter()

    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mUploads: ArrayList<Upload>
    private lateinit var upload: Upload

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")

        setDatabase()
        initRecyclerview()

    }

    private fun setDatabase() {
        mDatabaseRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("RestrictedApi")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot: DataSnapshot in snapshot.children) {
                    upload = postSnapshot.getValue(upload.javaClass)!!
                    mUploads.add(upload)
                }
                adapter.ImageAdapters(Context(), mUploads)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ImagesActivity, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initRecyclerview() {
        binding.recyclerview.let {
            it.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            it.adapter = adapter
            it.setHasFixedSize(true)
        }
    }
}