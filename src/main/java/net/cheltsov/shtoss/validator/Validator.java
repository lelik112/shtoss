package net.cheltsov.shtoss.validator;

public class Validator {
    private static final String REGEX_EMAIL = "(^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$)";
    private static final String REGEX_LOGIN = "(\\w|\\d){1,20}";
    private static final String REGEX_PASSWORD = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}";
    private static final String REGEX_AMOUNT = "^[1-9][\\d]{0,5}[\\.,]?[\\d]{0,2}$";
    private static final String REGEX_LANGUAGE = "(?i)(en)|(ru)";

    public static boolean validateRegisterData (String login, String password, String email) {
        return login != null && login.matches(REGEX_LOGIN) && 
                password != null && password.matches(REGEX_PASSWORD) && 
                email != null && email.matches(REGEX_EMAIL);
    }
    public static boolean validatePayment (String amount) {
        return amount != null && amount.matches(REGEX_AMOUNT);
    }

    public static boolean validateLanguage (String language) {
        return language != null && language.matches(REGEX_LANGUAGE);
    }

    public static boolean validatePassword (String password) {
        return password != null && password.matches(REGEX_PASSWORD);
    }
}
