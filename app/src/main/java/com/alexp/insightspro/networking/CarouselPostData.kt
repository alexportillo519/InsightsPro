package com.alexp.insightspro.networking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarouselPostData(
    @Json(name = "data") val carouselData: List<CarouselImageUrl>?
)

@JsonClass(generateAdapter = true)
data class CarouselImageUrl(
    @Json(name = "media_url") val carouselImageUrl: String?
)