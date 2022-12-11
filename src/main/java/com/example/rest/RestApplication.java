package com.example.rest;

import com.example.rest.service.MemberService;
import com.example.rest.vo.MemberReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class RestApplication {

    @Autowired
    private MemberService memberService;

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }


    @PostConstruct
    public void init() {
        MemberReqVo memberReqVo = new MemberReqVo();
        memberReqVo.setEmail("lauv@lsek.com");
        memberReqVo.setName("lauv");
        memberReqVo.setPassword("123456");
        memberService.save(memberReqVo);
    }
}
