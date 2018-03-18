import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Embeddable
public class MfDate {
    @Temporal(TemporalType.TIMESTAMP)
    private GregorianCalendar date;

    public MfDate() {
        this.date = new GregorianCalendar();
    }

    public MfDate(Date date) {
        this.date = new GregorianCalendar();
        this.date.setTime(date);
    }

    public MfDate(GregorianCalendar date) {
        this.date = date;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public Date toSqlDate() {
        return new Date(date.getTime().getTime());
    }

    public MfDate addDays(int days) {
        GregorianCalendar newDate = (GregorianCalendar) date.clone();
        newDate.add(Calendar.DAY_OF_MONTH, days);
        return new MfDate(newDate);
    }

    public boolean after(MfDate date) {
        return this.date.after(date.getDate());
    }

    static public MfDate getCurrentDate() {
        return new MfDate((GregorianCalendar) GregorianCalendar.getInstance());
    }

    public int compareTo(MfDate other) {
        return getDate().compareTo(other.getDate());
    }

    public boolean lessThanEqualTo(MfDate other) {
        return (compareTo(other) > 0);
    }


}
