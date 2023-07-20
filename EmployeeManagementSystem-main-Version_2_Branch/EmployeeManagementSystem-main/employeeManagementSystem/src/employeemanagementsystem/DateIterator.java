package employeemanagementsystem;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DateIterator implements Iterator{
    private Integer index = 0;
    protected ArrayList<Employee> employeeCollection;
    protected Employee currentEmployee = null;

    public DateIterator(){
    }
    public DateIterator(ArrayList<Employee> employeeCollection){
        this.employeeCollection = employeeCollection;
        Collections.sort(employeeCollection, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                Date d1 = e1.getDate();
                Date d2 = e2.getDate();
                return d1.compareTo(d2);
            }
        });
    }

    public void setCollection(ArrayList<Employee> employeeCollection){
        this.employeeCollection = employeeCollection;
        Collections.sort(employeeCollection, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                String str1 = e1.getFirstName() + " " + e1.getLastName();
                String str2 = e2.getFirstName() + " " + e2.getLastName();
                return str1.compareTo(str2);
            }
        });
    }

    @Override
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    @Override
    public Employee getNext() {
        if(currentEmployee == null){
            if(employeeCollection.isEmpty()) return null;
            currentEmployee = employeeCollection.get(index);
            index++;
            return currentEmployee;
        }
        if(hasMore()){
            currentEmployee = employeeCollection.get(index);
            index++;
            return currentEmployee;
        }
        return null;
    }

    @Override
    public Boolean hasMore() {
        if(index >= employeeCollection.size()) return false;
        return true;
    }

    public Integer getIndex() { return index; }

    public void setIndex(Integer index){
        this.index = index;
    }
}
