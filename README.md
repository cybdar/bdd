# Тестирование перевода денег

[![Java CI](https://github.com/cybdar/bdd/actions/workflows/gradle.yml/badge.svg)](https://github.com/cybdar/bdd/actions)

```bash
mkdir -p artifacts
wget -O artifacts/app-ibank-build-for-testers.jar \
  https://github.com/netology-code/aqa-homeworks/raw/master/bdd/app-ibank-build-for-testers.jar

java -jar artifacts/app-ibank-build-for-testers.jar &
./gradlew test
```

Баг: перевод суммы больше баланса возможен.