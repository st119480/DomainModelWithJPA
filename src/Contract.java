import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="CONTRACTS")
public class Contract {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "contract")
    private List<RevenueRecognition> revenueRecognitions = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Product product;

    @Embedded
    private Money revenue;

    @Embedded
    private MfDate dateSigned;

    static private ArrayList<Contract> data = new ArrayList<Contract>();
    static private long index = 0;

    public Contract() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Money getRevenue() {
        return revenue;
    }

    public void setRevenue(Money revenue) {
        this.revenue = revenue;
    }

    public MfDate getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(MfDate dateSigned) {
        this.dateSigned = dateSigned;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Contract(Product product, Money revenue, MfDate dateSigned) {
        this.product = product;
        this.revenue = revenue;
        this.dateSigned = dateSigned;
    }

    public Money recognizedRevenue(MfDate asOf) {
        Money result = Money.dollars(0);
        Iterator it = revenueRecognitions.iterator();
        while (it.hasNext()) {
            RevenueRecognition r = (RevenueRecognition) it.next();
            if (r.isRecognizableBy(asOf))
                result = result.add(r.getAmount());
        }
        return result;
    }

    public void addRevenueRecognition(RevenueRecognition revenueRecognition) {
        revenueRecognition.insert();
        revenueRecognitions.add(revenueRecognition);
    }

    public void calculateRecognitions() {
        product.calculateRevenueRecognitions(this);
    }

    public long insert() {
        this.setId(index++);
        data.add(this);
        return this.getId();
    }

    public static Contract find(long id) throws ApplicationException {
        for(Contract contract: data) {
            if (contract.getId() == id) return contract;
        }
        throw new ApplicationException("Find Error: Contract not found.");
    }

    public void delete() throws ApplicationException {
        if (!data.remove(this))
            throw new ApplicationException("Delete Error: Contract not found.");
    }
}
