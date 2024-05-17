package rs.raf.demo.repositories.news;

import rs.raf.demo.entities.*;
import rs.raf.demo.repositories.MySqlAbstractRepository;
import rs.raf.demo.repositories.comment.CommentRepository;
import rs.raf.demo.repositories.tag.TagRepository;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySqlNewsRepository extends MySqlAbstractRepository implements NewsRepository {

    @Inject
    private TagRepository tagRepository;
    @Inject
    private CommentRepository commentRepository;

    @Override
    public List<News> allNews() {
        List<News> newsList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSetNews = null;
        ResultSet resultSetUser = null;
        ResultSet resultSetCategory = null;

        int i = 0;

        try{
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSetNews = statement.executeQuery("SELECT * FROM news ORDER BY creation_date DESC");
            while(i < 10 && resultSetNews.next()){
                i++;
                Integer idNews = resultSetNews.getInt("id");
                String titleNews = resultSetNews.getString("title");
                String contentNews = resultSetNews.getString("content");
                Date creationDateNews = resultSetNews.getDate("creation_date");
                News news = new News(idNews, titleNews, contentNews, creationDateNews);

                preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
                preparedStatement.setString(1, resultSetNews.getString("author"));
                resultSetUser = preparedStatement.executeQuery();

                while(resultSetUser.next()){
                    Integer userId = resultSetUser.getInt("id");
                    String userEmail = resultSetUser.getString("email");
                    String userFirstName = resultSetUser.getString("first_name");
                    String userLastName = resultSetUser.getString("last_name");
                    String userPassword = resultSetUser.getString("password");
                    User user = new User(userId, userFirstName, userLastName, userEmail, userPassword);

                    user.setRole(resultSetUser.getInt("role"));
                    user.setStatus(resultSetUser.getInt("status"));

                        synchronized (this){
                            news.setAuthor(user);
                        }
                    }
                    preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE name = ?");
                    preparedStatement.setString(1, resultSetNews.getString("category"));
                    resultSetCategory = preparedStatement.executeQuery();

                    while(resultSetCategory.next()){
                        Integer categoryId = resultSetCategory.getInt("id");
                        String categoryName = resultSetCategory.getString("name");
                        String description = resultSetCategory.getString("description");

                        Category category = new Category(categoryId, categoryName, description);

                        synchronized (this){
                            news.setCategory(category);
                        }
                    }

                newsList.add(news);

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);
            if(preparedStatement != null){
                this.closeStatement(preparedStatement);
            }
           if(resultSetCategory != null){
               this.closeResultSet(resultSetCategory);
           }
           if(resultSetNews != null){
               this.closeResultSet(resultSetNews);
           }
           if(resultSetUser != null){
               this.closeResultSet(resultSetUser);
           }
           this.closeConnection(connection);
        }

        return newsList;
    }

    @Override
    public List<News> allNewsByVisits() {
        List<News> newsList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSetNews = null;
        ResultSet resultSetUser = null;
        ResultSet resultSetCategory = null;
        PreparedStatement preparedStatement = null;

        int i = 0;

        try{
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSetNews = statement.executeQuery("SELECT * FROM news WHERE creation_date BETWEEN NOW() - INTERVAL 30 DAY AND NOW() ORDER BY visits DESC");

            while(i < 10 && resultSetNews.next()){
                i++;
                News news = new News(resultSetNews.getInt("id"), resultSetNews.getString("title"),
                        resultSetNews.getString("content"), resultSetNews.getDate("creation_date"));

                news.setVisits(resultSetNews.getInt("visits"));

                preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
                preparedStatement.setString(1, resultSetNews.getString("author"));
                resultSetUser = preparedStatement.executeQuery();

                while(resultSetUser.next()){


                    User user = new User(resultSetUser.getInt("id"), resultSetUser.getString("first_name"),
                            resultSetUser.getString("last_name"), resultSetUser.getString("email"), resultSetUser.getString("password"));

                    user.setStatus(resultSetUser.getInt("status"));
                    user.setRole(resultSetUser.getInt("role"));

                    synchronized (this){
                        news.setAuthor(user);
                    }
                }

                preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE name = ?");
                preparedStatement.setString(1, resultSetNews.getString("category"));
                resultSetCategory = preparedStatement.executeQuery();

                while(resultSetCategory.next()){
                    Category category = new Category(resultSetCategory.getInt("id"), resultSetCategory.getString("name"),
                            resultSetCategory.getString("description"));

                    synchronized (this){
                        news.setCategory(category);
                    }
                }

                newsList.add(news);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSetNews);
            this.closeResultSet(resultSetUser);
            this.closeResultSet(resultSetCategory);
            this.closeConnection(connection);
        }

        return newsList;
    }

    @Override
    public News addNews(News news) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSetNews = null;
        ResultSet resultSetTag = null;

        try{
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO news (title, content, author, creation_date, visits, category) VALUES (?, ?, ?, ?, ?, ?)", generatedColumns);

            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getContent());
            preparedStatement.setString(3, news.getAuthor().getEmail());
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setInt(5, news.getVisits());
            preparedStatement.setString(6, news.getCategory().getName());

            preparedStatement.executeUpdate();
            resultSetNews = preparedStatement.getGeneratedKeys();

            if(resultSetNews.next()){
                news.setId(resultSetNews.getInt(1));
            }

            for(String tag : news.getTag()){
                preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE tag_name = ?");
                preparedStatement.setString(1, tag);
                resultSetTag = preparedStatement.executeQuery();

                if(resultSetTag.next()){
                    preparedStatement = connection.prepareStatement("INSERT INTO tag_news (id_news, id_tag) VALUES (?, ?)", generatedColumns);
                    preparedStatement.setInt(1, news.getId());
                    preparedStatement.setInt(2, resultSetTag.getInt("id"));

                    preparedStatement.executeUpdate();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSetNews);
            this.closeResultSet(resultSetTag);
            this.closeConnection(connection);
        }

        return news;
    }

    @Override
    public News updateNews(News news) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE news AS n SET n.title = ?, n.content = ?, n.author = ?, n.category = ? WHERE n.id = ?");
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getContent());
            preparedStatement.setString(3, news.getAuthor().getEmail());
            preparedStatement.setString(4, news.getCategory().getName());
            preparedStatement.setInt(5, news.getId());

            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return news;
    }

    @Override
    public News findNewsById(Integer id) {
        News news = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSetNews = null;
        ResultSet resultSetUser = null;
        ResultSet resultSetCategory = null;

        try{
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSetNews = preparedStatement.executeQuery();

            if(resultSetNews.next()){
                String title = resultSetNews.getString("title");
                String content = resultSetNews.getString("content");
                Date creationDate = resultSetNews.getDate("creation_date");
                Integer visits = resultSetNews.getInt("visits");

                preparedStatement = connection.prepareStatement("UPDATE news SET news.visits = news.visits + 1 WHERE news.id = ?");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();

                news = new News(id, title, content, creationDate, visits);

                preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
                preparedStatement.setString(1, resultSetNews.getString( "author"));
                resultSetUser = preparedStatement.executeQuery();

                while(resultSetUser.next()){
                    Integer userId = resultSetUser.getInt("id");
                    String userEmail = resultSetUser.getString("email");
                    String userFirstName = resultSetUser.getString("first_name");
                    String userLastName = resultSetUser.getString("last_name");
                    String userPassword = resultSetUser.getString("password");
                    User user = new User(userId, userFirstName, userLastName, userEmail, userPassword);

                    user.setStatus(resultSetUser.getInt("status"));
                    user.setRole(resultSetUser.getInt("role"));

                    synchronized (this){
                        news.setAuthor(user);
                    }

                    preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE name = ?");
                    preparedStatement.setString(1, resultSetNews.getString("category"));
                    resultSetCategory = preparedStatement.executeQuery();

                    while(resultSetCategory.next()){
                        Integer categoryId = resultSetCategory.getInt("id");
                        String categoryName = resultSetCategory.getString("name");
                        String categoryDescription = resultSetCategory.getString("description");

                        Category category = new Category(categoryId, categoryName, categoryDescription);

                        news.setCategory(category);
                    }
                }

            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSetNews);
            this.closeResultSet(resultSetCategory);
            this.closeResultSet(resultSetUser);
            this.closeConnection(connection);
        }

        return news;
    }

    @Override
    public void deleteNews(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM news WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("DELETE FROM tag_news WHERE id_tag = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            List<Comment> commentsByNews = this.allCommentsByNews(id);

            for(Comment comment : commentsByNews){
                this.commentRepository.deleteComment(comment.getId());
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

    }

    @Override
    public List<News> allNewsByCategory(String name) {
        List<News> newsList = new ArrayList<>();

        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE category LIKE ? ORDER BY creation_date DESC");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                News news = new News(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("content"), resultSet.getDate("creation_date"));

                news.setVisits(resultSet.getInt("visits"));

                newsList.add(news);
            }



        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return newsList;
    }

    @Override
    public List<News> allNewsByTag(Integer id) {
        List<News> newsList = new ArrayList<>();
        News news = null;

        Connection connection = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        ResultSet resultSetUser = null;
        ResultSet resultSetCategory = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM tag_news WHERE id_tag = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Integer newsId = resultSet.getInt("id_news");

                preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE id = ?");
                preparedStatement.setInt(1, newsId);
                resultSet1 = preparedStatement.executeQuery();

                if(resultSet1.next()){
                    String title = resultSet1.getString("title");
                    String content = resultSet1.getString("content");
                    Date creationDate = resultSet1.getDate("creation_date");
                    Integer visits = resultSet1.getInt("visits");

                    news = new News(newsId, title, content, creationDate, visits);

                    preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
                    preparedStatement.setString(1, resultSet1.getString("author"));
                    resultSetUser = preparedStatement.executeQuery();

                    while(resultSetUser.next()){
                        User user = new User(resultSetUser.getInt("id"), resultSetUser.getString("first_name"),
                                resultSetUser.getString("last_name"), resultSetUser.getString("email"), resultSetUser.getString("password"));

                        user.setStatus(resultSetUser.getInt("status"));
                        user.setRole(resultSetUser.getInt("role"));

                        synchronized (this){
                            news.setAuthor(user);
                        }

                    }

                    preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE name = ?");
                    preparedStatement.setString(1, resultSet1.getString("category"));
                    resultSetCategory = preparedStatement.executeQuery();

                    while(resultSetCategory.next()){
                        Category category = new Category(resultSetCategory.getInt("id"),
                                resultSetCategory.getString("name"), resultSetCategory.getString("description"));

                        synchronized (this){
                            news.setCategory(category);
                        }
                    }
                }
                newsList.add(news);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeResultSet(resultSet1);
            this.closeResultSet(resultSetUser);
            this.closeResultSet(resultSetCategory);
            this.closeConnection(connection);
        }

        return newsList;
    }

    @Override
    public List<Tag> allTagByNews(Integer id) {
        List<Tag> tagList = new ArrayList<>();

        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM tag_news WHERE id_news = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Tag tag = tagRepository.findTagById(resultSet.getInt("id_tag"));
                tagList.add(tag);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return tagList;
    }

    @Override
    public List<Comment> allCommentsByNews(Integer id) {
        List<Comment> comments = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM comments WHERE news_id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Comment comment = commentRepository.findCommentById(resultSet.getInt("id"));
                comments.add(comment);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comments;
    }
}
