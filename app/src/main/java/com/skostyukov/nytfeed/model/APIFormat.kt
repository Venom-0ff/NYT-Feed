package com.skostyukov.nytfeed.model

data class APIFormat (
    var copyright: String,
    var results: List<Result>
)