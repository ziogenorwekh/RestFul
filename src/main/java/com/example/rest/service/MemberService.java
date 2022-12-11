package com.example.rest.service;

import com.example.rest.domain.Member;
import com.example.rest.domain.Role;
import com.example.rest.exception.NotFoundMember;
import com.example.rest.repo.MemberRepository;
import com.example.rest.vo.MemberReqVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long save(MemberReqVo memberReqVo) {
        memberReqVo.setPassword(passwordEncoder.encode(memberReqVo.getPassword()));
        Member member = Member.builder()
                .email(memberReqVo.getEmail())
                .name(memberReqVo.getName())
                .password(memberReqVo.getPassword())
                .build();
//         권한 설정
        Role role = Role.builder().role("ROLE_USER").build();
        // 권한 부여
        member.addRole(role);

        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public Member update(Long id, MemberReqVo reqVo) {
        Member member = memberRepository.findOne(id).orElseThrow(NotFoundMember::new);
        member.update(reqVo);
        return member;
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long id) {
        return memberRepository.findOne(id).orElseThrow(NotFoundMember::new);
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

}