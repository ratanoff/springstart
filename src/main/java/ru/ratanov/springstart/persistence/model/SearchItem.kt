package ru.ratanov.springstart.persistence.model

class SearchItem {

    var title: String? = null
    var link: String? = null
    var size: String? = null
    var seeds: String? = null
    var date: String? = null

    override fun toString(): String {
        return "$title($seeds)"
    }
}