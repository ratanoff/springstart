package ru.ratanov.springstart.persistence.model


data class Filter(val title: String, val key: String, val params: List<Param>)
data class Param(val value: String, val name: String)