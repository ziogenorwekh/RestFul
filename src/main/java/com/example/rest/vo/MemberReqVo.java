package com.example.rest.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MemberReqVo {


    @ApiModelProperty(example = "hello@naver.com")
    @ApiParam(value = "사용자 이메일", required = true)
    @Email
    @Size(min = 4, message = "최소 4글자 이상이어야 합니다.")
    private String email;

    @ApiModelProperty(example = "lsek")
    @ApiParam(value = "사용지 이름")
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    private String name;

    @ApiModelProperty(example = "123456")
    @ApiParam(value = "사용자 비밀번호")
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    private String password;
}
