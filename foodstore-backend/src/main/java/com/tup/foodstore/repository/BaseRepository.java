package com.tup.foodstore.repository;

import com.tup.foodstore.model.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends Base>
        extends JpaRepository<T, Long> {
}