# Temi the Robot

Dieses Repository enthält die Android App für den Temi Roboter. Die App wurde im Rahmen des Moduls "
Einführung in die Robotik" an der Fachhochschule Kiel entwickelt.

## Gruppenmitglieder

* Benjamin (@sihingbenni)
* Karlina ()
* Jane (@janelucia)

## Ziel

Funktionen

* Auswahl der Art der Führung durch die Besucher*innen.
* Navigation des Roboters zu den Bereichen / Exponaten.
* Abspielen von Erklärungen, Fotos und Videos zu den Bereichen / Exponaten.
* funktionale und benutzerfreundliche Oberfläche.
* Fähigkeiten vertiefen in den Bereichen: Benutzeroberflächendesign, Robotiksteuerung und
  Medienintegration

## Meilensteine

| Meilenstein                       | Datum      | Beschreibung                                                                                                          |
|-----------------------------------|------------|-----------------------------------------------------------------------------------------------------------------------|
| Projektinitialisierung            | 06.10.2021 | Richten Sie die Entwicklungsumgebung ein; Machen Sie sich mit der temi Roboter API und dem Beispielprojekt vertraut.  |
| UI-Design und Implementierung     | 13.10.2021 | Entwerfen und setzen Sie die Benutzeroberfläche um; Implementieren Sie die Auswahlmöglichkeiten für die Führung.      |
| Navigation und Steuerung          | 20.10.2021 | Integrieren Sie die Navigationsfunktionen; Implementieren Sie die Statusanzeige.                                      |
| Erklärungen und Medienintegration | 27.10.2021 | Rufen Sie die Erklärungstexte ab und stellen Sie diese dar; Integrieren und synchronisieren Sie die Fotos und Videos. |
| Test und Fehlerbehebung           | 10.11.2021 | Führen Sie Tests zur Sicherstellung der Funktionalität durch; Beheben Sie Navigations- und Anzeigefehler.             |

## Anforderungen

1. Benutzeroberfläche (UI):
   • Erstellen Sie einen Startbildschirm mit Auswahlmöglichkeiten für die Art der Führung:
   ◦ Kurz (kurze Erklärungen) oder Lang (ausführliche Erklärungen).
   ◦ Einfach (nur wichtige Bereiche) oder Ausführlich (alle Bereiche).
   ◦ Individuelle Führung
   • Implementieren Sie eine Fortschrittsleiste oder eine Übersicht der Bereiche während der
   Führung.
2. Interaktive Elemente:
   • Ermöglichen Sie den Besucher*innen, die Art der Führung auszuwählen.
   • Implementieren Sie Feedback-Möglichkeiten für die Besucher*innen, um die Qualität der
   Führung zu bewerten. Die Bewertung sollte in Form einer Simley-Bewertung (gut / neutral /
   schlecht) erfolgen.
3. Navigation:
   • Integrieren Sie die Funktion zur Steuerung des Roboters zu den jeweiligen Exponaten.
   ◦ Schaffen Sie die Möglichkeit, die Führung zu pausieren oder zu beenden.
4. Erklärungen und Medienwiedergabe:
   • Rufen Sie kurze und lange Erklärungstexte zu den Themenbereichen und Exponaten ab und
   lassen Sie den Roboter diese sprechen.
   ◦ Implementieren Sie die Wiedergabe von Fotos und Videos zu den Exponaten.
   ◦ Stellen Sie sicher, dass die Medieninhalte zusammen mit der Sprache wiedergegeben
   werden.
5. Datenverwaltung:
   • Nutzen Sie für die Datenverwaltung die Funktionen im Beispielprojekt.
6. Fehlerbehandlung:
   • Entwickeln Sie eine Anzeige von Fehlermeldungen bei Navigationsfehlern oder anderen
   Problemen.
   • Schaffen Sie die Möglichkeit zur Wiederholung oder zum Überspringen eines Exponats bei
   Navigationsfehlern.

## Technologische Anforderungen

Technische Anforderungen
• Programmiersprache: Kotlin
• Entwicklungsumgebung: Android Studio
• Zugriff auf temi Roboter: Nutzen Sie die bereitgestellten Funktionen zur Navigation und
Statusüberprüfung.