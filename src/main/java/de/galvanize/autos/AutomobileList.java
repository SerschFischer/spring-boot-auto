package de.galvanize.autos;

import java.util.List;
import java.util.Objects;

public class AutomobileList {
    private List<Automobile> automobiles;

    public AutomobileList(List<Automobile> automobiles) {
        this.automobiles = automobiles;
    }

    public List<Automobile> getAutomobiles() {
        return automobiles;
    }

    public void setAutomobiles(List<Automobile> automobiles) {
        this.automobiles = automobiles;
    }

    public boolean isEmpty(){
        return this.automobiles.isEmpty();
    }

    @Override
    public String toString() {
        return "AutoList{" +
                "automobiles=" + automobiles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AutomobileList)) return false;
        AutomobileList autoList = (AutomobileList) o;
        return Objects.equals(getAutomobiles(), autoList.getAutomobiles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAutomobiles());
    }
}
