package employeemanagementsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public interface Iterator {
    public Employee getNext();
    public Boolean hasMore();
    public void setCollection(ArrayList<Employee> employeeCollection);
    public Employee getCurrentEmployee();
    public Integer getIndex();
    public void setIndex(Integer index);
}
