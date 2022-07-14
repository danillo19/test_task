package com.nsu.danilllo.repositories;

import com.nsu.danilllo.model.Banner;
import com.nsu.danilllo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    Optional<Banner> findByIdAndDeletedFalse(Long id);
    Optional<Banner> findByNameAndDeletedFalse(String name);
    List<Banner> findByCategoriesContainingAndDeletedFalse(Category category);

    @Query(value = "Select b from Banner b where b.name like %:namePart% and b.deleted = false")
    List<Banner> findBannersByName(@Param("namePart") String namePart);
}
