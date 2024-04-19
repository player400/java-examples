package org.example;

import java.io.Serializable;

public class Message implements Serializable {

    private int number;
    private String text;

    public int getNumber()
    {
        return number;
    }

    public String getText() {
        return text;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setText(String text) {
        this.text = text;
    }
}
