package com.jacotest.jacocotest.user.action;

public class AddAction {

    public  String addClass;

    public AddAction(String addClass) {
        this.addClass = addClass;
    }

    public String getAddClass() {
        return this.addClass;
    }

    public void setAddClass(String addClass) {
        this.addClass = addClass;
    }

    @Override
    public String toString() {
        return addClass.toString();
    }
}
