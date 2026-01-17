# BDD Проект

[![Java CI with Gradle](https://github.com/cybdar/bdd/actions/workflows/gradle.yml/badge.svg)](https://github.com/cybdar/bdd/actions)

Тесты перевода денег между картами.

### **Быстрый старт**

````
mkdir -p artifacts
wget -O artifacts/app.jar \
https://github.com/netology-code/aqa-homeworks/raw/master/bdd/app-ibank-build-for-testers.jar

java -jar artifacts/app.jar  # В одном терминале
./gradlew test               # В другом терминале

````

### **Данные для входа**

Логин: `vasya`

Пароль: `qwerty123`

Код: `12345`

### **Заметка**

Тесты падают - так и задумано в задании (ищем баги).

### **Ссылки**

[Баги в Issues](https://github.com/cybdar/bdd/issues)

[CI/CD Status](https://github.com/cybdar/bdd/actions)
