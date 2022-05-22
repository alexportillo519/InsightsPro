package com.alexp.insightspro.networking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InstagramAccountId(
    @Json(name = "data")val data: List<IgAndFbPageId>
)

@JsonClass(generateAdapter = true)
data class IgAndFbPageId(
    @Json(name = "connected_instagram_account")val instagramId: InstagramData,
    @Json(name = "id")val facebookPageId: String
)

@JsonClass(generateAdapter = true)
data class InstagramData(
    @Json(name = "id")val instagramId: String
)
