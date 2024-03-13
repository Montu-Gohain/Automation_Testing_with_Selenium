import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) {
        String date_range_text = "18 of 44";

        String[] words = date_range_text.split(" ");
        String total_count = words[words.length - 1];
        System.out.println(total_count);
    }
    public static String formatPhoneNumber(String phoneNumber) {
        String areaCode = phoneNumber.substring(0, 3);
        String middleDigits = phoneNumber.substring(3, 6);
        String lastDigits = phoneNumber.substring(6);
        return String.format("(%s) %s-%s", areaCode, middleDigits, lastDigits);
    }
}
