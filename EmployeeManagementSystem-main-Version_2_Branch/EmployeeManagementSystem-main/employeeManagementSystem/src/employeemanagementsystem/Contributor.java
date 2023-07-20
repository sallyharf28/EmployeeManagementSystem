package employeemanagementsystem;

import java.sql.Date;

public class Contributor extends Employee{
    public Contributor(Integer employeeId, String firstName){
        this.employeeId = employeeId;
        this.firstName = firstName;
    }

    public Contributor(Integer employeeId, String firstName, String lastName, String gender, String phoneNum, String position, Integer manager, String image, Date date){
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
    public Contributor(Integer employeeId, String firstName, String lastName,String position, Integer manager, Integer salary){
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.manager = manager;
        this.salary = salary;
    }
}
