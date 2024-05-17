package rs.raf.demo.resources;


import rs.raf.demo.entities.Tag;
import rs.raf.demo.services.TagService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tags")
public class TagResource {

    @Inject
    private TagService tagService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> allTags(){
        return this.tagService.allTags();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Tag createTag(@Valid Tag tag){
        System.out.println("ulazi u rutu POST: /tags");
        return this.tagService.addTag(tag);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("name/{name}")
    public Tag findTagByName(@PathParam("name") String name){
        return this.tagService.findTagByName(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Tag findTagById(@PathParam("id") Integer id){
        return this.tagService.findTagById(id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteTag(@PathParam("id") Integer id){
        this.tagService.deleteTag(id);
    }

}
