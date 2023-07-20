package employeemanagementsystem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SalaryIterator implements Iterator{

    private Integer index = 0;
    protected ArrayList<Employee> employeeCollection;
    protected Employee currentEmployee = null;

    public SalaryIterator(ArrayList<Employee> employeeCollection){
        this.employeeCollection = employeeCollection;
        Collections.sort(employeeCollection, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                if(e1.getSalary() <= e2.getSalary()) return -1;
                else return 1;
            }
        });
    }

    public SalaryIterator(){
    }

    public void setCollection(ArrayList<Employee> employeeCollection){
        this.employeeCollection = employeeCollection;
        Collections.sort(employeeCollection, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                if(e1.getSalary() <= e2.getSalary()) return 1;
                else return -1;
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
