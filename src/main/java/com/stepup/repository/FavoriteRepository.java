package com.stepup.repository;

import com.stepup.entity.Color;
import com.stepup.entity.Favorite;
import com.stepup.entity.User;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
  //  List<Favorite> findByProductId(Long productId);
    void deleteByUserId(Long userId);
    Optional<Favorite> findByUserAndColor(User user, Color color);

}
