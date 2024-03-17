package ru.cherkashin.orm_framework.orm;

import ru.cherkashin.orm_framework.orm.enumORM.ORMGenerateId;
import ru.cherkashin.orm_framework.orm.interfaceORM.ColumnDescriptionORM;
import ru.cherkashin.orm_framework.orm.interfaceORM.DataRepositoryORM;
import ru.cherkashin.orm_framework.orm.interfaceORM.ORMObject;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataRepository implements DataRepositoryORM {

    private static final String QUERY_SELECT_TABLE_WITH_WHERE = "SELECT * FROM %s WHERE id = ?";
    private static final String QUERY_SELECT_TABLE = "SELECT * FROM %s";
    private static final String QUERY_SELECT_LAST_INDEX = "SELECT MAX(id) FROM %s";
    private static final String QUERY_INSERT_TABLE = "INSERT INTO %s(%s) VALUES('%s')";
    private final ORMService ormService;

    public DataRepository() {
        this.ormService = new ORMService(this);
    }

    @Override
    public <T> T read(String tableName, int id, Map<String, String> fieldsParam, Class<T> classObject) {
        String query = String.format(QUERY_SELECT_TABLE_WITH_WHERE, tableName);
        Object obj = null;
        try (PreparedStatement statement = DataSource.getInstance().prepareStatement(query);) {

            statement.setInt(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            obj = classObject.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();

            while (resultSet.next()) {
                for (Map.Entry<String, String> entry : fieldsParam.entrySet()) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (field.getName().equals(entry.getKey())) {
                            String strResult = resultSet.getString(entry.getValue());
                            ColumnDescriptionORM fieldAnnotation = field.getAnnotation(ColumnDescriptionORM.class);
                            if (fieldAnnotation.thisORMObject()) {
                                var ORMObject = field.get(obj);
                                ORMObject = ormService.getValueOnIndex(Integer.parseInt(strResult), ORMObject.getClass());
                                field.set(obj, ORMObject);
                            } else if (field.getType().toString().equals("int")) {
                                field.set(obj, Integer.parseInt(strResult));
                            } else {
                                field.set(obj, strResult);
                            }
                        }
                    }
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (T) obj;
    }

    @Override
    public <T> List<?> readTable(String tableName, Map<String, String> fieldsParam, Class<T> classObject) {
        String query = String.format(QUERY_SELECT_TABLE, tableName);
        List<T> listObj = new ArrayList<>();
        try (PreparedStatement statement = DataSource.getInstance().prepareStatement(query);) {
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Object obj = classObject.newInstance();
                Field[] fields = obj.getClass().getDeclaredFields();

                for (Map.Entry<String, String> entry : fieldsParam.entrySet()) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (field.getName().equals(entry.getKey())) {
                            String strResult = resultSet.getString(entry.getValue());
                            if (strResult == null) continue;
                            ColumnDescriptionORM fieldAnnotation = field.getAnnotation(ColumnDescriptionORM.class);
                            if (fieldAnnotation.thisORMObject()) {
                                var ORMObject = field.get(obj);
                                ORMObject = ormService.getValueOnIndex(Integer.parseInt(strResult), ORMObject.getClass());
                                field.set(obj, ORMObject);
                            } else if (field.getType().toString().equals("int")) {
                                field.set(obj, Integer.parseInt(strResult));
                            } else field.set(obj, strResult);
                        }
                    }
                }
                listObj.add((T) obj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return listObj;
    }

    @Override
    public <T> boolean add(String tableName, T objValue) {
        List<String> stringParamsList = new ArrayList<>();
        List<String> stringValuesList = new ArrayList<>();
        Field[] fields = objValue.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ColumnDescriptionORM annotationsField = field.getAnnotation(ColumnDescriptionORM.class);
            if (annotationsField != null) {
                if (annotationsField.generatedId() != ORMGenerateId.AUTO_GENERATED) {
                    try {
                        String valueForList = "";
                        if (annotationsField.thisORMObject()) {
                            valueForList = getVaulueFromORMObject(field, (ORMObject) objValue);
                        } else valueForList = field.get(objValue).toString();
                        stringValuesList.add(valueForList);
                        stringParamsList.add(annotationsField.name());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else setIdForORMObject((ORMObject) objValue, tableName);
            }
        }
        String paramsForQuery = String.join(",", stringParamsList);
        String valueForQuery = String.join("','", stringValuesList);
        String query = String.format(QUERY_INSERT_TABLE, tableName, paramsForQuery, valueForQuery);
        boolean result = true;

        try (Statement statement = DataSource.getInstance().createStatement();) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }

    private void setIdForORMObject(ORMObject objValue, String tableName) {
        int idForObject = objValue.getIdForORM();
        if (objValue.getIdForORM() == 0) {
            idForObject = getLastIndex(tableName);
        }
        objValue.setIdForORM(idForObject);
    }

    private int getLastIndex(String tableName) {
        int result = 0;
        try (Statement statement = DataSource.getInstance().createStatement();) {
            statement.executeQuery(String.format(QUERY_SELECT_LAST_INDEX, tableName));

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) result = resultSet.getInt(1) + 1;
        } catch (SQLException e) {
            result = 0;
        }
        return result;
    }

    private String getVaulueFromORMObject(Field field, ORMObject objValue) {
        ORMObject valueORMObj = null;
        try {
            valueORMObj = (ORMObject) field.get(objValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String valueForList = "";
        if (valueORMObj.getIdForORM() == 0) {
            if (ormService.addNewValue(valueORMObj))
                valueForList = String.valueOf(valueORMObj.getIdForORM());
        } else valueForList = String.valueOf(valueORMObj.getIdForORM());
        return valueForList;
    }

}
