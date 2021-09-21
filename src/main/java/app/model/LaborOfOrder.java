package app.model;

import java.io.Serializable;

/**
 * Класс представляет собой работу связанную с заказом
 */
public class LaborOfOrder implements Serializable{

    // ид работы
    private final int laborId;
    // наименование работы
    private final String nameLabor;
    // кол-во нормо часов требуемых для данной работы
    private final double amountStandardHour;
    // стоимость нормо часа (формируется уже с учетом автомобиля)
    private final double costStandardHour;

    public LaborOfOrder(int laborId, String nameLabor, double amountStandardHour, double costStandardHour) {
        this.laborId = laborId;
        this.nameLabor = nameLabor;
        this.amountStandardHour = amountStandardHour;
        this.costStandardHour = costStandardHour;
    }

    public int getLaborId() {
        return laborId;
    }

    public String getNameLabor() {
        return nameLabor;
    }

    public double getAmountStandardHour() {
        return amountStandardHour;
    }

    public double getCostStandardHour() {
        return costStandardHour;
    }

    @Override
    public String toString() {
        return "LaborOfOrder{" + "laborId=" + laborId + ", nameLabor=" + nameLabor + ", amountStandardHour=" + amountStandardHour + ", costStandardHour=" + costStandardHour + '}';
    }

}
