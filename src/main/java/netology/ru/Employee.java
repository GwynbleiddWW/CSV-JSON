package netology.ru;


public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {
    }

    public Employee(long id) {
        this.id = id;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "Id = " + id +
                ", FirstName:'" + firstName + '\'' +
                ", LastName:'" + lastName + '\'' +
                ", Country:'" + country + '\'' +
                ", age = " + age +
                '}';
    }
}

