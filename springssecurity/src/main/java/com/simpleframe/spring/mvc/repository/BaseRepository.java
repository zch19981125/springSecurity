package com.simpleframe.spring.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

// ~ File Information
// ====================================================================================================================

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	// ~ Static Fields
	// ==================================================================================================================

	// ~ Methods
	// ==================================================================================================================

}
