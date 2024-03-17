package ru.cherkashin.orm_framework;

import ru.cherkashin.orm_framework.entity.Task;
import ru.cherkashin.orm_framework.entity.User;
import ru.cherkashin.orm_framework.orm.DataRepository;
import ru.cherkashin.orm_framework.orm.DataSource;
import ru.cherkashin.orm_framework.orm.ORMService;

public class App 
{
    public static void main( String[] args )
    {

        ORMService ormService = new ORMService(new DataRepository());

        Task task = new Task();
        task.setName("DELETE any user");
        task.setStatus("Progress");
        task.setInfo("Delete users pleas");

        var author = ormService.getValueOnIndex(1, User.class);
        task.setAuthor(author);

        ormService.addNewValue(task);

        DataSource.closeConnection();

    }
}
