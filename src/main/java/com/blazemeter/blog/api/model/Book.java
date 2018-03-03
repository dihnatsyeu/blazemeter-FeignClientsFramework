package com.blazemeter.blog.api.model;

public class Book {

    private String title;
    private String author;
    private int price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        Book compareTo = (Book)o;
        return title.equals(compareTo.getTitle())
                && author.equals(compareTo.getAuthor())
                &&price == compareTo.getPrice();
    }
}
