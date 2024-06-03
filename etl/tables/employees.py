import json
from psycopg2.extras import execute_values
from datetime import datetime

def extract_transform_load_employees(cursor):
    # Создание таблицы, если она не существует
    create_table_query = '''
    CREATE TABLE IF NOT EXISTS employees (
        id VARCHAR PRIMARY KEY,
        date DATE NOT NULL,
        time_work VARCHAR NOT NULL,
        fio VARCHAR NOT NULL,
        uchastok VARCHAR NOT NULL,
        smena VARCHAR NOT NULL,
        rank VARCHAR NOT NULL,
        sex VARCHAR NOT NULL
    );
    '''
    cursor.execute(create_table_query)

    # Чтение JSON-файла
    with open('datasets/Сотрудники.json', encoding='utf-8') as file:
        data = json.load(file)

    # Преобразуем данные в список кортежей для bulk-операции
    records = []
    for employee in data:
        # Преобразование даты
        date = datetime.strptime(employee['DATE'], '%d.%m.%Y').date()
        
        # Собираем кортеж
        records.append((
            employee['ID'],
            date,
            employee['TIME_WORK'],
            employee['FIO'],
            employee['UCHASTOK'],
            employee['SMENA'],
            employee['RANK'],
            employee['SEX']
        ))

    # Вставка данных в таблицу с помощью bulk-операции
    insert_query = '''
    INSERT INTO employees (id, date, time_work, fio, uchastok, smena, rank, sex)
    VALUES %s
    ON CONFLICT (id) DO NOTHING;
    '''
    execute_values(cursor, insert_query, records)