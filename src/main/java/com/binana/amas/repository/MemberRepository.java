package com.binana.amas.repository;

import com.binana.amas.domain.Member;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Member entity.
 */
@SuppressWarnings("unused")
public interface MemberRepository extends JpaRepository<Member,Long> {

}
