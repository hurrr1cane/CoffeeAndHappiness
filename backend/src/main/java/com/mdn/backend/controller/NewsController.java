package com.mdn.backend.controller;

import com.mdn.backend.exception.NewsNotFoundException;
import com.mdn.backend.model.News;
import com.mdn.backend.model.dto.NewsDTO;
import com.mdn.backend.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/news")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "Get all news", description = "Retrieve a list of all available news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        log.info("Getting all news");
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @Operation(summary = "Get news by id", description = "Retrieve a news by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "News not found")
    })
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

    @Operation(summary = "Add news", description = "Add a new news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
    })
    @PostMapping
    public ResponseEntity<?> addNews(@RequestBody @Valid NewsDTO news) {
        log.info("Adding new news");

        News addedNews = newsService.addNews(news);
        return new ResponseEntity<>(addedNews, HttpStatus.CREATED);
    }

    @Operation(summary = "Edit news", description = "Edit a news by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "News not found"),
    })
    @PutMapping("{id}")
    public ResponseEntity<?> editNews(@PathVariable Integer id, @RequestBody @Valid NewsDTO news) {
        log.info("Editing news with id {}", id);

        try {
            News editedNews = newsService.editNews(id, news);
            return ResponseEntity.ok(editedNews);
        } catch (NewsNotFoundException ex) {
            log.error("News not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("News not found with id: " + id);
        }
    }

    @Operation(summary = "Delete news", description = "Delete a news by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", description = "News not found"),
    })
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
        }
    }

    @Operation(summary = "Add news image", description = "Add an image to a news by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "News not found"),
    })
    @PostMapping("{newsId}/image/add")
    public ResponseEntity<?> addNewsImage(@PathVariable Integer newsId, @RequestParam("image") MultipartFile image) {
        log.info("Adding image to news with id {}", newsId);

        try {
            News news = newsService.addNewsImage(newsId, image);
            return ResponseEntity.ok(news);
        } catch (NewsNotFoundException ex) {
            log.error("News not found with id: {}", newsId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("News not found with id: " + newsId);
        }
    }

    @Operation(summary = "Delete news image", description = "Delete an image from a news by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success"),
            @ApiResponse(responseCode = "404", description = "News not found"),
    })
    @DeleteMapping("{newsId}/image/delete")
    public ResponseEntity<?> deleteNewsImage(@PathVariable Integer newsId) {
        log.info("Deleting image from news with id {}", newsId);

        try {
            News news = newsService.deleteNewsImage(newsId);
            return ResponseEntity.ok(news);
        } catch (NewsNotFoundException ex) {
            log.error("News not found with id: {}", newsId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("News not found with id: " + newsId);
        }
    }
}
