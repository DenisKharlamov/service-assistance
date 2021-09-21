package app.model;

import java.io.Serializable;

/**
 * Класс представляющий собой автомобиль клиента
 */
public class UserAuto extends TemplateAuto implements Serializable{

    // ид автомобиля
    private final int userAutoId;
    // гос. номер
    private final String plateNumber;
    // VIN номер автомобиля
    private final String vin;

    public UserAuto(int userAutoId, String plateNumber, String vin, int id, String brand, String model,
            Engine engine, Transmission transmission, double costStandardHour) {
        super(id, brand, model, engine, transmission, costStandardHour);
        this.userAutoId = userAutoId;
        this.plateNumber = plateNumber;
        this.vin = vin;
    }

    public int getUserAutoId() {
        return userAutoId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getVin() {
        return vin;
    }
}