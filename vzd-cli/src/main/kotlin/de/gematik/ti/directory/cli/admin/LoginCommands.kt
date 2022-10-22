package de.gematik.ti.directory.cli.admin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.enum
import de.gematik.ti.directory.admin.AdminEnvironment
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import java.util.*

private fun doLogin(context: CommandContext, env: AdminEnvironment, overrideProxy: Boolean?, clientID: String, clientSecret: String) {

    context.adminAPI.config.currentEnvironment = env.title
    context.adminAPI.config.httpProxy.enabled = overrideProxy ?: context.adminAPI.config.httpProxy.enabled
    context.adminAPI.updateConfig()

    val claims = context.adminAPI.login(env, clientID, clientSecret)

    TermUi.echo("Login successful. Environment set to: $env")

    claims["exp"]?.jsonPrimitive?.int?.let {
        val expDate = Date(it.toLong() * 1000)
        TermUi.echo("Token valid until $expDate")
    }
}

class LoginCommand : CliktCommand(name = "login", help = "Login to OAuth2 Server and store token(s)") {
    private val context by requireObject<CommandContext>()
    private val env by argument().enum<AdminEnvironment>(ignoreCase = true, key = { it.title })
    private val overrideProxy: Boolean? by option(
        "-x",
        "--proxy-on",
        help = "Forces the use of the proxy, overrides the configuration"
    )
        .flag("--proxy-off", "-X")

    private val password by option("--password", "-p", help = "Password for protection of the Vault")
        .prompt("Enter Vault Password", hideInput = true)

    override fun run() = catching {
        val vault = vaultProvider.open(password)
        val secret = vault.get(env.title) ?: throw CliktError("Secret for env '$env' not found in Vault")
        doLogin(context, env, overrideProxy, secret.name, secret.secret)
    }
}

class LoginCredCommand : CliktCommand(name = "login-cred", help = "Login using the client credentials") {
    private val context by requireObject<CommandContext>()
    private val env by argument().enum<AdminEnvironment>(ignoreCase = true, key = { it.title })
    private val overrideProxy: Boolean? by option(
        "-x",
        "--proxy-on",
        help = "Forces the use of the proxy, overrides the configuration"
    )
        .flag("--proxy-off", "-X")
    private val clientId by option("--client-id", "-c", help = "OAuth2 client id", envvar = "CLIENT_ID").required()
    private val clientSecret by option("--secret", "-s", help = "OAuth2 client secret", envvar = "CLIENT_SECRET").required()

    override fun run() = catching {
        doLogin(context, env, overrideProxy, clientId, clientSecret)
    }
}
