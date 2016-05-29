# Projet Export d'agences

Le but de ce projet est de créer un programme Java permettant d'exporter les agences d'un service d'urgence au format XML.

##Utilisation:
```
java ExpAgncy [-dbserver db] -u unum [-o fichier.xml] [-d] [-t] 

-dbserver db est la référence à la base de données, par défaut désigne la base de données de développement. Voir fichier MyDatabases.prop (optionnel).
-u unum est la référence du service d'urgence (obligatoire).
-o fichier.xml est le nom du fichier qui recevra les agences au format XML. Amorcé à agences.xml par défaut (optionnel).
-d le programme s'exécute en mode débug, il est beaucoup plus verbeux. Désactivé par défaut (optionnel).
-t le programme s'exécute en mode test, les transcations en base de données ne sont pas faites. Désactivé par défaut (optionnel).
```

##Pré-requis :
- Java 6 ou supérieur.
- JDBC Informix
- JDBC MySql

##Formats XML reconnus :

Il existe deux types de formats XML reconnus pour décrire les agences :
- un format simple dit "plat" défini dans le fichier *agences_plat.xsd*. Avec ce format permet le fichier XML des agences peut être importé dans Microsoft Excel.
- un format plus structuré défini dans le fichier *agences.xsd*.

Il existe également un ancien format défini par une DTD dans le fichier *agences.dtd". Le fichier XML résultant n'est pas importable dans Microsoft Excel.

##Références:

- Construire une application XML, J.C. Bernadac, F. Knab, Eyrolles.

- [OpenClassroom Java XML](https://openclassrooms.com/courses/structurez-vos-donnees-avec-xml/dom-exemple-d-utilisation-en-java)
- [Syntaxe Markdown](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)
- [Tuto OpenClassroom sur DTD](https://openclassrooms.com/courses/structurez-vos-donnees-avec-xml/introduction-aux-definitions-et-aux-dtd)
- [Tuto W3C sur DTD (en)](https://www.google.fr/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&sqi=2&ved=0ahUKEwiDrurll-fMAhWHBsAKHYdzAegQFggfMAA&url=http%3A%2F%2Fwww.w3schools.com%2Fxml%2Fxml_dtd_intro.asp&usg=AFQjCNGCt7X2oRyUSkTES1aXf8GljqhekA&bvm=bv.122448493,d.ZGg)
- [Validation fichier XML](http://www.xmlvalidation.com/)
- [Convertisseur DTD/XSD](http://www.freeformatter.com/xsd-generator.html)
- [Tuto XML/XSD](http://www.codeguru.com/java/article.php/c13529/XSD-Tutorial-XML-Schemas-For-Beginners.htm)

##Fichier des paramètres : 

Ce fichier permet de spécifier les paramètres d'accès aux différentes bases de données.

A adapter selon les implémentations locales.

Ce fichier est nommé : *MyDatabases.prop*.

Le fichier ci-dessous *MyDatabases_Example.prop" est donné à titre d'exemple.
```
# Properties for production environnement
prod.dbserver.name=eole
prod.dbserver.ip=1.2.3.4
prod.dbserver.port=1234
prod.dbserver.dbname=bdd
prod.dbserver.login=user
prod.dbserver.passwd=passwd
prod.dbserver.informixserver=bdd
prod.dbserver.drivername=Informix
prod.dbserver.driverclass=com.informix.jdbc.IfxDriver
prod.dbserver.nb.thread=8

# No pre-prod dbserver for Anstel

# Properties for development environnement
dev.dbserver.name=zephir
dev.dbserver.ip=1.2.3.5
dev.dbserver.port=1235
dev.dbserver.dbname=bdd
dev.dbserver.login=user
dev.dbserver.passwd=passwd
dev.dbserver.informixserver=bdd
dev.dbserver.drivername=Informix
dev.dbserver.driverclass=com.informix.jdbc.IfxDriver
dev.dbserver.nb.thread=8

# Properties for MySQL development environnement
mysql.dbserver.name=vmsrv
mysql.dbserver.ip=localhost
mysql.dbserver.port=1234
mysql.dbserver.dbname=bdd
mysql.dbserver.login=user
mysql.dbserver.passwd=passwd
mysql.dbserver.drivername=MySQL
mysql.dbserver.driverclass=com.mysql.jdbc.Driver
mysql.dbserver.nb.thread=8
```

