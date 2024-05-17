package rs.raf.demo.resources;

import rs.raf.demo.entities.Category;
import rs.raf.demo.repositories.category.CategoryRepository;
import rs.raf.demo.services.CategoryService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/categories")
public class CategoryResource {

    @Inject
    private CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> allCategories(){
        return this.categoryService.allCategories();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Category createCategory(@Valid Category category){
        return this.categoryService.addCategory(category);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public Category findCategory(@PathParam("name") String name){
        return this.categoryService.findCategory(name);
    }

    @DELETE
    @Path("/{name}")
    public void deleteCategory(@PathParam("name") String name){
        this.categoryService.deleteCategory(name);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{name}")
    public Category updateCategory(@Valid Category category, @PathParam("name") String name){
        return this.categoryService.updateCategory(category, name);
    }

}
