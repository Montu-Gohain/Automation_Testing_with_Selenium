public class test {
    public static void main(String[] args) {
        String phoneNumber = "769835674895";
        String formattedPhoneNumber = formatPhoneNumber(phoneNumber);
        System.out.println(formattedPhoneNumber);
    }
    public static String formatPhoneNumber(String phoneNumber) {
        String areaCode = phoneNumber.substring(0, 3);
        String middleDigits = phoneNumber.substring(3, 6);
        String lastDigits = phoneNumber.substring(6);
        return String.format("(%s) %s-%s", areaCode, middleDigits, lastDigits);
    }
}
