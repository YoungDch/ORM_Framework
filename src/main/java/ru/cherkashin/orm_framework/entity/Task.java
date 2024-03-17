package ru.cherkashin.orm_framework.entity;

import ru.cherkashin.orm_framework.orm.enumORM.ORMGenerateId;
import ru.cherkashin.orm_framework.orm.interfaceORM.AnnotationORM;
import ru.cherkashin.orm_framework.orm.interfaceORM.ORMObject;
import ru.cherkashin.orm_framework.orm.interfaceORM.ColumnDescriptionORM;

@AnnotationORM(nameTable = "tasks")
public class Task implements ORMObject {
    @ColumnDescriptionORM(name = "id", generatedId = ORMGenerateId.AUTO_GENERATED)
    private int id;

    @ColumnDescriptionORM(name = "name")
    private String name;

    @ColumnDescriptionORM(name = "status")
    private String status;

    @ColumnDescriptionORM(name = "info")
    private String info;

    @ColumnDescriptionORM(name = "authorid", thisORMObject = true)
    private User author;

    public Task(){
        this.author = new User();
    }

    public User getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", info='" + info + '\'' +
                ", author=" + author.toString() +
                '}';
    }

    @Override
    public int getIdForORM() {
        return id;
    }

    @Override
    public void setIdForORM(int id) {
        this.id = id;
    }
}
