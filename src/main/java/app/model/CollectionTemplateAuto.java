package app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Класс содержит коллекцию автомобилей которые входят 
 * в список обслуживаемых для данного сервиса
 */
public class CollectionTemplateAuto {

    // список автомобилей подлежащих обслуживанию
    private ArrayList<TemplateAuto> templateAutoList;

    public ArrayList<TemplateAuto> getTemplateAutoList() {
        return templateAutoList;
    }

    /**
     * Метод для добавления авто в список
     * @param templateAuto 
     */
    public void addTemplateAuto(TemplateAuto templateAuto) {
        if (templateAuto != null) {
            if (templateAutoList != null) {
                templateAutoList.add(templateAuto);
            } else {
                templateAutoList = new ArrayList<>();
                templateAutoList.add(templateAuto);
            }
        }
    }

    /**
     * Метод для получения авто из списка по индексу
     * @param index
     * @return 
     */
    public TemplateAuto getAuto(int index) {
        if (templateAutoList != null) {
            if (index < templateAutoList.size()) {
                return templateAutoList.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Метод определяющий количество автомобилей в списке
     * @return 
     */
    public int amountTemplateAuto() {
        if (templateAutoList != null) {
            return templateAutoList.size();
        }
        return 0;
    }

    /**
     * Метод сортирует список
     */
    public void sortAuto() {
        Collections.sort(templateAutoList, new Comparator<TemplateAuto>() {
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