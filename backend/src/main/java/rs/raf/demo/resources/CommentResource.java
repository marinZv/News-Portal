package rs.raf.demo.resources;

import rs.raf.demo.entities.Comment;
import rs.raf.demo.services.CommentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/comments")
public class CommentResource {

    @Inject
    private CommentService commentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> allComments(){
        return this.commentService.allComments();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Comment createComment(@Valid Comment comment){
        return this.commentService.addComment(comment);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Comment findComment(@PathParam("id") Integer id){
        return this.commentService.findCommentById(id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteComment(@PathParam("id") Integer id){
        this.commentService.deleteComment(id);
    }

}
