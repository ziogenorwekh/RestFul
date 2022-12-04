package com.example.rest.repo;

import com.example.rest.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.id=?1")
    Optional<Member> findOne(Long id);
}