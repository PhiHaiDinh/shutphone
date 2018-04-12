# ShutPhone
Die mobile Anwendung "ShutPhone" ist im Rahmen meiner Bachelorarbeit enstanden. 
Im vorherigen Modul [Praxisprojekt](https://github.com/PhiHaiDinh/ShutApps) wurde ein ähnliches Thema behandelt, weswegen in meiner Bachelorarbeit eine andere Vorgehensweise im Kontext auf die Gaststätten behandelt wurde. 
Das Thema lautet "Konzeption und Implementierung einer mobilen Anwendung zur standortbasierten Gamifizierung der Nicht-Nutzung von Smartphones". 


Das Ziel dieser Bachelorarbeit ist es die Smartphone-Nutzung im Kontext auf die Gaststätten zu minimieren. 
Für die Nicht-Nutzung des Smartphones erhalten die Nutzer Punkte, die sie gegen Belohungen umtauschen können. 
Hierbei handelt es sich um einen Prototypen in Android. 
 
Genauere Informationen finden Sie in der [Bachelorarbeit](https://github.com/PhiHaiDinh/ShutPhone/blob/master/BA_ShutPhone_Vu_Phi_Hai_Dinh.pdf)

## Architektur & Komponenten
![architektur](https://user-images.githubusercontent.com/38287483/38677541-da8818b2-3e5e-11e8-94ec-68bf5bfd7ac5.jpg)

* **Android Studio** als Entwicklungsumgebung
* **Kiosk Mode** zur Einschränkung des Smartphones
* **Estimote Beacon** zur Lokalisierung des Smartphones
* Client als mobiler Client in **Android**
  * **Accessibility-Service** zur Erkennung der Vordergrund-App
* **Google Game Play Services** für die Anbindung von Google Play
  * **Achievements API** zur Erhaltung von Errungenschaften
* **Firebase Authentication** zur Authentifizierung in der Anwendung
* Anbindung von Facebook mit der **Facebook API**

-------------------
-------------------

## English version

The mobile application "ShutPhone was developed as part of my bachelor thesis. In my previous module [practical project](https://github.com/PhiHaiDinh/ShutApps) I have dealt with a similar topic. Because of that I'm trying an another approach in context of the restaurants.  
The title of my bachelor thesis is "
Conception and implementation of a mobile application for the location-based gamification of the non-use of smartphones".

The aim of my thesis is to minimize the use of smartphones in restaurants. 
For this purpose, the users should be rewarded for the non-use of smartphone.
This is a protoype in Android. 
