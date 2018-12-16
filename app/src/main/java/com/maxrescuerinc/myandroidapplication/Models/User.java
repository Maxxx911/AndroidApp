package com.maxrescuerinc.myandroidapplication.Models;

import com.orm.SugarRecord;

public class User extends SugarRecord {
    public String Name = null;
    public String LastName = null;
    public String Email = null;
    public String PhoneNumber= null;
    public String URI = null;
    public String Password = null;
    public User()
    {

    }
    public User(String name, String lastName, String email, String phoneNumber, String password, String uri)
    {
        this.URI = uri;
        this.Name = name;
        this.LastName = lastName;
        this.Email = email;
        this.Password = password;
        this.PhoneNumber = phoneNumber;
    }
    public User(String name, String lastName, String email, String phoneNumber, String password)
    {
        this.Name = name;
        this.LastName = lastName;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.Password = password;
    }
}
