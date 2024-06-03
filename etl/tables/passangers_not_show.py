import json
from psycopg2.extras import execute_values
from datetime import datetime

def parse_date(date_str, formats=['%d.%m.%Y %H:%M', '%Y-%m-%d %H:%M:%S']):
    """
    Функция для парсинга даты, поддерживающая несколько форматов.
    """
    for fmt in formats:
        try:
            return datetime.strptime(date_str, fmt)
        except ValueError:
            pass
    raise ValueError(f"Date format for {date_str} is not supported")

def extract_transform_load_passenger_no_show(cursor):
    # Создание таблицы, если она не существует
    create_table_query = '''
    CREATE TABLE IF NOT EXISTS passenger_no_show (
        id_bid VARCHAR PRIMARY KEY,
        date_time TIMESTAMP NOT NULL
    );
    '''
    cursor.execute(create_table_query)

    # Чтение JSON-файла
    with open('datasets/Неявка пассажира.json', encoding='utf-8') as file:
        data = json.load(file)

    # Преобразуем данные в список кортежей для bulk-операции
    records = []
    for record in data:
        date_time_val = parse_date(record['DATE_TIME'], formats=['%d.%m.%Y %H:%M'])
        
        records.append((
            record['ID_BID'],
            date_time_val
        ))

    # Вставка данных в таблицу с помощью bulk-операции
    insert_query = '''
    INSERT INTO passenger_no_show (id_bid, date_time)
    VALUES %s
    ON CONFLICT (id_bid) DO NOTHING;
    '''
    execute_values(cursor, insert_query, records)