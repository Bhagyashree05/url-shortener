package com.example.url_shortener.controller

import com.example.url_shortener.dto.OriginalUrlRequest
import com.example.url_shortener.dto.OriginalUrlResponse
import com.example.url_shortener.dto.ShortenUrlRequest
import com.example.url_shortener.dto.ShortenUrlResponse
import com.example.url_shortener.exception.UrlNotFoundException
import com.example.url_shortener.service.UrlService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UrlController::class)
@ExtendWith(MockitoExtension::class)
class UrlControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var urlService: UrlService

    @Test
    fun `should shorten URL`() {
        val originalUrl = "http://example.com"
        val shortUrl = "MQ=="
        val request = ShortenUrlRequest(originalUrl)
        val response = ShortenUrlResponse(shortUrl)

        Mockito.`when`(urlService.shortenUrl(request)).thenReturn(response)

        mockMvc.perform(post("/api/v1/url/shorten")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"originalUrl": "$originalUrl"}"""))
            .andExpect(status().isCreated)
            .andExpect(content().json("""{"shortUrl": "$shortUrl"}"""))
    }

    @Test
    fun `should return original URL`() {
        val shortUrl = "MQ=="
        val originalUrl = "http://example.com"
        val request = OriginalUrlRequest(shortUrl)
        val response = OriginalUrlResponse(originalUrl)

        Mockito.`when`(urlService.getOriginalUrl(request)).thenReturn(response)

        mockMvc.perform(post("/api/v1/url/resolve")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"shortUrl": "$shortUrl"}"""))
            .andExpect(status().isOk)
            .andExpect(content().json("""{"originalUrl": "$originalUrl"}"""))
    }

    @Test
    fun `should return 404 when URL not found`() {
        val shortUrl = "MQ=="
        val request = OriginalUrlRequest(shortUrl)

        Mockito.`when`(urlService.getOriginalUrl(request)).thenThrow(UrlNotFoundException("URL not found"))

        mockMvc.perform(post("/api/v1/url/resolve")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"shortUrl": "$shortUrl"}"""))
            .andExpect(status().isNotFound)
    }
}