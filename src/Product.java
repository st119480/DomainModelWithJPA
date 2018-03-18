import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name="PRODUCTS")
public class Product implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    @Transient
    private RecognitionStrategy recognitionStrategy;
    @Transient
    static private ArrayList<Product> data = new ArrayList<Product>();
    @Transient
    static private long index = 0;

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Product(String name, RecognitionStrategy recognitionStrategy) {
        this.name = name;
        this.recognitionStrategy = recognitionStrategy;
    }
    public static Product newWordProcessor(String name) {
        return new Product(name, new CompleteRecognitionStrategy());
    }

    public static Product newSpreadsheet(String name) {
        return new Product(name, new ThreeWayRecognitionStrategy(60, 90));
    }
    public static Product newDatabase(String name) {
        return new Product(name, new ThreeWayRecognitionStrategy(30, 60));
    }

    /*
    public long insert() {
        this.setId(index++);
        data.add(this);
        return this.getId();
    }

    public static Product find(long id) throws ApplicationException {
        for(Product product: data) {
            if (product.getId() == id) return product;
        }
        throw new ApplicationException("Find error: Product not found");
    }

    public void delete() throws ApplicationException{
        if(!data.remove(this))
            throw new ApplicationException("Delete Error: Product not found.");
    }
    */

    void calculateRevenueRecognitions(Contract contract) {
        recognitionStrategy.calculateRevenueRecognitions(contract);
    }
}
