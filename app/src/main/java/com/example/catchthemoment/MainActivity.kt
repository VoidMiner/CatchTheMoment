package com.example.catchthemoment

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CatchMomentService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Добавьте код для обработки события "Поймать момент" здесь
        // Этот код будет выполняться при запуске сервиса
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var momentsList: MutableList<Moment>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MomentsAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private val ONE_HOUR = 60 * 60 * 1000 // 1 час в миллисекундах
    private val ONE_DAY = 24 * ONE_HOUR // 1 день в миллисекундах
    private val PICK_IMAGE_REQUEST = 1 // Любое уникальное целочисленное значение
    private var selectedImageUri: Uri? = null

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
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val chooser = Intent.createChooser(pickIntent, "Выберите изображение")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(captureIntent))

            startActivityForResult(chooser, PICK_IMAGE_REQUEST)
        }


        readyButton.setOnClickListener {
            val text = textEditText.text.toString()
            val timestamp = System.currentTimeMillis()

            val moment = selectedImageUri?.let { Moment(it, text, timestamp) }
            if (moment != null) {
                momentsList.add(moment)
                adapter.notifyItemInserted(momentsList.size - 1)
            }

            // Очистить текстовое поле и сбросить изображение
            textEditText.text.clear()
            photoImageView.setImageURI(null)

            // Сохраняем список моментов в SharedPreferences
            saveMomentsToSharedPreferences()
        }

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("MomentsPref", Context.MODE_PRIVATE)

        // Восстановление списка моментов
        restoreMomentsFromSharedPreferences()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Получить URI выбранного изображения
            selectedImageUri = data?.data

            // Установить выбранное изображение в photoImageView
            val photoImageView = findViewById<ImageView>(R.id.photoImageView)
            photoImageView.setImageURI(selectedImageUri)
        }
    }

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

    // Сохранение списка моментов в SharedPreferences
    private fun saveMomentsToSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear()
        for ((index, moment) in momentsList.withIndex()) {
            editor.putString("moment_photo_uri_$index", moment.photoUri.toString())
            editor.putString("moment_text_$index", moment.text)
            editor.putLong("moment_timestamp_$index", moment.timestamp)
        }
        editor.apply()
    }

    // Восстановление списка моментов из SharedPreferences
    private fun restoreMomentsFromSharedPreferences() {
        momentsList.clear()
        var index = 0
        while (true) {
            val photoUri = sharedPreferences.getString("moment_photo_uri_$index", null)
            if (photoUri == null) {
                break
            }
            val text = sharedPreferences.getString("moment_text_$index", "") ?: ""
            val timestamp = sharedPreferences.getLong("moment_timestamp_$index", 0L)
            if (!photoUri.isNullOrEmpty()) {
                momentsList.add(Moment(Uri.parse(photoUri), text, timestamp))
            }
            index++
        }
        adapter.notifyDataSetChanged()
    }
}

data class Moment(val photoUri: Uri, val text: String, val timestamp: Long)

