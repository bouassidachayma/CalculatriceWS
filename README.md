# CalculatriceWS — Développement d'un Service Web SOAP avec Java

> **TP2 : Mise en œuvre de Jakarta XML Web Services**
> **Matière :** Architecture SOA et Services Web
> **Filière :** LSI3-GLSI
> **Établissement :** Faculté des Sciences de Sfax
> **Année universitaire :** 2025-2026
> **Réalisé par :** Chayma Bouassida

---

## 1. Présentation du projet

Ce projet consiste à développer et à publier un service Web SOAP en Java à l'aide de Maven et de l'API JAX-WS.

L'application **CalculatriceWS** expose des opérations mathématiques élémentaires permettant à un client distant d'effectuer des calculs à travers une interface de communication standardisée.

Le service propose quatre opérations :

* `somme` : addition de deux nombres ;
* `multiplication` : multiplication de deux nombres ;
* `soustraction` : soustraction de deux nombres ;
* `division` : division de deux nombres.

Le service est publié à l'aide de la méthode `Endpoint.publish()`. Son contrat est décrit par un fichier WSDL généré automatiquement. Les opérations sont ensuite testées à l'aide de **SoapUI**, qui permet d'envoyer des requêtes SOAP et d'analyser les réponses XML retournées par le serveur.

Ce TP permet ainsi de mettre en pratique les concepts fondamentaux de l'architecture orientée services (SOA) et de la communication entre applications distribuées.

## 2. Architecture SOAP : cycle de vie d'un service Web

### 2.1 Principe général

Le cycle de vie d'un service Web SOAP repose sur trois acteurs principaux :

* **Serveur (producteur)** : développe et publie le service Web.
* **Annuaire (UDDI)** : permet théoriquement la publication et la découverte des services Web.
* **Client (consommateur)** : recherche le service, récupère son contrat WSDL et invoque ses opérations.

### 2.2 Schéma du cycle de vie

Le schéma suivant représente les principales étapes de communication entre le serveur, l'annuaire et le client.

```mermaid
sequenceDiagram
    participant S as Serveur<br/>(Producteur)
    participant A as Annuaire<br/>(UDDI)
    participant C as Client<br/>(SoapUI)

    S->>A: ① Publication du service et du contrat WSDL
    C->>A: ② Interrogation de l'annuaire
    A-->>C: ③ Informations du service et contrat WSDL
    C->>S: ④ Requête HTTP contenant un message SOAP
    S-->>C: ⑤ Réponse HTTP contenant le résultat SOAP
```

### 2.3 Description des étapes

| Étape           | Acteurs            | Description                                                                            |
| --------------- | ------------------ | -------------------------------------------------------------------------------------- |
| ① Publication   | Serveur → Annuaire | Le producteur publie le service Web et son contrat WSDL.                               |
| ② Interrogation | Client → Annuaire  | Le client recherche un service correspondant à son besoin.                             |
| ③ Découverte    | Annuaire → Client  | L'annuaire retourne les informations permettant d'utiliser le service.                 |
| ④ Invocation    | Client → Serveur   | Le client envoie une requête HTTP contenant une enveloppe SOAP.                        |
| ⑤ Réponse       | Serveur → Client   | Le serveur retourne une réponse HTTP contenant le résultat sous forme de message SOAP. |

> **Application au TP :** l'annuaire UDDI n'est pas utilisé directement. SoapUI importe le WSDL à partir de l'URL du service, puis invoque les opérations SOAP.

### 2.4 Structure du contrat WSDL

Le fichier WSDL décrit le contrat du service Web. Il permet au client de connaître les opérations disponibles, les paramètres attendus et l'adresse du service.

| Élément WSDL  | Type     | Rôle                                                                |
| ------------- | -------- | ------------------------------------------------------------------- |
| `definitions` | Racine   | Déclare le contrat et les espaces de noms.                          |
| `types`       | Abstrait | Définit les types de données échangés à l'aide de XML Schema (XSD). |
| `message`     | Abstrait | Décrit les messages d'entrée et de sortie.                          |
| `portType`    | Abstrait | Définit les opérations proposées par le service.                    |
| `binding`     | Concret  | Précise le protocole et le format de communication SOAP.            |
| `service`     | Concret  | Décrit le service et son adresse d'accès (endpoint).                |

---

## 3. Structure du projet

Le projet Maven est organisé de manière à séparer le contrat du service, son implémentation et sa publication.

```text
CalculatriceWS/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            ├── serveur/
            │   └── ServeurJWS.java
            └── service/
                ├── ICalculatrice.java
                └── Calculatrice.java
```

### Rôle des composants

| Package   | Classe          | Responsabilité                                                |
| --------- | --------------- | ------------------------------------------------------------- |
| `service` | `ICalculatrice` | Définit le contrat du service Web et les opérations exposées. |
| `service` | `Calculatrice`  | Implémente les opérations mathématiques.                      |
| `serveur` | `ServeurJWS`    | Publie et démarre le service Web.                             |
| —         | `pom.xml`       | Configure le projet Maven et ses dépendances.                 |

Cette organisation permet de distinguer la définition des fonctionnalités, leur implémentation et leur mise à disposition aux clients.

---

### Relations entre les classes

```mermaid
classDiagram
    class ICalculatrice {
        <<interface>>
        +somme(double a, double b) double
        +multiplication(double a, double b) double
        +soustraction(double a, double b) double
        +division(double a, double b) double
    }
    class Calculatrice {
        +somme(double a, double b) double
        +multiplication(double a, double b) double
        +soustraction(double a, double b) double
        +division(double a, double b) double
    }
    class ServeurJWS {
        +main(String[] args) void
    }
    Calculatrice ..|> ICalculatrice : implémente
    ServeurJWS ..> Calculatrice : publie
```

Cette organisation permet de distinguer la définition des fonctionnalités, leur implémentation et leur mise à disposition aux clients.

---
---

## 4. Dépendances Maven

Le projet utilise Maven pour gérer les dépendances nécessaires au développement et à l'exécution du service Web SOAP.

Les principales dépendances sont :

| Dépendance           | Rôle                                                                    |
| -------------------- | ----------------------------------------------------------------------- |
| `jakarta.xml.ws-api` | Fournit les interfaces de l'API des services Web, notamment `Endpoint`. |
| `jaxws-rt`           | Fournit l'implémentation d'exécution du service Web SOAP.               |
| `jakarta.jws-api`    | Fournit les annotations utilisées pour déclarer les services Web.       |

Le fichier `pom.xml` centralise la configuration du projet et facilite la gestion des bibliothèques nécessaires.

![Configuration Maven](docs/images/01-maven-configuration.png)

---

## 5. Implémentation du service Web

### 5.1 Interface `ICalculatrice` : définition du contrat

L'interface `ICalculatrice` définit les opérations accessibles à travers le service Web.

Elle représente le contrat fonctionnel entre le client et le serveur.

Les annotations JAX-WS utilisées sont :

| Annotation              | Rôle                                                            |
| ----------------------- | --------------------------------------------------------------- |
| `@WebService`           | Déclare l'interface comme contrat d'un service Web.             |
| `@WebMethod`            | Expose une méthode comme opération du service.                  |
| `@WebParam(name = "a")` | Définit explicitement le nom du paramètre dans le message SOAP. |

#### Code source

```java
package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface ICalculatrice {

    @WebMethod
    double somme(@WebParam(name = "a") double a,
                 @WebParam(name = "b") double b);

    @WebMethod
    double multiplication(@WebParam(name = "a") double a,
                         @WebParam(name = "b") double b);

    @WebMethod
    double soustraction(@WebParam(name = "a") double a,
                       @WebParam(name = "b") double b);

    @WebMethod
    double division(@WebParam(name = "a") double a,
                    @WebParam(name = "b") double b);
}
```

L'interface définit les signatures des méthodes, sans contenir leur logique de calcul.

![Interface ICalculatrice](docs/images/03-intellij-icalculatrice.png)

### 5.2 Classe `Calculatrice` : implémentation des opérations

La classe `Calculatrice` implémente l'interface `ICalculatrice`.

L'annotation `@WebService(endpointInterface = "service.ICalculatrice")` permet de relier l'implémentation au contrat défini par l'interface.

Chaque méthode réalise une opération mathématique et retourne le résultat correspondant.

La division par zéro est traitée par le déclenchement d'une exception.

#### Code source

```java
package service;

import javax.jws.WebService;

@WebService(endpointInterface = "service.ICalculatrice")
public class Calculatrice implements ICalculatrice {

    @Override
    public double somme(double a, double b) {
        return a + b;
    }

    @Override
    public double multiplication(double a, double b) {
        return a * b;
    }

    @Override
    public double soustraction(double a, double b) {
        return a - b;
    }

    @Override
    public double division(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException(
                "Division by zero is not allowed."
            );
        }
        return a / b;
    }
}
```

![Classe Calculatrice](docs/images/04-intellij-calculatrice.png)

### 5.3 Classe `ServeurJWS` : publication du service

La classe `ServeurJWS` permet de démarrer le service Web et de le rendre accessible à travers une adresse HTTP.

La publication est réalisée par la méthode `Endpoint.publish()`.

#### Code source

```java
package serveur;

import javax.xml.ws.Endpoint;
import service.Calculatrice;

public class ServeurJWS {

    public static void main(String[] args) {

        String url = "http://localhost:8085/CalculatriceWS";

        Endpoint.publish(url, new Calculatrice());

        System.out.println("Service SOAP published successfully!");
        System.out.println("Service URL: " + url);
        System.out.println("WSDL URL: " + url + "?wsdl");
    }
}
```

Lors de l'exécution, le service est publié à l'adresse suivante :

```text
http://localhost:8085/CalculatriceWS
```

Le WSDL est accessible à partir de :

```text
http://localhost:8085/CalculatriceWS?wsdl
```

![Publication du service](docs/images/02-intellij-serveurjws.png)

---

## 6. Publication du service et génération du WSDL

### 6.1 Démarrage du service

Pour démarrer le service Web :

1. Ouvrir le projet dans IntelliJ IDEA.
2. Accéder à la classe `ServeurJWS`.
3. Exécuter la méthode `main`.
4. Vérifier les messages affichés dans la console.

La console indique que le service a été publié avec succès et affiche les URL d'accès.

### 6.2 Accès au service

| Ressource     | URL                                         |
| ------------- | ------------------------------------------- |
| Endpoint SOAP | `http://localhost:8085/CalculatriceWS`      |
| Contrat WSDL  | `http://localhost:8085/CalculatriceWS?wsdl` |

### 6.3 Rôle du WSDL

Le WSDL est généré automatiquement par l'environnement JAX-WS.

Il décrit les opérations du service, leurs paramètres, les messages SOAP et l'adresse du endpoint.

Le client peut ainsi découvrir les fonctionnalités du service sans avoir accès au code source de la classe `Calculatrice`.

![WSDL généré](docs/images/09-wsdl.png)

---

## 7. Tests fonctionnels avec SoapUI

### 7.1 Configuration du projet SoapUI

Les tests fonctionnels sont réalisés à l'aide de SoapUI.

La procédure consiste à :

1. Lancer le serveur `ServeurJWS`.
2. Ouvrir SoapUI.
3. Créer un nouveau projet SOAP.
4. Importer le WSDL :

```text
http://localhost:8085/CalculatriceWS?wsdl
```

5. Sélectionner l'opération à tester.
6. Renseigner les paramètres `a` et `b`.
7. Envoyer la requête.
8. Analyser la réponse XML retournée.

SoapUI génère automatiquement la structure des requêtes SOAP à partir du contrat WSDL.

### 7.2 Test de l'opération `somme`

**Paramètres :**

* `a = 5`
* `b = 3`

**Résultat attendu :** `8.0`

#### Requête SOAP

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:ser="http://service/">

    <soapenv:Header/>

    <soapenv:Body>
        <ser:somme>
            <a>5</a>
            <b>3</b>
        </ser:somme>
    </soapenv:Body>

</soapenv:Envelope>
```

#### Réponse SOAP

```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <ns2:sommeResponse xmlns:ns2="http://service/">
            <return>8.0</return>
        </ns2:sommeResponse>
    </S:Body>
</S:Envelope>
```

La réponse contient le résultat de l'addition dans l'élément `return`.

![Test SoapUI somme](docs/images/05-soapui-somme.png)

### 7.3 Test de l'opération `multiplication`

**Paramètres :**

* `a = 5`
* `b = 3`

**Résultat attendu :** `15.0`

La requête SOAP utilise l'opération `multiplication`. La réponse contient le résultat du calcul dans l'élément `return`.

![Test SoapUI multiplication](docs/images/06-soapui-multiplication.png)

### 7.4 Test de l'opération `soustraction`

**Paramètres :**

* `a = 5`
* `b = 3`

**Résultat attendu :** `2.0`

La requête SOAP utilise l'opération `soustraction`. Le service calcule la différence entre les deux paramètres et retourne le résultat.

![Test SoapUI soustraction](docs/images/07-soapui-soustraction.png)

### 7.5 Test de l'opération `division`

**Paramètres :**

* `a = 6`
* `b = 3`

**Résultat attendu :** `2.0`

La requête SOAP utilise l'opération `division`. Le service retourne le quotient des deux paramètres.

![Test SoapUI division](docs/images/08-soapui-division.png)

---

## 8. Récapitulatif des résultats

Les tests réalisés avec SoapUI permettent de vérifier le bon fonctionnement des quatre opérations exposées par le service Web.

| Opération        | Paramètre `a` | Paramètre `b` | Résultat attendu | Résultat obtenu | Temps de réponse | Taille de la réponse |
| ---------------- | ------------: | ------------: | ---------------: | --------------: | ---------------: | -------------------: |
| `somme`          |             5 |             3 |              8.0 |       **8.0** ✅ |           256 ms |           219 octets |
| `multiplication` |             5 |             3 |             15.0 |      **15.0** ✅ |            12 ms |           238 octets |
| `soustraction`   |             5 |             3 |              2.0 |       **2.0** ✅ |            11 ms |           233 octets |
| `division`       |             6 |             3 |              2.0 |       **2.0** ✅ |            56 ms |           225 octets |

Les résultats obtenus correspondent aux résultats attendus pour les quatre opérations testées.

Les temps de réponse et les tailles des réponses sont ceux relevés lors des tests SoapUI. Ils peuvent varier selon l'environnement d'exécution.

---

## 9. Évolution du projet

Le projet a été développé en deux versions :

| Version             | Fonctionnalités                                                                                    |
| ------------------- | -------------------------------------------------------------------------------------------------- |
| `CalculatriceWS`    | Première version contenant uniquement l'opération `somme`.                                         |
| `CalculatriceWS_V2` | Version étendue contenant les quatre opérations : somme, multiplication, soustraction et division. |

Cette évolution permet d'illustrer l'enrichissement progressif d'un service Web à partir d'une première fonctionnalité, jusqu'à une interface proposant plusieurs opérations.

---

## 10. Conclusion

Ce TP a permis de mettre en pratique les concepts fondamentaux du développement des services Web SOAP avec Java.

La réalisation du projet CalculatriceWS a permis de comprendre les différentes étapes de création d'un service Web : définition du contrat, implémentation des opérations, publication du service, génération du WSDL et tests fonctionnels avec SoapUI.

L'utilisation de JAX-WS a facilité l'exposition des méthodes Java sous forme d'opérations SOAP accessibles à distance. Le WSDL a permis de décrire le contrat du service, tandis que SoapUI a été utilisé pour envoyer des requêtes XML et vérifier les réponses obtenues.

Les tests réalisés ont confirmé le bon fonctionnement des quatre opérations mathématiques dans les cas testés.

Ce travail constitue une introduction pratique aux architectures orientées services (SOA), aux services Web SOAP et à la communication entre applications distribuées.

---

## Auteur

**Chayma Bouassida**
LSI3-GLSI — Faculté des Sciences de Sfax
Année universitaire 2025-2026
