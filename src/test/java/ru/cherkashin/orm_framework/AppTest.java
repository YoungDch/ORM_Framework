package ru.cherkashin.orm_framework;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.cherkashin.orm_framework.entity.Task;
import ru.cherkashin.orm_framework.entity.User;
import ru.cherkashin.orm_framework.orm.DataRepository;
import ru.cherkashin.orm_framework.orm.ORMService;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{

    public AppTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testApp()
    {
        assertTrue( true );
    }

    public void testORMFramework_addValue(){

        User user = new User();
        user.setLogin("admin");
        user.setEmail("admin@gmail.com");

        Task task = new Task();
        task.setName("Creating test for app");
        task.setStatus("Testing");
        task.setInfo("Testing my app, pls");
        task.setAuthor(user);

        ORMService ormService = new ORMService(new DataRepository());
        assertTrue(ormService.addNewValue(task));


    }


}
