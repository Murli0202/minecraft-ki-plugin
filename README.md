# 🤖 Minecraft AI Plugin (Ollama + External AI APIs + Paper)

A Paper plugin that integrates AI directly into the Minecraft chat. Choose between running a **local AI with Ollama** or connecting to **external AI services** like OpenAI, Anthropic, Cohere, and others.

---

## ⚙️ Konfigurierbarer lokaler AI-Server (neu)

Du kannst jetzt die IP/URL deines lokalen AI-Servers in der Plugin-Konfiguration setzen und zur Laufzeit ändern.

Standard (src/main/resources/config.yml):

```yaml
ollama-url: "http://localhost:11434/api/generate"
```

- Die Plugin-Config wird beim Start geladen.
- Zur Laufzeit kannst du den Server per Ingame-Befehl ändern:
  - /setaiserver <url> (benötigt Permission `kiplugin.setaiserver`, Default: op)
  - Beispiel: `/setaiserver 192.168.1.10:11434` (wenn kein http/https angegeben wird, wird `http://` vorangestellt)
- Die Änderung wird in `plugins/KiPlugin/config.yml` gespeichert.

Hinweis: Das Plugin sendet standardmäßig JSON an die konfigurierte URL. Wenn dein lokaler AI-Server ein anderes Endpoint-Format benötigt, passe die Klasse `de.kibot.ai.AIClient` im Projekt an (Pfad: src/main/java/de/kibot/ai/AIClient.java).

---

(Die restliche README wurde beibehalten; diese Sektion wurde ergänzt.)
