package pooop.android.sidedish;

public class Statistic {

    private String name;
    private int numSold;
    private float totalRevenue;

    public Statistic(String title, int unitsSold, float price) {
        name = title;
        numSold = unitsSold;
        totalRevenue = unitsSold * price;
    }

    public String getName() {
        return name;
    }

    public int getNumSold() {
        return numSold;
    }

    public float getTotalRevenue() {
        return totalRevenue;
    }
}
