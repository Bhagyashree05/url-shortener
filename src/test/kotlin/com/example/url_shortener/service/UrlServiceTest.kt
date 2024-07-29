package com.example.url_shortener.service

import com.example.url_shortener.dto.OriginalUrlRequest
import com.example.url_shortener.dto.ShortenUrlRequest
import com.example.url_shortener.exception.BadRequestException
import com.example.url_shortener.exception.UrlNotFoundException
import com.example.url_shortener.model.Url
import com.example.url_shortener.repository.UrlRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UrlServiceTest {

    @Mock
    private lateinit var urlRepository: UrlRepository

    @InjectMocks
    private lateinit var urlService: UrlService

    @Test
    fun `should shorten URL`() {
        val originalUrl = "http://example.com"
        val url = Url(id = 1, originalUrl = originalUrl)
        val request = ShortenUrlRequest(originalUrl)

        Mockito.`when`(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(null)
        Mockito.`when`(urlRepository.save(Mockito.any(Url::class.java))).thenReturn(url)

        val response = urlService.shortenUrl(request)
        assertNotNull(response.shortUrl)
        Mockito.verify(urlRepository).save(Mockito.any(Url::class.java))
    }

    @Test
    fun `should return original URL`() {
        val shortUrl = "MQ=="
        val url = Url(id = 1, originalUrl = "http://example.com")
        val request = OriginalUrlRequest(shortUrl)

        Mockito.`when`(urlRepository.findById(1)).thenReturn(Optional.of(url))

        val response = urlService.getOriginalUrl(request)
        assertEquals(url.originalUrl, response.originalUrl)
    }

    @Test
    fun `should throw UrlNotFoundException when URL not found`() {
        val shortUrl = "MQ=="
        val request = OriginalUrlRequest(shortUrl)

        Mockito.`when`(urlRepository.findById(1)).thenReturn(Optional.empty())

        assertThrows<UrlNotFoundException> {
            urlService.getOriginalUrl(request)
        }
    }

    @Test
    fun `should throw BadRequestException for invalid short URL`() {
        val shortUrl = "invalid"
        val request = OriginalUrlRequest(shortUrl)

        assertThrows<BadRequestException> {
            urlService.getOriginalUrl(request)
        }
    }

    @Test
    fun `should throw BadRequestException for invalid original URL`() {
        val invalidUrl = "invalid-url"
        val request = ShortenUrlRequest(invalidUrl)

        assertThrows<BadRequestException> {
            urlService.shortenUrl(request)
        }
    }
}