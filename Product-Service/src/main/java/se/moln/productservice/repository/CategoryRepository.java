package se.moln.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.moln.productservice.model.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByNameIgnoreCase(String name);
}