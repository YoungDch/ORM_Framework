package ru.cherkashin.orm_framework.orm.interfaceORM;

import java.util.List;
import java.util.Map;

public interface DataRepositoryORM {

    public <T> T read(String tableName, int id, Map<String, String> fieldsParam, Class<T> classObject);
    public <T> List<?> readTable(String tableName, Map<String, String> fieldsParam, Class<T> classObject);
    public <T> boolean add(String tableName, T objValue);

}
