/* (C) 2024 */ 

package com.example.demo.bookstore.api.controller;

import com.example.demo.bookstore.model.input.BookCreateInput;
import com.example.demo.bookstore.model.input.BookUpdateInput;
import com.example.demo.bookstore.model.output.BookInfo;
import com.example.demo.bookstore.model.output.PageableOutput;
import com.example.demo.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Book APIs")
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(description = "Create a book")
    @PostMapping(
            value = "/v1/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public BookInfo create(@Valid @RequestBody BookCreateInput input) {
        BookInfo bookInfo = bookService.create(input);
        return bookInfo;
    }

    @Operation(description = "Create a book with cover image")
    @PostMapping(
            value = "/v2/",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public BookInfo createWithCover(
            @RequestPart(value = "file", required = true) MultipartFile file,
            @Valid @RequestPart(value = "jsonData", required = true) BookCreateInput input) {
        BookInfo bookInfo = bookService.create(input, file);
        return bookInfo;
    }

    @Operation(description = "Update book's information via id-based")
    @PutMapping(value = "/v1/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public BookInfo put(@PathVariable("id") Integer id, @Valid @RequestBody BookUpdateInput input) {
        return bookService.update(id, input);
    }

    @Operation(description = "get available pageable books")
    @GetMapping("/v1/{index}/{size}")
    @ResponseStatus(HttpStatus.OK)
    public PageableOutput<BookInfo> findAvailableBooks(
            @PathVariable("index") Integer index, @PathVariable("size") Integer size) {
        return bookService.findAvailableBooks(index, size);
    }

    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Found the book",
                        content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = BookInfo.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)
            }) // @formatter:on
    @Operation(description = "get a book's detail information")
    @GetMapping("/v1/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookInfo getDetail(@PathVariable("id") Integer id) {
        return bookService.findById(id);
    }
}
