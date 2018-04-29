package com.example.yangm89.grocerytracker;

public class ItemSpec {
    private String item ;
    private String price ;
    private String quantity ;
    private String protein ;
    private String carbs ;
    private String other ;
    private String fat ;
    private String category ;

    public ItemSpec(String item, String price, String quantity, String cat, String protein, String carbs, String fat, String other)
    {
       this.item = item ;
       this.price = price ;
       this.quantity = quantity ;
       this.protein = protein ;
       this.carbs = carbs ;
       this.other = other   ;
       this.category = cat ;
       this.fat = fat ;
    }

    public String getName()
    {
        return item ;
    }

    public String getPrice()
    {
        return price ;
    }

    public String getQuantity()
    {
        return quantity ;
    }

    public String getProtein()
    {
        return protein ;
    }

    public String getCarbs()
    {
        return carbs ;
    }

    public String getOther()
    {
        return other ;
    }

    public String getCategory()
    {
        return category ;
    }

    public String getFat()
    {
        return fat ;
    }
}
