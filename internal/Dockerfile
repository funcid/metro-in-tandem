FROM openjdk:21-jdk

RUN microdnf install findutils unzip curl

WORKDIR /app

COPY . .

# Устанавливаем последнюю версию Gradle вручную
RUN curl -LO https://services.gradle.org/distributions/gradle-8.2.1-bin.zip \
    && unzip gradle-8.2.1-bin.zip \
    && mv gradle-8.2.1 /opt/gradle \
    && ln -s /opt/gradle/bin/gradle /usr/bin/gradle \
    && rm gradle-8.2.1-bin.zip

# Скачиваем все зависимости и собираем проект
RUN gradle build --no-daemon -x test

# Устанавливаем команду для запуска приложения
CMD ["java", "-jar", "build/libs/internal-0.0.1-SNAPSHOT.jar"]