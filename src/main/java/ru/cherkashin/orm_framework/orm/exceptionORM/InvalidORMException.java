package ru.cherkashin.orm_framework.orm.exceptionORM;

public class InvalidORMException extends RuntimeException{
    public InvalidORMException(){
        super("Имя таблицы не задано, либо имеет некорректный вид.");
    }
}
