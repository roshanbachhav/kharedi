package com.kharedi.model;


import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	private String title;
    private String Description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_Id" , referencedColumnName = "category_Id")
	private Category category;
	private int off;
    private double listPrice;
    private double ourPrice;
    private double starRating;
    private boolean active = true;
    private boolean featureProducts = true;

	private int stock;

    @Transient
    MultipartFile images;

	private String imageName;
	
    

	public boolean getFeatureProducts() {
		return featureProducts;
	}

	public void setFeatureProducts(boolean featureProducts) {
		this.featureProducts = featureProducts;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Product() {
		super();
	}

	public Product(int id, String title, String description, Category category, int off, double listPrice,
			double ourPrice, double starRating, boolean active, boolean featureProducts, int stock, MultipartFile images, String imageName) {
		super();
		this.id = id;
		this.title = title;
		Description = description;
		this.category = category;
		this.off = off;
		this.listPrice = listPrice;
		this.ourPrice = ourPrice;
		this.starRating = starRating;
		this.active = active;
		this.stock = stock;
		this.images = images;
		this.imageName = imageName;
		this.featureProducts = featureProducts;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getOff() {
		return off;
	}

	public void setOff(int off) {
		this.off = off;
	}

	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public double getOurPrice() {
		return ourPrice;
	}

	public void setOurPrice(double ourPrice) {
		this.ourPrice = ourPrice;
	}

	public double getStarRating() {
		return starRating;
	}

	public void setStarRating(double starRating) {
		this.starRating = starRating;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public MultipartFile getImages() {
		return images;
	}

	public void setImages(MultipartFile images) {
		this.images = images;
	}
    

  
}
