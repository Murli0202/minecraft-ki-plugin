package de.kibot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class KiPlugin extends JavaPlugin implements Listener {

    // Current plugin version — bump this with each release
    private static final String PLUGIN_VERSION = "1.2";

    // Cached update check result (null = not checked yet)
    private String neuesteVersion = null;
    private boolean updateVerfuegbar = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("KI-Plugin v" + PLUGIN_VERSION + " gestartet! Befehl: " + getBefehl());

        // Check for updates once on startup (async, so server doesn't freeze)
        getServer().getScheduler().runTaskAsynchronously(this, this::pruefeAufUpdates);
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
    
    // Cow Protection Feature
    private boolean isCowProtectionEnabled() { return getConfig().getBoolean("cow-protection.enabled", true); }
    private String getCowKickMessage() { return getConfig().getString("cow-protection.kick-message", "§cDon't kill cows on this server"); }
    private String getCowDeathMessage() { return getConfig().getString("cow-protection.death-message", "§e[Cow Protection] §fA cow has died!"); }
    // ───────────────────────────────────────────────────────────────[...]

    // ── Update-Check ───────────────────────────────────────────────────────
    private void pruefeAufUpdates() {
        try {
            // Fetch latest release from GitHub
            String apiUrl = "https://api.github.com/repos/Murli0202/minecraft-ki-plugin/releases/latest";
            HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/vnd.github+json");
            con.setRequestProperty("User-Agent", "KiPlugin/" + PLUGIN_VERSION);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            if (con.getResponseCode() != 200) {
                getLogger().info("[Update] Konnte GitHub nicht erreichen (Code: " + con.getResponseCode() + ")");
                return;
            }

            Scanner sc = new Scanner(con.getInputStream(), StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) sb.append(sc.nextLine());
            String json = sb.toString();

            // Parse "tag_name" field from JSON (e.g. "v1.1" or "1.1")
            int start = json.indexOf("\"tag_name\":\"") + 12;
            int end   = json.indexOf("\"", start);
            if (start < 12 || end < 0) return;

            neuesteVersion = json.substring(start, end).replace("v", "").trim();

            if (!neuesteVersion.equals(PLUGIN_VERSION)) {
                updateVerfuegbar = true;
                getLogger().info("[Update] Neue Version verfügbar: v" + neuesteVersion + " (aktuell: v" + PLUGIN_VERSION + ")");
                getLogger().info("[Update] https://github.com/DEIN-NAME/minecraft-ki-plugin/releases/latest");
            } else {
                getLogger().info("[Update] Plugin ist aktuell (v" + PLUGIN_VERSION + ")");
            }

        } catch (Exception e) {
            getLogger().warning("[Update] Update-Check fehlgeschlagen: " + e.getMessage());
        }
    }
    // ───────────────────────────────────────────────────────────────[...]

    // ── Spieler-Join: Update-Hinweis für OPs ──────────────────────────────
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!updateVerfuegbar) return;
        if (!event.getPlayer().isOp()) return;

        // Small delay so the message appears after join messages
        getServer().getScheduler().runTaskLater(this, () -> {
            event.getPlayer().sendMessage("§6§l[KI-Plugin] §eNeue Version verfügbar: §av" + neuesteVersion);
            event.getPlayer().sendMessage("§7Aktuell: §cv" + PLUGIN_VERSION + " §7→ Neu: §av" + neuesteVersion);
            event.getPlayer().sendMessage("§7Download: §bhttps://github.com/DEIN-NAME/minecraft-ki-plugin/releases/latest");
        }, 40L); // 2 Sekunden nach dem Joinen
    }
    // ──────────────────────────────────────────────────────────────[...]

    // ── Cow Protection Feature ──────────────────────────────────────────────
    @EventHandler
    public void onCowDeath(EntityDeathEvent event) {
        if (!isCowProtectionEnabled()) return;
        
        Entity entity = event.getEntity();
        
        // Check if the entity is a cow
        if (!(entity instanceof Cow)) return;
        
        // Cow is a LivingEntity, cast it
        LivingEntity cow = (LivingEntity) entity;
        
        // Get the player who killed the cow
        Player killer = cow.getKiller();
        
        if (killer != null) {
            // Kick the player who killed the cow
            killer.kickPlayer(getCowKickMessage());
            getLogger().info("[Cow Protection] Player " + killer.getName() + " was kicked for killing a cow");
        }
        
        // Broadcast message to all players that a cow has died
        getServer().broadcastMessage(getCowDeathMessage());
    }
    // ──────────────────────────────────────────────────────────────[...]

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

    // /kireload Befehl
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kireload")) {
            if (!sender.hasPermission("kiplugin.reload")) {
                sender.sendMessage("§cKeine Berechtigung!");
                return true;
            }
            reloadConfig();
            sender.sendMessage("§aKI-Plugin Config neu geladen!");
            // Re-check for updates after reload
            getServer().getScheduler().runTaskAsynchronously(this, this::pruefeAufUpdates);
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
