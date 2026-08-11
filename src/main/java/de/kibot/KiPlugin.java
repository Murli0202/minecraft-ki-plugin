package de.kibot;

import de.kibot.ai.AIClient;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Integration helper that holds the AIClient and provides helper methods.
 * This is intentionally NOT a JavaPlugin subclass to avoid defining a second
 * plugin main class named de.kibot.KiPlugin which would conflict with an
 * existing main class in the repository.
 */
class KiPluginIntegration {
    private final JavaPlugin plugin;
    private AIClient aiClient;

    KiPluginIntegration(JavaPlugin plugin){
        this.plugin = plugin;
        String ollamaUrl = plugin.getConfig().getString("ollama-url", "http://localhost:11434/api/generate");
        int timeoutSeconds = plugin.getConfig().getInt("timeout-sekunden", 60);
        int timeoutMillis = Math.max(1000, timeoutSeconds * 1000);
        this.aiClient = new AIClient(ollamaUrl, timeoutMillis);
    }

    public AIClient getAiClient(){
        return aiClient;
    }

    public void setAiServer(String url){
        if (!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://" + url;
        }
        plugin.getConfig().set("ollama-url", url);
        plugin.saveConfig();
        aiClient.setServerUrl(url);
        plugin.getLogger().info("AI-Server set to: " + url);
    }

    // Helper to be called from the real plugin's command handlers
    public boolean handleSetAiServerCommand(CommandSender sender, Command command, String label, String[] args){
        if (!sender.hasPermission("kiplugin.setaiserver")){
            sender.sendMessage("§cKeine Berechtigung.");
            return true;
        }
        if (args.length == 0){
            sender.sendMessage("§eVerwendung: /setaiserver <url>");
            return true;
        }
        String url = args[0].trim();
        setAiServer(url);
        sender.sendMessage("§aAI-Server gesetzt auf: " + url);
        return true;
    }

    public boolean handleReloadCommand(CommandSender sender, Command command, String label, String[] args){
        if (!sender.hasPermission("kiplugin.reload")){
            sender.sendMessage("§cKeine Berechtigung.");
            return true;
        }
        plugin.reloadConfig();
        String newUrl = plugin.getConfig().getString("ollama-url", "http://localhost:11434/api/generate");
        aiClient.setServerUrl(newUrl);
        sender.sendMessage("§aConfig neu geladen. AI-Server: " + newUrl);
        return true;
    }
}
