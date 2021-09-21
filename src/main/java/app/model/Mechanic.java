package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляет работника с должностью механик
 */
public class Mechanic implements Serializable{

    // ид механика
    private final int id;
    // ид начальника
    private final int patronId;
    // имя
    private final String name;
    // фамилия
    private final String surname;
    // отчество
    private String patronymic;
    // список содержащий месячные графики работ
    private List<EmployeeMontlyWorkSchedule> workingDays;

    public Mechanic(int id, int patronId, String name, String surname) {
        this.id = id;
        this.patronId = patronId;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Метод добавляет месячный график работ в список графиков
     * @param schedule 
     */
    public void addMontlyWorkSchedule(EmployeeMontlyWorkSchedule schedule) {
        if (schedule != null) {
            if (workingDays != null) {
                if (!workingDays.contains(schedule)) {
                    workingDays.add(schedule);
                } else {
                    int index = workingDays.indexOf(schedule);
                    workingDays.add(index, schedule);
                }
            } else {
                workingDays = new ArrayList<>();
                workingDays.add(schedule);
            }
        }
    }

    /**
     * Метод позволяет определить кол-во графиков в списке
     * @return 
     */
    public int amountEmployeeMontlyWorkSchedule() {
        if (workingDays != null) {
            return workingDays.size();
        }
        return 0;
    }

    /**
     * Метод позволяет определить работает ли механик в определённый день
     * @param year год
     * @param month месяц
     * @param day число
     * @return true - работает, false - выходной
     */
    public boolean isWork(int year, String month, int day) {
        for (EmployeeMontlyWorkSchedule x : workingDays) {
            if (x.getYear() == year && x.getMonth().equalsIgnoreCase(month)) {
                return x.getDays()[day - 1] > 0;
            }
        }
        return false;
    }

    /**
     * Метод позволяет узнать со скольки начинает работать механик 
     * в определённый день
     * @param year
     * @param month
     * @param day
     * @return целое число определяющее час 
     * с которого начинается рабочий день
     */
    public int whatTimeIsToday(int year, String month, int day) {
        for (EmployeeMontlyWorkSchedule x : workingDays) {
            if (x.getYear() == year && x.getMonth().equalsIgnoreCase(month)) {
                return x.getTimes()[day - 1];
            }
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public int getPatronId() {
        return patronId;
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

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mechanic other = (Mechanic) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}