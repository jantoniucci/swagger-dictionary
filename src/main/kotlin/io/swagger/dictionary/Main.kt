package io.swagger.dictionary

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import io.swagger.dictionary.model.Dictionary
import io.swagger.dictionary.model.SourceEnum
import java.io.File

class Main : CliktCommand(name = "swagger-dictionary",
                          help = "Creates and validates a terms dictionary with a swagger file") {

    private val dictionaryFile by argument("dictionary", help = "The dictionary path and file name.")
            .file()

    private val swaggerFiles by option("-s", "--swagger", help = "The swagger path and file name")
            .file(exists = true)
            .multiple()

    val dictionary: Dictionary = Dictionary()

    override fun run() {
        printStartingMessage()
        val ingestor = Ingestor(dictionary)
        ingestor.ingestSwaggers(swaggerFiles, SourceEnum.CI_AUTOMATIONS)
        saveDictionary()
    }

    private fun saveDictionary() {
        echo("")
        echo("* Extracted dictionary:")
        echo("")
        val dictionaryJson = dictionary.asJson()
        echo(dictionaryJson)
        echo("")
        dictionaryFile.writeText(dictionaryJson)
    }

    fun printStartingMessage() {
        echo("")
        echo("* Starting...")
        echo("")
        for (file in swaggerFiles) {
            echo("  - swagger file : ${file.absolutePath} ")
        }
        echo("  - dictionary file : ${dictionaryFile.absolutePath} ")
        echo("")
        echo("* Running...")
        echo("")
    }
}

fun main(args: Array<String>) = Main().main(args)
