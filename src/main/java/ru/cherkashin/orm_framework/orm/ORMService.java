package ru.cherkashin.orm_framework.orm;

import ru.cherkashin.orm_framework.orm.exceptionORM.InvalidORMException;
import ru.cherkashin.orm_framework.orm.interfaceORM.AnnotationORM;
import ru.cherkashin.orm_framework.orm.interfaceORM.ColumnDescriptionORM;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ORMService {

    private final DataRepository dataRepository;

    public ORMService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public <T> T getValueOnIndex(int index, Class<T> classObject){
        String tableName = getTableName(classObject);
        Map<String, String> fieldsParams = getStructColumnOnFields(classObject);

        return (T) dataRepository.read(tableName, index, fieldsParams, classObject);
    }

    public <T> List<?> getListValue(Class<T> classObject){
        String tableName = getTableName(classObject);
        Map<String, String> fieldsParams = getStructColumnOnFields(classObject);

        return dataRepository.readTable(tableName, fieldsParams, classObject);
    }

    public <T> boolean addNewValue(T objValue){
        String tableName = getTableName(objValue.getClass());

        return dataRepository.add(tableName, objValue);
    }

    private <T> String getTableName(Class<T> classObject){
        AnnotationORM annotation = classObject.getAnnotation(AnnotationORM.class);
        String tableName = null;
        if(annotation != null) tableName = annotation.nameTable();
        if(tableName == null){
            throw new InvalidORMException();
        }
        return tableName;
    }

    private <T> HashMap<String, String> getStructColumnOnFields(Class<T> classObject){
        Field[] fields = classObject.getDeclaredFields();
        HashMap<String, String> fieldsParams = new HashMap<>();
        for(Field field : fields){
            ColumnDescriptionORM fieldAnnotation = field.getAnnotation(ColumnDescriptionORM.class);
            if(fieldAnnotation != null) fieldsParams.put(field.getName(), fieldAnnotation.name());
        }
        return fieldsParams;
    }
}
