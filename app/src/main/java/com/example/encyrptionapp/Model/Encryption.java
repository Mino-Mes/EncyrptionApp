package com.example.encyrptionapp.Model;

public class Encryption {
    private int id;
    private String key;
    private String value;
    private String encrypted;

    public Encryption(int id, String key, String value, String encrypted)
    {
        this.id=id;
        this.key=key;
        this.value=value;
        this.encrypted=encrypted;
    }
    public Encryption()
    {

    }

    public Encryption( String key, String value, String encrypted)
    {
        this.key=key;
        this.value=value;
        this.encrypted=encrypted;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }


}
