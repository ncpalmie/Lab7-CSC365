# Lab7-CSC365
Lab 7 for Migler's 365 Winter 2020

## Team Members
[Nathan Palmiero](github.com/ncpalmie)

[Garrett O'Keefe](github.com/GMOkeefe)

## Compilation Instructions
### Requirements
- JUnit
- MySQL Connector/J

### For Console
```javac -d bin/classes src/com/lab7/console/*.java src/com/lab7/lib/*.java```

```cd bin/classes```

```java com.lab7.console.InnReservations```

### Environment Variables
```Database with lab7 specific tables setup: ncpalmie```

```CLASSPATH```: ensure that bin/classes, src/com/lab7/gui, are here, as well as the directories of the MySQL Connector/J .jar file and the JUnit .jar file(s).

```APP_JDBC_URL```: URL to MySQL database.

```APP_JDBC_USER```: username to MySQL database.

```APP_JDBC_PW```: password to MySQL database.
