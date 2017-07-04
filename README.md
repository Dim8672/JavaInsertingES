# JavaInsertingES

# Search Guard 5 for Elasticsearch 5

Ce programme a été téléchargé sur le GitHub officiel de SearchGuard: https://github.com/floragunncom/search-guard

# Adaptation in CityFlow

Les classes que j'ai ajouté se trouvent dans le package "ch.hearc.ig.tb.cityFlow.*" et elles permettent l'insertion de données aléatoires dans ElasticSearch

# Configuration

Afin de faire fonctionner l'application, il faut compléter le fichier configuration.properties avec les bonnes valeurs, ce fichier prend en compte 4 paramètres :
SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD : Mot de passe keystore (se trouve dans le fichier de configuration d'ElasticSearch)
SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD : Mot de passe trustore (se trouve dans le fichier de configuration d'ElasticSearch)
SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH : Chemin d'accès au fichier keystore.jks
SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH : Chemin d'accès au fichier trustore.jks

La classe principale se trouve dans le dossier src.main.ch.hearc.ig.tb.cityFlow.application. La logique est la suivante :

1) Mise en place de la connexion à ElasticSearch avec le fichier de configuration
2) Génération d'un nombre X de personnes aléatoires conformément à la classe Person.
3) Génération de fait aléatoires sur une borne avec la méthode generateDataFacts avec le dernier paramètre à false
4) Génération de trajet aléatoires sur toutes les bornes avec la méthode generateDataFacts avec le dernier paramètre à true
