package ru.ratanov.springstart.persistence.repo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.ratanov.springstart.persistence.model.Film;
import ru.ratanov.springstart.persistence.model.SameItem;
import ru.ratanov.springstart.web.Cookies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SameRepository {

    public static List<SameItem> getSameById(String id) {
        try {
            Integer.parseInt(id);
            Film film = FilmRepository.getFilm(id);
            return getSameItems(film.getSameLink());

        } catch (NumberFormatException e) {
            return getSameItems(id);
        }
    }


    public static List<SameItem> getSameItems(String sameLink) {
        String BASE_URL = "https://kinozal.guru";
        List<SameItem> items = new ArrayList<>();

        try {
            Document doc = Jsoup
                    .connect(sameLink)
                    .cookies(Cookies.getCookies())
                    .get();

            Elements elements = doc.select("tr.bg");
            for (Element row : elements) {
                SameItem sameItem = new SameItem();

                sameItem.setTitle(row.select("td.nam").text());
                sameItem.setSize(row.select("td").get(3).text());
                sameItem.setSeeds(row.select("td").get(4).text());
                sameItem.setDate(row.select("td").get(6).text());
                sameItem.setPageUrl(BASE_URL + row.select("td.nam").select("a").attr("href"));

                items.add(sameItem);
            }
            return items;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
