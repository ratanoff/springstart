package ru.ratanov.springstart.persistence.repo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.util.UriComponentsBuilder;
import ru.ratanov.springstart.persistence.model.SearchItem;
import ru.ratanov.springstart.web.Cookies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchRepository {

    public static List<SearchItem> search(String query, String filter, String sort) {

        String BASE_URL = "https://kinozal.guru";
        String SEARCH_URL = BASE_URL + "/browse.php";

        List<SearchItem> items = new ArrayList<>();

        int pages = 0;

            String url = UriComponentsBuilder.fromUriString(SEARCH_URL)
                .queryParam("s", query)
                .build()
                .toString();

        System.out.println(url);

        try {
            Document doc = Jsoup
                    .connect(url)
                    .cookies(Cookies.getCookies())
                    .get();

            Elements elements = doc.select("td");
            for (Element element : elements) {
                if (element.text().contains("Найдено")) {
                    pages = getNumberPages(element.text());
                    break;
                }
            }

            for (int i = 0; i < pages; i++) {
                String pageUrl = UriComponentsBuilder.fromUriString(SEARCH_URL)
                        .queryParam("s", query)
                        .queryParam("t", filter)
                        .queryParam("f", sort)  .build()
                        .toString();

                Document document = Jsoup.connect(pageUrl)
                        .cookies(Cookies.getCookies())
                        .get();

                Elements sElements = document.select("tr.bg");

                for (Element element : sElements) {
                    String title = element.select("td.nam").select("a").text();
                    String link = BASE_URL + element.select("td.nam").select("a").attr("href");
                    String size = element.select("td.s").get(1).text();
                    String seeds = element.select("td.sl_s").text();
                    String date = element.select("td.s").get(2).text();

                    SearchItem item = new SearchItem();
                    item.setTitle(title);
                    item.setLink(link);
                    item.setSize(size);
                    item.setSeeds(seeds);
                    item.setDate(date);

                    items.add(item);

                }
            }

            return items;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int getNumberPages(String found) {
        int count = Integer.parseInt(found.split(" ")[1]);
        int a = count / 50;
        return (a * 50 == count) ? a : a + 1;
    }
}