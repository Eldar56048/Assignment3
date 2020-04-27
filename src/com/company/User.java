package com.company;

public class User {
    // id (you need to generate this id by static member variable)
    // name, surname
    // username
    // password
    private int id,idGen=0;
    private String name,surname,username,password;
    public User(int id,String name,String surname,String username,String password){
        setId(id);
        setName(name);
        setSurname(surname);
        setUsername(username);
        setPassword(password);
    }


    public User(String account,int lastid,String name,String surname,String username,String password) {
        idGen=++lastid;
        id = idGen;
        setName(name);
        setSurname(surname);
        setUsername(username);
        setPassword(password);
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void setSurname(String surname){
        this.surname=surname;
    }

    public String getSurname(){
        return surname;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getPassword(){
        return password;
    }


    @Override
    public String toString() {
        return id+" "+name+" "+surname+" "+username+" "+password;
    }
}
