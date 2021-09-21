package app.model;

import java.io.Serializable;

/**
 * Класс представляющий автомобиль определённой конфигурации
 * подлежащий обслуживанию на сервисе
 */
public class TemplateAuto implements Serializable{

    // ид автомобиля
    private final int id;
    // марка
    private final String brand;
    // модель
    private final String model;
    // двигатель
    private final Engine engine;
    // коробка передач
    private final Transmission transmission;
    // стоимость нормо-часа (для работ)
    private final double costStandardHour;

    public TemplateAuto(int id, String brand, String model, Engine engine, Transmission transmission, double costStandardHour) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.engine = engine;
        this.transmission = transmission;
        this.costStandardHour = costStandardHour;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Engine getEngine() {
        return engine;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public double getCostStandardHour() {
        return costStandardHour;
    }

    @Override
    public String toString() {
        return model + " " + engine + " " + transmission;
    }

}
