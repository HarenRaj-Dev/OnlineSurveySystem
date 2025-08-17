package com.example.survey;

import com.example.survey.handlers.AdminHandler;
import com.example.survey.handlers.HomeHandler;
import com.example.survey.handlers.SubmitHandler;
import com.example.survey.store.ResponseStore;
import com.example.survey.store.FileUtil;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {
    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        // Ensure data folder exists
        FileUtil.ensureDir("data");

        // Initialize the store (loads existing CSV if present)
        ResponseStore store = new ResponseStore("data/responses.csv");

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Routes
        server.createContext("/", new HomeHandler());
        server.createContext("/submit", new SubmitHandler(store));
        server.createContext("/admin", new AdminHandler(store));  // simple read-only dashboard

        server.setExecutor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        server.start();

        System.out.println("Online Survey running at: http://localhost:" + PORT + "/");
        System.out.println("Admin dashboard:          http://localhost:" + PORT + "/admin");
    }
}
