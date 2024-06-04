FROM openjdk:22-jdk

RUN microdnf install findutils

WORKDIR /app

COPY . .

# Даём права на выполнение скрипта gradlew
RUN chmod +x gradlew

# Скачиваем все зависимости и собираем проект
RUN ./gradlew build --no-daemon

# Устанавливаем команду для запуска приложения
CMD ["java", "-jar", "build/libs/internal-0.0.1-SNAPSHOT.jar"]