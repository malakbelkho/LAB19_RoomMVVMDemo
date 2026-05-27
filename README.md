# MemoPulse – LAB 19

Application Android permettant de créer, afficher, sauvegarder et supprimer des notes locales en utilisant une architecture moderne basée sur **Room**, **MVVM**, **Repository**, **ViewModel**, **LiveData** et **RecyclerView**.

L’application propose une interface personnalisée, colorée et responsive, avec des cartes arrondies, des dégradés, un affichage dynamique des notes et une persistance locale via SQLite/Room.

---

## Objectif:

Le but de ce laboratoire est de :

- Comprendre la différence entre une application codée directement dans l’Activity et une application structurée selon l’architecture MVVM
- Mettre en place une base de données locale avec Room
- Créer une Entity représentant les données persistées
- Définir un DAO pour accéder aux données
- Utiliser une RoomDatabase comme point central de la base SQLite
- Ajouter une couche Repository entre la base de données et le ViewModel
- Utiliser un ViewModel pour conserver la logique de présentation
- Observer les données automatiquement avec LiveData
- Afficher une liste dynamique avec RecyclerView
- Exécuter les opérations Room hors du thread principal
- Gérer la persistance des notes après fermeture de l’application
- Tester le comportement de l’application après rotation de l’écran

---

## Description de l’application:

L’application **MemoPulse** permet de gérer une liste de notes personnelles stockées localement sur l’appareil.

L’application contient :

- Un champ pour saisir le titre de la note
- Un champ pour saisir la description détaillée
- Un bouton pour ajouter une note
- Un bouton pour vider toute la liste
- Une liste de notes affichée avec RecyclerView
- Une suppression individuelle par clic long
- Un message d’état vide lorsque aucune note n’est enregistrée
- Une persistance locale grâce à Room/SQLite

Chaque note contient :

- Un titre
- Une description
- Une date et heure de création
- Un badge visuel avec l’initiale du titre

---

## Fonctionnalités:

- Ajout d’une note avec validation des champs
- Affichage automatique des notes sauvegardées
- Mise à jour instantanée de la liste grâce à LiveData
- Suppression d’une note par clic long
- Suppression globale de toutes les notes
- Conservation des notes après fermeture et réouverture de l’application
- Conservation de l’état de l’écran lors d’une rotation grâce au ViewModel
- Interface moderne avec :
  - Fond en dégradé
  - Cartes arrondies
  - Badges colorés
  - Boutons personnalisés
  - Design responsive en portrait et paysage
  - Scroll global avec NestedScrollView

---

## Technologies utilisées:

- Android Studio
- Java
- XML
- Room Database
- SQLite
- MVVM
- Repository Pattern
- ViewModel
- LiveData
- RecyclerView
- CardView
- DiffUtil
- API minimum : 24 (Android 7.0)

---

## Aperçu de l’application:

▶️ Une démonstration vidéo complète est disponible dans le dossier **Demo** du repository.

⚠️ En cas de problème de lecture de la vidéo depuis le repository :

👉 [▶️ Voir la démo sur Google Drive](https://drive.google.com/file/d/1nGUHLn-FUYHwqYeL57_-UsbgjfT6JC9t/view?usp=sharing)

---

## Architecture globale:

L’application suit l’architecture **MVVM** afin de séparer clairement les responsabilités.

```text
MainActivity
    ↓
MemoBoardViewModel
    ↓
MemoRepository
    ↓
MemoAccess DAO
    ↓
PulseRoomDb / SQLite
```
Dans le sens descendant, l’interface utilisateur récupère les actions de l’utilisateur puis délègue la logique au ViewModel.

Dans le sens remontant, les données stockées dans Room sont exposées sous forme de `LiveData<List<MemoCard>>`. Dès qu’une note est ajoutée ou supprimée, Room notifie automatiquement l’interface, ce qui met à jour le RecyclerView sans rechargement manuel.

---

## Structure du projet:

```text
com.malak.roommvvmdemo
│
├── data.local
│   ├── MemoCard.java
│   ├── MemoAccess.java
│   └── PulseRoomDb.java
│
├── data
│   └── MemoRepository.java
│
├── ui
│   ├── MainActivity.java
│   └── MemoCardAdapter.java
│
└── viewmodel
    └── MemoBoardViewModel.java
```
## Détail des classes:

### data/local/MemoCard.java

Cette classe représente l’Entity Room de l’application.

Elle contient les données persistées dans la base locale :

- `memoId`
- `headline`
- `content`
- `createdAt`

L’annotation `@Entity` permet à Room de transformer cette classe en table SQLite.

---

### data/local/MemoAccess.java

Cette interface représente le DAO de l’application.

Elle définit les opérations d’accès aux données :

- Ajouter une note
- Supprimer une note
- Supprimer toutes les notes
- Observer la liste des notes

La méthode d’observation retourne un `LiveData<List<MemoCard>>`, ce qui permet une mise à jour automatique de l’interface.

---

### data/local/PulseRoomDb.java

Cette classe représente la base Room de l’application.

Elle contient :

- La déclaration de la base
- La liaison avec l’Entity `MemoCard`
- La méthode d’accès au DAO
- Le pattern Singleton pour éviter de créer plusieurs instances de la base

Room génère automatiquement l’implémentation réelle de cette base lors de la compilation.

---

### data/MemoRepository.java

Le Repository joue le rôle d’intermédiaire entre le ViewModel et la base de données.

Il permet de :

- Centraliser l’accès aux données
- Masquer les détails de Room au ViewModel
- Exécuter les insertions et suppressions dans un thread secondaire
- Préparer une éventuelle évolution vers une API distante

Dans ce projet, les opérations d’écriture utilisent un `ExecutorService` afin d’éviter de bloquer le thread principal.

---

### viewmodel/MemoBoardViewModel.java

Le ViewModel contient la logique de présentation de l’écran.

Il permet de :

- Recevoir les actions venant de l’Activity
- Appeler le Repository
- Exposer la liste des notes à l’interface
- Survivre aux changements de configuration comme la rotation de l’écran

Le ViewModel ne communique pas directement avec Room. Il passe toujours par le Repository.

---

### ui/MemoCardAdapter.java

Cet Adapter permet d’afficher les notes dans un RecyclerView.

Il gère :

- La création des lignes de la liste
- L’association des données avec les vues
- Le clic simple sur une note
- Le clic long pour supprimer une note
- L’affichage du titre, de la description, de la date et du badge initial

L’utilisation de `DiffUtil` permet d’optimiser les mises à jour de la liste au lieu de recharger toute l’interface avec `notifyDataSetChanged()`.

---

### ui/MainActivity.java

Cette Activity représente l’interface principale de l’application.

Elle gère :

- La récupération des champs de saisie
- L’ajout d’une note
- La suppression globale
- L’observation des données via LiveData
- L’affichage ou le masquage du message d’état vide
- La connexion entre le RecyclerView et l’Adapter

La logique métier n’est pas placée directement dans l’Activity. Elle est déléguée au ViewModel afin de respecter l’architecture MVVM.

---

## Layouts:

### res/layout/activity_main.xml

Layout principal de l’application.

Il contient :

- Le titre de l’application
- Le sous-titre
- Le panneau de saisie
- Les deux champs de texte
- Le bouton d’ajout
- Le bouton de suppression globale
- Le titre de la section des notes
- Le message d’état vide
- Le RecyclerView

Le layout utilise un `NestedScrollView` pour permettre le scroll en mode paysage.

---

### res/layout/memo_card_item.xml

Layout représentant une note dans le RecyclerView.

Il contient :

- Un badge circulaire avec l’initiale du titre
- Le titre de la note
- La description
- La date de création

Chaque note est affichée dans une carte arrondie avec un style moderne.

---

## Design:

Le design de l’application repose sur plusieurs fichiers XML dans `res/drawable`.

### Drawables utilisés:

- `bg_screen.xml` : fond principal en dégradé
- `bg_panel.xml` : panneau clair contenant les champs et boutons
- `bg_input.xml` : style personnalisé des champs de saisie
- `bg_button_primary.xml` : bouton principal d’ajout
- `bg_button_danger.xml` : bouton de suppression globale
- `bg_memo_card.xml` : fond des cartes de notes
- `bg_initial_badge.xml` : badge circulaire coloré

### Objectif du design:

L’objectif était d’obtenir une application plus agréable visuellement qu’une interface Android basique, avec :

- Des couleurs harmonieuses
- Une hiérarchie claire
- Des espacements propres
- Des cartes lisibles
- Une bonne adaptation en mode portrait et paysage

---

## Fonctionnement de l’ajout d’une note:

Lorsque l’utilisateur ajoute une note :

1. L’utilisateur saisit un titre et une description.
2. Il clique sur le bouton d’ajout.
3. `MainActivity` crée un objet `MemoCard`.
4. `MainActivity` appelle le `MemoBoardViewModel`.
5. Le ViewModel délègue l’action au `MemoRepository`.
6. Le Repository exécute l’insertion dans un thread secondaire.
7. Le DAO insère la note dans Room/SQLite.
8. Room notifie automatiquement le LiveData.
9. L’Activity reçoit la nouvelle liste.
10. Le RecyclerView se met à jour automatiquement.

---

## Fonctionnement de la suppression:

Pour supprimer une note :

1. L’utilisateur effectue un clic long sur une carte.
2. L’Adapter détecte le clic long.
3. `MainActivity` demande au ViewModel de supprimer la note.
4. Le ViewModel transmet la demande au Repository.
5. Le Repository appelle le DAO dans un thread secondaire.
6. Room supprime la note de SQLite.
7. LiveData notifie l’interface.
8. Le RecyclerView est mis à jour.

---

## Tests réalisés:

### Test 1 : Ajout simple

Action :

- Saisir un titre
- Saisir une description
- Cliquer sur le bouton d’ajout

Résultat attendu :

- La note apparaît directement dans la liste.

---

### Test 2 : Validation des champs

Action :

- Laisser le titre ou la description vide
- Cliquer sur le bouton d’ajout

Résultat attendu :

- Un message d’erreur apparaît sur le champ concerné.

---

### Test 3 : Suppression individuelle

Action :

- Effectuer un clic long sur une note

Résultat attendu :

- La note est supprimée de la liste.

---

### Test 4 : Suppression globale

Action :

- Cliquer sur le bouton `VIDER LA LISTE`

Résultat attendu :

- Toutes les notes sont supprimées.
- Le message d’état vide réapparaît.

---

### Test 5 : Persistance locale

Action :

- Ajouter plusieurs notes
- Fermer complètement l’application
- Rouvrir l’application

Résultat attendu :

- Les notes sont toujours présentes.

---

### Test 6 : Rotation de l’écran

Action :

- Ajouter des notes
- Tourner l’écran en mode paysage

Résultat attendu :

- Les notes restent affichées.
- L’application conserve son état grâce au ViewModel.
- Le scroll permet de parcourir l’interface en mode paysage.

---

## Ce que j’ai appris:

Ce laboratoire m’a permis de comprendre l’importance d’une architecture propre dans une application Android.

J’ai appris à :

- Structurer une application avec MVVM
- Séparer l’interface, la logique et l’accès aux données
- Utiliser Room pour stocker des données localement
- Créer une Entity, un DAO et une RoomDatabase
- Utiliser un Repository pour centraliser l’accès aux données
- Manipuler ViewModel pour gérer les changements de configuration
- Observer les données avec LiveData
- Afficher une liste dynamique avec RecyclerView
- Supprimer des éléments par clic long
- Éviter les opérations longues sur le thread principal
- Améliorer l’interface avec des drawables XML personnalisés

---

## Conclusion:

Ce laboratoire montre comment construire une application Android locale plus professionnelle en utilisant une architecture moderne et maintenable.

Grâce à Room, les notes sont stockées durablement dans SQLite. Grâce à LiveData, l’interface se met à jour automatiquement lorsque les données changent. Grâce au ViewModel, l’application gère mieux les changements de configuration comme la rotation de l’écran.

L’architecture MVVM rend le projet plus clair, plus testable et plus facile à faire évoluer. Cette structure pourra être réutilisée dans des applications plus avancées, par exemple avec une synchronisation distante, une recherche, une modification de notes ou une authentification utilisateur.
