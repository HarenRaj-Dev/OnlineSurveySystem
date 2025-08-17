package com.example.survey.store;

import com.example.survey.model.SurveyResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResponseStore {
    private final String csvPath;
    private final List<SurveyResponse> responses = new ArrayList<>();

    public ResponseStore(String csvPath) {
        this.csvPath = csvPath;
        FileUtil.ensureFile(csvPath); // create file if missing
        load();
    }

    public synchronized void add(SurveyResponse r) {
        responses.add(r);
        appendToCsv(r);
    }

    public synchronized List<SurveyResponse> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(responses));
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(csvPath), StandardCharsets.UTF_8))) {
            String line;
            // Skip header if present
            boolean headerChecked = false;
            while ((line = br.readLine()) != null) {
                if (!headerChecked) {
                    headerChecked = true;
                    if (line.startsWith("timestamp,")) continue;
                }
                String[] parts = parseCsvLine(line);
                if (parts == null || parts.length < 5) continue;
                responses.add(new SurveyResponse(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        } catch (IOException ignored) { }
    }

    private void appendToCsv(SurveyResponse r) {
        boolean fileEmpty = new File(csvPath).length() == 0;
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(csvPath, true), StandardCharsets.UTF_8))) {
            if (fileEmpty) {
                bw.write("timestamp,name,email,rating,comments");
                bw.newLine();
            }
            bw.write(escape(r.getTimestampIso())); bw.write(",");
            bw.write(escape(r.getName()));        bw.write(",");
            bw.write(escape(r.getEmail()));       bw.write(",");
            bw.write(escape(r.getRating()));      bw.write(",");
            bw.write(escape(r.getComments()));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String v = s.replace("\"", "\"\"");
        return needQuotes ? "\"" + v + "\"" : v;
    }

    private static String[] parseCsvLine(String line) {
        if (line == null) return null;
        ArrayList<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') { // escaped quote
                        sb.append('"'); i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    sb.append(c);
                }
            } else {
                if (c == ',') {
                    out.add(sb.toString()); sb.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    sb.append(c);
                }
            }
        }
        out.add(sb.toString());
        return out.toArray(new String[0]);
    }
}