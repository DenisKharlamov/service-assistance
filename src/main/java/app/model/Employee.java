package app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляющий отдельного сотрудника с должностью мастер
 */
public class Employee extends User {

    // список механиков относящихся к мастеру
    private List<Mechanic> mechanicList;

    public Employee(boolean confirmEmail, String name, String surname, String telephone, String email, int id) {
        super(confirmEmail, name, surname, telephone, email, id);
    }

    /**
     * Метод определяет количество механиков у мастера
     * @return 
     */
    public int amountMechanic() {
        if (mechanicList != null) {
            return mechanicList.size();
        } else {
            return 0;
        }
    }

    /**
     * Метод добавляет механика мастеру
     * @param mechanic 
     */
    public void addMechanic(Mechanic mechanic) {
        if (mechanic != null) {
            if (mechanicList != null) {
//                if (!mechanicList.contains(mechanic)) {
                mechanicList.add(mechanic);
//                }
            } else {
                mechanicList = new ArrayList<>();
                mechanicList.add(mechanic);
            }
        }
    }

    /**
     * Метод для получения механика по индексу
     * @param index
     * @return 
     */
    public Mechanic getMechanic(int index) {
        if (mechanicList != null) {
            if (index < mechanicList.size()) {
                return mechanicList.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}