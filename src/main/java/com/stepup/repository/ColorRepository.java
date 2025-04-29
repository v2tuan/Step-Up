package com.stepup.repository;

import com.stepup.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {
    @Query("SELECT DISTINCT c.name FROM Color c")
    List<String> findAllDistinctColorNames();
}
