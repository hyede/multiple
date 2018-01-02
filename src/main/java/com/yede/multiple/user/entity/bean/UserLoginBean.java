package com.yede.multiple.user.entity.bean;

import com.yede.multiple.model.GenericObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginBean extends GenericObject{

    @NotBlank(message = "用户名不可以为空")
    @Length(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    private String username;

    @NotBlank(message = "密码不可以为空")
    @Length(min = 6, max = 20, message = "密码长度需要在6-20长度以内")
    private String password;

    @NotBlank(message = "单位码不可以为空")
    private String organizationCode;

}
