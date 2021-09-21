package app.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс представляет собой график работ на месяц
 */
public class EmployeeMontlyWorkSchedule implements Serializable{

    // ид работника которому принадлежит график
    private final int employeeId;
    // год
    private final int year;
    // месяц
    private final String month;
    // список рабочих дней
    private int[] days;
    // время начала смены
    private int[] times;

    public EmployeeMontlyWorkSchedule(int employeeId, int year, String month, int[] days, int[] times) {
        this.employeeId = employeeId;
        this.year = year;
        this.month = month;
        this.days = days;
        this.times = times;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.year;
        hash = 41 * hash + Objects.hashCode(this.month);
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
        final EmployeeMontlyWorkSchedule other = (EmployeeMontlyWorkSchedule) obj;
        if (this.year != other.year) {
            return false;
        }
        if (!Objects.equals(this.month, other.month)) {
            return false;
        }
        return true;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public int[] getDays() {
        return days;
    }

    public int[] getTimes() {
        return times;
    }

    public void setDays(int[] days) {
        this.days = days;
    }

    public void setTimes(int[] times) {
        this.times = times;
    }
}