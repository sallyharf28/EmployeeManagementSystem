package employeemanagementsystem;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class RaiseVisitor extends Visitor{

    private Integer bonus;
    private Connection connect;
    private PreparedStatement prepare;

    public RaiseVisitor(Integer bonus){
        this.bonus = bonus;
    }

    @Override
    public String visit(Contributor c) {
        Double newSalary = c.getSalary() * (1 + bonus/100.0);
        String sql = "UPDATE employee_info SET salary = " + newSalary + " WHERE employee_id = " + c.getEmployeeId() ;
        connect = database.connectDb();

        try{
            prepare = connect.prepareStatement(sql);
            prepare.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "1.0";
    }

    @Override
    public String visit(Manager m) {
        Double newSalary = m.getSalary() * (1 + bonus/100.0);
        String sql = "UPDATE employee_info SET salary = " + newSalary + " WHERE employee_id = " + m.getEmployeeId() ;
        connect = database.connectDb();

        try{
            prepare = connect.prepareStatement(sql);
            prepare.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Double totalRaised = 1.0;
        ArrayList<Integer> managedIDs = m.getManagedIDs();
        for(Integer i : managedIDs){
            visit(Employee.find(i));
            totalRaised++;
        }
        return totalRaised.toString();
    }
}
