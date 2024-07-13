package com.kharedi.model;


import jakarta.persistence.*;

@Entity
public class Category {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_Id")
    private int id;

    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(){
        super();
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + "]";
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

    
}
