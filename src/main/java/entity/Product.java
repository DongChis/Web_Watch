package entity;


public class Product {
   
	private int productID;
    private String title;
    private String name;
    private String description;
    private double price;
    private String imageURL;
    private String gender;
    private int quantityP;

    public int getQuantityP() {
		return quantityP;
	}


	public void setQuantityP(int quantityP) {
		this.quantityP = quantityP;
	}


	public Product(int productID, String title, String name, String description, double price, String imageURL, String gender) {
        this.productID = productID;
        this.title = title;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.gender = gender;
    }


    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    @Override
   	public String toString() {
   		return "Product [productID=" + productID + ", title=" + title + ", name=" + name + ", description="
   				+ description + ", price=" + price + ", imageURL=" + imageURL + ", gender=" + gender + "]" +"\n";
   	}
}