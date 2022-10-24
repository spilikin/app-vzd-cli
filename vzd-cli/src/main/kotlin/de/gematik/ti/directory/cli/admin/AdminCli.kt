package de.gematik.ti.directory.cli.admin

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import de.gematik.ti.directory.admin.*
import de.gematik.ti.directory.cli.admin.compat.CmdCommand
import de.gematik.ti.directory.util.GenericDirectoryExceptionException
import de.gematik.ti.directory.util.PKIClient
import de.gematik.ti.directory.util.VaultException
import io.ktor.client.network.sockets.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Must love Kotlin - create a simple try / catch function and use in all classes that throws these exceptions
 */
fun catching(throwingBlock: () -> Unit = {}) {
    try {
        throwingBlock()
    } catch (e: GenericDirectoryExceptionException) {
        throw CliktError(e.message)
    } catch (e: AdminResponseException) {
        throw CliktError(e.details)
    } catch (e: SerializationException) {
        throw CliktError(e.message)
    } catch (e: VaultException) {
        throw CliktError(e.message)
    } catch (e: ConnectTimeoutException) {
        throw CliktError("${e.message}. Try using proxy: vzd-cli admin -x ...")
    } catch (e: io.ktor.http.parsing.ParseException) {
        if (e.message.contains("Expected `=` after parameter key ''")) {
            throw CliktError("ACCESS_TOKEN is invalid. Please login again using `vzd-cli admin login`.")
        } else {
            throw e
        }
    } catch (e: IllegalStateException) {
        // dirty, but no other way atm
        if (e.message?.contains("Unsupported byte code, first byte is 0xfc") == true) {
            throw CliktError("ACCESS_TOKEN is invalid. Please login again using `vzd-cli admin login`.")
        } else {
            throw e
        }
    }
}

class CommandContext(
    private val clientDelegate: CommandContext.() -> Client,
    private val pkiClientDelegate: CommandContext.() -> PKIClient,
    var outputFormat: OutputFormat,
    val env: AdminEnvironment,
    val enableOcsp: Boolean,
    var firstCommand: Boolean = true
) {

    val adminAPI = AdminAPI()

    val client by lazy {
        clientDelegate.invoke(this)
    }

    val pkiClient by lazy {
        pkiClientDelegate.invoke(this)
    }
}

class DirectoryAdministrationCli :
    CliktCommand(name = "admin", help = """CLI for DirectoryAdministration API""".trimMargin()) {
    private val outputFormat by option().switch(
        "--human" to OutputFormat.HUMAN,
        "--json" to OutputFormat.JSON,
        "--yaml" to OutputFormat.YAML,
        "--csv" to OutputFormat.CSV,
        "--short" to OutputFormat.TABLE,
        "--table" to OutputFormat.TABLE
    )
        .default(OutputFormat.HUMAN)
        .deprecated("DEPRECATED: Specify the format on specific sub-command.")

    private val env by option(
        "-e",
        "--env",
        help = "Environment. Either tu, ru or pu. If not specified default env is used."
    )
        .choice("tu", "ru", "pu")
        .deprecated("DEPRECATED: Switch environment globally using vzd-cli admin login <ENV>")

    private val enableOcsp: Boolean by option(
        "-o",
        "--ocsp",
        help = "Validate certificates using OCSP"
    )
        .flag()

    private val useProxy: Boolean? by option(
        "--proxy-on",
        "-x",
        help = "Forces the use of the proxy, overrides the configuration"
    )
        .flag("--proxy-off", "-X")

    override fun run() = catching {
        val provider = FileConfigProvider()
        val clientEnvStr =
            env ?: provider.config.currentEnvironment ?: throw CliktError("Default environment is not configured")

        val clientEnv = AdminEnvironment.valueOf(clientEnvStr.uppercase())

        val clientDelegate: CommandContext.() -> Client = {
            logger.info { "Using environment: $clientEnv" }
            adminAPI.createClient(clientEnv)
        }

        val pkiClientDelegate: CommandContext.() -> PKIClient = {
            PKIClient {
                if (provider.config.httpProxy.enabled || useProxy == true) {
                    httpProxyURL = provider.config.httpProxy.proxyURL
                }
            }
        }

        currentContext.obj = CommandContext(clientDelegate, pkiClientDelegate, outputFormat, clientEnv, enableOcsp)
    }

    init {
        subcommands(
            VaultCommand(), ConfigCommand(),
            LoginCommand(), LoginCredCommand(), TokenCommand(),
            Info(), ListCommand(), TemplateCommand(),
            AddBaseCommand(), LoadBaseCommand(), EditBaseCommand(),
            ModifyBaseCommand(), ModifyBaseAttrCommand(), DeleteCommand(),
            ListCertCommand(), AddCertCommand(), SaveCertCommand(), DeleteCertCommand(), ClearCertCommand(),
            CertInfoCommand(), DumpCommand(), CmdCommand(), SearchCommand(),
        )
    }
}

class Info : CliktCommand(name = "info", help = "Show information about the API") {
    private val context by requireObject<CommandContext>()

    override fun run() = catching {
        val info = runBlocking { context.client.getInfo() }
        Output.printHuman(info)
    }
}
