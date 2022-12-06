package com.example.rest.vo;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.persistence.Column;

@Data
@JsonFilter("MemberInfo")
//@JsonIgnoreProperties(value = {"id"})
public class MemberRespVo {

    @ApiParam(value = "사용자 기본 키")
    private Long id;
    @ApiParam(value = "사용자 아이디", required = true)
    private String memberId;

    @ApiParam(value = "사용자 이메일", required = true)
    private String email;

    @ApiParam(value = "사용지 이름")
    private String name;
}
