package com.example.rest.security;

import com.example.rest.domain.Member;
import com.example.rest.exception.NotFoundMember;
import com.example.rest.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomizedMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByName(username).orElseThrow(NotFoundMember::new);
        UserDetails userDetails = new CustomizedMemberDetails(member);
        return userDetails;
    }

    @Transactional(readOnly = true)
    public UserDetails loadMemberByMemberId(String memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElseThrow(NotFoundMember::new);
        UserDetails userDetails = new CustomizedMemberDetails(member);
        return userDetails;
    }
}
