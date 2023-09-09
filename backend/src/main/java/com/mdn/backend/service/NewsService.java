package com.mdn.backend.service;

import com.mdn.backend.exception.CafeNotFoundException;
import com.mdn.backend.exception.NewsNotFoundException;
import com.mdn.backend.model.Cafe;
import com.mdn.backend.model.News;
import com.mdn.backend.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final AzureBlobStorageService azureStorageService;

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
            news.setPublishedAt(new Date());
            return newsRepository.save(news);
        } catch (Exception ex) {
            throw new RuntimeException("Error while adding news: " + ex.getMessage(), ex);
        }
    }

    public News editNews(Integer id, News news) {
        var editedNews = newsRepository.findById(id).orElseThrow(
                () -> new NewsNotFoundException("No news found with id " + id)
        );

        editedNews.setTitleEN(news.getTitleEN());
        editedNews.setTitleUA(news.getTitleUA());
        editedNews.setDescriptionEN(news.getDescriptionEN());
        editedNews.setDescriptionUA(news.getDescriptionUA());
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

    public News addNewsImage(Integer newsId, MultipartFile image) {

        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new NewsNotFoundException("No such news with id " + newsId + " found")
        );

        azureStorageService.deleteImage("news", newsId);
        String imageUrl = azureStorageService.saveImage(image, "news", newsId);

        news.setImageUrl(imageUrl);
        return newsRepository.save(news);
    }

public News deleteNewsImage(Integer newsId) {

        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new NewsNotFoundException("No such news with id " + newsId + " found")
        );

        azureStorageService.deleteImage("news", newsId);

        news.setImageUrl(null);
        return newsRepository.save(news);
    }
}
