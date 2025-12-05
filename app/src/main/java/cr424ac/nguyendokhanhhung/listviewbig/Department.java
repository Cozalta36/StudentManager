package cr424ac.nguyendokhanhhung.listviewbig;

import java.io.Serializable;

public class Department implements Serializable {

    private int id;
    private String name;
    private String description;
    private String phone;

    public Department(int id, String name, String description, String phone) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phone = phone;
    }

    public Department(String name, String description, String phone) {
        this.name = name;
        this.description = description;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return this.name;
    }
}