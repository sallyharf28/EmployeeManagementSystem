package employeemanagementsystem;

public class SimpleIteratorFactory {
    String iterator;

    public SimpleIteratorFactory(){}
    public SimpleIteratorFactory(String iterator){
       this.iterator = iterator;
    }

    public Iterator getNewIterator(){
        switch(iterator){
            case("By Name") : return new NameIterator();
            case("By Salary") : return new SalaryIterator();
            case("By Employment Date") : return new DateIterator();
            default: return null;
        }
    }

    public void setIterator(String iterator){
        this.iterator = iterator;
    }
}
