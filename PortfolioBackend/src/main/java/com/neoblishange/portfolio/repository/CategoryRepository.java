package com.neoblishange.portfolio.repository;

import com.neoblishange.portfolio.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
