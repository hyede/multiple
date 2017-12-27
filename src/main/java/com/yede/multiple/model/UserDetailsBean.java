package com.yede.multiple.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserDetailsBean  implements Serializable {

    private String UUID;
    private Long userId;
    private String userName;
    private String password;
    private String fullName;

}
