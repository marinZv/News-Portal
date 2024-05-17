package rs.raf.demo.repositories.comment;

import rs.raf.demo.entities.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> allComments();
    Comment findCommentById(Integer id);
    Comment addComment(Comment comment);
    void deleteComment(Integer id);

}
