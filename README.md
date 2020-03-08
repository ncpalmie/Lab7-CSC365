# Lab7-CSC365
Lab 7 for Migler's 365 Winter 2020

## Team Members
[Nathan Palmiero](github.com/ncpalmie)
[Garrett O'Keefe](github.com/GMOkeefe)

## Compilation Instructions
### Requirements
OpenJFX
MySQL Connector/J

### For Console
```javac -d bin/classes src/com/lab7/console/*.java src/com/lab7/lib/*.java```
```java com.lab7.console.InnReservations```

### For GUI
```javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -d bin/classes/ src/com/lab7/gui/*.java src/com/lab7/gui/controller/*.java src/com/lab7/lib/*.java```
```java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml com.lab7.gui.InnReservations```

### Environment Variables
```CLASSPATH```: ensure that bin/classes, src/com/lab7/gui/view, src/com/lab7/gui/style here, as well as the directories of OpenJFX .jar files and MySQL Connector/J .jar file.
```PATH_TO_FX```: path to your OpenJFX .jar files.
```HP_JDBC_URL```: URL to MySQL database.
```HP_JDBC_USER```: username to MySQL database.
```HP_JDBC_PW```: password to MySQL database.
