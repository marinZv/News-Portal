package rs.raf.demo.repositories.category;

import rs.raf.demo.entities.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> allCategories();
    Category findCategoryByName(String name);
    Category addCategory(Category category);
    void deleteCategory(String name);
    Category updateCategory(Category category, String name);

}
