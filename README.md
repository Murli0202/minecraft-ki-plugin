# 🤖 Minecraft AI Plugin (Ollama + External AI APIs + Paper)

A Paper plugin that integrates AI directly into the Minecraft chat. Choose between running a **local AI with Ollama** or connecting to **external AI services** like OpenAI, Anthropic, Cohere, and others. No bot player, no extra account – runs invisibly in the background.

---

## ✅ Requirements

- Paper Server (26.1.x)
- Java 25
- **Option A (Local)**: [Ollama](https://ollama.com) running on the same server
- **Option B (External)**: API key from an AI provider (OpenAI, Anthropic, Cohere, or others)

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

## 🌐 Using External AI Services (2026 Models)

You can connect to external AI providers with cutting-edge 2026 models instead of using Ollama. All major AI companies have released new flagship models!

### Step 1: Get an API Key

Choose your provider and get an API key:

#### **OpenAI (GPT-5.6 Family - July 2026)** 🔥
- Sign up at: https://platform.openai.com/signup
- Get API key: https://platform.openai.com/api-keys
- **Latest Models (July 2026):**
  - **`gpt-5.6-sol`** - Most capable, state-of-the-art for complex reasoning & generation
  - **`gpt-5.6-terra`** - Balanced mid-tier, 50% cheaper than Sol, great performance
  - **`gpt-5.6-luna`** - Fastest & most cost-effective, best for high-volume use
  - **`gpt-5.5`** - Previous generation, still powerful
  - **`gpt-4-turbo`** - Earlier model, lower cost

#### **Anthropic / Claude (Latest 2026)** 🧠
- Sign up at: https://console.anthropic.com/
- Get API key: https://console.anthropic.com/account/keys
- **Latest Models (June-July 2026):**
  - **`claude-fable-5`** ⭐ - Safe, powerful variant for public/enterprise use (RECOMMENDED)
  - **`claude-sonnet-5`** - Agentic, cost-effective, approaching Opus performance
  - **`claude-opus-4.8`** - Top-tier reasoning & complex tasks
  - **`claude-mythos-5`** - Frontier research model with lifted safeguards
  - **`claude-3-opus`** - Earlier generation, still excellent

#### **Cohere (2026 Updates)**
- Sign up at: https://dashboard.cohere.com/
- Get API key from dashboard
- **Latest Models:**
  - **`command-r-plus`** - Enterprise-grade model
  - **`command-r`** - Fast and accurate
  - **`command`** - Lightweight option

#### **Google Gemini (July 2026)**
- Sign up at: https://ai.google.dev/
- Get API key from Google AI Studio
- **Latest Models:**
  - **`gemini-3.5`** - Latest release with advanced multi-modal capabilities
  - **`gemini-3.1`** - Strong reasoning and live translation support
  - **`gemini-2.0`** - Earlier version, still capable

#### **xAI Grok (July 2026)**
- Sign up at: https://grok.ai.com/
- **Latest Model:**
  - **`grok-4.5`** - Competitive reasoning and coding abilities

### Step 2: Configure the Plugin

Edit `plugins/KiPlugin/config.yml`:

```yml
# Switch to external provider
provider: "external"

# Configure your API service
external-ai:
  service: "openai"                              # or "anthropic", "cohere", "gemini", "xai"
  api-key: "sk-xxxxx..."                         # Your API key (keep it secret!)
  model: "gpt-5.6-terra"                         # Model to use (see list above)
  api-endpoint: "https://api.openai.com/v1/chat/completions"  # Usually auto-detected
  max-tokens: 100                                # Max response length
  temperature: 0.7                               # 0.0=deterministic, 1.0=creative
```

### Step 3: Restart the Server

```
/stop
# Server restarts, plugin loads new config
```

### Complete Examples

**Example 1: OpenAI GPT-5.6 (Best for complex reasoning)**
```yml
provider: "external"

external-ai:
  service: "openai"
  api-key: "sk-proj-xxxxxxxxxxxxx"
  model: "gpt-5.6-sol"
  api-endpoint: "https://api.openai.com/v1/chat/completions"
  max-tokens: 100
  temperature: 0.7

prompt-prefix: "Answer briefly in English, max 2 sentences: "
chat-prefix: "§b[AI-Sol] §f"
```

**Example 2: Claude Fable 5 (Recommended - Safe & Powerful)**
```yml
provider: "external"

external-ai:
  service: "anthropic"
  api-key: "sk-ant-xxxxxxxxxxxxx"
  model: "claude-fable-5"
  api-endpoint: "https://api.anthropic.com/v1/messages"
  max-tokens: 100
  temperature: 0.7

prompt-prefix: "Answer briefly in English, max 2 sentences: "
chat-prefix: "§b[Claude] §f"
```

**Example 3: Budget Option (GPT-5.6 Luna)**
```yml
provider: "external"

external-ai:
  service: "openai"
  api-key: "sk-proj-xxxxxxxxxxxxx"
  model: "gpt-5.6-luna"  # Fastest & cheapest
  max-tokens: 80         # Keep responses shorter to save costs
  temperature: 0.5

prompt-prefix: "Answer briefly: "
chat-prefix: "§b[Luna] §f"
```

---

## 🔒 Security & Best Practices

⚠️ **IMPORTANT**: Your API keys are sensitive credentials!

- **Never share** your `config.yml` file with your API key
- **Don't commit** it to GitHub (add to `.gitignore`)
- **Use environment variables** if hosting on a remote server
- Consider using a separate API key just for your Minecraft server
- **Rotate your API keys** periodically
- **Monitor your API usage** to avoid unexpected charges
- Keep API keys out of server backups

---

## 💰 Cost Comparison (2026 Pricing)

When using external AI services, costs vary significantly:

| Model | Cost per 1K tokens | Speed | Quality | Best For |
|-------|-------------------|-------|---------|----------|
| GPT-5.6-Sol | ~$0.003 | Slower | ⭐⭐⭐⭐⭐ | Complex reasoning |
| GPT-5.6-Terra | ~$0.0015 | Medium | ⭐⭐⭐⭐⭐ | Best balance |
| GPT-5.6-Luna | ~$0.0003 | Fast | ⭐⭐⭐⭐ | Budget option |
| Claude Fable-5 | ~$0.003 | Medium | ⭐⭐⭐⭐⭐ | Safe & powerful |
| Claude Sonnet-5 | ~$0.003 | Fast | ⭐⭐⭐⭐ | Cost-effective |
| Cohere Command-R | ~$0.0005 | Fast | ⭐⭐⭐ | Very budget |

**Tip**: Set `max-tokens: 80` to reduce costs while keeping responses quick!

---

## 🌍 Ollama vs External Services

| Feature | Ollama | OpenAI | Anthropic | Cohere |
|---------|--------|--------|-----------|--------|
| Cost | Free | Pay per use | Pay per use | Pay per use |
| Privacy | 100% Local | Cloud-based | Cloud-based | Cloud-based |
| Speed | Depends on hardware | ⚡⚡⚡ Fast | ⚡⚡⚡ Fast | ⚡⚡⚡ Fast |
| Quality | Good | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Setup | Complex | Simple | Simple | Simple |
| Latest Models | Slower updates | Always current | Always current | Regular updates |

**Choose Ollama if:** You want privacy, free usage, and don't mind slower setup  
**Choose OpenAI:** You want the absolute best (GPT-5.6-Sol) and newest models  
**Choose Claude Fable-5:** You want a safe, powerful, enterprise-ready model  
**Choose Cohere:** You're on a tight budget and just need basic responses  

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
- Check your API key is correct and active
- Verify the service is online and accessible
- Check firewall allows HTTPS connections (port 443)
- Look at server logs for detailed error messages
- Ensure you have API credits/balance remaining

### Ollama not responding?
- Ensure Ollama is running: `ollama serve`
- Check the model is downloaded: `ollama list`
- Verify URL is correct: `http://localhost:11434`
- Check firewall allows localhost connections

### API charges too high?
- Switch to GPT-5.6-Luna or Claude Sonnet-5 (cheaper)
- Reduce `max-tokens` value (saves money per query)
- Set `message-limit` to restrict usage per player
- Use `Cohere` for budget-friendly option

---

**Need help?** Open an issue on GitHub!  
**Want to support local AI?** Stick with Ollama!  
**Want cutting-edge AI in Minecraft?** Use GPT-5.6 or Claude Fable-5!
