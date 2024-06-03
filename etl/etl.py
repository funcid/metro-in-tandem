import psycopg2
from dotenv import load_dotenv
import os
import pathlib
import time

from tables.metro_stations import extract_transform_load_metro_stations
from tables.metro_times import extract_transform_load_metro_times
from tables.metro_transfer_times import extract_transform_load_metro_transfer_times
from tables.employees import extract_transform_load_employees
from tables.applications import extract_transform_load_applications
from tables.passangers_not_show import extract_transform_load_passenger_no_show
from tables.bid_cancellations import extract_transform_load_bid_cancellations
from tables.time_changes import extract_transform_load_time_changes

# Загрузка переменных окружения из .env файла
load_dotenv(dotenv_path=pathlib.Path('..') / '.env')

def measure_time_and_execute(func, cursor):
    start_time = time.time()
    func(cursor)
    end_time = time.time()
    execution_time = end_time - start_time
    print(f"Function {func.__name__} executed in {execution_time:.4f} seconds")

def create_tables_and_insert_data():
    # Получение данных из переменных окружения
    dbname = os.getenv('POSTGRES_DB')
    user = os.getenv('POSTGRES_USER')
    password = os.getenv('POSTGRES_PASSWORD')
    host = os.getenv('POSTGRES_HOST')
    port = os.getenv('POSTGRES_PORT')

    # Подключение к базе данных
    connection = psycopg2.connect(
        dbname=dbname,
        user=user,
        password=password,
        host=host,
        port=port
    )

    cursor = connection.cursor()

    functions = [
        extract_transform_load_metro_stations,
        extract_transform_load_metro_times,
        extract_transform_load_metro_transfer_times,
        extract_transform_load_employees,
        extract_transform_load_applications,
        extract_transform_load_passenger_no_show,
        extract_transform_load_bid_cancellations,
        extract_transform_load_time_changes,
    ]

    for func in functions:
        measure_time_and_execute(func, cursor)

    # Сохранение изменений и закрытие подключения
    connection.commit()
    cursor.close()
    connection.close()

if __name__ == "__main__":
    create_tables_and_insert_data()