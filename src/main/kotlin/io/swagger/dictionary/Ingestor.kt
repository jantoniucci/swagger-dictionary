package io.swagger.dictionary

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.ajalt.clikt.output.TermUi
import io.swagger.dictionary.model.*
import java.io.File

class Ingestor(dictionary: Dictionary) {

    private val dictionary: Dictionary = dictionary

    fun ingestSwaggers(swaggerFiles: List<File>, source : SourceEnum) {
        // TODO: Refactor pending to support async processing
        swaggerFiles?.forEach {
            TermUi.echo("  - Processing swagger file : ${it.absolutePath} ")
            ingestSwagger(it, source)
        }
    }

    private fun ingestSwagger(swaggerFile: File, source : SourceEnum) {
        val openApiRoot = getOpenApiRoot(swaggerFile)
        ingestSwaggerPaths(openApiRoot, source)
        ingestSwaggerSchemas(openApiRoot, source)
    }

    private fun getOpenApiRoot(swaggerFile: File): JsonNode {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())
        return mapper.readTree(swaggerFile)
    }

    private fun ingestSwaggerSchemas(openApiRoot: JsonNode, source: SourceEnum) {
        openApiRoot.path("definitions").fields()?.forEach {
            TermUi.echo("    · Schema : ${it.key} ")
            dictionary.add(Term(
                    id = it.key,
                    name = it.key,
                    type = TypeEnum.OBJECT,
                    dataType = it.value.get("type")?.textValue() ?: "OBJECT",
                    dataFormat = "",
                    description = it.key,
                    state = StateEnum.IDENTIFIED,
                    scheme = it.value.get("properties")?.toString() ?: it.value.get("items")?.toString() ?: "",
                    example = "", // TODO: extract example
                    contributor = source.name,
                    source = source
            )
            )
        }
    }

    private fun ingestSwaggerPaths(openApiRoot: JsonNode, source: SourceEnum) {
        openApiRoot.path("paths").fields()?.forEach {
            TermUi.echo("    · Path : ${it.key} ")
            it.value.fields()?.forEach {
                TermUi.echo("        · Operation : ${it.key} ")
                ingestSwaggerOperations(it.value, source)
            }
            ingestSwaggerResponse(it.value, source)
        }
    }

    private fun ingestSwaggerOperations(operation: JsonNode, source: SourceEnum) {
        operation.get("parameters")?.elements()?.forEach {
            if ("header".equals(it.get("in").textValue())) {
                TermUi.echo("          · Parameter : ${it.get("name").textValue()} ")
                dictionary.add(Term(
                        id = it.get("name").textValue(),
                        name = it.get("name").textValue(),
                        type = TypeEnum.HEADER,
                        dataType = it.get("type").textValue(),
                        dataFormat = "",
                        description = it.get("description")?.textValue() ?: "",
                        state = StateEnum.IDENTIFIED,
                        scheme = "",
                        example = "", // TODO: extract example
                        contributor = source.name,
                        source = source
                    )
                )
            }
        }
    }

    private fun ingestSwaggerResponse(operations: JsonNode, source: SourceEnum) {
        operations?.forEach {
            it.get("responses")?.fields()?.forEach {
                it.value.fields().forEach() {
                    if ("headers".equals(it.key)) {
                        it.value.fields().forEach {
                            TermUi.echo("          · Parameter : ${it.key} ")
                            dictionary.add(Term(
                                    id = it.key,
                                    name = it.key,
                                    type = TypeEnum.HEADER,
                                    dataType = it.value.get("type")?.textValue() ?: "",
                                    dataFormat = "",
                                    description = it.value.get("description")?.textValue() ?: "",
                                    state = StateEnum.IDENTIFIED,
                                    scheme = "",
                                    example = "", // TODO: extract example
                                    contributor = source.name,
                                    source = source
                            )
                            )
                        }
                    }
                }
            }
        }
    }

}