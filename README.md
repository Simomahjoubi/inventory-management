# Système de gestion d'inventaire client-serveur avec RMI

## Description

Ce projet implémente un **système de gestion d'inventaire client-serveur** utilisant **Java RMI** (Remote Method Invocation) pour la communication entre un client et un serveur. Le système permet à l'utilisateur de gérer un inventaire de produits via une interface graphique (GUI) Swing côté client et une base de données MySQL pour stocker les informations des produits et des utilisateurs.

### Objectifs du projet

- **Gestion des produits** : Ajouter, modifier, supprimer et rechercher des produits dans un inventaire.
- **Authentification des utilisateurs** : Permettre aux utilisateurs de se connecter au système via un système de validation des identifiants.
- **Architecture client-serveur** : Utilisation de **RMI** pour la communication entre le client et le serveur.
- **Base de données** : Utilisation de **MySQL** pour stocker les informations des produits et des utilisateurs.

## Architecture

### Diagrammes UML

#### Diagramme de classe

![Diagramme de classe UML](sandbox:/mnt/data/A_detailed_UML_class_diagram_for_a_client-server_i.png)

#### Diagramme de séquence

![Diagramme de séquence UML](sandbox:/mnt/data/A_detailed_UML_sequence_diagram_for_a_client-serve.png)

### Description de l'architecture client-serveur

Le projet utilise une **architecture client-serveur** avec **Java RMI** pour la communication. Voici un aperçu de cette architecture :

- **Client** :
  - Utilise **Swing** pour l'interface utilisateur.
  - Se connecte au serveur via **RMI** pour effectuer des opérations sur les produits et les utilisateurs.
  
- **Serveur** :
  - Implémente l'interface **`InventoryRemote`** pour exposer les méthodes de gestion des produits et de l'authentification.
  - Interagit avec la **base de données** via les classes **DAO** (Data Access Object).
  
- **Base de données (MySQL)** :
  - Stocke les informations des produits et des utilisateurs.
  - Les classes **`ProductDAO`** et **`UserDAO`** s'occupent de l'interaction avec la base de données.

## Fonctionnement des principales classes

1. **`InventoryRemote`** : Interface distante exposant les méthodes utilisées par le client pour interagir avec le serveur.
2. **`InventoryServerImpl`** : Implémentation de `InventoryRemote`, responsable de la gestion des produits et des utilisateurs.
3. **`ProductDAO`** : Classe qui gère l'accès aux données des produits dans la base de données à l'aide de JDBC.
4. **`UserDAO`** : Classe responsable de l'authentification des utilisateurs, interrogeant la base de données pour valider les informations de connexion.
5. **`InventoryGUIClient`** : Interface graphique côté client permettant à l'utilisateur de gérer l'inventaire.
6. **`LoginGUI`** : Interface pour la connexion des utilisateurs au système.

## Justification des choix technologiques

1. **JDBC** : Utilisé pour interagir avec la base de données MySQL. JDBC permet d'exécuter des requêtes SQL pour gérer les informations des produits et des utilisateurs.
2. **Java RMI** : Utilisé pour la communication client-serveur, permettant à l'interface graphique du client de communiquer avec le serveur à distance.
3. **MySQL** : Base de données relationnelle utilisée pour stocker les informations des produits et des utilisateurs.
4. **Swing** : Utilisé pour créer l'interface graphique côté client.

## Instructions d'installation et d'exécution

### Prérequis

- **Java 8 ou supérieur**.
- **MySQL 5.7 ou supérieur**.
- **JDBC (MySQL Connector)** : Le connecteur MySQL doit être inclus dans le projet.

### Étapes pour configurer la base de données

1. **Créer la base de données** :
   Exécutez le script `schema.sql` pour créer les tables nécessaires dans MySQL.

   ```sql
   CREATE DATABASE inventory_db;
   USE inventory_db;

   CREATE TABLE products (
       id INT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(100),
       category VARCHAR(50),
       quantity INT,
       price DECIMAL(10, 2)
   );

   CREATE TABLE users (
       id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) UNIQUE,
       password VARCHAR(255)
   );
