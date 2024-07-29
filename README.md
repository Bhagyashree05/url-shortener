# URL Shortener API

## Overview

This is a simple URL shortener API built with Kotlin, Spring Boot, and various supporting libraries. The API allows users to shorten URLs and retrieve the original URLs.

## Libraries Used

- **Commons Codec**: Used for Base64 encoding and decoding.
- **Commons Validator**: Used for URL validation.

## Why We Chose Commons Codec

Commons Codec was chosen for its simplicity and reliability in handling Base64 encoding and decoding, which is necessary for generating and resolving shortened URLs.

### Other Options Considered

- **Guava**: Google's library that provides additional utilities for encoding/decoding, but it is a larger dependency.

## Setting Up and Running

1. **Build the Project**:
    ```bash
    mvn clean install
    ```

2. **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```

3. **Running Tests**:
    ```bash
    mvn test
    ```

## API Endpoints

### Shorten URL

- **Endpoint**: `/api/v1/url/shorten`
- **Method**: POST
- **Request Body**:
    ```json
    {
      "originalUrl": "http://example.com"
    }
    ```
- **Response**:
    ```json
    {
      "shortUrl": "MQ=="
    }
    ```

### Resolve URL

- **Endpoint**: `/api/v1/url/resolve`
- **Method**: POST
- **Request Body**:
    ```json
    {
      "shortUrl": "MQ=="
    }
    ```
- **Response**:
    ```json
    {
      "originalUrl": "http://example.com"
    }
    ```

## Error Handling

- **BadRequestException**: Thrown for invalid input, returns HTTP 400.
- **UrlNotFoundException**: Thrown when a URL is not found, returns HTTP 404.
- **InternalServerErrorException**: Generic error handler, returns HTTP 500.
