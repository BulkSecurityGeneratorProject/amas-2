package com.binana.amas.repository;

import com.binana.amas.domain.Asso;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Asso entity.
 */
@SuppressWarnings("unused")
public interface AssoRepository extends JpaRepository<Asso,Long> {

}
