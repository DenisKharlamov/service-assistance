package app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Класс позволяет определить корректность данных, введённых пользователем
 */
public class Validator {

    private final EmailValidator validatorEmail = EmailValidator.getInstance();
    private final String NamePattern = "[а-яА-ЯёЁ]{3,25}$";
    private final String TelephonePattern = "^(\\+)?([-()]?\\d[-()]?){11}$";
    private final String plateNumberPattern = "^[а-яё]{1}[0-9]{3}[а-яё]{2}[0-9]{2,3}$";

    private final Pattern patternName = Pattern.compile(NamePattern);
    private final Pattern patternTelephone = Pattern.compile(TelephonePattern);
    private final Pattern patternPlateNumber = Pattern.compile(plateNumberPattern);

    public boolean isValidName(String name) {
        Matcher matcher = patternName.matcher(name);
        return matcher.matches();
    }

    public boolean isValidSurname(String surname) {
        Matcher matcher = patternName.matcher(surname);
        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        return validatorEmail.isValid(email);
    }

    public boolean isValidTelephone(String telephone) {
        Matcher matcher = patternTelephone.matcher(telephone);
        return matcher.matches();
    }

    public boolean isValidPlateNumber(String plateNumber) {
        Matcher matcher = patternPlateNumber.matcher(plateNumber);
        return matcher.matches();
    }
}
