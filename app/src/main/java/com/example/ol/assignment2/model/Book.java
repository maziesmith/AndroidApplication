package com.example.ol.assignment2.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String Title;
    private String Author;
    private String Genre;
    private String img;
    private String pdfDownload;
    private double price;
    private double Rating;
    private int Pages;
    private int Downloads;

    public Book(){
    }

    public Book(int id,String author,int downloads,String genre, String img,int pages, double price, double rating, String title, String pdfDownload) {
        this.img = img;
        this.id = id;
        this.Title = title;
        this.Author = author;
        this.Genre = genre;
        this.price = price;
        this.Rating = rating;
        this.Pages=pages;
        this.Downloads=downloads;
        this.pdfDownload = pdfDownload;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getGenre() {
        return Genre;
    }

    public double getPrice() {return price;}

    public double getRating() {return Rating; }

    public int getPages() {return Pages; }

    public int getDownloads() {return Downloads; }



    public void setImg(String img) { this.img = img; }

    public void setTitle(String title) {
        Title = title;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public void setPrice(double price) { this.price = price; }

    public void setRating(double rating) { Rating = rating; }

    public void setPages(int pages) { Pages = pages; }

    public void setDownloads(int downloads) { Downloads = downloads; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getPdfDownload() { return pdfDownload; }

    public void setPdfDownload(String pdfDownload) { this.pdfDownload = pdfDownload; }

    public Book getBook() {return this;}


    private Book(Parcel in){
        this.id = in.readInt();
        this.Title = in.readString();
        this.Author = in.readString();
        this.Genre = in.readString();
        this.img = in.readString();
        this.pdfDownload = in.readString();
        this.price = in.readDouble();
        this.Rating = in.readDouble();
        this.Pages = in.readInt();
        this.Downloads = in.readInt();

    }


}

