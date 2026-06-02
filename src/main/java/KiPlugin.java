package de.kibot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class KiPlugin extends JavaPlugin implements Listener {

    // ── Einstellungen ──────────────────────────
    private static final String BEFEHL = "!ki";
    private static final String MODELL  = "mistral";
    private static final String OLLAMA  = "http://localhost:11434/api/generate";
    // ───────────────────────────────────────────

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("KI-Plugin gestartet!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String nachricht = event.getMessage();
        if (!nachricht.startsWith(BEFEHL)) return;

        String frage = nachricht.substring(BEFEHL.length()).trim();
        if (frage.isEmpty()) {
            event.getPlayer().sendMessage("§eBeispiel: !ki Was ist ein Creeper?");
            return;
        }

        event.getPlayer().sendMessage("§7Einen Moment...");

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                String antwort = frageOllama(frage);
                getServer().getScheduler().runTask(this, () ->
                    getServer().broadcastMessage("§b[KI] §f" + antwort)
                );
            } catch (Exception e) {
                getLogger().severe("Ollama Fehler: " + e.getMessage());
                event.getPlayer().sendMessage("§cFehler beim Abrufen der KI.");
            }
        });
    }

    private String frageOllama(String frage) throws Exception {
        String body = "{\"model\":\"" + MODELL + "\"," +
                      "\"prompt\":\"Antworte kurz auf Deutsch, maximal 2 Saetze: " +
                      frage.replace("\"", "'") + "\"," +
                      "\"stream\":false}";

        HttpURLConnection con = (HttpURLConnection) new URL(OLLAMA).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        con.setConnectTimeout(30000);
        con.setReadTimeout(60000);

        try (OutputStream os = con.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        Scanner sc = new Scanner(con.getInputStream(), StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) sb.append(sc.nextLine());

        String response = sb.toString();
        int start = response.indexOf("\"response\":\"") + 12;
        int end = response.indexOf("\",\"done\"");
        return response.substring(start, end)
                       .replace("\\n", " ")
                       .replace("\\\"", "\"")
                       .trim();
    }
}
