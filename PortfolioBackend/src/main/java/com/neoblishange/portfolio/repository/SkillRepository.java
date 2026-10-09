package com.neoblishange.portfolio.repository;


import com.neoblishange.portfolio.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByNameIn(Collection<String> names);
}
