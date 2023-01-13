# Examen POO3 (2022-2023)

Le code source présent dans ce repo a servi à l'examen de POO3 de janvier 2023 à la HELHa Charleroi.

## Cahier des charges

<details>
  <summary>Contenu du cahier des charges</summary>

### Intro

Dans le contexte d’une application réalisée en classe utilisant le port série d’un Arduino, réaliser une application
permettant de faire du datalogging et d’en tracer le résultat sur une fenêtre secondaire. L’application doit être
réalisée en java OU en kotlin pour les plus téméraires d’entre vous.

### Fenêtre principale

Réaliser une fenêtre (libre choix sur les outils : java, javafx, netbeans, glade, Idea, …) qui doit permettre de :

* Lister les ports séries et d’en sélectionner un dans une combo.

* Se connecter au port série (griser le bouton si la connexion est ok pour ne pas se connecter deux fois).

* Deux champs textes permettent d’encoder le nom de l’opérateur et une remarque éventuelle. L’opérateur est obligatoire.

* Deux boutons permettent de démarrer et d’arrêter un enregistrement.

    * En phase d’enregistrement, le logiciel stocke les données dans une structure de donnée enregistrant le "moment"
      d’acquisition de la donnée ainsi que la donnée.

    * Lors de l’arrêt de l’enregistrement&nbsp;: reprendre les différentes mesures et les mettre en forme dans un
      fichier type
      CSV, libre à vous pour le choix du nom et la méthode de stockage.

* Un troisième bouton doit permettre d’afficher le graphique d’un enregistrement précédemment fait dans une simple
  JFrame.

* A la fermeture de la fenêtre, le port série doit être fermé s’il avait été ouvert précédemment.

L’acquisition prend une mesure par seconde (donc 60 mesures par minute) mais l’Arduino, pour les besoins d’une
réactivité d’affichage, envoie une mesure toutes les 200ms. A vous de "filtrer" l’arrivée des données pour ne prendre
qu’aux intervalles définis. La fenêtre se rafraîchit à 200ms.

### Fenêtre secondaire

Libre à vous de trouver ou de faire vous-même un dessin de graphique (style line plot). Pas besoin de faire de chichis.
Vous pouvez aussi utiliser des librairies.

</details>

## Prérequis à l'installation

- Java 17 (ou plus récent)
- jSerialComm 2.9.3 (déjà présent dans les sources) (https://fazecast.github.io/jSerialComm/)
- Un Arduino Uno (ou autre microcontrôleur avec un port série)
- Un navigateur web

## Bugs connus

### Initialisation de la fenêtre

Lors de l'initialisation de la fenêtre, il est possible que celle-ci ne s'affiche pas correctement.
Toutefois, il suffit de redimensionner la fenêtre pour que celle-ci s'affiche correctement.
L'erreur rencontrée est la suivante :

```
Exception in thread "AWT-EventQueue-0" java.lang.IllegalStateException: javax.swing.JButton[,0,0,0x0,invalid,disabled,alignmentX=0.0,alignmentY=0.5,border=javax.swing.plaf.BorderUIResource$CompoundBorderUIResource@49e442df,flags=296,maximumSize=,minimumSize=,preferredSize=,defaultIcon=,disabledIcon=,disabledSelectedIcon=,margin=javax.swing.plaf.InsetsUIResource[top=2,left=14,bottom=2,right=14],paintBorder=true,paintFocus=true,pressedIcon=,rolloverEnabled=true,rolloverIcon=,rolloverSelectedIcon=,selectedIcon=,text=Stop,defaultCapable=true] is not attached to a horizontal group
	at java.desktop/javax.swing.GroupLayout.checkComponents(GroupLayout.java:1090)
	at java.desktop/javax.swing.GroupLayout.prepare(GroupLayout.java:1044)
	at java.desktop/javax.swing.GroupLayout.layoutContainer(GroupLayout.java:914)
	at java.desktop/java.awt.Container.layout(Container.java:1541)
	at java.desktop/java.awt.Container.doLayout(Container.java:1530)
	at java.desktop/java.awt.Container.validateTree(Container.java:1725)
	at java.desktop/java.awt.Container.validateTree(Container.java:1734)
	at java.desktop/java.awt.Container.validateTree(Container.java:1734)
	at java.desktop/java.awt.Container.validateTree(Container.java:1734)
	at java.desktop/java.awt.Container.validate(Container.java:1660)
	at java.desktop/java.awt.Window.dispatchEventImpl(Window.java:2778)
	at java.desktop/java.awt.Component.dispatchEvent(Component.java:4833)
	at java.desktop/java.awt.EventQueue.dispatchEventImpl(EventQueue.java:773)
	at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:722)
	at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:716)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:399)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:86)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:97)
	at java.desktop/java.awt.EventQueue$5.run(EventQueue.java:746)
	at java.desktop/java.awt.EventQueue$5.run(EventQueue.java:744)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:399)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:86)
	at java.desktop/java.awt.EventQueue.dispatchEvent(EventQueue.java:743)
	at java.desktop/java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:203)
	at java.desktop/java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:124)
	at java.desktop/java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:113)
	at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:109)
	at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:101)
	at java.desktop/java.awt.EventDispatchThread.run(EventDispatchThread.java:90)
```