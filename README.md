# 🤖 Minecraft AI Plugin (Ollama + External AI APIs + Paper)

A Paper plugin that integrates AI directly into the Minecraft chat. Choose between running a **local AI with Ollama** or connecting to **external AI services** like OpenAI, Anthropic, Cohere, and HuggingFace. No bot player, no extra account – runs invisibly in the background.

---

## ✅ Requirements

- Paper Server (26.1.x)
- Java 25
- **Option A (Local)**: [Ollama](https://ollama.com) running on the same server
- **Option B (External)**: API key from an AI provider (OpenAI, Anthropic, Cohere, or HuggingFace)

For local Ollama:
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

Everything can be changed in the `config.yml` file inside `plugins/KiPlugin/` after the first start.

### Quick Setup

```yml
befehl: "!ki"            # Command to trigger the AI
provider: "ollama"       # Use "ollama" or "external"
modell: "mistral"        # Ollama model to use
chat-prefix: "§b[AI] §f"
nur-anfrager-sieht-antwort: false  # true = only the asker sees the reply
```

Reload config without restarting the server:
```
/kireload
```

---

## 🌐 Using External AI Services

You can connect to external AI providers like **OpenAI**, **Anthropic**, **Cohere**, or **HuggingFace** instead of using Ollama.

### Step 1: Get an API Key

Choose your provider and get an API key:

- **OpenAI (GPT-3.5, GPT-4)**
  - Sign up at: https://platform.openai.com/signup
  - Get API key: https://platform.openai.com/api-keys
  - Recommended models: `gpt-3.5-turbo`, `gpt-4`, `gpt-4-turbo`

- **Anthropic (Claude)**
  - Sign up at: https://console.anthropic.com/
  - Get API key: https://console.anthropic.com/account/keys
  - Recommended models: `claude-3-opus`, `claude-3-sonnet`, `claude-3-haiku`

- **Cohere**
  - Sign up at: https://dashboard.cohere.com/
  - Get API key from dashboard
  - Recommended models: `command`, `command-light`

- **HuggingFace**
  - Sign up at: https://huggingface.co/join
  - Get API key: https://huggingface.co/settings/tokens
  - Use any model ID from HuggingFace Hub

### Step 2: Configure the Plugin

Edit `plugins/KiPlugin/config.yml`:

```yml
# Switch to external provider
provider: "external"

# Configure your API service
external-ai:
  service: "openai"                              # or "anthropic", "cohere", "huggingface"
  api-key: "sk-xxxxx..."                         # Your API key (keep it secret!)
  model: "gpt-3.5-turbo"                         # Model to use
  api-endpoint: "https://api.openai.com/v1/chat/completions"  # Usually auto-detected
  max-tokens: 100                                # Max response length
  temperature: 0.7                               # 0.0=deterministic, 1.0=creative
```

### Step 3: Restart the Server

```
/stop
# Server restarts, plugin loads new config
```

### Complete Example: OpenAI Setup

```yml
provider: "external"

external-ai:
  service: "openai"
  api-key: "sk-proj-xxxxxxxxxxxxx"
  model: "gpt-3.5-turbo"
  api-endpoint: "https://api.openai.com/v1/chat/completions"
  max-tokens: 100
  temperature: 0.7

prompt-prefix: "Answer briefly in English, max 2 sentences: "
chat-prefix: "§b[AI] §f"
```

---

## 🔒 Security & Best Practices

⚠️ **IMPORTANT**: Your API keys are sensitive credentials!

- **Never share** your `config.yml` file with your API key
- **Don't commit** it to GitHub (add to `.gitignore`)
- **Use environment variables** if hosting on a remote server
- Consider using a separate API key just for your Minecraft server
- Rotate your API keys periodically
- Monitor your API usage to avoid unexpected charges

---

## 💰 Cost Considerations

When using external AI services:

- **OpenAI**: Pay-per-use (GPT-3.5 is ~$0.0005 per message)
- **Anthropic**: Pricing varies by model
- **Cohere**: Free tier available, pay-per-use for more
- **HuggingFace**: Free for many models, paid inference available

Tip: Set a lower `max-tokens` value to reduce costs and speed up responses!

---

## 🌍 Ollama vs External Services

| Feature | Ollama | OpenAI | Anthropic |
|---------|--------|--------|-----------|
| Cost | Free | Pay per use | Pay per use |
| Privacy | 100% Local | API sent to servers | API sent to servers |
| Speed | Depends on hardware | Fast (cloud) | Fast (cloud) |
| Quality | Good | Excellent (GPT-4) | Excellent (Claude) |
| Setup | Complex | Simple | Simple |

Choose **Ollama** for privacy and no costs, choose **External** for better quality and faster responses!

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

---

## 🔔 Update Notifications

When a new plugin version is released on GitHub, any OP player will automatically receive a notification when they join the server. No manual checking needed!

---

## 🛠️ Starting Ollama (Local Option)

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

---

## 🆘 Troubleshooting

### External API not working?
- Check your API key is correct
- Verify the service is online
- Check firewall allows HTTPS connections
- Look at server logs for error messages

### Ollama not responding?
- Ensure Ollama is running: `ollama serve`
- Check the model is downloaded: `ollama list`
- Verify URL is correct: `http://localhost:11434`

---

**Need help?** Open an issue on GitHub!
