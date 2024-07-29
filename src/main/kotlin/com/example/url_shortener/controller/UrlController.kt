package com.example.url_shortener.controller

import com.example.url_shortener.dto.OriginalUrlRequest
import com.example.url_shortener.dto.OriginalUrlResponse
import com.example.url_shortener.dto.ShortenUrlRequest
import com.example.url_shortener.dto.ShortenUrlResponse
import com.example.url_shortener.service.UrlService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/v1/url")
class UrlController(private val urlService: UrlService) {

    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody request: ShortenUrlRequest): ResponseEntity<ShortenUrlResponse> {
        val response = urlService.shortenUrl(request)
        logger.info { "Shortened URL created: ${response.shortUrl}" }
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @PostMapping("/resolve")
    fun getOriginalUrl(@RequestBody request: OriginalUrlRequest): ResponseEntity<OriginalUrlResponse> {
        val response = urlService.getOriginalUrl(request)
        logger.info { "Original URL resolved: ${response.originalUrl}" }
        return ResponseEntity(response, HttpStatus.OK)
    }
}