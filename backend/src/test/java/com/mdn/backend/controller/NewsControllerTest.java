package com.mdn.backend.controller;

import com.mdn.backend.exception.notfound.NewsNotFoundException;
import com.mdn.backend.model.News;
import com.mdn.backend.model.dto.NewsDTO;
import com.mdn.backend.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class NewsControllerTest {

    @Mock
    private NewsService newsService;

    @InjectMocks
    private NewsController newsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllNews() {
        List<News> newsList = new ArrayList<>();
        newsList.add(new News());
        when(newsService.getAllNews()).thenReturn(newsList);

        ResponseEntity<?> response = newsController.getAllNews();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newsList, response.getBody());
    }

    @Test
    void testGetNewsById() {
        Integer newsId = 1;
        News news = new News();
        news.setId(newsId);
        when(newsService.getNewsById(newsId)).thenReturn(news);

        ResponseEntity<?> response = newsController.getNewsById(newsId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(news, response.getBody());
    }

    @Test
    void testGetNewsByIdNotFound() {
        Integer newsId = 3;
        when(newsService.getNewsById(newsId)).thenThrow(new NewsNotFoundException("News not found with id: " + newsId));

        ResponseEntity<?> response = newsController.getNewsById(newsId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("News not found with id: " + newsId, response.getBody());
    }

    @Test
    void testAddNews() {
        NewsDTO newsDTO = new NewsDTO();
        News news = new News();
        when(newsService.addNews(newsDTO)).thenReturn(news);

        ResponseEntity<?> response = newsController.addNews(newsDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(news, response.getBody());
    }

    @Test
    void testEditNews() {
        Integer newsId = 1;
        NewsDTO newsDTO = new NewsDTO();
        News news = new News();
        news.setId(newsId);
        when(newsService.editNews(newsId, newsDTO)).thenReturn(news);

        ResponseEntity<?> response = newsController.editNews(newsId, newsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(news, response.getBody());
    }

    @Test
    void testEditNewsNotFound() {
        Integer newsId = 3;
        NewsDTO newsDTO = new NewsDTO();

        when(newsService.editNews(newsId, newsDTO)).thenThrow(new NewsNotFoundException("News not found with id: " + newsId));

        ResponseEntity<?> response = newsController.editNews(newsId, newsDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("News not found with id: " + newsId, response.getBody());
    }

    @Test
    void testDeleteNews() {
        Integer newsId = 1;
        News news = new News();
        news.setId(newsId);

        ResponseEntity<?> response = newsController.deleteNews(newsId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteNewsNotFound() {
        Integer newsId = 1;

        doThrow(new NewsNotFoundException("News not found with id: " + newsId))
                .when(newsService).deleteNews(newsId);

        ResponseEntity<?> response = newsController.deleteNews(newsId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("News not found with id: " + newsId, response.getBody());
    }

    @Test
    void testAddNewsImage() {
        Integer newsId = 1;
        News news = new News();
        news.setId(newsId);

        MultipartFile image = null;

        when(newsService.addNewsImage(newsId, image)).thenReturn(news);

        ResponseEntity<?> response = newsController.addNewsImage(newsId, image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(news, response.getBody());
    }

    @Test
    void testAddNewsImageNotFound() {
        Integer newsId = 3;

        MultipartFile image = null;

        when(newsService.addNewsImage(newsId, image)).thenThrow(new NewsNotFoundException("News not found with id: " + newsId));

        ResponseEntity<?> response = newsController.addNewsImage(newsId, image);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("News not found with id: " + newsId, response.getBody());
    }

    @Test
    void testDeleteNewsImage() {
        Integer newsId = 1;
        News news = new News();
        news.setId(newsId);

        when(newsService.deleteNewsImage(newsId)).thenReturn(news);

        ResponseEntity<?> response = newsController.deleteNewsImage(newsId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(news, response.getBody());
    }

    @Test
    void testDeleteNewsImageNotFound() {
        Integer newsId = 3;

        when(newsService.deleteNewsImage(newsId)).thenThrow(new NewsNotFoundException("News not found with id: " + newsId));

        ResponseEntity<?> response = newsController.deleteNewsImage(newsId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("News not found with id: " + newsId, response.getBody());
    }
}
