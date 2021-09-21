package app.model;

import java.io.Serializable;

/**
 * Класс представляет собой запчасть связанную с заказом
 */
public class PartOfOrder implements Serializable{

    // ид запчасти
    private final int partId;
    // наименование запчасти
    private final String namePart;
    // количество
    private final int amountParts;
    // цена запчасти
    private final double price;

    public PartOfOrder(int partId, String namePart, int amountParts, double price) {
        this.partId = partId;
        this.namePart = namePart;
        this.amountParts = amountParts;
        this.price = price;
    }

    public int getPartId() {
        return partId;
    }

    public String getNamePart() {
        return namePart;
    }

    public int getAmountParts() {
        return amountParts;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "PartOfOrder{" + "partId=" + partId + ", namePart=" + namePart + ", amountParts=" + amountParts + ", price=" + price + '}';
    }

}
