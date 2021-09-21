package app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Класс содержит список автомобилей клиента
 */
public class UserGarage {

    // список автомобилей клиента
    private ArrayList<UserAuto> userAutoList;

    /**
     * Метод добавления автомобиля в список авто
     * @param userAuto 
     */
    public void addUserAuto(UserAuto userAuto) {
        if (userAuto != null) {
            if (userAutoList != null) {
                userAutoList.add(userAuto);
            } else {
                userAutoList = new ArrayList<>();
                userAutoList.add(userAuto);
            }
        }
    }

    /**
     * Метод для получения автомобиля по индексу
     * @param index
     * @return 
     */
    public UserAuto getAuto(int index) {
        if (userAutoList != null) {
            if (index < userAutoList.size()) {
                return userAutoList.get(index);
            }
        }
        return null;
    }

    /**
     * Метод позволяющий получить автомобиль по его ид
     * @param userAutoId
     * @return 
     */
    public UserAuto getUserAutoForId(int userAutoId) {
        for (UserAuto x : userAutoList) {
            if (x.getUserAutoId() == userAutoId) {
                return x;
            }
        }
        return null;
    }

    /**
     * Метод определяет количество автомобилей в списке авто
     * @return 
     */
    public int amountUserAuto() {
        if (userAutoList != null) {
            return userAutoList.size();
        }
        return 0;
    }

    /**
     * Метод выполняет сортировку автомобилей в списке
     */
    public void sortAuto() {
        Collections.sort(userAutoList, new Comparator<TemplateAuto>() {
            @Override
            public int compare(TemplateAuto o1, TemplateAuto o2) {
                if (o1.getModel().compareTo(o2.getModel()) == 0) {
                    if (Double.compare(o1.getEngine().getDisplacement(), o2.getEngine().getDisplacement()) == 0) {
                        return o1.getEngine().getPowerHp() - o2.getEngine().getPowerHp();
                    } else {
                        return Double.compare(o1.getEngine().getDisplacement(), o2.getEngine().getDisplacement());
                    }
                } else {
                    return o1.getModel().compareTo(o2.getModel());
                }
            }
        });
    }
}
