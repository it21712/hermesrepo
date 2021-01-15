package gr.convr.hermes.core.resources;

public class RetailProduct {

    private String id;
    private String name;
    private double price;

    public RetailProduct(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
