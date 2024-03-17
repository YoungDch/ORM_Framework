package ru.cherkashin.orm_framework.entity;

import ru.cherkashin.orm_framework.orm.enumORM.ORMGenerateId;
import ru.cherkashin.orm_framework.orm.interfaceORM.AnnotationORM;
import ru.cherkashin.orm_framework.orm.interfaceORM.ORMObject;
import ru.cherkashin.orm_framework.orm.interfaceORM.ColumnDescriptionORM;

import java.util.Objects;

@AnnotationORM(nameTable = "users")
public class User implements ORMObject {

    @ColumnDescriptionORM(name = "id", generatedId = ORMGenerateId.AUTO_GENERATED)
    private int id;

    @ColumnDescriptionORM(name = "login")
    private String login;

    @ColumnDescriptionORM(name = "email")
    private String email;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
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
