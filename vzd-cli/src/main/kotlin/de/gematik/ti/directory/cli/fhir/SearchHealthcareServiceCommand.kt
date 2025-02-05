package de.gematik.ti.directory.cli.fhir

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.*
import de.gematik.ti.directory.cli.catching
import de.gematik.ti.directory.fhir.SearchQuery
import de.gematik.ti.directory.fhir.SearchResource
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

class SearchHealthcareServiceCommand : CliktCommand(name = "healthcare-service", help = "Search HealthcareService resources (alias: hs)") {
    private val logger = KotlinLogging.logger {}
    private val context by requireObject<SearchCommand.SearchContext>()

    private val outputFormat by option()
        .switch(
            "--json" to OutputFormat.JSON,
            "--json-ext" to OutputFormat.JSON_EXT,
            "--yaml" to OutputFormat.YAML,
            "--human" to OutputFormat.HUMAN,
            "--table" to OutputFormat.TABLE,
        ).default(OutputFormat.TABLE)

    private val active: Boolean by option("--active", "-a", help = "Filter by active status").flag(default = true)

    private val includeOrganization by option()
        .switch(
            "--include-organization" to "HealthcareService:organization",
            "--exclude-organization" to "",
        ).default("HealthcareService:organization")

    private val includeLocation by option()
        .switch(
            "--include-location" to "HealthcareService:location",
            "--exclude-location" to "",
        ).default("HealthcareService:location")

    private val includeEndpoint by option()
        .switch(
            "--include-endpoint" to "HealthcareService:endpoint",
            "--exclude-endpoint" to "",
        ).default("HealthcareService:endpoint")

    private val textArguments by argument("SEARCH_TEXT").multiple(required = false)
    private val telematikID by option("--telematik-id", "-t", help = "Telematik-ID of the Organization")
    private val professionOID by option("--professionOID", help = "OID of the profession")
    private val summary by option("--summary", "-s", help = "Summary mode").flag()

    private val customParams: Map<String, String> by option(
        "-p",
        "--param",
        help = "Specify query parameters to find matching entries",
        metavar = "NAME=VALUE",
    ).associate()

    override fun run() =
        catching {
            logger.info { "Searching HealthcareService resources in FHIR Directory ${context.ctx.env.name}" }
            val query = SearchQuery(SearchResource.HealthcareService)

            listOf(includeOrganization, includeLocation, includeEndpoint).forEach {
                if (it.isNotEmpty()) {
                    query.addParam("_include", it)
                }
            }

            query.addParam("organization.active", active.toString())

            if (textArguments.isNotEmpty()) {
                query.addParam("_text", textArguments.joinToString(" "))
            }

            if (telematikID != null) {
                query.addParam("organization.identifier", "https://gematik.de/fhir/sid/telematik-id|$telematikID")
            }

            if (professionOID != null) {
                query.addParam("organization.type", "https://gematik.de/fhir/directory/CodeSystem/OrganizationProfessionOID|$professionOID")
            }

            if (summary) {
                query.addParam("_summary", "count")
            }

            customParams.forEach {
                query.addParam(it.key, it.value)
            }

            val bundle =
                runBlocking {
                    context.search(context.ctx, query)
                }

            if (summary) {
                echo("Total: ${bundle.total}")
            } else {
                echo(bundle.toStringOutput(outputFormat))
            }
        }
}
