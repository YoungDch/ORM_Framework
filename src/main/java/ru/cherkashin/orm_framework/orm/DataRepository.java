package ru.cherkashin.orm_framework.orm;

import ru.cherkashin.orm_framework.orm.enumORM.ORMGenerateId;
import ru.cherkashin.orm_framework.orm.interfaceORM.ColumnDescriptionORM;
import ru.cherkashin.orm_framework.orm.interfaceORM.DataRepositoryORM;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataRepository implements DataRepositoryORM {

    private static final String QUERY_SELECT_TABLE_WITH_WHERE = "SELECT * FROM %s WHERE id = ?";
    private static final String QUERY_SELECT_TABLE = "SELECT * FROM %s";
    private static final String QUERY_INSERT_TABLE = "INSERT INTO %s(%s) VALUES('%s')";

    @Override
    public <T> T read(String tableName, int id, Map<String, String> fieldsParam, Class<T> classObject) {
        String query = String.format(QUERY_SELECT_TABLE_WITH_WHERE, tableName);
        Object obj = null;
        try {
            Connection conn = DataSource.getInstance();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            obj = classObject.newInstance();
            Field[] fields = obj.getClass().getFields();

            while (resultSet.next()){
                for(Map.Entry<String, String> entry : fieldsParam.entrySet()){
                    for(Field field : fields){
                        if(field.getName().equals(entry.getKey())){
                            String strResult = resultSet.getString(entry.getValue());
                            if(field.getType().toString().toLowerCase().equals("int")){
                                field.set(obj, Integer.parseInt(strResult));
                            }else field.set(obj, strResult);
                        }
                    }
                }
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (T) obj;
    }

    @Override
    public <T> List<?> readTable(String tableName, Map<String, String> fieldsParam, Class<T> classObject) {
        String query = String.format(QUERY_SELECT_TABLE, tableName);
        List<T> listObj = new ArrayList<>();
        try {
            Connection conn = DataSource.getInstance();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()){
                Object obj = classObject.newInstance();
                Field[] fields = obj.getClass().getFields();

                for(Map.Entry<String, String> entry : fieldsParam.entrySet()){
                    for(Field field : fields){
                        if(field.getName().equals(entry.getKey())){
                            String strResult = resultSet.getString(entry.getValue());
                            if(field.getType().toString().equals("int")){
                                field.set(obj, Integer.parseInt(strResult));
                            }else field.set(obj, strResult);
                        }
                    }
                }
                listObj.add((T)obj);
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return listObj;
    }

    @Override
    public <T> boolean add(String tableName, T objValue) {
        List<String> stringParamsList = new ArrayList<>();
        List<String> stringValuesList = new ArrayList<>();
        Field[] fields = objValue.getClass().getFields();
        for(Field field : fields){
            ColumnDescriptionORM annotationsField = field.getAnnotation(ColumnDescriptionORM.class);
            if(annotationsField != null){
                if(annotationsField.generatedId() != ORMGenerateId.AUTO_GENERATED){
                    try {
                        stringValuesList.add(field.get(objValue).toString());
                        stringParamsList.add(annotationsField.name());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        String paramsForQuery = String.join(",", stringParamsList);
        String valueForQuery = String.join("','", stringValuesList);
        String query = String.format(QUERY_INSERT_TABLE, tableName, paramsForQuery, valueForQuery);
        boolean result = true;

        try {
            Connection conn = DataSource.getInstance();
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }
}
