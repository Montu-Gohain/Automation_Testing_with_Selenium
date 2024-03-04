import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
public class test {
    public static void main(String[] args) {

    String comparison_string = "Targeted date : %s & Scheduled date : %s";
        String AP_Date = "6th March 2024";
        String Final_Date = "06th March 2024";


    String date_matcher = String.format(comparison_string, AP_Date, Final_Date);

        System.out.println(date_matcher);

    }
}
