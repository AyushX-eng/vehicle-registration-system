package com.vrs.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true, length = 255)
    private String model;

    @Column(name = "number", unique = true, nullable = false, length = 255)
    private String number;

    @Column(nullable = true, length = 255)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SparePart> spareParts = new ArrayList<>();

    public Vehicle() {
    }

    public Vehicle(String model, String number, String city, Customer customer) {
        this.model = model;
        this.number = number;
        this.city = city;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SparePart> getSpareParts() {
        return spareParts;
    }

    public void setSpareParts(List<SparePart> spareParts) {
        this.spareParts = spareParts;
    }

    public void addSparePart(SparePart sparePart) {
        spareParts.add(sparePart);
        sparePart.setVehicle(this);
    }

    public void removeSparePart(SparePart sparePart) {
        spareParts.remove(sparePart);
        sparePart.setVehicle(null);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", number='" + number + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
