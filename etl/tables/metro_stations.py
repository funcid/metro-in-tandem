import json
from psycopg2.extras import execute_values

def extract_transform_load_metro_stations(cursor):
    # Создание таблицы, если она не существует
    create_table_query = '''
    CREATE TABLE IF NOT EXISTS metro_stations (
        id VARCHAR PRIMARY KEY,
        name_station VARCHAR NOT NULL,
        name_line VARCHAR NOT NULL,
        id_line VARCHAR NOT NULL
    );
    '''
    cursor.execute(create_table_query)
    
    # Чтение JSON-файла
    with open('datasets/Наименование станций метро.json', encoding='utf-8') as file:
        data = json.load(file)

    # Преобразуем данные в список кортежей для bulk-операции
    records = [
        (station['id'], station['name_station'], station['name_line'], station['id_line'])
        for station in data
    ]

    # Вставка данных в таблицу с помощью bulk-операции
    insert_query = '''
    INSERT INTO metro_stations (id, name_station, name_line, id_line)
    VALUES %s
    ON CONFLICT (id) DO NOTHING;
    '''
    execute_values(cursor, insert_query, records)