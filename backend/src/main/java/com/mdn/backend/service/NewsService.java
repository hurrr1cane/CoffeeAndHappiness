package com.mdn.backend.service;

import com.mdn.backend.exception.NewsNotFoundException;
import com.mdn.backend.model.News;
import com.mdn.backend.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getNewsById(Integer id) {
        return newsRepository.findById(id).orElseThrow(
                () -> new NewsNotFoundException("No news found with id " + id)
        );
    }

    public News addNews(News news) {
        try {
            return newsRepository.save(news);
        } catch (Exception ex) {
            throw new RuntimeException("Error while adding news: " + ex.getMessage(), ex);
        }
    }

    public News editNews(Integer id, News news) {
        var editedNews = newsRepository.findById(id).orElseThrow(
                () -> new NewsNotFoundException("No news found with id " + id)
        );

        editedNews.setTitle(news.getTitle());
        editedNews.setDescription(news.getDescription());
        editedNews.setImageUrl(news.getImageUrl());
        editedNews.setPublishedAt(news.getPublishedAt());

        try {
            return newsRepository.save(editedNews);
        } catch (Exception ex) {
            throw new RuntimeException("Error while editing news: " + ex.getMessage(), ex);
        }
    }

    public void deleteNews(Integer id) {
        var newsToDelete = newsRepository.findById(id).orElseThrow(
                () -> new NewsNotFoundException("No news found with id " + id)
        );
        newsRepository.delete(newsToDelete);
    }
}
