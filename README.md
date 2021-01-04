# README

Implementation of game Cards Against Humanity.

## Setup

## Build

```
set PATH=C:\a\software\apache-maven-3.5.0\bin;%PATH%
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_131
mvn clean package "-Dspring.profiles.active=dev" "-Dspring.config.location=file:../cocorido-props/backend/"
```

## Run

### Eclipse - Run configuration - DEV

```
Goal: compile exec:java -Dexec.mainClass="it.vitalegi.cocorido.CocoridoApplication"
Parameters:
spring.profiles.active=dev
spring.config.location=file:../cocorido-props/backend/
```

### Windows - PROD

```
java -jar .\target\cocorido-${project.version}.jar --spring.profiles.active=prod --spring.config.location=file:../cocorido-props/backend/
```

### Linux - PROD

```
java -jar ./target/cocorido-${project.version}.jar --spring.profiles.active=prod --spring.config.location=file:../cocorido-props/backend/
```
