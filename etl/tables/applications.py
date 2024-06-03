import json
from psycopg2.extras import execute_values
from datetime import datetime

def parse_date(date_str):
    """
    Функция для парсинга даты, поддерживающая несколько форматов.
    """
    for fmt in ('%d.%m.%Y %H:%M:%S', '%Y-%m-%d %H:%M:%S'):
        try:
            return datetime.strptime(date_str, fmt)
        except ValueError:
            pass
    raise ValueError(f"Date format for {date_str} is not supported")

def extract_transform_load_applications(cursor):
    # Создание таблицы, если она не существует
    create_table_query = '''
    CREATE TABLE IF NOT EXISTS applications (
        id VARCHAR PRIMARY KEY,
        id_pas VARCHAR,
        datetime TIMESTAMP,
        time3 TIME,
        time4 TIME,
        cat_pas VARCHAR,
        status VARCHAR,
        tpz TIMESTAMP,
        insp_sex_m INT,
        insp_sex_f INT,
        time_over TIME,
        id_st1 VARCHAR,
        id_st2 VARCHAR
    );
    '''
    cursor.execute(create_table_query)

    # Чтение JSON-файла
    with open('datasets/Заявки.json', encoding='utf-8') as file:
        data = json.load(file)

    # Преобразуем данные в список кортежей для bulk-операции
    records = []
    for application in data:
        # Преобразование временных и датовых полей
        datetime_val = parse_date(application['datetime'])
        tpz_val = parse_date(application['tpz'])
        time3_val = datetime.strptime(application['time3'], '%H:%M:%S').time()
        time4_val = datetime.strptime(application['time4'], '%H:%M:%S').time()
        time_over_val = datetime.strptime(application['TIME_OVER'], '%H:%M:%S').time()
        
        # Собираем кортеж
        records.append((
            application['id'],
            application['id_pas'],
            datetime_val,
            time3_val,
            time4_val,
            application['cat_pas'],
            application['status'],
            tpz_val,
            int(application['INSP_SEX_M']),
            int(application['INSP_SEX_F']),
            time_over_val,
            application['id_st1'],
            application['id_st2']
        ))

    # Вставка данных в таблицу с помощью bulk-операции
    insert_query = '''
    INSERT INTO applications (id, id_pas, datetime, time3, time4, cat_pas, status, tpz, insp_sex_m, insp_sex_f, time_over, id_st1, id_st2)
    VALUES %s
    ON CONFLICT (id) DO NOTHING;
    '''
    execute_values(cursor, insert_query, records)
