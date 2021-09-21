package app.model;

import java.io.Serializable;

/**
 * Класс представляющий коробку передач
 */
public class Transmission implements Serializable{

    // буквенное обозначение
    private final String letterDesignation;
    // количество передач
    private final int gearNumber;
    // тип коробки передач (автомат, механика, DSG)
    private final String typeTm;

    public Transmission(String letterDesignation, int gearNumber, String typeTm) {
        this.letterDesignation = letterDesignation;
        this.gearNumber = gearNumber;
        this.typeTm = typeTm;
    }

    public String getLetterDesignation() {
        return letterDesignation;
    }

    public int getGearNumber() {
        return gearNumber;
    }

    public String getTypeTm() {
        return typeTm;
    }

    @Override
    public String toString() {
        return gearNumber + " speed " + typeTm;
    }
}
