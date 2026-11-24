public final class UiTemplate {
    private UiTemplate() {
    }

    public static String head(String title) {
        return """
                <!doctype html>
                <html>
                <head>
                <meta charset='UTF-8'>
                <meta name='viewport' content='width=device-width, initial-scale=1'>
                <title>__TITLE__</title>
                <style>
                  :root {
                    --bg: #05070c;
                    --panel: #0f1420;
                    --panel-2: #151c2b;
                    --text: #e7eef8;
                    --muted: #a0adbf;
                    --accent: #9dc83f;
                    --danger: #ff6b6b;
                    --line: #24324a;
                  }
                  * { box-sizing: border-box; }
                  body {
                    margin: 0;
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    color: var(--text);
                    background: radial-gradient(circle at top left, #121a2b, var(--bg) 45%);
                  }
                  .container { max-width: 1100px; margin: 28px auto; padding: 0 18px; }
                  .card {
                    background: linear-gradient(180deg, var(--panel), var(--panel-2));
                    border: 1px solid var(--line);
                    border-radius: 14px;
                    box-shadow: 0 16px 38px rgba(0,0,0,0.35);
                    padding: 20px;
                    margin-bottom: 18px;
                  }
                  h1, h2, h3 { margin-top: 0; }
                  p { color: var(--muted); }
                  a { color: var(--accent); text-decoration: none; }
                  a:hover { text-decoration: underline; }
                  .muted { color: var(--muted); }
                  .success, .error {
                    border-radius: 10px; padding: 10px 12px; margin-bottom: 14px; font-weight: 600;
                  }
                  .success { background: #1a2b11; color: #d7f3a8; border: 1px solid #406121; }
                  .error { background: #3a1515; color: #ffd4d4; border: 1px solid #8a3030; }
                  .row { display: flex; gap: 10px; flex-wrap: wrap; align-items: center; }
                  .grid-3 { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 12px; }
                  label { display: block; color: var(--muted); margin-bottom: 6px; }
                  input {
                    width: 100%;
                    background: #0b111b;
                    color: var(--text);
                    border: 1px solid #2a3b56;
                    border-radius: 10px;
                    padding: 10px 11px;
                    outline: none;
                  }
                  input:focus { border-color: var(--accent); }
                  .btn {
                    border: 1px solid #4f6623;
                    background: linear-gradient(180deg, #afdd46, #8fbf35);
                    color: #10210a;
                    font-weight: 700;
                    border-radius: 10px;
                    padding: 9px 14px;
                    cursor: pointer;
                  }
                  .btn.secondary {
                    border-color: #3b4b66;
                    background: #182335;
                    color: var(--text);
                  }
                  .btn.danger {
                    border-color: #8a3030;
                    background: #4f1e1e;
                    color: #ffdede;
                  }
                  table {
                    width: 100%;
                    border-collapse: collapse;
                    overflow: hidden;
                    border-radius: 10px;
                  }
                  th, td {
                    border-bottom: 1px solid #213048;
                    padding: 11px 10px;
                    text-align: left;
                    vertical-align: top;
                  }
                  th { background: #111a29; color: #cfe2ff; }
                  tr:hover td { background: #101929; }
                  .menu-link {
                    display: block;
                    border: 1px solid #2a3b56;
                    border-radius: 12px;
                    padding: 14px;
                    background: #10192a;
                  }
                  .menu-link strong { color: #d8ecff; }
                </style>
                </head>
                <body>
                <div class='container'>
                """.replace("__TITLE__", escapeHtml(title));
    }

    public static String footer() {
        return "</div></body></html>";
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}

