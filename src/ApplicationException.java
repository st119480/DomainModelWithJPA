import java.sql.SQLException;

public class ApplicationException extends Exception{
    String msg;

    public SQLException getE() {
        return e;
    }

    public void setE(SQLException e) {
        this.e = e;
    }

    private SQLException e;

    public ApplicationException(SQLException e) {
        this.e = e;
    }

    public ApplicationException(String str) {
        msg = str;
    }

    @Override
    public String toString() {
        e.printStackTrace();
      return super.toString();
    }
}
