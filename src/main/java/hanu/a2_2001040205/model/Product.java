package hanu.a2_2001040205.model;

public class Product {
    private int idOfTheProduct;
    private String imageOfUrl;
    private String nameOfTheProduct;
    private int priceOfTheProduct;
    private int quantityOfTheProduct;
//    private String categoryOfProduct;

    public Product(int idOfTheProduct, String imageOfUrl, String nameOfTheProduct, int priceOfTheProduct, int quantityOfTheProduct) {
        this.idOfTheProduct = idOfTheProduct;
        this.imageOfUrl = imageOfUrl;
        this.nameOfTheProduct = nameOfTheProduct;
        this.priceOfTheProduct = priceOfTheProduct;
        this.quantityOfTheProduct = quantityOfTheProduct;
//        this.categoryOfProduct = categoryOfProduct;
    }

    public Product(int idOfTheProduct, String imageOfUrl, String nameOfTheProduct, int priceOfTheProduct) {
        this.idOfTheProduct = idOfTheProduct;
        this.imageOfUrl = imageOfUrl;
        this.nameOfTheProduct = nameOfTheProduct;
        this.priceOfTheProduct = priceOfTheProduct;
//        this.categoryOfProduct = categoryOfProduct;
    }

    public Product() {
    }

    public int getId() {
        return idOfTheProduct;
    }

    public void setId(int idOfTheProduct) {
        this.idOfTheProduct = idOfTheProduct;
    }

    public String getName() {
        return nameOfTheProduct;
    }

    public void setName(String nameOfTheProduct) {
        this.nameOfTheProduct = nameOfTheProduct;
    }

    public String getImageUrl() {
        return imageOfUrl;
    }

    public void setImageUrl(String imageOfUrl) {
        this.imageOfUrl = imageOfUrl;
    }

    public int getQuantity() {
        return quantityOfTheProduct;
    }

//    public String getCategory() {
//        return categoryOfProduct;
//    }
//
//    public void setCategory(String categoryOfProduct) {
//        this.categoryOfProduct = categoryOfProduct;
//    }

    public void setQuantity(int quantityOfTheProduct) {
        this.quantityOfTheProduct = quantityOfTheProduct;
    }

    public int getPrice() {
        return priceOfTheProduct;
    }

    public void setPrice(int priceOfTheProduct) {
        this.priceOfTheProduct = priceOfTheProduct;
    }


}

