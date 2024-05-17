package rs.raf.demo.services;

import rs.raf.demo.entities.Comment;
import rs.raf.demo.repositories.comment.CommentRepository;

import javax.inject.Inject;
import java.util.List;

public class CommentService {

    @Inject
    private CommentRepository commentRepository;

    public List<Comment> allComments(){
        return this.commentRepository.allComments();
    }

    public void deleteComment(Integer id){
        this.commentRepository.deleteComment(id);
    }

    public Comment addComment(Comment comment){
        return this.commentRepository.addComment(comment);
    }

    public Comment findCommentById(Integer id){
        return this.commentRepository.findCommentById(id);
    }

}
