package com.example.url_shortener.repository

import com.example.url_shortener.model.Url
import org.springframework.data.jpa.repository.JpaRepository

interface UrlRepository : JpaRepository<Url, Long> {
    fun findByOriginalUrl(originalUrl: String): Url?
}