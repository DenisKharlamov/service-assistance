package app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс содержит список работников автосервиса
 */
public class Staff {

    // список работников
    private List<Employee> employeeList;

    /**
     * Метод определяет количество работников в списке
     * @return 
     */
    public int amountEmployee() {
        if (employeeList != null) {
            return employeeList.size();
        }
        return 0;
    }

    /**
     * Метод позволяет получить работника по индексу
     * @param index
     * @return 
     */
    public Employee getEmployeeIndex(int index) {
        if (employeeList != null) {
            if (index < employeeList.size()) {
                return employeeList.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Метод позволяет добавить работника в список
     * @param employee 
     */
    public void addEmployee(Employee employee) {
        if (employee != null) {
            if (employeeList != null) {
                employeeList.add(employee);
            } else {
                employeeList = new ArrayList<>();
                employeeList.add(employee);
            }
        }
    }

    /**
     * Метод позволяет получить механика по его ид
     * @param mechanicId
     * @return 
     */
    public Mechanic getMechanicForId(int mechanicId) {
        if (employeeList != null) {
            int k = amountEmployee();
            for (int i = 0; i < k; i++) {
                int z = getEmployeeIndex(i).amountMechanic();
                for (int n = 0; n < z; n++) {
                    if (getEmployeeIndex(i).getMechanic(n).getId() == mechanicId) {
                        return getEmployeeIndex(i).getMechanic(n);
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Метод позволяет получить работника по его email
     * @param email
     * @return 
     */
    public Employee getEmployeeFromEmail(String email) {
        if (employeeList != null) {
            for (int i = 0; i < employeeList.size(); i++) {
                if (getEmployeeIndex(i).getEmail().equalsIgnoreCase(email)) {
                    return getEmployeeIndex(i);
                }
            }
        }
        return null;
    }
}