package com.example.rest.domain;

import com.example.rest.vo.MemberReqVo;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@ToString
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID", nullable = false)
    private Long id;

    @Column(name = "MEMBER_UUID",unique = true)
    private String memberId;
    @Column(nullable = false, length = 20)
    private String email;

    private String name;

    @Builder
    public Member(String email, String name) {
        this.memberId = UUID.randomUUID().toString();
        this.email = email;
        this.name = name;
    }

    public void update(MemberReqVo memberReqVo) {
        this.email = memberReqVo.getEmail();
        this.name = memberReqVo.getName();
    }
}
