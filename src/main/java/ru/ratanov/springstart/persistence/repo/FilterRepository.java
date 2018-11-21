package ru.ratanov.springstart.persistence.repo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.ratanov.springstart.persistence.model.Filter;
import ru.ratanov.springstart.web.Cookies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterRepository {

    private static HashMap<String, String> filters = new HashMap<String, String>() {
        {
            put("t", "Категория");
            put("d", "Год выпуска");
            put("k", "Страна");
            put("w", "Добавлен");
        }
    };

    public static HashMap<String, List<Filter>> getFilters() {

        String BASE_URL = "https://kinozal.guru";
        HashMap<String, List <Filter>> items = new HashMap<>();

        try {
            Document doc = Jsoup
                    .connect(BASE_URL.concat("/top.php"))
                    .cookies(Cookies.getCookies()).get();

            for (Map.Entry<String, String> pair : filters.entrySet()) {
                List<Filter> filterList = new ArrayList<>();
                Element param = doc.selectFirst("select[name=" + pair.getKey() +"]");
                for (Element option : param.select("option")) {
                    Filter filter = new Filter();
                    filter.setKey(pair.getKey());
                    filter.setValue(option.attr("value"));
                    filter.setTitle(option.text());

                    filterList.add(filter);
                }
                items.put(pair.getValue(), filterList);
            }

            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
