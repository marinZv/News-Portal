package rs.raf.demo.repositories.comment;

import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.News;
import rs.raf.demo.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlCommentRepository extends MySqlAbstractRepository implements CommentRepository {
    @Override
    public List<Comment> allComments() {
        List<Comment> comments = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSetComment = null;
        ResultSet resultSetNews = null;

        try{
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSetComment = statement.executeQuery("SELECT * FROM comments ORDER BY creation_date DESC");

            while(resultSetComment.next()){
                Comment comment = new Comment(resultSetComment.getInt("id"),
                        resultSetComment.getString("author"), resultSetComment.getString("content"),
                            resultSetComment.getDate("creation_date"));

                preparedStatement = connection.prepareStatement("SELECT * from news WHERE id = ?");
                preparedStatement.setInt(1,resultSetComment.getInt("news_id"));
                resultSetNews = preparedStatement.executeQuery();

                while(resultSetNews.next()){
                    News news = new News(resultSetNews.getInt("id"), resultSetNews.getString("title"),
                            resultSetNews.getString("content"), resultSetNews.getDate("creation_date"));

                    news.setVisits(resultSetNews.getInt("visits"));

                    synchronized (this){
                        comment.setNews(news);
                    }
                }
                comments.add(comment);
            }



        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);

            if(preparedStatement != null){
                this.closeStatement(preparedStatement);
            }
            if(resultSetComment != null){

                this.closeResultSet(resultSetComment);
            }
            if(resultSetNews != null){
                this.closeResultSet(resultSetNews);
            }

            this.closeConnection(connection);
        }

        return comments;
    }

    @Override
    public Comment findCommentById(Integer id) {
        Comment comment = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM comments where id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String author = resultSet.getString("author");
                String content = resultSet.getString("content");
                java.util.Date creationDate = resultSet.getDate("creation_date");

                comment = new Comment(id, author, content, creationDate);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comment;
    }

    @Override
    public Comment addComment(Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO comments (author, content, creation_date, news_id) VALUES (?, ?, ?, ?)" ,generatedColumns);
            preparedStatement.setString(1, comment.getAuthor());
            preparedStatement.setString(2, comment.getContent());
            Date sqlDate = new Date(System.currentTimeMillis());
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.setInt(4, comment.getNews().getId());

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()){
                comment.setId(resultSet.getInt(1));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comment;
    }

    @Override
    public void deleteComment(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM comments WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

    }
}
