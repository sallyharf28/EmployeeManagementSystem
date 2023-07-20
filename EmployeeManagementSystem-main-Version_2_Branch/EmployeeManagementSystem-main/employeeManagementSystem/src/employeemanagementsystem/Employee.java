package employeemanagementsystem;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Employee{

    protected static final String[] ManagerPositions = {"Manager", "Assistant Manager", "Director", "Executive", "Team Lead"};
    protected static final List<String> ManagerPositionsList = Arrays.asList(ManagerPositions);
    protected static Connection connect = database.connectDb();
    protected static ResultSet result;
    protected static PreparedStatement prepare;

    protected Integer employeeId;
    protected String firstName;
    protected String lastName;
    protected String gender;
    protected String phoneNum;
    protected String position;
    protected Integer manager;
    protected String image;
    protected Date date;
    protected Integer salary;
    protected ArrayList<Integer> managedIDs = new ArrayList<Integer>();

    public String accept(Visitor v){
        return v.visit(this);
    }
    public static Employee find(Integer id){
        if(id <= 0) return null;
        String sql = "SELECT * FROM employee WHERE employee_id = " + id ;

        try{
            Alert alert;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if(result.next()){

                if (ManagerPositionsList.contains(result.getString("position"))) {
                    return new Manager(result.getInt("employee_id"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("gender"), result.getString("phoneNum"),
                        result.getString("position"), result.getInt("manager"),
                        result.getString("image"), result.getDate("date"));
                }
                else{
                    return new Contributor(result.getInt("employee_id"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("gender"), result.getString("phoneNum"),
                        result.getString("position"), result.getInt("manager"),
                        result.getString("image"), result.getDate("date"));
                }
            }
            else return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static void removeManager(Integer id){
        if(id <= 0 || find(id)==null) return;
        String sql1 = "UPDATE employee SET manager = 0 WHERE employee_id = " + id ;
        String sql2 = "UPDATE employee_info SET manager = 0 WHERE employee_id = " + id ;

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

    public Boolean isManager(){
        if(ManagerPositionsList.contains(this.position)){
            return true;
        }
        else{
            return false;
        }
    }

    public Integer getEmployeeId(){
        return employeeId;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getGender(){
        return gender;
    }
    public String getPhoneNum(){
        return phoneNum;
    }
    public String getPosition(){
        return position;
    }
    public String getImage(){
        return image;
    }
    public Date getDate() {
        if (date != null) return date;
        String sql = "SELECT * FROM employee_info WHERE employee_id = " + this.getEmployeeId();

        try {
            Alert alert;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                date = result.getDate("date");
                return date;
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Integer getSalary()
    {
        if(salary != null) return salary;
        String sql = "SELECT * FROM employee_info WHERE employee_id = " + this.getEmployeeId();

        try{
            Alert alert;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if(result.next()){
                salary = result.getInt("salary");
                return salary;
            }
            else return 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public Integer getManager() {
        return manager;
    }
}