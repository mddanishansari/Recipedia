package com.md.recipedia.modal;


public class CategoryModal {
    String category_name;
    int category_banner;

    public CategoryModal() {
    }

    public CategoryModal(String category_name, int category_banner) {
        this.category_name = category_name;
        this.category_banner = category_banner;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_banner() {
        return category_banner;
    }

    public void setCategory_banner(int category_banner) {
        this.category_banner = category_banner;
    }
}
