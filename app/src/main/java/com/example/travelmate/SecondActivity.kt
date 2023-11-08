package com.example.travelmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.bumptech.glide.Glide
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.tour_layout)

        val tourDataUrl = "https://kraina-ua.com/ru/tours/tours-ukraine"
        val tourContainer = findViewById<LinearLayout>(R.id.tourContainer)

        // Загрузка данных и парсингы
        Thread {
            try {
                val doc: Document = Jsoup.connect(tourDataUrl).get()
                Log.d("DEBUG", "HTML загружен: $doc")

                val tourElements: Elements = doc.select(".txt-top, .tour-list, .price")

                Log.d("DEBUG", "Найдено элементов: ${tourElements.size}")

                runOnUiThread {
                    for (element in tourElements) {
                        val view = LayoutInflater.from(this).inflate(R.layout.tour_layout, null)

                        val tourImage = view.findViewById<ImageView>(R.id.tourImage)
                        val tourName = view.findViewById<TextView>(R.id.tourName)
                        val tourPrice = view.findViewById<TextView>(R.id.tourPrice)

                        // Получение данных из элемента
                        val imageUrlRelativePath = element.select(".tour-img img").attr("data-src")
                        val fullImageUrl = "https://kraina-ua.com$imageUrlRelativePath"
                        val name = element.select(".txt-top").text()
                        val price = element.select(".price").text()

                        Log.d("DEBUG", "URL изображения: $imageUrlRelativePath")
                        Log.d("DEBUG", "Название тура: $name")
                        Log.d("DEBUG", "Цена тура: $price")

                        Glide.with(this)
                            .load(fullImageUrl) // Полный URL изображения
                            .placeholder(R.drawable.white_background)
                            .centerCrop()
                            .into(tourImage)



                        tourName.text = name
                        tourPrice.text = price

                        tourContainer.addView(view)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ERROR", "Ошибка при загрузке данных: ${e.message}")
            }
        }.start()
}
}
