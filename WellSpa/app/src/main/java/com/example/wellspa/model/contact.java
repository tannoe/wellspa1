package com.example.wellspa.model;

public class contact {

    private String email,first_name,last_name,messages;

    public contact(){

    }

    public contact(String email, String first_name, String last_name, String messages) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.messages = messages;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
