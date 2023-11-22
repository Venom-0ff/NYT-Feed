package com.skostyukov.nytfeed.model

data class Result(
    var title: String,
    var byline: String,
    var first_published_date: String,
    var url: String,
    var abstract: String,
    var multimedia: List<Media>
)