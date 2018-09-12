package Tests;

import Entities.Clients.NewClient;
import Entities.Product;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.*;

public class AnnotationTests {

    @BeforeClass
    public static void initTestSessions(){System.out.println("Starting Test Sessions:");}

    @Before
    public void beforeTest(){ System.out.println("Starting test:"); }

    @After
    public void afterTest(){ System.out.println("Test Ended..."); }

    @AfterClass
    public static void endTestSessions(){System.out.println("Test Sessions Ended...");}

    @Test(expected = Exception.class)
    public void productTest1() throws Exception {
        System.out.println("Product assertion test negative price value.");
        new Product("Test", -10,10);
    }

    @Test(expected = Exception.class)
    public void productTest2() throws Exception {

        System.out.println("Product assertion test negative amount value.");
        new Product("Test", 100,-20);
    }

    @Test
    public void productTest3() throws Exception {

        System.out.println("Product assertion test different name values.");

        Product p1 = new Product("Test1", 10,1);
        Product p2 = new Product("Test2", 10, 1);

        assertThat(p1,not(p2)); //not equals.
    }

    @Test
    public void productTest4() throws Exception {

        System.out.println("Product assertion test same name different price and amount values.");

        Product p1 = new Product("Test1", 10,1);
        Product p2 = new Product("Test1", 100, 10);

        assertEquals(p1,p2); //same values.
        assertNotSame(p1,p2); //different references.
    }

    @Test
    public void productTest5() throws Exception {

        System.out.println("Product assertion test 2 objects same reference.");

        Product p1 = new Product("Test1", 10,1);
        Product p2 = p1;

        assertSame(p1,p2); //same references.
    }

    @Test
    public void clientTest1(){

        System.out.println("Client assertion test 2 objects same id.");

        NewClient c1 = new NewClient(123456789,"Client1 Client1","012-3456789",1);
        NewClient c2 = new NewClient(123456789,"Client2 Client2","052-1234567",2);

        assertEquals(c1,c2);
    }
}
