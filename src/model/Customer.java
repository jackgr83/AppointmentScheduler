package model;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String zipcode;
    private String phone;
    private String country;
    private String division;

    /**
     * This function initializes a new Customer
     */
    public Customer(int id, String name, String address, String zipcode, String phone, String country, String division) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phone = phone;
        this.country = country;
        this.division = division;
    }

    /**
     * Getter functions for all Customer attributes
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getDivision() {
        return division;
    }

}
