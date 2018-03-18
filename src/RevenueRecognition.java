import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name="REVENUERECOGNITIONS")
public class RevenueRecognition {
    @Id
    @GeneratedValue
    private long id;
    private Money amount;
    @Embedded
    private MfDate recognizedOn;
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Contract contract;
    @Transient
    private static ArrayList<RevenueRecognition> data = new ArrayList<RevenueRecognition>();

    public RevenueRecognition() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RevenueRecognition(Contract contract, Money amount, MfDate date) {
        this.contract = contract;
        this.amount = amount;
        this.recognizedOn = date;
    }

    public Money getAmount() {
        return amount;
    }

    boolean isRecognizableBy(MfDate asOf) {
        return asOf.after(recognizedOn) || asOf.equals(recognizedOn);
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public MfDate getRecognizedOn() {
        return recognizedOn;
    }

    public void setRecognizedOn(MfDate recognizedOn) {
        this.recognizedOn = recognizedOn;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public static ArrayList<RevenueRecognition> getData() {
        return data;
    }

    public void insert() {
        data.add(this);
    }

    public void delete() throws ApplicationException {
        if (!data.remove(this))
            throw new ApplicationException("Delete Error: RevenueRecognition not found.");
    }

    public static RevenueRecognition find(Contract contract, MfDate recognizedOn) throws ApplicationException {
        for (RevenueRecognition revenueRecognition : data) {
            if (revenueRecognition.getContract().getId() == contract.getId() &&
                    recognizedOn.lessThanEqualTo(revenueRecognition.getRecognizedOn())) return revenueRecognition;
        }
        throw new ApplicationException("Find Error: Contract not found.");
    }

    public String toString() {
        return String.format("Contract ID: %d, Amount: %s, RecognizedOn: %s",
                getContract().getId(), getAmount().amount(), getRecognizedOn().getDate().getTime());
    }
}
