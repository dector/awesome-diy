package io.github.dector.project

import org.hjson.JsonValue
import java.io.File

fun main() {
    val file = File("data/collection.hjson")

    println("Reading from ${file.absolutePath}")

    val data = JsonValue.readHjson(file.reader()).asObject()
    val items = data["items"].asArray()

    println("Found ${items.size()} items")
}
