package io.swagger.dictionary

import org.junit.Test
import java.io.File
import java.nio.charset.Charset

class MainTest {
    @Test
    fun `Integration test - Read PetStore and write its dictionary`() {
        assert(
                executeIngestorAndCheckResult(
                        "petstore-simple"
                )
        )
    }

    @Test
    fun `Integration test - Read a full PetStore and write its dictionary`() {
        assert(
                executeIngestorAndCheckResult(
                        "petstore"
                )
        )
    }

    @Test
    fun `Integration test - Read an extended PetStore and write its dictionary`() {
        assert(
                executeIngestorAndCheckResult(
                        "petstore-extended"
                )
        )
    }

    fun executeIngestorAndCheckResult(swaggerSample: String): Boolean {
        val tmpDictionaryFile = File.createTempFile("dictionary-", ".json").absolutePath
        val main = Main()
        main.main( arrayOf(tmpDictionaryFile, "--swagger=/Users/jrai/swagger-dictionary/src/test/resources/${swaggerSample}.yaml")  );
        return File(tmpDictionaryFile).readText(Charsets.UTF_8).compareTo(
                        File("/Users/jrai/swagger-dictionary/src/test/resources/${swaggerSample}.dictionary.json")
                                .readText(Charsets.UTF_8)) == 0
    }
}