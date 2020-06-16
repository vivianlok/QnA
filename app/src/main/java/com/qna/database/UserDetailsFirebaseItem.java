package com.qna.database;

public class UserDetailsFirebaseItem {
    String name;
    String email;
    String country;

    public UserDetailsFirebaseItem(){}

    public UserDetailsFirebaseItem(String name, String email, String country) {
        this.name = name;
        this.email = email;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
