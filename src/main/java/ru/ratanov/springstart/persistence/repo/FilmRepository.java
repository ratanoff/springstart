package ru.ratanov.springstart.persistence.repo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.web.util.UriComponentsBuilder;
import ru.ratanov.springstart.persistence.model.Film;
import ru.ratanov.springstart.web.Cookies;

import java.io.IOException;
import java.util.HashMap;

public class FilmRepository {

    private static final String KEY_TITLE = "Название";
    private static final String KEY_ORIGINAL_TITLE = "Оригинальное название";
    private static final String KEY_YEAR = "Год выпуска";
    private static final String KEY_GENRE = "Жанр";
    private static final String KEY_PRODUCTION = "Выпущено";
    private static final String KEY_DIRECTOR = "Режиссер";
    private static final String KEY_CAST = "В ролях";
    private static final String KEY_QUALITY = "Качество";
    private static final String KEY_VIDEO = "Видео";
    private static final String KEY_AUDIO = "Аудио";
    private static final String KEY_SIZE = "Размер";
    private static final String KEY_LENGTH = "Продолжительность";
    private static final String KEY_TRANSLATE = "Перевод";
    private static final String KEY_CATEGORY = "Категория";

    private static HashMap<String, String> hashMap = new HashMap<>();

    public static Film getFilm(String id) {
        String BASE_URL = "https://kinozal.guru/details.php";
        String BROWSE_URL = "https://kinozal.guru";
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("id", id)
                .build()
                .toString();

        try {
            Document doc = Jsoup
                    .connect(url)
                    .cookies(Cookies.getCookies())
                    .get();

            fillHashMap(doc.select("h2").html());
            fillHashMap(doc.select("div.justify.mn2.pad5x5").html());

            Film film = new Film();

            film.setLink(url);
            film.setId(id);
            film.setTitle(doc.select("h1").text());

            String tmp = doc.select("img.p200").attr("src");
            String pictureUrl = null;

            if (tmp.contains("poster")) {
                pictureUrl = "https://kinozal.guru" + tmp;
            } else {
                pictureUrl = tmp;
            }

            film.setPosterUrl(pictureUrl);
            film.setQuality(getPair(KEY_QUALITY));
            film.setVideo(getPair(KEY_VIDEO));
            film.setAudio(getPair(KEY_AUDIO));
            film.setSize(getPair(KEY_SIZE));
            film.setLength(getPair(KEY_LENGTH));
            film.setTranslate((hashMap.get(KEY_TRANSLATE) == null) ? KEY_TRANSLATE + ": Не требуется" : getPair(KEY_TRANSLATE));
            film.setYear(getPair(KEY_YEAR));
            film.setGenre(getPair(KEY_GENRE));

            Elements elements = doc.select("ul.men.w200").select("li");
            for (Element element : elements) {
                if (element.text().contains("Раздают")) {
                    film.setSeeds(element.select("span").text());
                }
                if (element.text().contains("Обновлен")) {
                    film.setDateTitle("Обновлен");
                    film.setDate(element.select("span").text());
                }
                if (element.text().contains("Залит")) {
                    film.setDateTitle("Залит");
                    film.setDate(element.select("span").text());
                }
                if (element.text().contains("Кинопоиск")) {
                    film.setRating(element.text().substring(9));
                    film.setKpUrl(element.select("a").attr("href"));
                }
                if (element.text().contains("Трейлер")) {
                    film.setTrailerUrl(element.select("a").attr("href"));
                }
            }

            film.setDescription(doc.select("div.bx1.justify").select("p").text());
            film.setSameLink(BROWSE_URL + doc.select("td.w90p").select("a").attr("href"));

            return film;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void fillHashMap(String html) {
        Document doc = Jsoup.parse(html);
        doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
        doc.select("br").append("_break_");
        String string = Jsoup.clean(doc.html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        String[] block = string.split("_break_");
        for (int i = 0; i < block.length; i++) {
            block[i] = block[i].trim();
            String[] pairs = block[i].split(": ");
            hashMap.put(pairs[0], pairs[1]);
        }
    }

    private static String getPair(String key) {
        return key + ": " + hashMap.get(key);
    }

}
