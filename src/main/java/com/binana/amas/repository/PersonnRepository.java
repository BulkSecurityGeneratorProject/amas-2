package com.binana.amas.repository;

import com.binana.amas.domain.Personn;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Personn entity.
 */
@SuppressWarnings("unused")
public interface PersonnRepository extends JpaRepository<Personn,Long> {

}
