package app.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Класс представляет собой пользователя
 */
public class User implements Serializable{

    // ид пользователя
    private final int id;
    // права пользователя
    private Role role;
    // активация учетной записи
    private final boolean confirmEmail;

    // имя пользователя
    private final String name;
    // фамилия
    private final String surname;
    // отчество
    private String patronymic;
    // номер телефона
    private final String telephone;
    // электронная почта
    private final String email;
    // день рождения
    private Date birthday;
    // серия паспорта
    private int passportSeries;
    // номер паспорта
    private int passportID;
    // дата выдачи паспорта
    private Date dateOfIssueOfPassport;
    // адрес регистрации
    private String registrationAddressOfPassport;

    public User(boolean confirmEmail, String name, String surname, String telephone, String email, int id) {
        this.confirmEmail = confirmEmail;
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        this.id = id;
    }

    public User(int id, Role role, boolean confirmEmail, String name, String surname, String patronymic, String telephone, String email, Date birthday, int passportSeries, int passportID, Date dateOfIssueOfPassport, String registrationAddressOfPassport) {
        this.id = id;
        this.role = role;
        this.confirmEmail = confirmEmail;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.telephone = telephone;
        this.email = email;
        this.birthday = birthday;
        this.passportSeries = passportSeries;
        this.passportID = passportID;
        this.dateOfIssueOfPassport = dateOfIssueOfPassport;
        this.registrationAddressOfPassport = registrationAddressOfPassport;
    }

    public Role getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public boolean isConfirmEmail() {
        return confirmEmail;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getPassportSeries() {
        return passportSeries;
    }

    public int getPassportID() {
        return passportID;
    }

    public Date getDateOfIssueOfPassport() {
        return dateOfIssueOfPassport;
    }

    public String getRegistrationAddressOfPassport() {
        return registrationAddressOfPassport;
    }

    public void setRole(String role) {
        if (role != null) {
            if (this.role == null) {
                switch (role) {
                    case "ADMIN":
                        this.role = Role.ADMIN;
                        break;
                    case "SUPERUSER":
                        this.role = Role.SUPERUSER;
                        break;
                    case "USER":
                        this.role = Role.USER;
                        break;
                }
            }
        }
    }
}