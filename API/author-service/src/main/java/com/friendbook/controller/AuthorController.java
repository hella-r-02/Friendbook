package com.friendbook.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.friendbook.model.Author;
import com.friendbook.service.AuthorService;
import com.friendbook.utils.AppError;
import com.friendbook.utils.Sort;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(authorService.getAuthorWithBooks(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Author with id:" + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/for-book/{id}")
    public ResponseEntity<?> getAuthorForBook(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(authorService.getAuthorForBook(id), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Author with id:" + id + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getAuthorsByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(authorService.getAuthorsByAuthorName(name), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Authors with name:" + name + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam int numberPage,
                                    @RequestParam int sizePage,
                                    @RequestParam(required = false) String word,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam(required = false, defaultValue = "0") Integer startRating,
                                    @RequestParam(required = false, defaultValue = "10") Integer finishRating) {
        Sort sortType = Sort.Nothing;
        if (sort != null) {
            if (sort.equals("popularity")) {
                sortType = Sort.Popularity;
            } else if (sort.equals("rating")) {
                sortType = Sort.Rating;
            }
        }
        try {
            return new ResponseEntity<>(authorService.search(numberPage, sizePage, sortType, word,
                    startRating, finishRating), HttpStatus.OK);
        } catch (EntityNotFoundException entityNotFoundException) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Authors with name:" + word + " not found."), HttpStatus.NOT_FOUND);
        }
    }
}
