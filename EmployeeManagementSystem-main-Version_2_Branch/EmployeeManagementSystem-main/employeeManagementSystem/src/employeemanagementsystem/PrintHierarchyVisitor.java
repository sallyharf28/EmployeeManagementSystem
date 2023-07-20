package employeemanagementsystem;
import java.util.ArrayList;

public class PrintHierarchyVisitor extends Visitor{
    private SimpleIteratorFactory iFactory;
    private String iterator;
    public PrintHierarchyVisitor(String iterator){
        iFactory = new SimpleIteratorFactory(iterator);
        this.iterator = iterator;
    }

    public String visit(Employee e, Integer depth){
        if(e.isManager()) return visit((Manager) e, depth);
        else return visit((Contributor) e, depth);
    }

    @Override
    public String visit(Contributor c) {
        return visit(c, 0);
    }

    public String visit(Contributor c, Integer depth) {
        String s = "        ".repeat(depth) + c.getFirstName() + " " + c.getLastName();
        switch(iterator){
            case("By Name") : s+= " (Position : " + c.getPosition() + ")\n"; break;
            case("By Salary") : s+= " (Salary : " + c.getSalary() + ")\n"; break;
            case("By Employment Date") : s+= " (Employment Date : " + c.getDate().toString() + ")\n"; break;
        }
        return s;
    }

    @Override
    public String visit(Manager m) {
        return visit(m, 0);
    }

    public String visit(Manager m, Integer depth) {
        Iterator i = iFactory.getNewIterator();
        String s = "        ".repeat(depth);
        s += m.getFirstName() + " " + m.getLastName();
        switch(iterator){
            case("By Name") : s+= " (Position : " + m.getPosition() + ")\n"; break;
            case("By Salary") : s+= " (Salary : " + m.getSalary() + ")\n"; break;
            case("By Employment Date") : s+= " (Employment Date : " + m.getDate().toString() + ")\n"; break;
        }
        ArrayList<Integer> managedIDs = m.getManagedIDs();
        ArrayList<Employee> employeeList = new ArrayList<Employee>();
        for(Integer id : managedIDs) employeeList.add(Employee.find(id));
        i.setCollection(employeeList);
        Employee currentEmployee;
        while(i.hasMore()){
            currentEmployee = i.getNext();
            s += visit(currentEmployee, depth + 1);
        }
        return s;
    }
}
