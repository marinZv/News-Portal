package rs.raf.demo.services;

import rs.raf.demo.entities.Comment;
import rs.raf.demo.entities.News;
import rs.raf.demo.entities.Tag;
import rs.raf.demo.repositories.news.NewsRepository;

import javax.inject.Inject;
import java.util.List;

public class NewsService {

    @Inject
    private NewsRepository newsRepository;

    public News addNews(News news){
        return this.newsRepository.addNews(news);
    }

    public News updateNews(News news){
        return this.newsRepository.updateNews(news);
    }

    public List<News> allNews(){
        return this.newsRepository.allNews();
    }

    public List<News> allNewsByVisits(){
        return this.newsRepository.allNewsByVisits();
    }

    public News findNewsById(Integer id){
        return this.newsRepository.findNewsById(id);
    }

    public void deleteNews(Integer id){
        this.newsRepository.deleteNews(id);
    }

    public List<News> allNewsByCategory(String name){
       return this.newsRepository.allNewsByCategory(name);
    }

    public List<News> allNewsByTag(Integer id){
        return this.newsRepository.allNewsByTag(id);
    }

    public List<Tag> allTagByNews(Integer id){
        return this.newsRepository.allTagByNews(id);
    }

    public List<Comment> allComentsByNews(Integer id){
        return this.newsRepository.allCommentsByNews(id);
    }



}
