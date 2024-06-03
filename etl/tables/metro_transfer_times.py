import json
from psycopg2.extras import execute_values

def extract_transform_load_metro_transfer_times(cursor):
    # Создание таблицы, если она не существует
    create_table_query = '''
    CREATE TABLE IF NOT EXISTS metro_transfer_times (
        id SERIAL PRIMARY KEY,
        time INTEGER NOT NULL,
        id1 VARCHAR NOT NULL,
        id2 VARCHAR NOT NULL
    );
    '''
    cursor.execute(create_table_query)
    
    # Чтение JSON-файла
    with open('datasets/Метро время пересадки между станциями.json', encoding='utf-8') as file:
        data = json.load(file)

    # Преобразуем данные в список кортежей для bulk-операции
    records = [
        (int(transfer['time']), transfer['id1'], transfer['id2'])
        for transfer in data
    ]

    # Вставка данных в таблицу с помощью bulk-операции
    insert_query = '''
    INSERT INTO metro_transfer_times (time, id1, id2)
    VALUES %s
    ON CONFLICT DO NOTHING;
    '''
    execute_values(cursor, insert_query, records)