# 🤖 Minecraft KI-Plugin (Ollama + Paper) (This plugin is all made by ai)

Ein Paper-Plugin das eine lokale KI (via Ollama) direkt in den Minecraft-Chat integriert. Kein Bot-Spieler, kein extra Account – läuft unsichtbar im Hintergrund.

---

## ✅ Voraussetzungen

- Paper Server (1.21.x)
- Java 21
- [Ollama](https://ollama.com) läuft auf demselben Server
- Mistral Modell installiert: `ollama pull mistral`

---

## 📦 Installation

### Option A – Selber bauen

```bash
git clone https://github.com/Murli0202/minecraft-ki-plugin.git
cd ki-plugin
mvn package -q
cp target/ki-plugin-1.0.jar /pfad/zu/server/plugins/
```

### Option B – Fertige JAR

1. Lade `ki-plugin-1.0.jar` aus dem [Releases](../../releases) Tab herunter
2. Kopiere die JAR in deinen `/plugins/` Ordner
3. Server neu starten

---

## 🚀 Nutzung

Im Minecraft-Chat einfach:
```
!ki Was ist ein Creeper?
!ki Wie baut man ein Portal?
!ki Erkläre Redstone
```

Die KI antwortet direkt im Chat für alle Spieler sichtbar.

---

## ⚙️ Einstellungen

In `KiPlugin.java` oben anpassen:

```java
private static final String BEFEHL = "!ki";       // Befehl zum Auslösen
private static final String MODELL  = "mistral";   // Ollama-Modell
private static final String OLLAMA  = "http://localhost:11434/api/generate"; // Ollama URL
```

Andere Modelle: `llama3:8b`, `phi3:mini`, `tinyllama`

---

## 🛠️ Ollama starten

```bash
# Einmalig installieren
curl -fsSL https://ollama.com/install.sh | sh

# Modell herunterladen
ollama pull mistral

# Ollama läuft automatisch als Service
# Falls nicht: ollama serve
```

---

## 📋 Minecraft Version

Getestet mit Paper **26.1.2**. Für andere Versionen `pom.xml` anpassen:
```xml
<version>26.1.2-R0.1-SNAPSHOT</version>
```

---

## 📄 Lizenz

MIT – mach damit was du willst!
