# Используем официальный образ Gradle для сборки проекта
FROM gradle:7.3.3-jdk11 AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /gradle

# Копируем весь проект в контейнер
COPY . .

# Сборка проекта
RUN gradle build

# Используем официальный образ для Java для запуска приложения
FROM openjdk:11-jre-slim

# Создаем директорию для приложения
WORKDIR /app

# Копируем собранный jar файл из предыдущего этапа
COPY --from=build /gradlebuild/libs/*.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]