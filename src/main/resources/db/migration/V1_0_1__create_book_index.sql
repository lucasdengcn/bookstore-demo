ALTER TABLE books
ADD CONSTRAINT uk_books_title UNIQUE (title);