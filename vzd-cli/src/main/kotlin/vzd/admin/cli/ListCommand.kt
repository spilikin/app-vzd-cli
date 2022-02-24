package vzd.admin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.associate
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.pair
import kotlinx.coroutines.runBlocking
import vzd.admin.client.DirectoryEntry
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.useLines

class ListCommand: CliktCommand(name = "list", help="List directory entries") {
    private val paramFile: Pair<String, String>? by option("-f", "--param-file",
        help="Read parameter values from file", metavar = "PARAM FILENAME").pair()
    private val params: Map<String, String> by option("-p", "--param",
        help="Specify query parameters to find matching entries", metavar = "NAME=VALUE").associate()
    private val context by requireObject<CommandContext>()

    override fun run() = catching {
        paramFile?.let { paramFile ->
            val file = Path(paramFile.second)
            if (!file.exists()) throw CliktError("File not found: ${paramFile.second}")
            file.useLines { line ->
                line.forEach {
                    runQuery(params + Pair(paramFile.first, it))
                }
            }
        } ?: run {
            runQuery(params)
        }
    }

    private fun runQuery(params: Map<String, String>) {
        val result: List<DirectoryEntry>? = if (context.syncMode) {
            runBlocking {  context.client.readDirectoryEntryForSync( params ) }
        } else {
            runBlocking {  context.client.readDirectoryEntry( params ) }
        }

        if (context.outputFormat == OutputFormat.CSV) {
            if (context.firstCommand) {
                context.firstCommand = false
                print('\uFEFF')
                Output.printCsv(DirectoryEntryCsvHeaders)
            }
        }

        DirectoryEntryOutputMapping[context.outputFormat]?.invoke(params, result)
    }

}