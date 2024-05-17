package rs.raf.demo.services;

import rs.raf.demo.entities.Category;
import rs.raf.demo.repositories.category.CategoryRepository;

import javax.inject.Inject;
import java.util.List;

public class CategoryService {

    @Inject
    private CategoryRepository categoryRepository;

    public Category addCategory(Category category){
        return this.categoryRepository.addCategory(category);
    }

    public List<Category> allCategories(){
        return this.categoryRepository.allCategories();
    }

    public Category findCategory(String name){
        return this.categoryRepository.findCategoryByName(name);
    }

    public void deleteCategory(String name){
        this.categoryRepository.deleteCategory(name);
    }

    public Category updateCategory(Category category, String name){
        return this.categoryRepository.updateCategory(category, name);
    }

}
