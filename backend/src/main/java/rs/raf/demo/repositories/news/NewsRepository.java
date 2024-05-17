package rs.raf.demo.repositories.news;

import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.News;
import rs.raf.demo.entities.Tag;

import java.util.List;

public interface NewsRepository {

    List<News> allNews();
    List<News> allNewsByVisits();
    News addNews(News news);
    News updateNews(News news);
    News findNewsById(Integer id);
    void deleteNews(Integer id);
    List<News> allNewsByCategory(String name);
    List<News> allNewsByTag(Integer id);
    List<Tag> allTagByNews(Integer id);
    List<Comment> allCommentsByNews(Integer id);


}
