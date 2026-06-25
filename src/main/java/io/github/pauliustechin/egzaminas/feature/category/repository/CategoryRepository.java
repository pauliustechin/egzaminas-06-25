package io.github.pauliustechin.egzaminas.feature.category.repository;

import io.github.pauliustechin.egzaminas.feature.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
