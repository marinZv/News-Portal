package rs.raf.demo.resources;


import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.News;
import rs.raf.demo.entities.Tag;
import rs.raf.demo.services.NewsService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/news")
public class NewsResource {

    @Inject
    private NewsService newsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<News> allNews(){
        return this.newsService.allNews();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public News createNews(@Valid News news){
        return this.newsService.addNews(news);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/visits")
    public List<News> allNewsByVisits(){
        return this.newsService.allNewsByVisits();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public News updateNews(@Valid News news){
        return this.newsService.updateNews(news);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public News findNewsById(@PathParam("id") Integer id){
        return this.newsService.findNewsById(id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteNews(@PathParam("id") Integer id){
        this.newsService.deleteNews(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/{name}")
    public List<News> allNewsByCategory(@PathParam("name") String name){
        return this.newsService.allNewsByCategory(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tag/{id}")
    public List<News> allNewsByTag(@PathParam("id") Integer id){
        return this.newsService.allNewsByTag(id);
    }

    @GET
    @Path("/newsTag/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> allTagsByNews(@PathParam("id") Integer id){
        return this.newsService.allTagByNews(id);
    }

    @GET
    @Path("/comments/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> allCommentsByNews(@PathParam("id") Integer id){
        return this.newsService.allComentsByNews(id);
    }

}
