package io.swagger.dictionary.model

enum class TypeEnum(val type: String) {
    OBJECT("OBJECT"),
    PARAMETER_URI("PARAMETER_URI"),
    PARAMETER_QUERY("PARAMETER_QUERY"),
    HEADER("HEADER"),
    UNIDENTIFIED("UNIDENTIFIED")
}

enum class StateEnum(val state: String) {
    IDENTIFIED("IDENTIFIED"),
    PROPOSED("PROPOSED"),
    DETECTED("DETECTED"),
    ACCEPTED("ACCEPTED"),
    REFUSED("REFUSED"),
    DEPRECATED("DEPRECATED"),
    DELETED("DELETED")
}

enum class SourceEnum(val source: String) {
    CI_AUTOMATIONS("CI_AUTOMATIONS"),
    WEB_CONSOLE("WEB_CONSOLE"),
    MANUAL("MANUAL")
}

data class Term ( val id : String,
                  val name : String,
                  val type : TypeEnum,
                  val dataType : String,
                  val dataFormat : String,
                  val description : String,
                  val state : StateEnum,
                  val scheme : String,
                  val example : String,
                  val contributor : String,
                  val source : SourceEnum  )