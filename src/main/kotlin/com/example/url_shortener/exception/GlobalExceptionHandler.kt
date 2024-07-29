package com.example.url_shortener.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<String> {
        logger.error { "Bad request error: ${ex.message}" }
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UrlNotFoundException::class)
    fun handleUrlNotFoundException(ex: UrlNotFoundException): ResponseEntity<String> {
        logger.error { "Not found error: ${ex.message}" }
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InternalServerException::class)
    fun handleInternalServerException(ex: InternalServerException): ResponseEntity<String> {
        logger.error { "Internal server error: ${ex.message}" }
        return ResponseEntity(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<String> {
        logger.error { "Unexpected error occurred: ${ex.message}" }
        return ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)
    }

}