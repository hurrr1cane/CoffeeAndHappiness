package com.mdn.backend.controller;

import com.mdn.backend.exception.NewsNotFoundException;
import com.mdn.backend.model.News;
import com.mdn.backend.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/news")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        log.info("Getting all news");
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getNewsById(@PathVariable Integer id) {
        log.info("Getting news with id {}", id);
        try {
            News news = newsService.getNewsById(id);
            return ResponseEntity.ok(news);
        } catch (NewsNotFoundException ex) {
            log.error("News not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found with id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<?> addNews(@RequestBody @Valid News news) {
        log.info("Adding new news");

        try {
            News addedNews = newsService.addNews(news);
            return new ResponseEntity<>(addedNews, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Error while adding news: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editNews(@PathVariable Integer id, @RequestBody @Valid News news) {
        log.info("Editing news with id {}", id);

        try {
            News editedNews = newsService.editNews(id, news);
            return ResponseEntity.ok(editedNews);
        } catch (NewsNotFoundException ex) {
            log.error("News not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("News not found with id: " + id);
        } catch (Exception ex) {
            log.error("Error while editing news: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while editing news: " + ex.getMessage());
        }
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<?> deleteNews(@PathVariable Integer id) {
        log.info("Deleting news with id {}", id);
        try {
            newsService.deleteNews(id);
            return ResponseEntity.noContent().build();
        } catch (NewsNotFoundException ex) {
            log.error("News not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("News not found with id: " + id);
        } catch (Exception ex) {
            log.error("Error while deleting news: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while deleting news: " + ex.getMessage());
        }
    }
}
