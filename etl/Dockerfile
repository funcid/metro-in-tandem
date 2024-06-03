# Используем официальный образ Python
FROM python:3.9-slim

# Установим системные зависимости
RUN apt-get update && \
    apt-get install -y build-essential libpq-dev && \
    rm -rf /var/lib/apt/lists/*

# Устанавливаем необходимые зависимости
RUN pip install psycopg2-binary python-dotenv

# Копируем все содержимое текущего каталога (корень проекта) в контейнер
COPY . /app

# Переходим в рабочую директорию
WORKDIR /app/

# Команда по умолчанию для запуска скрипта
CMD ["python", "etl.py"]