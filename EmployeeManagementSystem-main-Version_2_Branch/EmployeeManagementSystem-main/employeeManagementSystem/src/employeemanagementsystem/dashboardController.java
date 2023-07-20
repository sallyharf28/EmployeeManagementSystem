
package employeemanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class dashboardController implements Initializable {

    private String[] positionList = {"Executive", "Director", "Manager", "Assistant Manager", "Team Lead", "Accountant", "Marketing Coordinator", "Business Analyst", "Web Developer", "Software Engineer", "Data Scientist"};
    protected static final String[] ManagerPositions = {"Executive", "Director", "Manager", "Assistant Manager", "Team Lead"};
    protected static final List<String> ManagerPositionsList = Arrays.asList(ManagerPositions);
    protected static final Integer[] raisePercentages = {0, 5, 10, 15, 20, 25};

    protected static final String[] orderList = {"By Name", "By Salary", "By Employment Date"};
    private Connection connect = database.connectDb();

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane salary_treeform;

    @FXML
    private Label salary_treelabel;


    @FXML
    private Label username;

    @FXML
    private Button home_btn;

    @FXML
    private Button addEmployee_btn;

    @FXML
    private Button salary_btn;


    @FXML
    private ComboBox<String> salary_raiseBtn;

    @FXML
    private Label salary_total_salary;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalEmployees;

    @FXML
    private Label home_totalPresents;

    @FXML
    private Label home_totalInactiveEm;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane addEmployee_form;

    @FXML
    private TableView<Employee> addEmployee_tableView;

    @FXML
    private TableColumn<Employee, String> addEmployee_col_employeeID;

    @FXML
    private TableColumn<Employee, String> addEmployee_col_firstName;

    @FXML
    private TableColumn<Employee, String> addEmployee_col_lastName;

    @FXML
    private TableColumn<Employee, String> addEmployee_col_gender;

    @FXML
    private TableColumn<Employee, String> addEmployee_col_phoneNum;

    @FXML
    private TableColumn<Employee, String> addEmployee_col_position;

    @FXML
    private TableColumn<Employee, String> addEmployee_col_manager;

    @FXML
    private TableColumn<Employee, String> addEmployee_col_date;

    @FXML
    private TextField addEmployee_search;

    @FXML
    private TextField salary_search;

    @FXML
    private TextField addEmployee_employeeID;

    @FXML
    private TextField addEmployee_firstName;

    @FXML
    private TextField addEmployee_lastName;

    @FXML
    private ComboBox<String> addEmployee_gender;

    @FXML
    private TextField addEmployee_phoneNum;

    @FXML
    private ComboBox<String> addEmployee_position;

    @FXML
    private ComboBox<String> addEmployee_manager;

    @FXML
    private ImageView addEmployee_image;

    @FXML
    private AnchorPane salary_form;

    @FXML
    private AnchorPane salary_manager_pane;

    @FXML
    private ComboBox<String> salary_print;

    @FXML
    private Label salary_employeeID;

    @FXML
    private Label salary_firstName;

    @FXML
    private Label salary_lastName;

    @FXML
    private Label salary_position;

    @FXML
    private Label salary_manager;

    @FXML
    private TextField salary_salary;

    @FXML
    private TableView<Employee> salary_tableView;

    @FXML
    private TableColumn<Employee, String> salary_col_employeeID;

    @FXML
    private TableColumn<Employee, String> salary_col_firstName;

    @FXML
    private TableColumn<Employee, String> salary_col_lastName;

    @FXML
    private TableColumn<Employee, String> salary_col_position;

    @FXML
    private TableColumn<Employee, String> salary_col_salary;

    @FXML
    private TableColumn<Employee, String> salary_col_manager;






    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;

    private Image image;



    public void homeTotalEmployees() {

        String sql = "SELECT COUNT(id) FROM employee";

        int countData = 0;
        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(id)");
            }

            home_totalEmployees.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void homeEmployeeTotalPresent() {

        String sql = "SELECT COUNT(id) FROM employee_info";


        int countData = 0;
        try {
            statement = connect.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                countData = result.getInt("COUNT(id)");
            }
            home_totalPresents.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void homeTotalInactive() {

        String sql = "SELECT COUNT(id) FROM employee_info WHERE salary = '0.0'";


        int countData = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(id)");
            }
            home_totalInactiveEm.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void homeChart() {

        home_chart.getData().clear();

        String sql = "SELECT date, COUNT(id) FROM employee GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 7";


        try {
            XYChart.Series chart = new XYChart.Series();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            home_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeDelete() {

        //Get old position
        String oldPosition = "";
        String getOldPositionQuery = "SELECT * FROM employee WHERE employee_id = '"
                + addEmployee_employeeID.getText() + "'";
        try{
            statement = connect.createStatement();
            result = statement.executeQuery(getOldPositionQuery);
            if(result.next()){
                oldPosition = result.getString("position");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "DELETE FROM employee WHERE employee_id = '"
                + addEmployee_employeeID.getText() + "'";

        try {

            Alert alert;
            if (addEmployee_employeeID.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please enter an ID");
                alert.showAndWait();
            } else if (Employee.find(Integer.parseInt(addEmployee_employeeID.getText().toString())) == null){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Employee ID doesn't exist");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Employee ID: " + addEmployee_employeeID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    if(ManagerPositionsList.contains(oldPosition)){
                        alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle("Cofirmation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("You are trying to delete Employee ID: " + addEmployee_employeeID.getText() + " who is a manager" +
                                "Proceeding will relocate all this employee's subordinates to this employee's manager");
                        option = alert.showAndWait();

                        if(option.get().equals(ButtonType.OK) == false){
                            return;
                        }
                        else{
                            Manager m = (Manager) Employee.find(Integer.parseInt(addEmployee_employeeID.getText()));
                            m.relocateManaged(m.getManager());
                        }
                    }


                    Employee e = Employee.find(Integer.parseInt(addEmployee_employeeID.getText().toString()));
                    if(e.getClass().equals(Manager.class)) ((Manager) e).removeManagement();


                    addEmployeeManagerList();

                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    String deleteInfo = "DELETE FROM employee_info WHERE employee_id = '"
                            + addEmployee_employeeID.getText() + "'";

                    prepare = connect.prepareStatement(deleteInfo);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();
                    salarySearch();
                    salaryShowListData();
                    addEmployeeSearch();
                    addEmployeeListData();
                    addEmployeeShowListData();
                    addEmployeeManagerList();
                    addEmployeeReset();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void addEmployeeAdd() {

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO employee "
                + "(employee_id,firstName,lastName,gender,phoneNum,position,manager,image,date) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        String employeeGender = addEmployee_gender.getSelectionModel().getSelectedItem();
        if(employeeGender == null) employeeGender = addEmployee_gender.getPromptText();
        if(addEmployee_gender.getItems().contains(employeeGender) == false) employeeGender = null;

        String employeePosition = addEmployee_position.getSelectionModel().getSelectedItem();
        if(employeePosition == null) employeePosition = addEmployee_position.getPromptText();
        if(addEmployee_position.getItems().contains(employeePosition) == false) employeePosition = null;

        String employeeManager = addEmployee_manager.getSelectionModel().getSelectedItem();
        if(employeeManager == null) employeeManager = addEmployee_manager.getPromptText();
        if((addEmployee_manager.getItems().contains(employeeManager) == false) || (employeeManager.equals("No Manager"))){
            employeeManager = null;
        }

        try {
            Alert alert;
            if (addEmployee_employeeID.getText().isEmpty()
                    || addEmployee_firstName.getText().isEmpty()
                    || addEmployee_lastName.getText().isEmpty()
                    || employeeGender == null
                    || addEmployee_phoneNum.getText().isEmpty()
                    || employeePosition == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else if(Integer.parseInt(addEmployee_employeeID.getText().toString()) <= 0){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("ID can't be 0 nor negative");
                alert.showAndWait();
            }
            else{

                String check = "SELECT employee_id FROM employee WHERE employee_id = '"
                        + addEmployee_employeeID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID: " + addEmployee_employeeID.getText() + " already exists!");
                    alert.showAndWait();
                } else {
                    int managerId;
                    if(employeeManager != null) managerId = Integer.parseInt(employeeManager.replaceAll("\\D+", ""));
                    else managerId = 0;

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, addEmployee_employeeID.getText());
                    prepare.setString(2, addEmployee_firstName.getText());
                    prepare.setString(3, addEmployee_lastName.getText());
                    prepare.setString(4, (String) employeeGender);
                    prepare.setString(5, addEmployee_phoneNum.getText());
                    prepare.setString(6, (String) employeePosition);
                    prepare.setInt(7, managerId);

                    String uri = getData.path;
                    if(uri != null) uri = uri.replace("\\", "\\\\");

                    prepare.setString(8, uri);
                    prepare.setString(9, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    String insertInfo = "INSERT INTO employee_info "
                            + "(employee_id,firstName,lastName,position,manager,salary,date) "
                            + "VALUES(?,?,?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertInfo);
                    prepare.setString(1, addEmployee_employeeID.getText());
                    prepare.setString(2, addEmployee_firstName.getText());
                    prepare.setString(3, addEmployee_lastName.getText());
                    prepare.setString(4, (String) employeePosition);
                    prepare.setInt(5, managerId);
                    prepare.setString(6, "0.0");
                    prepare.setString(7, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();
                    addEmployeeManagerList();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeUpdate() {

        String uri = getData.path;
        if(uri != null) uri = uri.replace("\\", "\\\\");

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String employeeGender = addEmployee_gender.getSelectionModel().getSelectedItem();
        if(employeeGender == null) employeeGender = addEmployee_gender.getPromptText();
        if(addEmployee_gender.getItems().contains(employeeGender) == false) employeeGender = null;

        String employeePosition = addEmployee_position.getSelectionModel().getSelectedItem();
        if(employeePosition == null) employeePosition = addEmployee_position.getPromptText();
        if(addEmployee_position.getItems().contains(employeePosition) == false) employeePosition = null;

        String employeeManager = addEmployee_manager.getSelectionModel().getSelectedItem();
        if(employeeManager == null) employeeManager = addEmployee_manager.getPromptText();
        if(addEmployee_manager.getItems().contains(employeeManager) == false || employeeManager.equals("No Manager"))
            employeeManager = null;

        int managerID = 0;
        if(employeeManager != null) managerID = Integer.parseInt(employeeManager.replaceAll("\\D+", ""));

        //Get old position
        String oldPosition = "";
        String getOldPositionQuery = "SELECT * FROM employee WHERE employee_id = '"
                + addEmployee_employeeID.getText() + "'";
        try{
            statement = connect.createStatement();
            result = statement.executeQuery(getOldPositionQuery);
            if(result.next()){
                oldPosition = result.getString("position");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }



        String sql = "UPDATE employee SET firstName = '"
                + addEmployee_firstName.getText() + "', lastName = '"
                + addEmployee_lastName.getText() + "', gender = '"
                + employeeGender + "', phoneNum = '"
                + addEmployee_phoneNum.getText() + "', position = '"
                + employeePosition + "', manager = '"
                + managerID + "', image = '"
                + uri + "' WHERE employee_id ='"
                + addEmployee_employeeID.getText() + "'";

        try {
            Alert alert;
            if (addEmployee_employeeID.getText().isEmpty()
                    || addEmployee_firstName.getText().isEmpty()
                    || addEmployee_lastName.getText().isEmpty()
                    || employeeGender == null
                    || addEmployee_phoneNum.getText().isEmpty()
                    || employeePosition == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String check = "SELECT employee_id FROM employee WHERE employee_id = '"
                        + addEmployee_employeeID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (!result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID: " + addEmployee_employeeID.getText() + " doesn't exist!");
                    alert.showAndWait();
                }
                else {

                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Cofirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to UPDATE Employee ID: " + addEmployee_employeeID.getText() + "?");
                    Optional<ButtonType> option = alert.showAndWait();


                    if (option.get().equals(ButtonType.OK)) {
                        if(ManagerPositionsList.contains(oldPosition)){
                            if(ManagerPositionsList.contains(employeePosition) == false){
                                alert = new Alert(AlertType.CONFIRMATION);
                                alert.setTitle("Cofirmation Message");
                                alert.setHeaderText(null);
                                alert.setContentText("You are trying to change Employee ID: " + addEmployee_employeeID.getText() + " 's position from" +
                                        "a management role to a non-management role. Proceeding will relocate all this employee's subordinates to his/her manager");
                                option = alert.showAndWait();

                                if(option.get().equals(ButtonType.OK) == false){
                                    return;
                                }
                                else{
                                    Manager m = (Manager) Employee.find(Integer.parseInt(addEmployee_employeeID.getText()));
                                    m.relocateManaged(m.getManager());
                                }
                            }
                        }

                        statement = connect.createStatement();
                        statement.executeUpdate(sql);

                        int salary = 0;

                        String checkData = "SELECT * FROM employee_info WHERE employee_id = '"
                                + addEmployee_employeeID.getText() + "'";

                        prepare = connect.prepareStatement(checkData);
                        result = prepare.executeQuery();

                        while (result.next()) {
                            salary = result.getInt("salary");
                        }

                        String updateInfo = "UPDATE employee_info SET firstName = '"
                                + addEmployee_firstName.getText() + "', lastName = '"
                                + addEmployee_lastName.getText() + "', position = '"
                                + employeePosition + "', manager = '" + managerID
                                + "' WHERE employee_id = '"
                                + addEmployee_employeeID.getText() + "'";

                        prepare = connect.prepareStatement(updateInfo);
                        prepare.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated!");
                        alert.showAndWait();

                        addEmployeeShowListData();
                        addEmployeeReset();
                        addEmployeeManagerList();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void salaryDelete() {

        String sql = "DELETE FROM employee WHERE employee_id = '"
                + salary_employeeID.getText() + "'";

        try {

            Alert alert;
            if (salary_employeeID.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please enter an ID");
                alert.showAndWait();
            } else if (Employee.find(Integer.parseInt(salary_employeeID.getText().toString())) == null){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Employee ID doesn't exist");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Employee ID: " + salary_employeeID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    Employee e = Employee.find(Integer.parseInt(salary_employeeID.getText().toString()));
                    if(e.getClass().equals(Manager.class)) ((Manager) e).removeManagement();


                    addEmployeeManagerList();

                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    String deleteInfo = "DELETE FROM employee_info WHERE employee_id = '"
                            + salary_employeeID.getText() + "'";

                    prepare = connect.prepareStatement(deleteInfo);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();
                    salaryReset();
                    salaryShowListData();
                    salarySearch();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeReset() {
        addEmployee_employeeID.setText("");
        addEmployee_firstName.setText("");
        addEmployee_lastName.setText("");
        addEmployee_gender.getSelectionModel().clearSelection();
        addEmployee_position.getSelectionModel().clearSelection();
        addEmployee_manager.getSelectionModel().clearSelection();
        addEmployee_phoneNum.setText("");
        addEmployee_image.setImage(null);
        getData.path = "";
        addEmployee_gender.setPromptText("choose");
        addEmployee_position.setPromptText("choose");
        addEmployee_manager.setPromptText("choose");
    }

    public void addEmployeeInsertImage(ActionEvent e) {

        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            getData.path = file.getAbsolutePath();

            //image = new Image(file.toURI().toString(), 101, 127, false, true);
            image = new Image(file.toURI().toString(),58,98,false, true);
            addEmployee_image.setImage(image);
        }
    }

    public void addEmployeePositionList() {
        List<String> listP = new ArrayList<>();

        for (String data : positionList) {
            listP.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listP);
        addEmployee_position.setItems(listData);
    }

    public void salaryRaiseList() {
        List<String> listS = new ArrayList<>();

        for (Integer data : raisePercentages) {
            listS.add(data + "%");
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        salary_raiseBtn.setItems(listData);
    }

    public void addEmployeeManagerList(){
        String sql = "SELECT * FROM employee";
        ArrayList<String> potentialManagers = new ArrayList<String>();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            Employee potentialManager;

            potentialManagers.add("No Manager");
            while (result.next()) {
                if(ManagerPositionsList.contains(result.getString("position"))){
                    potentialManager = new Manager(result.getInt("employee_id"),
                            result.getString("firstName"));
                    potentialManagers.add(potentialManager.getFirstName() + " (ID: " + potentialManager.getEmployeeId() + ")");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ObservableList listPotentialManagers = FXCollections.observableArrayList(potentialManagers);
        addEmployee_manager.setItems(listPotentialManagers);
    }

    public void addEmployeeManagerList(Integer id){
        String sql = "SELECT * FROM employee";

        ArrayList<String> potentialManagers = new ArrayList<String>();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            Employee potentialManager;

            potentialManagers.add("No Manager");
            while (result.next()) {
                if(id != result.getInt("employee_id")){

                    if(ManagerPositionsList.contains(result.getString("position"))){
                        potentialManager = new Manager(result.getInt("employee_id"),
                                result.getString("firstName"));
                        potentialManagers.add(potentialManager.getFirstName() + " (ID: " + potentialManager.getEmployeeId() + ")");
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ObservableList listPotentialManagers = FXCollections.observableArrayList(potentialManagers);
        addEmployee_manager.setItems(listPotentialManagers);
    }

    private String[] listGender = {"Male", "Female", "Other"};
    public void addEmployeeGendernList() {
        List<String> listG = new ArrayList<>();

        for (String data : listGender) {
            listG.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listG);

        addEmployee_gender.setItems(listData);
    }


    public void addEmployeeSearch() {


        FilteredList<Employee> filteredList = new FilteredList<>(addEmployeeList, e -> true);


        SortedList sortedList = new SortedList<>(filteredList);


        sortedList.comparatorProperty().bind(addEmployee_tableView.comparatorProperty());


        addEmployee_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(predicateEmployee -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();


                if (predicateEmployee.getEmployeeId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateEmployee.getFirstName().toLowerCase().startsWith(searchKey)) {
                    return true;
                } else if (predicateEmployee.getLastName().toLowerCase().startsWith(searchKey)) {
                    return true;
                } else if (predicateEmployee.getGender().toLowerCase().startsWith(searchKey)) {
                    return true;
                } else if (predicateEmployee.getPhoneNum().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployee.getPosition().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployee.getManager().toString().contains(searchKey)) {
                    return true;
                } else if (predicateEmployee.getDate().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });


            addEmployee_tableView.getSelectionModel().clearSelection();


            addEmployee_tableView.setItems(sortedList);
        });
    }

    public void salarySearch() {


        FilteredList<Employee> filteredList = new FilteredList<>(salaryList, e -> true);


        SortedList sortedList = new SortedList<>(filteredList);


        sortedList.comparatorProperty().bind(salary_tableView.comparatorProperty());


        salary_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(predicateEmployee -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateEmployee.getEmployeeId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateEmployee.getFirstName().toLowerCase().startsWith(searchKey)) {
                    return true;
                } else if (predicateEmployee.getLastName().toLowerCase().startsWith(searchKey)) {
                    return true;
                } else if (predicateEmployee.getPosition().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployee.getManager().toString().contains(searchKey)) {
                    return true;
                } else if (predicateEmployee.getSalary().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });

            // Clear previous selection
            salary_tableView.getSelectionModel().clearSelection();

            salary_tableView.setItems(sortedList);
        });
    }


    public ObservableList<Employee> addEmployeeListData() {

        ObservableList<Employee> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM employee";


        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            Employee employeeD;

            while (result.next()) {
                if(ManagerPositionsList.contains(result.getString("position"))){
                    employeeD = new Manager(result.getInt("employee_id"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("gender"),
                        result.getString("phoneNum"),
                        result.getString("position"),
                        result.getInt("manager"),
                        result.getString("image"),
                        result.getDate("date"));
                }
                else{
                    employeeD = new Contributor(result.getInt("employee_id"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("gender"),
                        result.getString("phoneNum"),
                        result.getString("position"),
                        result.getInt("manager"),
                        result.getString("image"),
                        result.getDate("date"));
                }
                listData.add(employeeD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<Employee> addEmployeeList;

    public void addEmployeeShowListData() {
        addEmployeeList = addEmployeeListData();
        addEmployee_col_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        addEmployee_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        addEmployee_col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addEmployee_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addEmployee_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        addEmployee_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        addEmployee_col_manager.setCellValueFactory(new PropertyValueFactory<>("manager"));
        addEmployee_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEmployee_tableView.setItems(addEmployeeList);

    }

    public void addEmployeeSelect() {
        Employee employeeD = addEmployee_tableView.getSelectionModel().getSelectedItem();
        int num = addEmployee_tableView.getSelectionModel().getSelectedIndex();

        if (((num - 1) < -1) || employeeD==null) {
            return;
        }

        addEmployee_employeeID.setText(String.valueOf(employeeD.getEmployeeId()));
        addEmployee_firstName.setText(employeeD.getFirstName());
        addEmployee_lastName.setText(employeeD.getLastName());
        addEmployee_phoneNum.setText(employeeD.getPhoneNum());
        addEmployee_gender.setPromptText(employeeD.getGender());
        addEmployee_position.setPromptText(employeeD.getPosition());

        addEmployeeManagerList(Integer.parseInt(addEmployee_employeeID.getText().toString()));
        //Manager set prompt
        Integer managerID = employeeD.getManager();
        Employee managerObject = Employee.find(managerID);
        if(managerObject != null){
            String managerString = managerObject.getFirstName() + " (ID: " + managerID + ")";
            addEmployee_manager.setPromptText(managerString);
        }
        else{
           // addEmployee_manager.getSelectionModel().clearSelection();
           // addEmployee_manager.setPromptText("choose");
            addEmployee_manager.setPromptText("No Manager");
        }

        getData.path = employeeD.getImage();

        String uri = "file:" + employeeD.getImage();

        image = new Image(uri, 101, 127, false, true);
        addEmployee_image.setImage(image);
    }

    public void salaryUpdate() {

        String sql = "UPDATE employee_info SET salary = '" + salary_salary.getText()
                + "' WHERE employee_id = '" + salary_employeeID.getText().toString() + "'";


        try {
            Alert alert;

            if (salary_employeeID.getText().isEmpty()
                    || salary_firstName.getText().isEmpty()
                    || salary_lastName.getText().isEmpty()
                    || salary_position.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select item first");
                alert.showAndWait();
            } else {
                statement = connect.createStatement();
                statement.executeUpdate(sql);
                Integer index = salary_tableView.getSelectionModel().getSelectedIndex();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                salaryShowListData();
                salary_tableView.getSelectionModel().select(index);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printTree(){
        String order = salary_print.getSelectionModel().getSelectedItem();
        if(order == null){
            Alert alert;
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter a valid printing method !");
            alert.showAndWait();
            return;
        }

        Employee e = salary_tableView.getSelectionModel().getSelectedItem();
        if(e == null){
            Alert alert;
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Select a manager from the table !");
            alert.showAndWait();
            return;
        }
        Visitor printVisitor = new PrintHierarchyVisitor(order);
        salary_treeform.setVisible(true);
        salary_treelabel.setText(e.accept(printVisitor));
    }


    public void salaryBack(){
        salary_treeform.setVisible(false);
        salaryShowListData();
    }


    public void salaryReset() {
        salary_employeeID.setText("");
        salary_firstName.setText("");
        salary_lastName.setText("");
        salary_position.setText("");
        salary_salary.setText("");
        salary_manager.setText("");
    }

    public ObservableList<Employee> salaryListData() {

        ObservableList<Employee> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM employee_info";

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            Employee employeeD;

            while (result.next()) {
                if(ManagerPositionsList.contains(result.getString("position"))){
                    employeeD = new Manager(result.getInt("employee_id"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("position"),
                        result.getInt("manager"),
                        result.getInt("salary"));
                }
                else{
                    employeeD = new Contributor(result.getInt("employee_id"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("position"),
                        result.getInt("manager"),
                        result.getInt("salary"));
                }

                listData.add(employeeD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<Employee> salaryList;

    public void salaryShowListData() {
        salaryList = salaryListData();

        salary_col_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        salary_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        salary_col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        salary_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        salary_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salary_col_manager.setCellValueFactory(new PropertyValueFactory<>("manager"));

        salary_tableView.setItems(salaryList);

    }

    public void salarySelect() {
        int managerId;
        Employee employeeD = salary_tableView.getSelectionModel().getSelectedItem();
        if((employeeD==null) || (employeeD.getManager() == null) ) managerId=0;
        else managerId = employeeD.getManager();
        Employee manager = Employee.find(managerId);
        int num = salary_tableView.getSelectionModel().getSelectedIndex();

        if (((num - 1) < -1) || employeeD==null) {
            return;
        }

        salary_employeeID.setText(String.valueOf(employeeD.getEmployeeId()));
        salary_firstName.setText(employeeD.getFirstName());
        salary_lastName.setText(employeeD.getLastName());
        salary_position.setText(employeeD.getPosition());
        salary_salary.setText(String.valueOf(employeeD.getSalary()));

        if(manager==null){
            salary_manager.setText("No Manager");
        }
        else{
            salary_manager.setText(manager.getFirstName() +" (ID: "+manager.getEmployeeId() +")");
        }

        if(employeeD.isManager()) {
            salary_manager_pane.setVisible(true);
            Visitor showTotalVisitor = new TotalSalaryVisitor();
            salary_total_salary.setText(employeeD.accept(showTotalVisitor).toString());
        }
        else
            salary_manager_pane.setVisible(false);
    }

    public void defaultNav() {
        home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right,#10E7DC, #0074e1);");
    }

    public void displayUsername() {
        username.setText(getData.username);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #10E7DC, #0074e1);");
            addEmployee_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            homeTotalEmployees();
            homeEmployeeTotalPresent();
            homeTotalInactive();
            homeChart();

        } else if (event.getSource() == addEmployee_btn) {
            home_form.setVisible(false);
            addEmployee_form.setVisible(true);
            salary_form.setVisible(false);

            addEmployee_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #10E7DC, #0074e1);");
            home_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            addEmployeeGendernList();
            addEmployeePositionList();
            addEmployeeManagerList();
            addEmployeeSearch();
            addEmployeeShowListData();

        } else if (event.getSource() == salary_btn) {
            home_form.setVisible(false);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(true);

            salary_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #10E7DC, #0074e1);");
            addEmployee_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");

            salaryShowListData();
            salarySearch();
            salary_manager_pane.setVisible(false);
            salary_treeform.setVisible(false);
        }
    }

    public void salaryOrderList(){
        List<String> listS = new ArrayList<>();

        for (String data : orderList) {
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        salary_print.setItems(listData);
    }

    public void raiseParty(){
        Employee e = salary_tableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cofirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to RAISE the salary of Employee ID: " + e.getEmployeeId() + " and all of their subordinates ?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get().equals(ButtonType.OK)) {
            Integer raise = Integer.parseInt(salary_raiseBtn.getSelectionModel().getSelectedItem().replaceAll("\\D+", ""));
            Integer index = salary_tableView.getSelectionModel().getSelectedIndex();
            Visitor raiseVisitor = new RaiseVisitor(raise);
            e.accept(raiseVisitor);
            salaryShowListData();
            salary_manager_pane.setVisible(true);
            salary_tableView.getSelectionModel().select(index);

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Raise Applied Successfully!");
            alert.showAndWait();
        }
        if (e == null) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a manager!");
            alert.showAndWait();
        }
    }

    private double x = 0;
    private double y = 0;

    public void logout() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        defaultNav();

        homeTotalEmployees();
        homeEmployeeTotalPresent();
        homeTotalInactive();
        homeChart();

        addEmployeeShowListData();
        addEmployeeGendernList();
        addEmployeePositionList();
        addEmployeeManagerList();

        salaryShowListData();
        salaryRaiseList();
        salaryOrderList();
    }

}