package app.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляет собой предварительный заказ-наряд
 */
public class Order implements Serializable {

    // ид заказ-наряда в базе данных
    private int orderId;
    // номер заказ-наряда
    private String orderNumber;
    // дата на которую выполнена запись
    private final LocalDate dateOrderRecording;
    // дата совершения записи
    private LocalDate dateOrderCreation;
    // время на которое произведена запись
    private final String clockEvent;
    // клиент выполнивший запись
    private final User client;
    // механик к которому записался клиент
    private final Mechanic mechanic;
    // авто клиента для ТО
    private UserAuto userAuto;
    // пробег авто
    private int mileage;
    // срок эксплуатации авто
    private int age;

    // список запчастей по заказ-наряду
    private List<PartOfOrder> parts;
    // список работ по заказ-наряду
    private List<LaborOfOrder> labors;

    // используется при создании события
    public Order(LocalDate dateOrderRecording, String clockEvent, User client, Mechanic mechanic) {
        this.dateOrderRecording = dateOrderRecording;
        this.clockEvent = clockEvent;
        this.client = client;
        this.mechanic = mechanic;
    }

    /**
     * Добавление запчасти
     * @param part 
     */
    public void addPart(PartOfOrder part) {
        if (part != null) {
            if (parts != null) {
                parts.add(part);
            } else {
                parts = new ArrayList<>();
                parts.add(part);
            }
        }
    }

    /**
     * Получение запчасти по индексу
     * @param index
     * @return 
     */
    public PartOfOrder getPart(int index) {
        if (parts != null) {
            if (index < parts.size()) {
                return parts.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Метод для определения количества запчастей в заказ-наряде
     * @return количество запчастей
     */
    public int getAmountPart() {
        if (parts != null) {
            return parts.size();
        } else {
            return 0;
        }
    }

    /**
     * Добавление работы к заказ-наряду
     * @param labor 
     */
    public void addLabor(LaborOfOrder labor) {
        if (labor != null) {
            if (labors != null) {
                labors.add(labor);
            } else {
                labors = new ArrayList<>();
                labors.add(labor);
            }
        }
    }

    /**
     * Получение работы из заказ-наряда по индексу
     * @param index
     * @return 
     */
    public LaborOfOrder getLabor(int index) {
        if (labors != null) {
            if (index < labors.size()) {
                return labors.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Количество работ по заказ-наряду
     * @return количество работ
     */
    public int getAmountLabor() {
        if (labors != null) {
            return labors.size();
        } else {
            return 0;
        }
    }

    /**
     * Метод позволяет получить дату записи клиента в виде массива целых чисел
     * @return год, месяц, день и час записи киента
     */
    public int[] getYearMonthDayHour() {
        if (dateOrderRecording != null) {
            final DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
            final DateTimeFormatter month = DateTimeFormatter.ofPattern("M");
            final DateTimeFormatter day = DateTimeFormatter.ofPattern("dd");
            int[] a = {Integer.parseInt(dateOrderRecording.format(year)),
                Integer.parseInt(dateOrderRecording.format(month)),
                Integer.parseInt(dateOrderRecording.format(day)),
                Integer.parseInt(clockEvent.substring(0, clockEvent.indexOf(":")))};
            return a;
        } else {
            return null;
        }
    }

    /**
     * Создание номера заказ-наряда
     */
    public void createOrderNumber() {
        if (client != null && userAuto != null
                && mechanic != null && dateOrderRecording != null) {
            final DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
            final DateTimeFormatter month = DateTimeFormatter.ofPattern("M");
            final DateTimeFormatter day = DateTimeFormatter.ofPattern("dd");
            orderNumber = client.getId() + userAuto.getUserAutoId()
                    + mechanic.getId() + dateOrderRecording.format(day)
                    + dateOrderRecording.format(month) + dateOrderRecording.format(year)
                    + clockEvent.substring(0, clockEvent.indexOf(":"));
        }
    }

    /**
     * Получение стоимости работ по заказ-наряду
     * @return стоимость работ
     */
    public double getPriceLabor() {
        if (labors != null) {
            double price = 0;
            price = labors.stream().map(x
                    -> x.getAmountStandardHour() * x.getCostStandardHour())
                    .reduce(price, (accumulator, _item) -> accumulator + _item);
            return price;
        }
        return 0;
    }

    /**
     * Получение стоимости по запчастям
     * @return стоимость запчастей
     */
    public double getPriceParts() {
        if (parts != null) {
            double price = 0;
            price = parts.stream().map(x
                    -> x.getPrice() * x.getAmountParts())
                    .reduce(price, (accumulator, _item) -> accumulator + _item);
            return price;
        }
        return 0;
    }

    /**
     * Общая стоимость заказ-наряда
     * @return общая стоимость
     */
    public double getPriceOrder() {
        if (labors != null && parts != null) {
            return getPriceLabor() + getPriceParts();
        }
        return 0;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public LocalDate getDateOrderRecording() {
        return dateOrderRecording;
    }

    public LocalDate getDateOrderCreation() {
        return dateOrderCreation;
    }

    public String getClockEvent() {
        return clockEvent;
    }

    public User getClient() {
        return client;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public UserAuto getUserAuto() {
        return userAuto;
    }

    public int getMileage() {
        return mileage;
    }

    public int getMileagePosition() {
        if (mileage != 0) {
            return mileage / 15 + 1;
        }
        return 1;
    }

    public int getAge() {
        return age;
    }

    public String getMonthCh() {
        if (dateOrderRecording != null) {
            final DateTimeFormatter monthCh = DateTimeFormatter.ofPattern("MMMM");
            return dateOrderRecording.format(monthCh);
        }
        return null;
    }

    public List<PartOfOrder> getParts() {
        return parts;
    }

    public List<LaborOfOrder> getLabors() {
        return labors;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setUserAuto(UserAuto userAuto) {
        this.userAuto = userAuto;
    }

    public void setDateOrderCreation(LocalDate dateOrderCreation) {
        this.dateOrderCreation = dateOrderCreation;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", orderNumber=" + orderNumber + ", clockEvent=" + clockEvent + ", mileage=" + mileage + ", age=" + age + '}';
    }
}
