package employeemanagementsystem;

import javafx.scene.control.Alert;

import java.sql.Date;
import java.util.ArrayList;

public class Manager extends Employee{
    public Manager(Integer employeeId, String firstName){
        this.employeeId = employeeId;
        this.firstName = firstName;
    }

    public Manager(Integer employeeId, String firstName, String lastName, String gender, String phoneNum, String position, Integer manager, String image, Date date){
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.position = position;
        this.manager = manager;
        this.image = image;
        this.date = date;
    }
    public Manager(Integer employeeId, String firstName, String lastName,String position, Integer manager, Integer salary){
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.manager = manager;
        this.salary = salary;
    }

    public ArrayList<Integer> getManagedIDs(){
        if(employeeId <= 0) return null;

        String sql = "SELECT * FROM employee WHERE manager = " + employeeId ;
        connect = database.connectDb();

        try{
            Alert alert;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            managedIDs.clear();
            while(result.next()){
                managedIDs.add(result.getInt("employee_id"));
            }
            return managedIDs;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void removeManagement(){
        ArrayList<Integer> managedIDs = this.getManagedIDs();
        for(Integer i : managedIDs){
            removeManager(i);
        }
    }

    public void relocateManaged(Integer newManagerID){
        ArrayList<Integer> managedIDs = this.getManagedIDs();
        for(Integer i : managedIDs){
            String sql1 = "UPDATE employee SET manager = " + newManagerID + " WHERE employee_id = " + i ;
            String sql2 = "UPDATE employee_info SET manager = " + newManagerID + " WHERE employee_id = " + i ;
            connect = database.connectDb();

            try{
                prepare = connect.prepareStatement(sql1);
                prepare.executeUpdate();
                prepare = connect.prepareStatement(sql2);
                prepare.executeUpdate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
