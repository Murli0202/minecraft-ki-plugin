package de.kibot;

import de.kibot.ai.AIClient;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class KiPlugin extends JavaPlugin {

    private AIClient aiClient;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        String ollamaUrl = getConfig().getString("ollama-url", "http://localhost:11434/api/generate");
        int timeoutSeconds = getConfig().getInt("timeout-sekunden", 60);
        int timeoutMillis = Math.max(1000, timeoutSeconds * 1000);
        aiClient = new AIClient(ollamaUrl, timeoutMillis);

        if (getCommand("kireload") != null) {
            getCommand("kireload").setExecutor(this::onKireload);
        }
        if (getCommand("setaiserver") != null) {
            getCommand("setaiserver").setExecutor(this::onSetAiServer);
        }

        getLogger().info("KiPlugin enabled. Using AI server: " + aiClient.getServerUrl());
    }

    @Override
    public void onDisable() {
        // nothing special
    }

    private boolean onKireload(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kiplugin.reload")) {
            sender.sendMessage("§cKeine Berechtigung.");
            return true;
        }
        reloadConfig();
        String newUrl = getConfig().getString("ollama-url", "http://localhost:11434/api/generate");
        aiClient.setServerUrl(newUrl);
        sender.sendMessage("§aConfig neu geladen. AI-Server: " + newUrl);
        return true;
    }

    private boolean onSetAiServer(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("kiplugin.setaiserver")) {
            sender.sendMessage("§cKeine Berechtigung.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§eVerwendung: /setaiserver <url>");
            return true;
        }
        String url = args[0].trim();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        getConfig().set("ollama-url", url);
        saveConfig();

        aiClient.setServerUrl(url);
        sender.sendMessage("§aAI-Server gesetzt auf: " + url);
        getLogger().info("AI-Server changed by " + (sender instanceof Player ? ((Player) sender).getName() : "console") + " -> " + url);
        return true;
    }

    public AIClient getAiClient() {
        return aiClient;
    }
}
