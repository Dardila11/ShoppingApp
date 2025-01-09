package dev.dardila.shoppingcartapp.repository;

import dev.dardila.shoppingcartapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}