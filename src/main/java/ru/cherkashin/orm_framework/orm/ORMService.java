package ru.cherkashin.orm_framework.orm;

import ru.cherkashin.orm_framework.orm.interfaceORM.AnnotationORM;
import ru.cherkashin.orm_framework.orm.interfaceORM.ColumnDescriptionORM;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ORMService {

    public <T> T getValueOnIndex(int index, Class<T> classObject){
        String tableName = getTableName(classObject);
        Map<String, String> fieldsParams = getStructColumnOnFields(classObject);

        DataRepository dataRepository = new DataRepository();
        Object obj = dataRepository.readObjectAsOrm(tableName, index, fieldsParams, classObject);

        return (T) obj;
    }

    public <T> List<?> getListValue(Class<T> classObject){
        String tableName = getTableName(classObject);
        Map<String, String> fieldsParams = getStructColumnOnFields(classObject);

        DataRepository dataRepository = new DataRepository();
        List<?> ListObj = dataRepository.readAllTable(tableName, fieldsParams, classObject);

        return ListObj;
    }

    public <T> boolean addNewValue(T objValue, Class<T> classObject){
        String tableName = getTableName(classObject);
        DataRepository dataRepository = new DataRepository();

        return dataRepository.addNewValue(tableName, objValue);
    }

    private <T> String getTableName(Class<T> classObject){
        Annotation[] annotations = classObject.getAnnotations();
        String tableName = null;
        for(Annotation annotation : annotations){
            if(annotation instanceof AnnotationORM){
                tableName = ((AnnotationORM) annotation).nameTable();
            }
        }
        return tableName;
    }

    private <T> HashMap<String, String> getStructColumnOnFields(Class<T> classObject){
        Field[] fields = classObject.getFields();
        HashMap<String, String> fieldsParams = new HashMap<>();
        for(Field field : fields){
            Annotation[] fieldAnnotations = field.getAnnotations();
            for(Annotation annotation : fieldAnnotations){
                if(annotation instanceof ColumnDescriptionORM){
                    fieldsParams.put(field.getName(), ((ColumnDescriptionORM) annotation).name());
                }
            }
        }
        return fieldsParams;
    }

}
