package ru.ratanov.springstart.web;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

public class Cookies {

    private static Map<String, String> cookies = null;

    public static Map<String, String> getCookies() {
        if (cookies == null) {
            cookies = updateCookies();
        }
        return cookies;
    }

    private static Map<String, String> updateCookies() {

        Connection.Response response = null;

        try {
            response = Jsoup
                    .connect("https://kinozal.guru/login.php")
                    .method(Connection.Method.GET)
                    .execute();

            response = Jsoup
                    .connect("https://kinozal.guru/takelogin.php")
                    .cookies(response.cookies())
                    .data("username", "rbaloo")
                    .data("password", "756530")
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.cookies();
    }
}
