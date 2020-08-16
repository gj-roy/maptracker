package com.loitp.model

class Movie {
    var title: String? = null
    var genre: String? = null
    var year: String? = null
    var cover: String? = null
    var isBottom: Boolean = false

    constructor() {}

    constructor(title: String, genre: String, year: String, cover: String) {
        this.title = title
        this.genre = genre
        this.year = year
        this.cover = cover
    }
}
