package ru.ratanov.springstart.persistence.repo

import org.jsoup.Jsoup
import ru.ratanov.springstart.persistence.model.Filter
import ru.ratanov.springstart.persistence.model.Param
import ru.ratanov.springstart.web.Cookies

class FilterRepository {

    companion object {
        private val filters = hashMapOf(
                "t" to "Категория",
                "d" to "Год выпуска",
                "k" to "Страна",
                "w" to "Добавлен"
        )


        fun getFilters(): List<Filter> {
            val BASE_URL = "https://kinozal.guru"
            val items = arrayListOf<Filter>()

            val doc = Jsoup
                    .connect("$BASE_URL/top.php")
                    .cookies(Cookies.getCookies())
                    .get()

            filters.forEach { key, title ->
                val options = doc.selectFirst("select[name=$key]").select("option")
                val params = options.map { Param(it.attr("value"), it.text()) }
                items.add(Filter(title, key, params))
            }

            return items
        }
    }



}