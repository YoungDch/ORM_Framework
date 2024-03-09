package ru.cherkashin.orm_framework;

import ru.cherkashin.orm_framework.entity.Task;
import ru.cherkashin.orm_framework.orm.DataRepository;
import ru.cherkashin.orm_framework.orm.ORMService;

public class App 
{
    public static void main( String[] args )
    {
        ORMService ormService = new ORMService(new DataRepository());
        var list = ormService.getListValue(Task.class);
        System.out.println(list);

        Task task = new Task();
        task.setName("Create new framework");
        task.setStatus("Development");
        task.setInfo("New developer should make new service");

        boolean result = ormService.addNewValue(task);
        if(result){
            System.out.println("Object " + task.toString() + " adding");
        }
    }
}
