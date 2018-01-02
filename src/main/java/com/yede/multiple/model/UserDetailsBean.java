package com.yede.multiple.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsBean  extends GenericObject {

    private String UUID;
    private Long userId;
    private String userName;
    private String password;
    private String fullName;
    private String organizationCode;

}
