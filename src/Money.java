import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

@Embeddable
public class Money {
    private long amount;
    private Currency currency;

    /* Default constructor with amount = 0, and currency = system currency */
    public Money() {
        this.amount = 0;
        this.currency = Currency.getInstance(Locale.getDefault());
    }

    /* Constructor with amount as double */
    public Money(double amount, Currency currency) {
        this.currency = currency;
        this.amount = Math.round(amount * centFactor());
    }

    /* Constructor with amount as long */
    public Money(long amount, Currency currency) {
        this.currency = currency;
        this.amount = amount * centFactor();
    }

    public Money(BigDecimal amount, Currency currency, int roundingMode) {
        this.currency = currency;
        this.amount = amount.longValue();
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal amount() {
        return BigDecimal.valueOf(amount, currency.getDefaultFractionDigits());
    }

    public static Money dollars(double amount) {
        return new Money(amount, Currency.getInstance(Locale.US));
    }

    public static Money dollars(BigDecimal amount) {
        return new Money(amount.longValue(), Currency.getInstance(Locale.US));
    }

    public boolean equals(Object other) {
        return (other instanceof Money) && equals((Money) other);
    }

    public boolean equals(Money other) {
        return currency.equals(other.currency) && (amount == other.amount);
    }

    public int hashCode() {
        return (int) (amount ^ (amount >>> 32));
    }


    private void assertSameCurrencyAs(Money arg) {
        assert currency.equals(arg.currency) : "Money math mismatch";
    }

    private Money newMoney(long amount) {
        Money money = new Money();
        money.currency = Currency.getInstance(this.currency.getCurrencyCode());
        money.amount = amount;
        return money;
    }

    public Money add(Money other) {
        assertSameCurrencyAs(other);
        return newMoney(amount + other.amount);
    }

    public Money subtract(Money other) {
        assertSameCurrencyAs(other);
        return newMoney(amount - other.amount);
    }

    public int compareTo(Object other) {
        return compareTo((Money) other);
    }

    public int compareTo(Money other) {
        assertSameCurrencyAs(other);
        if (amount < other.amount) return -1;
        else if (amount == other.amount) return 0;
        else return 1;
    }

    public boolean greaterThan(Money other) {
        return (compareTo(other) > 0);
    }

    public Money multiply(double amount) {
        return multiply(new BigDecimal(amount));
    }

    public Money multiply(BigDecimal amount) {
        return multiply(amount, BigDecimal.ROUND_HALF_EVEN);
    }

    public Money multiply(BigDecimal amount, int roundingMode) {
        return new Money(amount().multiply(amount), currency, roundingMode);
    }

    public Money[] allocate(int n) {
        Money lowResult = newMoney(amount / n);
        Money highResult = newMoney(lowResult.amount + 1);
        Money[] results = new Money[n];
        int remainder = (int) amount % n;
        for (int i = 0; i < remainder; i++) results[i] = highResult;
        for (int i = remainder; i < n; i++) results[i] = lowResult;
        return results;
    }

    /* Private methods */

    private static final int[] cents = new int[]{1, 10, 100, 1000};

    private int centFactor() {
        return cents[currency.getDefaultFractionDigits()];
    }
}
