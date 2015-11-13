package edu.cvtc.android.grocerylist_lchase1;


public class GroceryItem {

    private String itemName;
    private Integer quantity;
    private boolean itemInCart;


    public GroceryItem(String itemName) {
        this.itemName = itemName;
        this.quantity = 1;
        this.itemInCart = false;
    }

    public GroceryItem(String itemName, Integer quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemInCart = false;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isItemInCart() {
        return itemInCart;
    }

    public void setItemInCart(boolean itemInCart) {
        this.itemInCart = itemInCart;
    }

    @Override
    public String toString() {
        return getItemName();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GroceryItem && ((GroceryItem) obj).getItemName().equals(itemName);
    }
}
