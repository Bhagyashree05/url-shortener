package com.example.url_shortener.model

import jakarta.persistence.*

@Entity
data class Url(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val originalUrl: String
)
