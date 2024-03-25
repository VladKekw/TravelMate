package com.example.travelmate

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URL

class SecondActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tour_layout)

        val tourContainer = findViewById<LinearLayout>(R.id.tourContainer)

        val budget = intent.getIntExtra("budget", 1000000)

        // Список URL-ов для парсинга
        val tourDataUrls = listOf(
            "https://kraina-ua.com/uk/tours/tours-ukraine",
            "https://lviv-tourist.info/ekskursii/ekskursiyi-za-misto/"
        )

        for (tourDataUrl in tourDataUrls) {
            parseAndDisplayTourData(tourDataUrl, tourContainer, budget)
        }
    }

    private fun parseAndDisplayTourData(
        tourDataUrl: String,
        tourContainer: LinearLayout,
        budget: Int
    ) {
        Thread {
            try {
                val doc: Document = Jsoup.connect(tourDataUrl).get()
//              Log.d("DEBUG", "HTML загружен: $doc")
                var lastPage: Int? = null
                if (tourDataUrl.contains("kraina-ua.com")) {
                    lastPage = doc.select(".last").select(".target").text().toInt()
                } else if (tourDataUrl.contains("lviv-tourist.info")) {
                    lastPage = 3
                }

                for (i in 1..lastPage!!) {
                    var pages: Document? = null
                    if (tourDataUrl.contains("kraina-ua.com")) {
                        pages = Jsoup.connect("$tourDataUrl?p=$i").get()
                    } else if (tourDataUrl.contains("lviv-tourist.info")) {
                        pages = Jsoup.connect("$tourDataUrl/page/$i").get()
                    }
                    Log.d("DEBUG", "tourDataUrl: $tourDataUrl?p=$i")
                    var tourBlocks: Elements? = null
                    if (tourDataUrl.contains("kraina-ua.com")) {
                        tourBlocks = pages?.select(".tour-itm")
                    } else if (tourDataUrl.contains("lviv-tourist.info")) {
                        tourBlocks = pages?.select(".tour__item")
                    }
                    Log.d("DEBUG", "Найдено блоков туров: ${tourBlocks?.size}")

                    runOnUiThread {
                        if (tourBlocks != null) {
                            for (block in tourBlocks) {
                                val view = LayoutInflater.from(this).inflate(R.layout.tour_layout, null)

                                val tourImage = view.findViewById<ImageView>(R.id.tourImage)
                                val tourName = view.findViewById<TextView>(R.id.tourName)
                                val tourPrice = view.findViewById<TextView>(R.id.tourPrice)

                                // Отримання даних з блоку
                                var imageUrlRelativePath: String? = null
                                var fullImageUrl: String? = null
                                var name: String? = null
                                var priceText: String? = null

                                if (tourDataUrl.contains("kraina-ua.com")) {
                                    imageUrlRelativePath = block.select(".tour-img img").attr("data-src")
                                    fullImageUrl = getFullImageUrl(tourDataUrl, imageUrlRelativePath)
                                    name = block.select(".tour-title").text()
                                    priceText = block.select(".price").text()
                                } else if (tourDataUrl.contains("lviv-tourist.info")) {
                                    fullImageUrl = block.select(".tour__item-img").attr("style")
                                        .substringAfter("url(").substringBeforeLast(")")
                                    name = block.select(".tour__item-title").text()
                                    priceText = block.select(".tour__item-price").text()
                                }

                                val price = priceText?.replace("[^0-9]".toRegex(), "")?.toIntOrNull()
                                val priceGrn = price.toString() + " грн"

                                Log.d("DEBUG", "Текст назви: $name")
                                Log.d("DEBUG", "Текст ціни: $priceText")
                                Log.d("DEBUG", "Ціна: $price")

                                // Виведення тура, якщо ціна знаходиться у вказаному діапазоні
                                if (price != null && price in 1..budget) {
                                    Glide.with(this)
                                        .load(fullImageUrl)
                                        .fitCenter()
                                        .into(tourImage)
                                    tourName.text = name
                                    tourPrice.text = priceGrn
                                    tourContainer.addView(view)
                                }
                                /*tourImage.setOnClickListener{
                                    val tourUrl = "https://kraina-ua.com/uk/tours/tours-ukraine"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tourUrl))
                                    startActivity(intent)
                                }*/
                            }
                        }
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ERROR", "Ошибка при загрузке данных: ${e.message}")
            }
        }.start()
    }

    private fun getBaseUrl(baseUrl: String): String {
        val url = URL(baseUrl)
        return "${url.protocol}://${url.host}"
    }

    private fun getFullImageUrl(baseUrl: String, relativePath: String?): String {
        val base = getBaseUrl(baseUrl)
        return "$base$relativePath"
    }
}