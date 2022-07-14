package com.nsu.danilllo.repositories;

import com.nsu.danilllo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);

    Optional<Category> findByIdAndDeletedFalse(Long id);

    Optional<Category> findByRequestedIDAndDeletedFalse(String requestID);

    @Query(value = "Select c from Category c where (c.requestedID = :requestedId or c.name = :name) and c.deleted = false")
    Optional<Category> findCategoryByRequestedIdOrName(@Param("requestedId") String requestedID, @Param("name") String name);

    @Query(value = "Select c from Category  c where c.name like %:namePart% and c.deleted = false")
    List<Category> findCategoriesByName(@Param("namePart") String namePart);

}
