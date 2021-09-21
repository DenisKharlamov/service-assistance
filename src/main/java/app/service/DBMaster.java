package app.service;

import app.model.CollectionTemplateAuto;
import app.model.Employee;
import app.model.EmployeeMontlyWorkSchedule;
import app.model.Engine;
import app.model.Event;
import app.model.LaborOfOrder;
import app.model.Mechanic;
import app.model.Order;
import app.model.Orders;
import app.model.PartOfOrder;
import app.model.Staff;
import app.model.TemplateAuto;
import app.model.TimeMatrix;
import app.model.Transmission;
import app.model.User;
import app.model.UserAuto;
import app.model.UserGarage;
import app.util.PswdCrypt;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 * Класс для выполнения запросов и создания соединения с базой данных
 */
@Stateless
public class DBMaster {

    // Инициализация логера
    private static final Logger logger = Logger.getLogger(DBMaster.class);

    //соединение с базой данных
    private Connection connection;

    // подготовленный запрос
    private PreparedStatement ps;

    // шаблоны даты и времени
    DateTimeFormatter today = DateTimeFormatter.ofPattern("dd MMMM yyyy EEEE");
    DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
    DateTimeFormatter month = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH);
    DateTimeFormatter day = DateTimeFormatter.ofPattern("dd");

    // получение соединения с базой данных из пула соединений
    @Resource(lookup = "jdbc/hsqldb_pool")
    private DataSource dataSource;

    @Inject
    PswdCrypt pswdCrypt;

    @PostConstruct
    public void initialize() {
        try {
            connection = dataSource.getConnection();
            logger.info("Connection ok...");
        } catch (SQLException ex) {
            logger.error("Could not establish a connection to the database = {}", ex);
        }
    }

    /**
     * Метод создает новую учетную запись пользователя
     *
     * @param name
     * @param surname
     * @param telephone
     * @param email
     * @param password
     * @return
     */
    public boolean addContragentAccount(String name, String surname, String telephone,
            String email, String password) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            logger.info("add_account");
            // создаём учётную запись для нового клиента
            ps = connection.prepareStatement(
                    "INSERT INTO user_accounts (telephone, email, password, role_id) VALUES (?, ?, ?, ?)",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // добавляем параметры запроса
            ps.setString(1, telephone);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setInt(4, 3);
            int n = ps.executeUpdate();
            ps.close();
            // при успешном создании учетной записи, 
            // добавляем нового клиента в базу
            if (n > 0) {
                logger.info("add_contragent");
                ps = connection.prepareStatement(
                        "INSERT INTO contragent (name, surname, account_id) VALUES "
                        + "(?, ?, (SELECT id FROM user_accounts WHERE email = ?))",
                        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, name);
                ps.setString(2, surname);
                ps.setString(3, email);
                n = ps.executeUpdate();
                ps.close();
                return (n > 0);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
            return false;
        }
    }

    /**
     * Метод проверяет наличие в базе учетной записи с определённым email
     *
     * @param email электронная почта пользователя
     * @return 1 - запись отсутствует, 2 - запись присутствует, 3 - ошибка
     * доступа к БД
     */
    public int containsEmail(String email) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("SELECT * FROM user_accounts WHERE email = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                // после выполнения запроса проверяем наличие результатов
                // в ResultSet
                if (numberOfRecordsRS(rs) > 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
            return 3;
        }
    }

    /**
     * Метод проверяет наличие в базе учетной записи с определённым номером
     * телефона
     *
     * @param telephone номер телефона
     * @return 1 - запись отсутствует, 2 - запись присутствует, 3 - ошибка
     * доступа к БД
     */
    public int containsTelephone(String telephone) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("SELECT * FROM user_accounts WHERE telephone = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, telephone);
            try (ResultSet rs = ps.executeQuery()) {
                if (numberOfRecordsRS(rs) > 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
            return 3;
        }
    }

    /**
     * Метод определяет количество записей в ResultSet
     *
     * @param resultSet
     * @return
     */
    public int numberOfRecordsRS(ResultSet resultSet) {
        if (resultSet == null) {
            return 0;
        }
        int count = 0;
        try {
            // переставляем положение курсора на последнюю строку
            resultSet.last();
            // метод возвращающий номер строки курсора
            count = resultSet.getRow();
            // возвращаем курсор ResultSet на начало
            resultSet.beforeFirst();
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return count;
    }

    /**
     * Метод выполняет аутентификацию пользователя
     *
     * @param email
     * @param password
     * @return
     */
    public boolean login(String email, String password) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("SELECT password FROM user_accounts WHERE email = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // сравниваем пароль с хеш-кодом хранящимся в базе данных
                    return pswdCrypt.pswdVerified(password, rs.getString("password"));
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return false;
    }

    /**
     * Метод проверяет активацию аккаунта пользователя
     *
     * @param email
     * @return true - если аккаунт был подтверждён, false - если нет
     */
    public boolean accountConfirmed(String email) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("SELECT confirm_email FROM user_accounts WHERE email = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("confirm_email");
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return false;
    }

    /**
     * Добавляет идентификатор текущей сессии для пользователя
     * (пока не используется)
     * @param email
     * @param sessionID
     */
    public void addSessionID(String email, String sessionID) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("UPDATE user_accounts SET session_id = ? WHERE email = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, sessionID);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
    }

    /**
     * Метод для определения прав пользователя
     *
     * @param email
     * @return строка с указанием прав пользователя
     */
    public String userRights(String email) {
        try (ResultSet rs = connection.createStatement().executeQuery(
                "SELECT name FROM roles WHERE id = ("
                + "SELECT role_id FROM user_accounts WHERE email = "
                + "'" + email + "');")) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return null;
    }

    /**
     * не используется
     * @param email
     * @return 
     */
    public String fullNameUser(String email) {
        try (ResultSet rs = connection.createStatement().executeQuery(
                "SELECT name, surname FROM contragent WHERE account_id = ("
                + "SELECT id FROM user_accounts WHERE email = "
                + "'" + email + "');")) {
            if (rs.next()) {
                return rs.getString(1) + " " + rs.getString(2);
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return null;
    }

    /**
     * Метод для определения id учетной записи по email
     *
     * @param email
     * @return id пользователя если пользователь с таким email присутствует в
     * базе и -1 если пользователь с заданным email не найден
     */
    public int getUserIdFromEmail(String email) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("SELECT id FROM user_accounts WHERE email = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return -1;
    }

    /**
     * Метод для подтверждения email пользователя, сравнивает hash полученный от
     * пользователя с email пользователя. Hash отправленный пользователю
     * изначально получен из его email.
     *
     * @param userId
     * @param hash
     * @return true - hash совпал, false - не совпал
     */
    public boolean confirmEmail(int userId, String hash) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("SELECT email FROM user_accounts WHERE id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (pswdCrypt.pswdVerified(rs.getString("email"), hash)) {
                        return (connection.createStatement().executeUpdate(
                                "UPDATE user_accounts SET confirm_email = TRUE WHERE id = " + userId) > 0);
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return false;
    }

    /**
     * Метод создаёт объект класса представляющий клиента в приложении
     * @param email
     * @return объект класса User
     */
    public User getInstanceUser(String email) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement(
                    "SELECT r.name, confirm_email, c.name, surname, telephone, email, c.id "
                    + "FROM contragent c "
                    + "JOIN user_accounts u ON (c.account_id = u.id) "
                    + "JOIN roles r ON (u.role_id = r.id) WHERE email = ?;");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getBoolean(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7));
                    user.setRole(rs.getString(1));
                    return user;
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return null;
    }

    /**
     * Метод делает выборку данных по персоналу автосервиса
     *
     * @return объект класса Staff, который содержит весь персонал автосервиса
     */
    public Staff getStaff() {
        Staff staff = null;
        Employee employee = null;
        EmployeeMontlyWorkSchedule workSchedule = null;
        try (
                Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs1 = st.executeQuery("SELECT u.confirm_email, s.name, s.surname, "
                        + "u.telephone, u.email, s.id FROM staff_master s JOIN user_accounts u ON (s.account_id = u.id);");
                ResultSet rs2 = st.executeQuery("SELECT id, patron_id, name, surname FROM staff_mechanic;");
                ResultSet rs3 = st.executeQuery("SELECT s.staff_id, s.year_, m.name, s.working_days, "
                        + "s.working_times FROM staff_work_schedule s JOIN months m ON (s.month_id = m.id);");) {
            while (rs1.next()) {
                // создаём мастера
                employee = new Employee(rs1.getBoolean(1), rs1.getString(2), rs1.getString(3),
                        rs1.getString(4), rs1.getString(5), rs1.getInt(6));
                employee.setRole("SUPERUSER");
                if (!rs2.isFirst()) {
                    rs2.beforeFirst();
                }
                while (rs2.next()) {
                    if (employee.getId() == rs2.getInt(2)) {
                        // создаём механика
                        Mechanic mechanic = new Mechanic(rs2.getInt(1), rs2.getInt(2), rs2.getString(3), rs2.getString(4));
                        // добавляем мастеру механика
                        employee.addMechanic(mechanic);

                        if (!rs3.isFirst()) {
                            rs3.beforeFirst();
                        }
                        while (rs3.next()) {
                            if (mechanic.getId() == rs3.getInt(1)) {
                                Object[] wDays = (Object[]) rs3.getArray(4).getArray();
                                Object[] wTimes = (Object[]) rs3.getArray(5).getArray();
                                // создаём график работы механика
                                workSchedule = new EmployeeMontlyWorkSchedule(rs3.getInt(1),
                                        rs3.getInt(2), rs3.getString(3),
                                        objConverter(wDays),
                                        objConverter(wTimes));
                                // добавляем механику его график работы
                                mechanic.addMontlyWorkSchedule(workSchedule);
                            }
                        }
                    }
                }
                if (staff == null) {
                    staff = new Staff();
                    // добавляем мастера к персоналу
                    staff.addEmployee(employee);
                } else {
                    staff.addEmployee(employee);
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return staff;
    }

    /**
     * Метод создает коллекцию содержащую все автомобили которые могут
     * обслуживаться на автосервие
     *
     * @return коллекцию шаблонных авто
     */
    public CollectionTemplateAuto getCollection() {
        CollectionTemplateAuto collection = null;
        TemplateAuto auto = null;
        Engine engine = null;
        Transmission transmission = null;
        int value = 0;
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            // делаем выборку автомобилей с разными конфигурациями
            ps = connection.prepareStatement(
                    "SELECT t.id, brand, model, e.letter_designation letter_designation_engine, displacement, "
                    + "injection_type, power_kwt, power_hp, fuel_type, tr.letter_designation, gear_number, "
                    + "type_tm, cost_standard_hour "
                    + "FROM template_auto t JOIN engine e ON (t.eng_id = e.id) "
                    + "JOIN transmission tr ON (t.trans_id = tr.id) "
                    + "JOIN model_auto m ON (t.model_id = m.id) "
                    + "JOIN brand_auto b ON (m.brand_id = b.id);", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            try (ResultSet rs = ps.executeQuery()) {
                collection = new CollectionTemplateAuto();
                while (rs.next()) {
                    // создаём двигатель
                    engine = new Engine(rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getInt(7), rs.getInt(8));
                    engine.setEngineType(rs.getString(9));
                    // создаём коробку передач
                    transmission = new Transmission(rs.getString(10), rs.getInt(11), rs.getString(12));
                    // создаём автомобиль
                    auto = new TemplateAuto(rs.getInt(1), rs.getString(2), rs.getString(3), engine, transmission, rs.getDouble(13));
                    // добавляем авто в коллекцию
                    collection.addTemplateAuto(auto);
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return collection;
    }

    /**
     * Метод для добавления клиентского авто в базу
     *
     * @param userId идентификатор клиента
     * @param autoId идентификатор шаблонного авто
     * @param plateNumber гос номер автомобиля клиента
     * @param vin вин номер автомобиля клиента
     * @return true - в случае успешного добавления авто в базу, false - в
     * противном случае
     */
    public boolean addClientAuto(int userId, int autoId, String plateNumber, String vin) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement(
                    "INSERT INTO contragent_auto(contragent_id, template_auto_id, plate_number, vin) "
                    + "VALUES (?, ?, ?, ?);");
            ps.setInt(1, userId);
            ps.setInt(2, autoId);
            ps.setString(3, plateNumber);
            ps.setString(4, vin);
            int n = ps.executeUpdate();
            return n > 0;
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return false;
    }

    /**
     * Создание гаража автомобилей пользователя
     *
     * @param userId ид клиента
     * @return
     */
    public UserGarage addUserAuto(int userId) {
        UserGarage userGarage = null;
        UserAuto userAuto = null;
        Engine engine = null;
        Transmission transmission;
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            // выборка автомобилей для определённого клиента
            ps = connection.prepareStatement(
                    "SELECT t.id, brand, model, e.letter_designation, displacement, "
                    + "injection_type, power_kwt, power_hp, fuel_type, "
                    + "tr.letter_designation, gear_number, type_tm, "
                    + "cost_standard_hour, c.plate_number, c.vin, c.id "
                    + "FROM contragent_auto c JOIN template_auto t "
                    + "ON (c.template_auto_id = t.id) JOIN engine e "
                    + "ON (t.eng_id = e.id) JOIN transmission tr "
                    + "ON (t.trans_id = tr.id) JOIN model_auto m "
                    + "ON (t.model_id = m.id) JOIN brand_auto b "
                    + "ON (m.brand_id = b.id) WHERE c.contragent_id = ?;",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    engine = new Engine(rs.getString(4), rs.getDouble(5),
                            rs.getString(6), rs.getInt(7), rs.getInt(8));
                    engine.setEngineType(rs.getString(9));

                    transmission = new Transmission(rs.getString(10), rs.getInt(11),
                            rs.getString(12));
                    // создание авто относящегося к определённому клиенту
                    userAuto = new UserAuto(rs.getInt(16), rs.getString(14), rs.getString(15),
                            rs.getInt(1), rs.getString(2), rs.getString(3),
                            engine, transmission, rs.getDouble(13));

                    if (userGarage == null) {
                        userGarage = new UserGarage();
                        // добавление авто в гараж пользователя
                        userGarage.addUserAuto(userAuto);
                    } else {
                        userGarage.addUserAuto(userAuto);
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return userGarage;
    }

    /**
     * Метод удаляет автомобиль относящийся к определённому клиенту
     * @param userAutoId
     * @return 
     */
    public boolean deleteUserAuto(int userAutoId) {
        try {
            return connection.createStatement().executeUpdate(
                    "DELETE FROM contragent_auto WHERE id = " + userAutoId + ";") > 0;
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return false;
    }

    /**
     * Метод создает событие(запись)
     *
     * @param mechanicId id механика
     * @param year год
     * @param month месяц
     * @param day число
     * @param startHour время начала события
     * @param duration продолжительность события
     * @return
     */
    public boolean createEvent(int mechanicId, int year, int month, int day, int startHour, int duration) {
        // событие не должно быть более 11 часов
        if (duration > 0 && duration < 12) {
            // фиксация 3-х часового события в базе (стандартная запись клиента на ТО)
            final String threeHourEvent = "UPDATE time_matrix "
                    + "SET " + "\"" + String.valueOf(day) + "\"" + "[" + String.valueOf(startHour) + "] = 1,"
                    + " " + "\"" + String.valueOf(day) + "\"" + "[" + String.valueOf(startHour + 1) + "] = 1,"
                    + " " + "\"" + String.valueOf(day) + "\"" + "[" + String.valueOf(startHour + 2) + "] = 1 "
                    + "WHERE mechanic_id = ? AND year_ = ? AND month_id = ? "
                    + "AND " + "\"" + String.valueOf(day) + "\"" + "[" + String.valueOf(startHour) + "] = 0 "
                    + "AND " + "\"" + String.valueOf(day) + "\"" + "[" + String.valueOf(startHour + 1) + "] = 0 "
                    + "AND " + "\"" + String.valueOf(day) + "\"" + "[" + String.valueOf(startHour + 2) + "] = 0;";
            try {
                if (ps != null) {
                    if (!ps.isClosed()) {
                        ps.close();
                    }
                }
                if (duration == 3) {
                    ps = connection.prepareStatement(threeHourEvent,
                            ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ps.setInt(1, mechanicId);
                    ps.setInt(2, year);
                    ps.setInt(3, month);
                    return ps.executeUpdate() > 0;
                }

            } catch (SQLException ex) {
                logger.error("Database access error = {}", ex);
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Метод делает выборку по работам и запчастям в соответствии с параметрами
     * заказа
     *
     * @param order класс представляющий заказ-наряд
     */
    public void orderCreator(Order order) {

        // age_auto >= mileage
        // выборка по сроку эксплуатации
        final String ageAutoFirst = "SELECT DISTINCT l.id, l.name, p.amount_standard_hour, "
                + "t.cost_standard_hour, p.part_id, p.amount_parts FROM parts_order p "
                + "JOIN list_of_labor l ON (p.labor_id = l.id) JOIN template_auto t "
                + "ON (p.template_auto_id = t.id) WHERE t.id = ? AND p.age_auto[" + String.valueOf(order.getAge()) + "] = 1;";
        // age_auto < mileage
        // выборка по пробегу
        final String mileageAutoFirst = "SELECT DISTINCT l.id, l.name, p.amount_standard_hour,"
                + " t.cost_standard_hour, p.part_id, p.amount_parts FROM parts_order p "
                + "JOIN list_of_labor l ON (p.labor_id = l.id) "
                + "JOIN template_auto t ON (p.template_auto_id = t.id) "
                + "WHERE t.id = ? AND p.mileage[" + String.valueOf(order.getMileagePosition()) + "] = 1 "
                + "UNION "
                + "SELECT l.id, l.name, p.amount_standard_hour, t.cost_standard_hour, p.part_id, p.amount_parts "
                + "FROM parts_order p JOIN list_of_labor l ON (p.labor_id = l.id) JOIN template_auto t "
                + "ON (p.template_auto_id = t.id) WHERE t.id = ? AND p.age_auto[" + String.valueOf(order.getAge()) + "] = 1 "
                + "AND p.mileage IS NULL;";
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            // определяем что наступает раньше (ТО по пробегу или по сроку
            // экслуатации
            // если по сроку эксплуатации
            if (order.getAge() * 15 >= order.getMileage()) {
                ps = connection.prepareStatement(ageAutoFirst, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, order.getUserAuto().getId());
                // делаем выборку по работам
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // добавляем работу в заказ-наряд
                        order.addLabor(new LaborOfOrder(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4)));
                        // получаем запчасти требуемые для этой работы
                        Array array = rs.getArray(5);
                        // проверяем, что для выполнения работы требуются 
                        // запчасти (не для всех работ требуются запчасти)
                        if (!Objects.isNull(array)) {
                            int[] parts = objConverter((Object[]) rs.getArray(5).getArray());
                            int[] amountParts = objConverter((Object[]) rs.getArray(6).getArray());
                            int p = parts.length;
                            for (int i = 0; i < p; i++) {
                                // делем выборку по запчастям для конкретной работы
                                try (ResultSet rs1 = connection.createStatement().executeQuery(
                                        "SELECT id, name, price FROM parts WHERE id = " + parts[i] + ";")) {
                                    if (rs1.next()) {
                                        // добавляем запчасти в заказ-наряд
                                        order.addPart(new PartOfOrder(rs1.getInt(1), rs1.getString(2), amountParts[i], rs1.getDouble(3)));
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // если по пробегу
                ps = connection.prepareStatement(mileageAutoFirst, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setInt(1, order.getUserAuto().getId());
                ps.setInt(2, order.getUserAuto().getId());
                // делаем выборку по работам
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // добавляем работу в заказ-наряд
                        order.addLabor(new LaborOfOrder(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4)));
                        // получаем запчасти требуемые для этой работы
                        Array array = rs.getArray(5);
                        // проверяем, что для выполнения работы требуются 
                        // запчасти (не для всех работ требуются запчасти)
                        if (!Objects.isNull(array)) {
                            int[] parts = objConverter((Object[]) rs.getArray(5).getArray());
                            int[] amountParts = objConverter((Object[]) rs.getArray(6).getArray());
                            int p = parts.length;
                            for (int i = 0; i < p; i++) {
                                try (ResultSet rs1 = connection.createStatement().executeQuery(
                                        "SELECT id, name, price FROM parts WHERE id = " + parts[i] + ";")) {
                                    if (rs1.next()) {
                                        // добавляем запчасти в заказ-наряд
                                        order.addPart(new PartOfOrder(rs1.getInt(1), rs1.getString(2), amountParts[i], rs1.getDouble(3)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
    }

    /**
     * Метод для заполнения временных матриц для создания 
     * записей клиентов нулями - на год для всех механиков
     * в программе пока не используется
     * @return количество изменённых строк в базе данных
     */
    public int fillTableTimeMatrix() {
        int a = 0;
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            // добавление строки с незанятым рабочим временем (сутки 
            // представляют собой битовое поле из 24 элементов) для одного
            // механика на месяц
            ps = connection.prepareStatement("INSERT INTO time_matrix(mechanic_id, year_, month_id, "
                    + "\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"10\", \"11\", \"12\", \"13\", \"14\", \"15\", \"16\", \"17\", "
                    + "\"18\", \"19\", \"20\", \"21\", \"22\", \"23\", \"24\", \"25\", \"26\", \"27\", \"28\", \"29\", \"30\", \"31\") "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            // создание битового массива на 24 часа
            Object[] numbers = new Object[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0};
            // для 12 механиков (пока хардкод потом будет 
            // сделана выборка на количество)
            for (int i = 0; i < 12; i++) {
                // на 12 месяцев
                for (int k = 0; k < 12; k++) {
                    ps.setInt(1, i + 1);
                    ps.setInt(2, 2021);
                    ps.setInt(3, k + 1);
                    for (int z = 0; z < 31; z++) {
                        ps.setObject(z + 4, numbers);
                    }
                    a += ps.executeUpdate();
                    Thread.sleep(100);
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        } catch (InterruptedException ex) {
            logger.error("InterruptedException = {}", ex);
        }
        return a;
    }

    /**
     * Метод получает из базы данных все графики работ (по часам) для механиков
     * @return объект класса Event содержащий графики работ механиков
     */
    public Event getEvent() {
        LocalDateTime date = LocalDateTime.now();
        int d = Integer.parseInt(date.format(year));
        Event event = new Event();
        TimeMatrix timeMatrix = null;
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("SELECT * FROM time_matrix t JOIN months m ON (t.month_id = m.id) WHERE year_ = ?;",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1, d);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    timeMatrix = new TimeMatrix(rs.getInt("mechanic_id"), rs.getInt("year_"), rs.getString("name"));
                    for (int i = 0; i < 30; i++) {
                        timeMatrix.addDayToMatrix(i, objConverter(
                                (Object[]) rs.getArray(String.valueOf(i + 1)).getArray()));
                    }
                    event.addTimeMatrix(timeMatrix);
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return event;
    }

    /**
     * Метод сохраняет заказ-наряд в базе данных
     *
     * @param order заказ-наряд
     */
    public void saveOrder(Order order) {
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("INSERT INTO orders(order_number, contragent_id, "
                    + "contragent_auto_id, mechanic_id, date_of_recording, hour_of_recording) VALUES (?, ?, ?, ?, ?, ?);");
            ps.setString(1, order.getOrderNumber());
            ps.setInt(2, order.getClient().getId());
            ps.setInt(3, order.getUserAuto().getUserAutoId());
            ps.setInt(4, order.getMechanic().getId());
            ps.setDate(5, Date.valueOf(order.getDateOrderRecording()));
            ps.setString(6, order.getClockEvent());
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
    }

    /**
     * Метод выполняет сериализацию объекта класса Order содержащего заказ-наряд
     * в файл
     *
     * @param order заказ-наряд
     */
    public void saveOrderToFile(Order order) {
        if (order != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("/home/deniskharlamov/OrderToFile/" + order.getOrderNumber() + ".bin"))) {
                out.writeObject(order);
            } catch (IOException ex) {
                logger.error("Database access error = {}", ex);
            }
        }
    }

    /**
     * Метод выполняет десериализацию объекта класса Order содержащего
     * заказ-наряд из файла
     *
     * @param orderNumber
     * @return
     */
    public Order getOrderOfFile(String orderNumber) {
        Order order = null;
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("/home/deniskharlamov/OrderToFile/" + orderNumber + ".bin"))) {
            order = (Order) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            logger.error("Database access error = {}", ex);
        }
        return order;
    }

    /**
     * Метод делает выборку заказ-нарядов из базы данных для определённой даты
     *
     * @param date дата для выборки
     * @return объект класса Orders содержащий все заказ-наряды для указанной
     * даты
     */
    public Orders getOrdersToDate(LocalDate date) {
        Orders orders = null;
        try {
            if (ps != null) {
                if (!ps.isClosed()) {
                    ps.close();
                }
            }
            ps = connection.prepareStatement("SELECT order_number FROM orders WHERE date_of_recording = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (numberOfRecordsRS(rs) > 0) {
                    orders = new Orders();
                    while (rs.next()) {
                        orders.addOrder(getOrderOfFile(rs.getString(1)));
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Database access error = {}", ex);
        }
        return orders;
    }

    /**
     * Метод удаляет запись и всё что с ней связано
     *
     * @param order заказ-наряд
     * @return true - запись удалена false - ошибка удаления
     */
    public boolean deleteEntry(Order order) {
        if (order != null) {
            // отмена 3-х часового события в базе (стандартная запись клиента на ТО)
            final String deleteThreeHourEvent = "UPDATE time_matrix "
                    + "SET " + "\"" + order.getYearMonthDayHour()[2] + "\"" + "[" + order.getYearMonthDayHour()[3] + "] = 0,"
                    + " " + "\"" + order.getYearMonthDayHour()[2] + "\"" + "[" + (order.getYearMonthDayHour()[3] + 1) + "] = 0,"
                    + " " + "\"" + order.getYearMonthDayHour()[2] + "\"" + "[" + (order.getYearMonthDayHour()[3] + 2) + "] = 0 "
                    + "WHERE mechanic_id = ? AND year_ = ? AND month_id = ?;";
            // удаление записи из таблицы Ordes
            final String deleteOrderFromOrders = "DELETE FROM orders "
                    + "WHERE order_number = " + order.getOrderNumber() + ";";
            try {
                if (ps != null) {
                    if (!ps.isClosed()) {
                        ps.close();
                    }
                }
                ps = connection.prepareStatement(deleteThreeHourEvent);
                ps.setInt(1, order.getMechanic().getId());
                ps.setInt(2, order.getYearMonthDayHour()[0]);
                ps.setInt(3, order.getYearMonthDayHour()[1]);
                // освобождаем рабочие часы персонала 
                if (ps.executeUpdate() > 0) {
                    // удаляем запись из таблицы Orders
                    if (connection.createStatement()
                            .executeUpdate(deleteOrderFromOrders) > 0) {
                        // удаляем файл с сериализованым объектом
                        if (Files.deleteIfExists(Paths.get("/home/deniskharlamov/OrderToFile/"
                                + order.getOrderNumber()
                                + ".bin"))) {
                            // удаляем файл .pdf
                            if (Files.deleteIfExists(Paths.get("/home/deniskharlamov/orderToPDF/"
                                    + order.getOrderNumber()
                                    + ".pdf"))) {
                                return true;
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                logger.error("Database access error = {}", ex);
            } catch (IOException ex) {
                logger.error("IOException error = {}", ex);
            }
        }
        return false;
    }

    /**
     * Метод конвертирует массив Object[] в int[]
     * @param object
     * @return int[]
     */
    private int[] objConverter(Object[] object) {
        int[] a = {};
        if (object != null) {
            a = new int[object.length];
            for (int i = 0; i < object.length; i++) {
                a[i] = (int) object[i];
            }
            return a;
        }
        return a;
    }

    /**
     * Метод жизненного цикла компонента, вызываемый при удалении,
     * выполняет закрытие подготовленного запроса и 
     * соединения с базой данных
     */
    @PreDestroy
    public void cleanup() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
            if (!ps.isClosed()) {
                ps.close();
            }
            connection = null;
            ps = null;
        } catch (SQLException ex) {
            logger.error("Could not close the database connection = {}", ex);
        }
    }
}
