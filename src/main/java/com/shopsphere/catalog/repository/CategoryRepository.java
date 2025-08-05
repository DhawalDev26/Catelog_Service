package com.shopsphere.catalog.repository;

import com.shopsphere.catalog.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
     Optional<Category> findByName(String name);

     @Query("SELECT c FROM Category c " +
             "WHERE (:categoryId IS NULL OR c.categoryId = :categoryId) " +
             "AND (:subCategoryId IS NULL OR c.subCategoryId = :subCategoryId)")
     List<Category> findAllByCategoryIdAndSubCategoryId(@Param("categoryId") String categoryId,
                                                        @Param("subCategoryId") String subCategoryId);

}
