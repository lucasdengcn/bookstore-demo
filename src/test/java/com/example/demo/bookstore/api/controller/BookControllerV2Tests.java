/* (C) 2024 */ 

package com.example.demo.bookstore.api.controller;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Disabled
class BookControllerV2Tests {

    //    public static final String TITLE = "book ABC";
    //    public static final String AUTHOR = "James";
    //    public static final String CATEGORY = "Java";
    //    public static final BigDecimal PRICE = BigDecimal.valueOf(50.20);
    //    public static final int AMOUNT = 100;
    //
    //    public static final String TITLE_V2 = "book ABC V2";
    //    public static final int AMOUNT_V2 = 110;
    //    public static final BigDecimal PRICE_V2 = BigDecimal.valueOf(50.50);
    //    public static final String APPLICATION_JSON = "application/json";
    //
    //    public static Faker faker = new Faker();
    //
    //    @Autowired
    //    ObjectMapper objectMapper;
    //
    //    @Autowired
    //    BookService bookService;
    //
    //    @Autowired
    //    BookRepository bookRepository;
    //
    //    @LocalServerPort
    //    private Integer port;
    //
    //    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    //
    //    @BeforeAll
    //    static void beforeAll() {
    //        postgres.start();
    //    }
    //
    //    @AfterAll
    //    static void afterAll() {
    //        postgres.stop();
    //    }
    //
    //    @DynamicPropertySource
    //    static void configureProperties(DynamicPropertyRegistry registry) {
    //        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    //        registry.add("spring.datasource.username", postgres::getUsername);
    //        registry.add("spring.datasource.password", postgres::getPassword);
    //        registry.add("spring.datasource.driverClassName", postgres::getDriverClassName);
    //    }
    //
    //    @BeforeEach
    //    void setUp() {
    //        RestAssured.baseURI = "http://localhost:" + port;
    //        bookRepository.deleteAll();
    //    }
    //
    //    private String randomTitle() {
    //        return faker.lorem().characters(10, 100, true, true);
    //    }
    //
    //    @Test
    //    void test_api_create_book_success() throws Exception {
    //        String url = "/books/v1/";
    //        // Prepare
    //        BookCreateInput bookCreateInput = BookCreateInput.builder()
    //                .title(randomTitle())
    //                .author(AUTHOR)
    //                .category(CATEGORY)
    //                .price(PRICE)
    //                .amount(AMOUNT)
    //                .build();
    //        //
    //        given().contentType(ContentType.JSON)
    //                .when()
    //                .post(url, bookCreateInput)
    //                .then()
    //                .statusCode(201)
    //                .body(".title", equalTo(bookCreateInput.getTitle()));
    //        //
    //    }
}
