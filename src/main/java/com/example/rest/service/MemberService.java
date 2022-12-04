package com.example.rest.service;

import com.example.rest.domain.Member;
import com.example.rest.exception.NotFoundMember;
import com.example.rest.repo.MemberRepository;
import com.example.rest.vo.MemberReqVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long save(MemberReqVo memberReqVo) {
        Member member = Member.builder()
                .email(memberReqVo.getEmail())
                .name(memberReqVo.getName())
                .build();
        memberRepository.save(member);
        return member.getId();
    }

    public Member update(Long id,MemberReqVo reqVo) {
        Member member = memberRepository.findOne(id).orElseThrow(NotFoundMember::new);
        member.update(reqVo);
        return member;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id).orElseThrow(NotFoundMember::new);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}