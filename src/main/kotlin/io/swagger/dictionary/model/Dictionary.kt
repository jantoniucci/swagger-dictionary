package io.swagger.dictionary.model

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class Dictionary {
    val mapper = jacksonObjectMapper()
    val dictionary: ArrayList<Term> = ArrayList()

    fun add(t: Term) {
        dictionary.add(t)
    }

    fun asJson(): String {
        return mapper.writeValueAsString(dictionary)
    }
}