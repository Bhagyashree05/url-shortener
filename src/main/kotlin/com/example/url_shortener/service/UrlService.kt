package com.example.url_shortener.service

import com.example.url_shortener.dto.OriginalUrlRequest
import com.example.url_shortener.dto.OriginalUrlResponse
import com.example.url_shortener.dto.ShortenUrlRequest
import com.example.url_shortener.dto.ShortenUrlResponse
import com.example.url_shortener.exception.BadRequestException
import com.example.url_shortener.exception.UrlNotFoundException
import com.example.url_shortener.model.Url
import com.example.url_shortener.repository.UrlRepository
import org.apache.commons.codec.binary.Base64
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.stereotype.Service
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Service
class UrlService(private val urlRepository: UrlRepository) {

    private val base64 = Base64(true)
    private val urlValidator = UrlValidator(arrayOf("http", "https"))

    fun shortenUrl(request: ShortenUrlRequest): ShortenUrlResponse {
        if (!urlValidator.isValid(request.originalUrl)) {
            logger.warn { "Invalid URL: ${request.originalUrl}" }
            throw BadRequestException("Invalid URL")
        }

        val existingUrl = urlRepository.findByOriginalUrl(request.originalUrl)
        val url = existingUrl ?: urlRepository.save(Url(originalUrl = request.originalUrl))
        val shortUrl = base64.encodeToString(url.id.toString().toByteArray())
        logger.info { "URL shortened: ${request.originalUrl} -> $shortUrl" }
        return ShortenUrlResponse(shortUrl)
    }

    fun getOriginalUrl(request: OriginalUrlRequest): OriginalUrlResponse {
        val id = String(base64.decode(request.shortUrl)).toLongOrNull()
        if (id == null) {
            logger.warn { "Invalid short URL: ${request.shortUrl}" }
            throw BadRequestException("Invalid short URL")
        }
        val url = urlRepository.findById(id).orElseThrow { UrlNotFoundException("URL not found") }
        logger.info { "URL resolved: ${request.shortUrl} -> ${url.originalUrl}" }
        return OriginalUrlResponse(url.originalUrl)
    }
}