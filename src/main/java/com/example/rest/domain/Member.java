package com.example.rest.domain;

import com.example.rest.vo.MemberReqVo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "MEMBER_UUID", unique = true)
    private String memberId;
    @Column(nullable = false, length = 20)
    private String email;

    private String name;

    private String password;

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @Builder
    public Member(String email, String name, String password) {
        this.memberId = UUID.randomUUID().toString();
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void addRole(Role role) {
        role.addMember(this);
    }

    public void update(MemberReqVo memberReqVo) {
        this.email = memberReqVo.getEmail();
        this.name = memberReqVo.getName();
    }
}
