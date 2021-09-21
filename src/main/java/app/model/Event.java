package app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс контейнерного типа, содержит в себе времменЫе матрицы событий
 */
public class Event {

    // список матриц
    private List<TimeMatrix> listTime;

    /**
     * Метод проверяет не занято ли время записи
     * @param mechanicId ид механика
     * @param year год
     * @param month месяц
     * @param day число
     * @param timeBegin время начала записи
     * @param timeEnd время окончания записи
     * @return true - время свободно(доступно для записи) или false - занято
     */
    public boolean isAvailableTime(int mechanicId, int year, String month, int day, int timeBegin, int timeEnd) {
        int a = 0;
        for (TimeMatrix x : listTime) {
            if (x.getMechanicId() == mechanicId && x.getYear() == year
                    && x.getMonth().equalsIgnoreCase(month)) {
                for (int i = timeBegin - 1; i < timeEnd - 1; i++) {
                    if (x.getMatrix()[day - 1][i] > 0) {
                        a++;
                    }
                }
            }
        }
        return a < 1;
    }

    /**
     * Метод для добавления матрицы в список
     * @param timeMatrix 
     */
    public void addTimeMatrix(TimeMatrix timeMatrix) {
        if (timeMatrix != null) {
            if (listTime != null) {
                listTime.add(timeMatrix);
            } else {
                listTime = new ArrayList<>();
                listTime.add(timeMatrix);
            }
        }
    }

    /**
     * Метод для определения количества матриц в списке
     * @return 
     */
    public int getAmountTimeMatrix() {
        if (listTime != null) {
            return listTime.size();
        } else {
            return 0;
        }
    }
}
