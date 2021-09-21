package app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс содержит список заказов
 */
public class Orders {
    
    // список заказов
    private List<Order> ordersList;
    
    /**
     * Метод для добавления заказа
     * @param order 
     */
    public void addOrder(Order order) {
        if (order != null) {
            if (ordersList != null) {
                ordersList.add(order);
            } else {
                ordersList = new ArrayList<>();
                ordersList.add(order);
            }
        }
    }
    
    /**
     * Метод для получения заказа по индексу
     * @param index
     * @return 
     */
    public Order getOrder(int index) {
        if (ordersList != null) {
            if (index < ordersList.size()) {
                return ordersList.get(index);
            }
        }
        return null;
    }
    
    /**
     * Метод для получения заказа по номеру
     * @param orderNumber
     * @return 
     */
    public Order getOrderFromNumber(String orderNumber) {
        if (orderNumber != null) {
            if (ordersList != null) {
                for (int i = 0; i < ordersList.size(); i++) {
                    if (ordersList.get(i).getOrderNumber()
                            .equalsIgnoreCase(orderNumber)) {
                        return ordersList.get(i);
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Метод для определения количества заказов в списке
     * @return 
     */
    public int amountOrder() {
        if (ordersList != null) {
            return ordersList.size();
        }
        return 0;
    }
}