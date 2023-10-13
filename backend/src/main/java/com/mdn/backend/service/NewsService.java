package com.mdn.backend.service;

import com.mdn.backend.exception.notfound.NewsNotFoundException;
import com.mdn.backend.model.News;
import com.mdn.backend.model.dto.NewsDTO;
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

    public News addNews(NewsDTO newsDTO) {
        try {
            News news = getNewsFromDTO(newsDTO);
            news.setPublishedAt(new Date());
            return newsRepository.save(news);
        } catch (Exception ex) {
            throw new RuntimeException("Error while adding news: " + ex.getMessage(), ex);
        }
    }

    public News editNews(Integer id, NewsDTO newsDTO) {
        var editedNews = newsRepository.findById(id).orElseThrow(
                () -> new NewsNotFoundException("No news found with id " + id)
        );

        try {
            editNewsWithCheckingForNull(newsDTO, editedNews);
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

    private static News getNewsFromDTO(NewsDTO newsDTO) {
        return News.builder()
                .titleEN(newsDTO.getTitleEN())
                .titleUA(newsDTO.getTitleUA())
                .descriptionEN(newsDTO.getDescriptionEN())
                .descriptionUA(newsDTO.getDescriptionUA())
                .imageUrl(newsDTO.getImageUrl())
                .build();
    }

    private static void editNewsWithCheckingForNull(NewsDTO newsDTO, News editedNews) {
        if (newsDTO.getTitleEN() != null) editedNews.setTitleEN(newsDTO.getTitleEN());
        if (newsDTO.getTitleUA() != null) editedNews.setTitleUA(newsDTO.getTitleUA());
        if (newsDTO.getDescriptionEN() != null) editedNews.setDescriptionEN(newsDTO.getDescriptionEN());
        if (newsDTO.getDescriptionUA() != null) editedNews.setDescriptionUA(newsDTO.getDescriptionUA());
        if (newsDTO.getImageUrl() != null) editedNews.setImageUrl(newsDTO.getImageUrl());
        if (newsDTO.getPublishedAt() != null) editedNews.setPublishedAt(newsDTO.getPublishedAt());
    }
}
