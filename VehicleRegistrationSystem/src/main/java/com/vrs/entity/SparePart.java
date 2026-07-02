package com.vrs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "spare_parts")
public class SparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "part_number", unique = true, nullable = false)
    private String partNumber;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    public SparePart() {
    }

    public SparePart(String name, String partNumber, Vehicle vehicle) {
        this.name = name;
        this.partNumber = partNumber;
        this.vehicle = vehicle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "SparePart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", partNumber='" + partNumber + '\'' +
                ", vehicle=" + (vehicle != null ? vehicle.getNumber() : null) +
                '}';
    }
}
