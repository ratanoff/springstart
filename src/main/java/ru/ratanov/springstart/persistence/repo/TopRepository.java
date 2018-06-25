package ru.ratanov.springstart.persistence.repo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.ratanov.springstart.persistence.model.TopFilm;
import ru.ratanov.springstart.web.Cookies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TopRepository {

    public static List<TopFilm> getTop() {

        String BASE_URL = "https://kinozal.guru";
        List<TopFilm> items = new ArrayList<>();

        try {
            Document doc = Jsoup
                    .connect(BASE_URL.concat("/top.php"))
                    .cookies(Cookies.getCookies()).get();

            Elements elements = doc.select("div.bx1").select("a");
            for (Element element : elements) {
                String link = BASE_URL + element.select("a").attr("href");
                String tmp = element.select("a").select("img").attr("src");

                String pictureUrl;

                if (tmp.contains("poster")) {
                    pictureUrl = "https://kinozal.guru" + tmp;
                } else {
                    pictureUrl = tmp;
                }

                TopFilm topFilm = new TopFilm();
                topFilm.setPosterUrl(pictureUrl);
                topFilm.setLink(link);

                items.add(topFilm);
            }

            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
