package com.example.catchthemoment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var momentsList: MutableList<Moment>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MomentsAdapter

    private val ONE_HOUR = 60 * 60 * 1000 // 1 час в миллисекундах
    private val ONE_DAY = 24 * ONE_HOUR // 1 день в миллисекундах
    private val PICK_IMAGE_REQUEST = 1 // Любое уникальное целочисленное значение


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        momentsList = mutableListOf()
        recyclerView = findViewById(R.id.recyclerView)
        adapter = MomentsAdapter(momentsList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val readyButton = findViewById<ImageButton>(R.id.readyButton)
        val textEditText = findViewById<EditText>(R.id.textEditText)
        val photoImageView = findViewById<ImageView>(R.id.photoImageView)

        photoImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
                // Получить URI выбранного изображения
                val selectedImageUri = data?.data

                // Установить выбранное изображение в photoImageView
                photoImageView.setImageURI(selectedImageUri)
            }
        }

        readyButton.setOnClickListener {
            val text = textEditText.text.toString()
            val timestamp = System.currentTimeMillis()
            val selectedImageUri: Uri? = null
            // Здесь должен быть код для получения выбранной фотографии

            val moment = selectedImageUri?.let { Moment(it, text, timestamp) }
            if (moment != null) {
                momentsList.add(moment)
                adapter.notifyItemInserted(momentsList.size - 1)
            }

    // Очистить текстовое поле и сбросить изображение
            textEditText.text.clear()
            photoImageView.setImageURI(null)

        }
    }

    // Создайте адаптер для RecyclerView
    inner class MomentsAdapter(private val momentsList: List<Moment>) :
        RecyclerView.Adapter<MomentsAdapter.MomentViewHolder>() {

        inner class MomentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
            val textView: TextView = itemView.findViewById(R.id.textView)
            val indicatorImageView: ImageView = itemView.findViewById(R.id.indicatorImageView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.moment_item, parent, false)
            return MomentViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
            val moment = momentsList[position]
            holder.photoImageView.setImageURI(moment.photoUri)
            holder.textView.text = moment.text

            val timeAgo = calculateTimeAgo(moment.timestamp)
            if (timeAgo >= ONE_HOUR) {
                holder.indicatorImageView.setImageResource(R.drawable.yellow_indicator)
            } else if (timeAgo >= ONE_DAY) {
                holder.indicatorImageView.setImageResource(R.drawable.red_indicator)
            } else {
                holder.indicatorImageView.setImageResource(0) // Очистить индикатор
            }
        }

        override fun getItemCount(): Int {
            return momentsList.size
        }
    }

    private fun calculateTimeAgo(timestamp: Long): Long {
        val currentTime = System.currentTimeMillis()
        return currentTime - timestamp
    }
}


