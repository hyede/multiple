package com.yede.multiple.model;

import java.io.Serializable;

public class User implements Serializable {
    private  Long ID;
    private String USER_FULL_NAME;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getUSER_FULL_NAME() {
        return USER_FULL_NAME;
    }

    public void setUSER_FULL_NAME(String USER_FULL_NAME) {
        this.USER_FULL_NAME = USER_FULL_NAME;
    }
}
