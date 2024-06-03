import json
from psycopg2.extras import execute_values
from datetime import datetime

def parse_date(date_str, formats=['%d.%m.%Y %H:%M:%S', '%d.%m.%Y %H:%M', '%Y-%m-%d %H:%M:%S']):
    """
    Функция для парсинга даты, поддерживающая несколько форматов.
    """
    for fmt in formats:
        try:
            return datetime.strptime(date_str, fmt)
        except ValueError:
            pass
    raise ValueError(f"Date format for {date_str} is not supported")

def extract_transform_load_time_changes(cursor):
    # Создание таблицы, если она не существует
    create_table_query = '''
    CREATE TABLE IF NOT EXISTS time_changes (
        id_bid VARCHAR PRIMARY KEY,
        time_edit TIMESTAMP,
        time_s TIME,
        time_f TIME
    );
    '''
    cursor.execute(create_table_query)

    # Чтение JSON-файла
    with open('datasets/Переносы заявок по времени.json', encoding='utf-8') as file:
        data = json.load(file)

    # Преобразуем данные в список кортежей для bulk-операции
    records = []
    for record in data:
        time_edit_val = parse_date(record['time_edit'])
        time_s_val = datetime.strptime(record['time_s'], '%H:%M').time()
        time_f_val = datetime.strptime(record['time_f'], '%H:%M').time()
        
        records.append((
            record['id_bid'],
            time_edit_val,
            time_s_val,
            time_f_val
        ))

    # Вставка данных в таблицу с помощью bulk-операции
    insert_query = '''
    INSERT INTO time_changes (id_bid, time_edit, time_s, time_f)
    VALUES %s
    ON CONFLICT (id_bid) DO NOTHING;
    '''
    execute_values(cursor, insert_query, records)