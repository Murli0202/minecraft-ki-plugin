package de.kibot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    @Override
    public void onEnable() {
        // Config erstellen falls nicht vorhanden
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("KI-Plugin gestartet! Befehl: " + getBefehl());
    }

    // ── Config-Werte lesen ─────────────────────────────────────────────────
    private String getBefehl()        { return getConfig().getString("befehl", "!ki"); }
    private String getModell()        { return getConfig().getString("modell", "mistral"); }
    private String getOllamaUrl()     { return getConfig().getString("ollama-url", "http://localhost:11434/api/generate"); }
    private String getPromptPrefix()  { return getConfig().getString("prompt-prefix", "Antworte kurz auf Deutsch, maximal 2 Saetze: "); }
    private String getChatPrefix()    { return getConfig().getString("chat-prefix", "§b[KI] §f"); }
    private String getWarteMeldung()  { return getConfig().getString("warte-meldung", "§7Einen Moment..."); }
    private String getHilfeMeldung()  { return getConfig().getString("hilfe-meldung", "Beispiel: !ki Was ist ein Creeper?"); }
    private String getFehlerMeldung() { return getConfig().getString("fehler-meldung", "§cFehler beim Abrufen der KI."); }
    private int    getTimeout()       { return getConfig().getInt("timeout-sekunden", 60) * 1000; }
    private boolean nurAnfrager()     { return getConfig().getBoolean("nur-anfrager-sieht-antwort", false); }
    // ───────────────────────────────────────────────────────────────────────

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String nachricht = event.getMessage();
        if (!nachricht.startsWith(getBefehl())) return;

        String frage = nachricht.substring(getBefehl().length()).trim();
        if (frage.isEmpty()) {
            event.getPlayer().sendMessage("§e" + getHilfeMeldung());
            return;
        }

        event.getPlayer().sendMessage(getWarteMeldung());

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                String antwort = frageOllama(frage);
                getServer().getScheduler().runTask(this, () -> {
                    if (nurAnfrager()) {
                        event.getPlayer().sendMessage(getChatPrefix() + antwort);
                    } else {
                        getServer().broadcastMessage(getChatPrefix() + antwort);
                    }
                });
            } catch (Exception e) {
                getLogger().severe("Ollama Fehler: " + e.getMessage());
                event.getPlayer().sendMessage(getFehlerMeldung());
            }
        });
    }

    // /ki reload Befehl
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kireload")) {
            if (!sender.hasPermission("kiplugin.reload")) {
                sender.sendMessage("§cKeine Berechtigung!");
                return true;
            }
            reloadConfig();
            sender.sendMessage("§aKI-Plugin Config neu geladen!");
            return true;
        }
        return false;
    }

    private String frageOllama(String frage) throws Exception {
        String body = "{\"model\":\"" + getModell() + "\"," +
                      "\"prompt\":\"" + getPromptPrefix() +
                      frage.replace("\"", "'") + "\"," +
                      "\"stream\":false}";

        HttpURLConnection con = (HttpURLConnection) new URL(getOllamaUrl()).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        con.setConnectTimeout(30000);
        con.setReadTimeout(getTimeout());

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
