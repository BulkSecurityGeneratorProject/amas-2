package com.binana.amas.repository;

import com.binana.amas.domain.Blog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Blog entity.
 */
@SuppressWarnings("unused")
public interface BlogRepository extends JpaRepository<Blog,Long> {

}
