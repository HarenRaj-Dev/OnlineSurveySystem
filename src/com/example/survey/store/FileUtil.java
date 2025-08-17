package com.example.survey.store;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static void ensureDir(String path) {
        File d = new File(path);
        if (!d.exists()) d.mkdirs();
    }
    public static void ensureFile(String path) {
        try {
            File f = new File(path);
            File parent = f.getParentFile();
            if (parent != null) parent.mkdirs();
            if (!f.exists()) f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create file: " + path, e);
        }
    }
}