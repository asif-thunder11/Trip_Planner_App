package org.thundercorp.tripplanner.DataModels;

public class User {
    String name;
    String mobile;
    String email;
    String password;
    int id, age;


    public int getId(){ return id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString(){
        String result="";
        result+="Name: "+name;
        result+="\nMobile: "+mobile;
        result+="\nEmail: "+email;
        result+="\nAge: "+age;
        return result;
    }



}
