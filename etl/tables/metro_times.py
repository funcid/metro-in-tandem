import json
from psycopg2.extras import execute_values

def extract_transform_load_metro_times(cursor):
    # Создание таблицы, если она не существует
    create_table_query = '''
    CREATE TABLE IF NOT EXISTS metro_times (
        id SERIAL PRIMARY KEY,
        id_st1 VARCHAR NOT NULL,
        id_st2 VARCHAR NOT NULL,
        time DOUBLE PRECISION NOT NULL
    );
    '''
    cursor.execute(create_table_query)
    
    # Чтение JSON-файла
    with open('datasets/Метро время между станциями.json', encoding='utf-8') as file:
        data = json.load(file)

    # Преобразуем данные в список кортежей для bulk-операции
    records = [
        (time_record['id_st1'], time_record['id_st2'], float(time_record['time'].replace(',', '.')))
        for time_record in data
    ]

    # Вставка данных в таблицу с помощью bulk-операции
    insert_query = '''
    INSERT INTO metro_times (id_st1, id_st2, time)
    VALUES %s
    ON CONFLICT DO NOTHING;
    '''
    execute_values(cursor, insert_query, records)