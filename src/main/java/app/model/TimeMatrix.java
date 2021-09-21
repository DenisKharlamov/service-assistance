package app.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс представляющий временную матрицу событий сервиса
 * (месячный почасовой график записей механика)
 */
public class TimeMatrix {

    // ид механика
    private final int mechanicId;
    // год
    private final int year;
    // месяц
    private final String month;
    // график событий на месяц
    private int[][] matrix;

    public TimeMatrix(int mechanicId, int year, String month) {
        this.mechanicId = mechanicId;
        this.year = year;
        this.month = month;
    }

    /**
     * Метод для заполнения графика
     * @param day число
     * @param array массив представляющий собой 24 часа
     */
    public void addDayToMatrix(int day, int[] array) {
        if (array != null) {
            if (matrix != null) {
                matrix[day] = array;
            } else {
                matrix = new int[30][23];
                matrix[day] = array;
            }
        }

    }

    public int getMechanicId() {
        return mechanicId;
    }

    public int getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Метод пока не используется - для будующей функциональности
     * @param matrix 
     */
    private void inizialize(int[][] matrix) {
        if (matrix != null) {
            for (int[] x : matrix) {
                Arrays.fill(x, 0);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + this.mechanicId;
        hash = 61 * hash + this.year;
        hash = 61 * hash + Objects.hashCode(this.month);
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
        final TimeMatrix other = (TimeMatrix) obj;
        return true;
    }
}