package employeemanagementsystem;

import java.util.ArrayList;

public class TotalSalaryVisitor extends Visitor{

    @Override
    public String visit(Contributor c) {
        return c.getSalary().toString();
    }

    @Override
    public String visit(Manager m) {
        Integer total = m.getSalary();
        ArrayList<Integer> managedIDs = m.getManagedIDs();
        for(Integer i : managedIDs){
            total += Integer.valueOf(visit(Employee.find(i)));
        }
        return total.toString();
    }
}
