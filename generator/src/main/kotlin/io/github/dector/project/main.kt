package io.github.dector.project

import net.steppschuh.markdowngenerator.link.Link
import net.steppschuh.markdowngenerator.table.Table
import net.steppschuh.markdowngenerator.text.Text
import net.steppschuh.markdowngenerator.text.code.Code
import org.hjson.JsonArray
import org.hjson.JsonValue
import java.io.File

fun main() {
    val file = File("data/collection.hjson")

    println("Reading from ${file.absolutePath}")

    val data = JsonValue.readHjson(file.reader()).asObject()
    val items = data["items"].asArray()

    println("Found ${items.size()} items")

    val outFile = File("out/README.md")
        .also { it.parentFile.mkdirs() }

    println("Writing to ${outFile.absolutePath}.")
    outFile.writeText(generateMarkdown(items))
    println("Done.")
}

fun generateMarkdown(items: JsonArray): String = buildString {
    run {
        val table = Table.Builder()
            .addRow("Title", "Description", "Tags")

        items.asSequence()
            .map { it.asObject() }
            .filter { it["type"].asString() == "link" }
            .forEach { item ->
                table.addRow(
                    Link(item["title"].asString(), item["url"].asString()),
                    item["description"].asString(),
                    Text(
                        item["tags"].asString()
                            .split(",")
                            .map { Code(it.trim()) }
                    )
                )
            }
        append(table.build())
    }
}
