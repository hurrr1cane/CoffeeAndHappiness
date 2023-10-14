package com.mdn.backend.service;

import com.mdn.backend.exception.notfound.NewsNotFoundException;
import com.mdn.backend.model.News;
import com.mdn.backend.model.dto.NewsDTO;
import com.mdn.backend.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllNews() {
        // Mock data
        List<News> newsList = new ArrayList<>();
        newsList.add(new News());
        newsList.add(new News());

        // Mock behavior
        when(newsRepository.findAll()).thenReturn(newsList);

        // Test
        List<News> result = newsService.getAllNews();

        assertEquals(newsList.size(), result.size());
    }

    @Test
    void testGetNewsById() {
        // Mock data
        int newsId = 1;
        News news = new News();
        news.setId(newsId);

        // Mock behavior
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));

        // Test
        News result = newsService.getNewsById(newsId);

        assertEquals(news, result);
    }

    @Test
    void testGetNewsByIdNotFound() {
        // Mock behavior
        int newsId = 1;
        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        // Test
        assertThrows(NewsNotFoundException.class, () -> newsService.getNewsById(newsId));
    }

    @Test
    void testAddNews() {
        // Mock data
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitleEN("Test News");
        newsDTO.setTitleUA("Тест Новина");

        News news = new News();
        news.setTitleEN("Test News");
        news.setTitleUA("Тест Новина");

        // Mock behavior
        when(newsRepository.save(any(News.class))).thenReturn(news);

        // Test
        News result = newsService.addNews(newsDTO);

        assertEquals(news, result);
    }

    @Test
    void testEditNews() {
        // Mock data
        int newsId = 1;
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitleEN("Updated News");

        News existingNews = new News();
        existingNews.setId(newsId);
        existingNews.setTitleEN("Original News");

        // Mock behavior
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));
        when(newsRepository.save(any(News.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test
        News result = newsService.editNews(newsId, newsDTO);

        assertEquals("Updated News", result.getTitleEN());
    }

    @Test
    void editNewsNotFound() {
        // Mock behavior
        int newsId = 1;
        NewsDTO newsDTO = new NewsDTO();
        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        // Test
        assertThrows(NewsNotFoundException.class, () -> newsService.editNews(newsId, newsDTO));
    }

    @Test
    void testEditNewsWithCheckingForNull() {
        // Similar to the example you provided for FoodService

        // Arrange
        int newsId = 1;
        News editedNews = new News();

        editedNews.setTitleEN("Original News");
        editedNews.setTitleUA("Оригінальна Новина");
        editedNews.setDescriptionEN("Original Description EN");
        editedNews.setDescriptionUA("Original Description UA");
        editedNews.setImageUrl("https://original_image_url");
        editedNews.setPublishedAt(new Date());

        NewsDTO newsDTO = new NewsDTO();
        editedNews.setId(newsId);

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(editedNews));

        // Act
        newsService.editNews(newsId, newsDTO);

        // Assert
        assertEquals("Original News", editedNews.getTitleEN());
        assertEquals("Оригінальна Новина", editedNews.getTitleUA());
        assertEquals("Original Description EN", editedNews.getDescriptionEN());
        assertEquals("Original Description UA", editedNews.getDescriptionUA());
        assertEquals("https://original_image_url", editedNews.getImageUrl());
        assertNotNull(editedNews.getPublishedAt());
    }

    @Test
    void testDeleteNews() {
        // Mock behavior
        int newsId = 1;
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(new News()));

        // Test
        assertDoesNotThrow(() -> newsService.deleteNews(newsId));
    }

    @Test
    void testDeleteNewsNotFound() {
        // Mock behavior
        int newsId = 1;
        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        // Test
        assertThrows(NewsNotFoundException.class, () -> newsService.deleteNews(newsId));
    }

}
