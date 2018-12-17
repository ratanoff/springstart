package ru.ratanov.springstart.persistence.repo

import org.jsoup.Jsoup
import ru.ratanov.springstart.web.Cookies

class MagnetRepository {

    companion object {
        fun getMagnetLink(filmUrl: String): String {
            val doc = Jsoup
                    .connect(filmUrl.replace("details.php?",
                            "get_srv_details.php?action=2&"))
                    .cookies(Cookies.getCookies())
                    .get()

            return doc.select("ul").select("li").first().text()
                    .replace("Инфо хеш: ",
                            "magnet:?xt=urn:btih:")
        }
    }
}