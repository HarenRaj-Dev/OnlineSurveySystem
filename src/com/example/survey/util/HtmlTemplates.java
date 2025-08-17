package com.example.survey.util;

import com.example.survey.model.SurveyResponse;
import java.util.List;

public class HtmlTemplates {

    public static String homePage() {
        return """
            <!doctype html>
            <html lang="en">
            <head>
              <meta charset="utf-8"/>
              <meta name="viewport" content="width=device-width,initial-scale=1"/>
              <title>Online Survey</title>
              <style>
                body{font-family:system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;margin:0;background:#f6f7fb}
                .container{max-width:720px;margin:40px auto;background:#fff;padding:24px;border-radius:16px;box-shadow:0 10px 30px rgba(0,0,0,.06)}
                h1{margin-top:0}
                label{display:block;margin:.5rem 0 .25rem;font-weight:600}
                input,select,textarea{width:100%;padding:.7rem;border:1px solid #dcdfea;border-radius:10px;font-size:1rem}
                textarea{min-height:120px;resize:vertical}
                button{margin-top:1rem;padding:.8rem 1.2rem;border:0;border-radius:10px;background:#3b82f6;color:#fff;font-weight:700;cursor:pointer}
                small{color:#555}
                .footer{margin-top:20px;color:#666}
              </style>
            </head>
            <body>
              <div class="container">
                <h1>Customer Satisfaction Survey</h1>
                <p>Please share your feedback. Fields marked * are required.</p>
                <form method="POST" action="/submit">
                  <label for="name">Name *</label>
                  <input id="name" name="name" required />

                  <label for="email">Email *</label>
                  <input id="email" name="email" type="email" required />

                  <label for="rating">Overall Rating *</label>
                  <select id="rating" name="rating" required>
                    <option value="">Select…</option>
                    <option>5</option><option>4</option><option>3</option><option>2</option><option>1</option>
                  </select>

                  <label for="comments">Comments</label>
                  <textarea id="comments" name="comments" placeholder="What did we do well? What can we improve?"></textarea>

                  <button type="submit">Submit Response</button>
                </form>
                <p class="footer"><small><a href="/admin">Admin dashboard</a></small></p>
              </div>
            </body>
            </html>
            """;
    }

    public static String thankYouPage() {
        return """
            <!doctype html>
            <html><head><meta charset="utf-8"><title>Thank You</title>
            <meta name="viewport" content="width=device-width,initial-scale=1"/>
            <style>body{font-family:system-ui;background:#f6f7fb;display:grid;place-items:center;height:100vh}
            .card{background:#fff;padding:24px;border-radius:16px;box-shadow:0 10px 30px rgba(0,0,0,.06);max-width:520px}
            a{display:inline-block;margin-top:14px;text-decoration:none;color:#3b82f6;font-weight:700}</style>
            </head><body>
              <div class="card">
                <h2>Thank you! 🎉</h2>
                <p>Your response has been recorded.</p>
                <a href="/">Submit another response</a>
              </div>
            </body></html>
            """;
    }

    public static String errorPage(String msg) {
        String safe = msg == null ? "Invalid request." : msg;
        return """
            <!doctype html>
            <html><head><meta charset="utf-8"><title>Error</title>
            <meta name="viewport" content="width=device-width,initial-scale=1"/>
            <style>body{font-family:system-ui;background:#fff;color:#b91c1c;padding:30px}</style>
            </head><body><h2>⚠️ Error</h2><p>""" + escapeHtml(safe) + """
            </p><p><a href="/">Back to form</a></p></body></html>
            """;
    }

    public static String adminPage(List<SurveyResponse> list) {
        StringBuilder rows = new StringBuilder();
        for (SurveyResponse r : list) {
            rows.append("<tr>")
                .append("<td>").append(escapeHtml(r.getTimestampIso())).append("</td>")
                .append("<td>").append(escapeHtml(r.getName())).append("</td>")
                .append("<td>").append(escapeHtml(r.getEmail())).append("</td>")
                .append("<td>").append(escapeHtml(r.getRating())).append("</td>")
                .append("<td>").append(escapeHtml(r.getComments())).append("</td>")
                .append("</tr>");
        }
        return """
            <!doctype html>
            <html lang="en"><head><meta charset="utf-8"/>
            <meta name="viewport" content="width=device-width,initial-scale=1"/>
            <title>Admin Dashboard</title>
            <style>
              body{font-family:system-ui;background:#f6f7fb;margin:0}
              .container{max-width:1000px;margin:40px auto;background:#fff;padding:24px;border-radius:16px;box-shadow:0 10px 30px rgba(0,0,0,.06)}
              table{width:100%;border-collapse:collapse}
              th,td{padding:10px;border-bottom:1px solid #eee;text-align:left;vertical-align:top}
              th{background:#fafafa}
              .muted{color:#666}
              a{color:#3b82f6;text-decoration:none}
            </style>
            </head><body>
              <div class="container">
                <h1>Admin Dashboard</h1>
                <p class="muted">Total responses: """
             + list.size() + 
            """
                </p>
                <table>
                  <thead><tr>
                    <th>Timestamp (UTC)</th><th>Name</th><th>Email</th><th>Rating</th><th>Comments</th>
                  </tr></thead>
                  <tbody>""" + rows + """
                  </tbody>
                </table>
                <p><a href="/">← Back to form</a></p>
              </div>
            </body></html>
            """;
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&#39;");
    }
}