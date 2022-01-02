# Keepass Tray - by please-open.it

**This is a prototype**

This little app is a small client for [keepass](https://keepass.info), that gives access to your Keepass database directly from
the system tray (Mac, Windows, Linux).

![](.README_images/92f98303.png)

Search for a password, click on the entry to copy it in the system clipboard.

![](.README_images/c583242e.png)

Supports databases with password and keys protection.

## Build

```bash
mvn clean compile assembly:single
```

## Run

```bash
java -jar ./target/KeepassTray-1.0-SNAPSHOT-jar-with-dependencies.jar
```

