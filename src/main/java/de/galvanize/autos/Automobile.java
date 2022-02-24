package de.galvanize.autos;

import java.util.Objects;

public class Automobile {

    private int year;
    private String make;
    private String model;
    private String color;
    private String owner;
    private String vin;

    public Automobile(int year, String make, String model, String color, String owner, String vin) {
        this.year = year;
        this.make = make;
        this.model = model;
        this.color = color;
        this.owner = owner;
        this.vin = vin;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "Automobile{" +
                "year=" + year +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", owner='" + owner + '\'' +
                ", vin='" + vin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Automobile)) return false;
        Automobile that = (Automobile) o;
        return getYear() == that.getYear() && Objects.equals(getMake(), that.getMake()) && Objects.equals(getModel(), that.getModel()) && Objects.equals(getColor(), that.getColor()) && Objects.equals(getOwner(), that.getOwner()) && Objects.equals(getVin(), that.getVin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getMake(), getModel(), getColor(), getOwner(), getVin());
    }
}
