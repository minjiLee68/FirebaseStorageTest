package com.sophia.firebasestoragetest

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.*
import com.sophia.firebasestoragetest.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PICK_IMAGE_REQUEST = 1;
    private lateinit var mImageUri: Uri
    private val mstorageRef = FirebaseStorage.getInstance().getReference("uploads")
    private val mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mButtonChooseImage()
        mButtonUpload()
        mTextViewShowUploads()
    }

    private fun mButtonChooseImage() {
        binding.buttonChooseImage.setOnClickListener {
            openFileChooser()
        }
    }

    private fun mButtonUpload() {
        binding.buttonUpload.setOnClickListener {
            uploadFile()
        }
    }

    private fun mTextViewShowUploads() {
        binding.textviewUpload.setOnClickListener {
            openImagesActivity()
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
             mImageUri = data.data!!

            Glide.with(this).load(mImageUri).into(binding.imageview)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val CR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(CR.getType(uri))
    }

    private fun uploadFile() {
        if (mImageUri != null) {
            //System.currentTimeMillis -> 현재시간 가져오기 getFileExtension -> 파일 확장자 가져오기
            val fileReference:StorageReference = mstorageRef.child(System.currentTimeMillis().toString()+"."+getFileExtension(mImageUri))

            fileReference.putFile(mImageUri)
                .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                    override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                        val handler = Handler()
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                binding.progressBar.setProgress(0)
                            }

                        }, 500)
                        Toast.makeText(this@MainActivity,"Upload successful", Toast.LENGTH_SHORT).show()
//                        mstorageRef.downloadUrl.addOnSuccessListener { uri ->
//                            var contentDTO = Upload()
//
//                            contentDTO.name = binding.edittextFilename.text.toString().trim()
//                            contentDTO.ImageUri = uri.toString()
//
//                        }
                        val upload = Upload(binding.edittextFilename.text.toString().trim(), mstorageRef.downloadUrl.toString())
                        val uploadId: String? = mDatabaseRef.push().key
                        mDatabaseRef.child(uploadId!!).setValue(upload)
                    }

                })
                .addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(p0: Exception) {
                        Toast.makeText(this@MainActivity, p0.message, Toast.LENGTH_SHORT).show()
                    }

                })
                .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot> {
                    override fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                        val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount)
                        binding.progressBar.progress = (progress.toInt())
                    }

                })

        } else {
            Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagesActivity() {
        val intent = Intent(this, ImagesActivity::class.java)
        startActivity(intent)
    }

}