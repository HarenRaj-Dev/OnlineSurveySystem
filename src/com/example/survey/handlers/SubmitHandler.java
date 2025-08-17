package com.example.survey.handlers;

import com.example.survey.model.SurveyResponse;
import com.example.survey.store.ResponseStore;
import com.example.survey.util.HtmlTemplates;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SubmitHandler implements HttpHandler {
    private final ResponseStore store;

    public SubmitHandler(ResponseStore store) {
        this.store = store;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        if (contentType == null || !contentType.toLowerCase().contains("application/x-www-form-urlencoded")) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        String form = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> fields = parseUrlEncoded(form);

        // Basic validation
        String name = trim(fields.getOrDefault("name", ""));
        String email = trim(fields.getOrDefault("email", ""));
        String rating = trim(fields.getOrDefault("rating", ""));
        String comments = trim(fields.getOrDefault("comments", ""));

        if (name.isEmpty() || email.isEmpty() || rating.isEmpty()) {
            byte[] body = HtmlTemplates.errorPage("Please fill Name, Email, and Rating.")
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(400, body.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(body); }
            return;
        }

        SurveyResponse resp = new SurveyResponse(Instant.now().toString(), name, email, rating, comments);

        store.add(resp);

        byte[] ok = HtmlTemplates.thankYouPage().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, ok.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(ok); }
    }

    private static String trim(String s) { return s == null ? "" : s.trim(); }

    private static Map<String, String> parseUrlEncoded(String s) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        for (String pair : s.split("&")) {
            if (pair.isEmpty()) continue;
            String[] kv = pair.split("=", 2);
            String k = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String v = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(k, v);
        }
        return map;
    }
}