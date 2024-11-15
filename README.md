# Temi the Robot

Dieses Repository enthält die Android App für den Temi Roboter. Der Temi Roboter führt
Besucher:innen durch die Ausstellung des Computer Museums der Fachhochschule Kiel. 
Die Besuchenden haben die Möglichkeit die Art der Führung zu wählen. Dabei können sie zwischen:

- einer langen und ausführlichen Führung (alle Stationen und alle Texte zu den Exponaten)
- einer langen und prägnanten Führung (alle Stationen und die wichtigsten Informationen zu den Exponaten)
- einer kurzen und ausführlichen Führung (nur die wichtigsten Stationen und alle Texte zu den Exponaten)
- einer kurzen und prägnanten Führung (nur die wichtigsten Stationen und Informationen zu den Exponaten)

Im Anschluss wird der Besuchende von dem Temi zu den ausgewählten Stationen geführt.
Die App wurde im Rahmen des Moduls "Einführung in die Robotik" an der Fachhochschule Kiel entwickelt.

## Gruppenmitglieder

- Benjamin (@sihingbenni)
- Karlina (@caprily)
- Jane (@janelucia)

## Benutzung der App

### Vorbedingungen
- Die Projekt-APK ist auf dem Temi installiert
- Der Temi Roboter ist eingeschaltet
- Es besteht eine Datenbank mit den notwendigen Informationen (siehe Datenbank)
- Die App ist gestartet

### Benötigte Rechte
* Bitte nehmen Sie alle notwendigen Rechte an, die der Temi Ihnen beim Start der App zeigt. Sonst wird die App nicht korrekt funktionieren.


### Einstellen des Temis
- Folgen Sie den Vorbereitungsschritten, welche unter dem Button: "Vorbereitungen" in der Setup UI zu finden sind
- Wählen Sie den Ort aus, an dem sich der Temi befindet.
  - Alternativ können sie auch den Temi automatisch den Ort bestimmen lassen (Button: "Den Roboter auswählen lassen")
  - Sollte dies fehlschlagen, ist es nur möglich, den Ort manuell zu bestimmen. Ein erneutes Drücken des Buttons "Den Roboter auswählen lassen" ist nicht möglich.
- Schalten Sie den Kioskmodus ein (Der Kiosk modus wird unteranderem dafür benötigt um den Inaktivitätscheck zu benutzen.)
Beachten Sie, dass Sie die Einstellung nur beim Start der App vornehmen können!

### App schließen
Sollte die App im Kioskmodus laufen, ist es nicht ohne weiteres möglich die App zu schließen.
Sollten Sie die App im Kiosk Modus schließen wollen, dann müssen Sie folgende Schritte befolgen:
- Drücken Sie auf das Fragezeichen oben rechts
- Drücken Sie auf "App schließen" (Dies soll in Zukunft nur über PIN Eingabe möglich sein, damit Kunden nicht die App schließen können)
- Sie sollten nun die Konfigurationsseite der App sehen
- Stellen Sie den Slider "Kioksmodus" auf Aus.
- Wechseln Sie in die Einstellungen des Temis und wählen Sie dort unter "Home Screen" eine andere App oder "Default" aus.
- Jetzt können Sie zurück in die App wechseln und diese schließen.


### Führung auswählen
- Wählen Sie die Art der Führung aus, die Sie durch den Temi erhalten möchten
- Drücken Sie auf den Button "Los geht's"
- Wählen Sie aus, ob Sie eine Führung oder nur zu einem bestimmten Exponat geführt werden möchten
- Zu erst können Sie auswählen, ob Sie eine kurze (nur die wichtigsten Stationen) oder eine lange Führung machen möchten
- Dann wählen Sie, ob Sie nur die wichtigsten Informationen oder alle Informationen zu den Exponaten erhalten möchten

### Führung

#### Generelle Informationen
- Der Temi wird Sie zu Ihrer gewählten Station führen. Sie können die Stationen über den Pfeil nach rechts überspringen oder mit dem Pfeil nach links zurückgehen
- Sobald Sie an der Station angekommen sind, wird der Temi Ihnen die Informationen zu der Station vorlesen. Sie können diese Informationen, aber auch auf dem Bildschirm einsehen.
- Der Temi pausiert nach jedem Exponat, so haben Sie Zeit sich die Informationen durchzulesen oder sich die Exponate anzusehen
- Sobald Sie bereit sind, müssen Sie den Pfeil nach rechts drücken, damit der Temi Ihnen Informationen zum nächsten Exponat vorliest
- Wenn Sie das letzte Exponat der Station gehört haben, wird der Temi Sie zur nächsten Station führen
- Sobald Sie alle Stationen gehört haben, können Sie die Führung beenden

#### Stationen wiederholen
- Über den Wiederholungsbutton, der gedrehte Pfeil, können Sie sich die Informationen zu dem aktuellen Exponat noch einmal anhören

#### Führung abbrechen
- Die Führung kann über das "Haus"-Symbol in der Führungsansicht abgebrochen werden, welche Sie in der obere rechten Ecke finden

#### Führung beenden
- Am letzten Exponat, der letzten Station haben Sie die Möglichkeit die Führung zu beenden. Drücken Sie dazu auf den Button "Führung beenden"
- Danach würden wir uns freuen, wenn Sie uns ein Feedback hinterlassen würden
- Im Anschluss haben Sie die Möglichkeit die Führung zu beenden. Dabei können Sie auswählen, ob Sie zur Startseite zurückkehren möchten oder den Temi zu seiner Ladestation schicken möchten

### Mit Fehlern umgehen
- Der Temi benötigt seinen personal space, bitte lassen Sie ihm diesen auch. Sollten Sie zu nah an den Temi herantreten, wird er nicht mehr weiter navigieren und ggf. die Navigation abbrechen
- Sie können die Navigation erneut anstoßen, in dem Sie auf den Button "Erneut versuchen" drücken. Dieser erscheint, wenn die Navigation abgebrochen wurde. Sie können diesen auch in dem Dialog, der erscheint, wenn ein Fehler auftritt, finden.

### Kleiner Temi Knigge
- Sollten Sie den Temi nicht mehr benötigen, dann lassen Sie Ihn bitte zu seiner Ladestation fahren. Er wird sich sonst auch bemerkbar machen.

## Weiterentwicklung der App

Wenn du dich dafür interessierst uns bei unserer Mission zu helfen, die beste Temi App für das Computermuseum der FH Kiel zu entwickeln, dann klone gerne unser Repository und folge den Anweisungen.

```bash
  git clone https://github.com/janelucia/rob-temi-robo.git
```

### Vorbedingungen
- Android Studio in der Version Ladybug ist installiert
- AGP Version: 8.7.2

### Schritte
- Wähle als Emulator ein Tablet aus. Die App wurde speziell für die Größe und Auflösung der Temi-Tablets entwickelt. Dadurch kann es sein, dass die UI-Elemente auf den Emulator-Tablets etwas gedrungen aussieht.
- Installiere die Dependencies per `gradle sync`
- Starte die App

### Beachte
- Um den vollkommenen Funktionsumfang zu testen, benötigst du einen Temi Roboter

### Ausblick
- per Videocall um Hilfe bitten
- PIN für das Schließen der App
- ein verbessertes Feedbacksystem
- Eigene Youtube Steuerung, um das Navigieren aus der App zu verhindern.
- Übersetzung auf Englisch




