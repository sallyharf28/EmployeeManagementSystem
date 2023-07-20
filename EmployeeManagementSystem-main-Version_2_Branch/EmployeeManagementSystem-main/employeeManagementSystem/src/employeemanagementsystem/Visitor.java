package employeemanagementsystem;

public abstract class Visitor {
    public String visit(Employee e){
        if(e.isManager()) return visit((Manager) e);
        else return visit((Contributor) e);
    }
    public abstract String visit(Contributor c);
    public abstract String visit(Manager m);

}
