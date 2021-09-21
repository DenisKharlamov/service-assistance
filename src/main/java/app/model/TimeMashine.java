package app.model;

import java.time.LocalDate;

/**
 * Класс позволяющий выбирать день записи на ТО
 */
public class TimeMashine {

    // выбранная дата
    private LocalDate variable;

    public TimeMashine() {
        this.variable = LocalDate.now();
    }

    /**
     * Метод прибавляющий 1 день к выбранному дню
     */
    public void nextDay() {
        this.variable = variable.plusDays(1);
    }

    /**
     * Метод отнимающий день от выбранного дня
     */
    public void previosDay() {
        this.variable = variable.minusDays(1);
    }

    /**
     * Переводит на текущую дату
     */
    public void today() {
        this.variable = LocalDate.now();
    }
    
    /**
     * Установка выбранного времени (используется 
     * для выбора через календарь)
     * @param time 
     */
    public void setTime(LocalDate time) {
        this.variable = time;
    }

    /**
     * Метод позволяет получить время выбранное пользователем
     * @return 
     */
    public LocalDate getTime() {
        return variable;
    }
}
