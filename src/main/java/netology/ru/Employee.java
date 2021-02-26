package netology.ru;

public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {
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

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }
}
