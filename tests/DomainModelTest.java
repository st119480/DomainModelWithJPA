import org.junit.*;

import javax.persistence.*;
import java.sql.*;
import java.util.Hashtable;

public class DomainModelTest {
    static Hashtable<String, Number> productDict = new Hashtable<String, Number>();
    @PersistenceContext
    static private EntityManager entityManager;
    static private PersistenceManager persistenceManager;
    static private EntityTransaction et;

    static private void populateProducts() {
        Product word = Product.newWordProcessor("Microsoft Word");
        et.begin();
        entityManager.persist(word);
        et.commit();
        long id = word.getId();
        productDict.put("Microsoft Word", id);

        Product excel = Product.newSpreadsheet("Microsoft Excel");
        et.begin();
        entityManager.persist(excel);
        et.commit();
        id = excel.getId();
        productDict.put("Microsoft Excel", id);

        Product sql = Product.newDatabase("Microsoft SQL");
        et.begin();
        entityManager.persist(sql);
        et.commit();
        id = sql.getId();
        productDict.put("Microsoft SQL", id);
    }

    @BeforeClass
    static public void setUp() {
        persistenceManager = new PersistenceManager();
        entityManager = persistenceManager.getEntityManager();
        et = entityManager.getTransaction();
        populateProducts();
    }

    @AfterClass
    static public void cleanUp() {
        for (String productName: productDict.keySet()) {
            Product p = entityManager.find(Product.class, productDict.get(productName));
            et.begin();
            entityManager.remove(p);
            et.commit();
        }

        persistenceManager.close();
    }

    /* Test for Microsoft Word */
    @Test
    public void test1() {
        Product word = entityManager.find(Product.class, productDict.get("Microsoft Word"));

        // Add a new contract for Microsoft Word with current date
        Contract contract = new Contract(word, Money.dollars(110.0), MfDate.getCurrentDate());
        word.calculateRevenueRecognitions(contract);
        et.begin();
        entityManager.persist(contract);
        et.commit();

        Money money = contract.recognizedRevenue(MfDate.getCurrentDate());
        Assert.assertEquals(110.0, money.amount().longValue(), 0.0);

        // Revenue generation 40 days ago
        money = contract.recognizedRevenue(MfDate.getCurrentDate().addDays(-40));
        // Contract 1 not written
        Assert.assertEquals(0.0, money.amount().longValue(), 0);

        et.begin();
        entityManager.remove(contract);
        et.commit();
    }

    /* Test for Microsoft Excel */
    @Test
    public void test2() {
            Product excel = entityManager.find(Product.class, productDict.get("Microsoft Excel"));
            // Add a new contract for Microsoft Excel, -61 days before today
            Contract contract = new Contract(excel, Money.dollars(600.0), MfDate.getCurrentDate().addDays(-61));
            excel.calculateRevenueRecognitions(contract);
            et.begin();
            entityManager.persist(contract);
            et.commit();

            // Revenue generation today
            Money money = contract.recognizedRevenue(MfDate.getCurrentDate());
            // Contract 2/3  written
            Assert.assertEquals(400.0, money.amount().longValue(), 0);

            // Revenue generation 40 days ago
            money = contract.recognizedRevenue(MfDate.getCurrentDate().addDays(-40));
            // Contract 2 1/3 written out of 600
            Assert.assertEquals(200.0, money.amount().longValue(), 0);

            et.begin();
            entityManager.remove(contract);
            et.commit();
    }

    /* Test for Microsoft SQL */
    @Test
    public void test3() {
        Product sql = entityManager.find(Product.class, productDict.get("Microsoft SQL"));
        // Add a new contract for Microsoft SQL, -61 days before today
        Contract contract = new Contract(sql, Money.dollars(900.0), MfDate.getCurrentDate().addDays(-61));
        sql.calculateRevenueRecognitions(contract);
        et.begin();
        entityManager.persist(contract);
        et.commit();

        // Revenue generation today
        Money money = contract.recognizedRevenue(MfDate.getCurrentDate());
        // Contract fully  written
        Assert.assertEquals(900.0, money.amount().longValue(), 0);

        // Revenue generation 40 days ago
        money = contract.recognizedRevenue(MfDate.getCurrentDate().addDays(-40));
        // Contract 3 1/3 written out of 900
        Assert.assertEquals(300.0, money.amount().longValue(), 0);

        et.begin();
        entityManager.remove(contract);
        et.commit();
    }

}