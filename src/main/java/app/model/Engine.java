package app.model;

import java.io.Serializable;

/**
 * Класс представляющий двигатель автомобиля
 */
public class Engine implements Serializable{

    /**
     * Перечисление определяет тип топлива автомобиля.
     */
    public static enum EngineType {
        /**
         * Бензин
         */
        PETROL,
        /**
         * Дизельное топливо
         */
        DIESEL;
    }

    // буквенное обозначение двигателя
    private final String letterDesignation;
    // объём двигателя
    private final double displacement;
    // тип впрыска
    private final String injectionType;
    // мощность в кВт
    private final int powerKwt;
    // мощность в л.с.
    private final int powerHp;
    // тип топлива
    private EngineType engineType;

    public Engine(String letterDesignation, double displacement, String injectionType, int powerKwt, int powerHp) {
        this.letterDesignation = letterDesignation;
        this.displacement = displacement;
        this.injectionType = injectionType;
        this.powerKwt = powerKwt;
        this.powerHp = powerHp;
    }

    public String getLetterDesignation() {
        return letterDesignation;
    }

    public double getDisplacement() {
        return displacement;
    }

    public String getInjectionType() {
        return injectionType;
    }

    public int getPowerKwt() {
        return powerKwt;
    }

    public int getPowerHp() {
        return powerHp;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        if (engineType != null) {
            if (this.engineType == null) {
                switch (engineType) {
                    case "Petrol":
                        this.engineType = EngineType.PETROL;
                        break;
                    case "Diesel":
                        this.engineType = EngineType.DIESEL;
                        break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return displacement + "L " + injectionType + " " + powerHp + "h.p. " + engineType;
    }

}
