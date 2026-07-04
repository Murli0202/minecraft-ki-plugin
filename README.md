[README.md](https://github.com/user-attachments/files/28571450/README.md)
# 🤖 Minecraft AI Plugin (Ollama + Paper) (This plugin is all made by AI)

A Paper plugin that integrates a local AI (via Ollama) directly into the Minecraft chat. No bot player, no extra account – runs invisibly in the background.

---

## ✅ Requirements

- Paper Server (26.1.x)
- Java 25
- [Ollama](https://ollama.com) running on the same server
- Mistral model installed: `ollama pull mistral`

---

## 📦 Installation

### Option A – Build it yourself

```bash
git clone https://github.com/Murli0202/minecraft-ki-plugin.git
cd ki-plugin
mvn package -q
cp target/ki-plugin-1.0.jar /path/to/server/plugins/
```

### Option B – Ready-made JAR

1. Download `ki-plugin-1.0.jar` from the [Releases](../../releases) tab
2. Copy the JAR into your `/plugins/` folder
3. Restart the server

---

## 🚀 Usage

### AI Chat Commands

Simply type in the Minecraft chat:
```
!ki What is a Creeper?
!ki How do you build a portal?
!ki Explain Redstone
```

The AI responds directly in the chat, visible to all players.

---

## ⚙️ Configuration

Everything can be changed in the `config.yml` file inside `plugins/KiPlugin/` after the first start:

```yml
befehl: "!ki"            # Command to trigger the AI
modell: "mistral"        # Ollama model to use
ollama-url: "http://localhost:11434/api/generate"  # Ollama API URL
prompt-prefix: "Answer briefly in English, max 2 sentences: "
chat-prefix: "§b[AI] §f"
nur-anfrager-sieht-antwort: false  # true = only the asker sees the reply
```

Other models: `llama3:8b`, `phi3:mini`, `tinyllama`

Reload config without restarting the server:
```
/kireload
```

---

## 🐄 Cow Protection Feature

Protect your cows from being killed! This feature automatically kicks players who kill cows and notifies all players when a cow dies.

### Configuration

```yml
cow-protection:
  enabled: true                              # Enable/disable the feature (default: true)
  kick-message: "§cDon't kill cows on this server"   # Message shown to kicked players
  death-message: "§e[Cow Protection] §fA cow has died!"  # Message broadcast to all players
```

### Behavior

- **When a player kills a cow**: The player is immediately kicked with the configured message
- **When a cow dies** (any cause): All players receive a notification in chat
- **Customizable**: All messages can be modified in the config
- **Disableable**: Set `enabled: false` to turn off the feature

### Example

```
Player kills a cow
└─ Player is kicked: "Don't kill cows on this server"
└─ All players see: "[Cow Protection] A cow has died!"
```

---

## 🔔 Update Notifications

When a new plugin version is released on GitHub, any OP player will automatically receive a notification when they join the server. No manual checking needed!

---

## 🛠️ Starting Ollama

```bash
# Install once
curl -fsSL https://ollama.com/install.sh | sh

# Download model
ollama pull mistral

# Ollama runs automatically as a service
# If not: ollama serve
```

---

## 📋 Minecraft Version

Tested with Paper **26.1.2**. For other versions adjust `pom.xml`:
```xml
<version>26.1.2-R0.1-SNAPSHOT</version>
```

---

## 📄 License

MIT – do whatever you want with it!
