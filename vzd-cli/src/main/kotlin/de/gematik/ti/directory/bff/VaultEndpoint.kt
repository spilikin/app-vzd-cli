package de.gematik.ti.directory.bff

import de.gematik.ti.directory.admin.AdminEnvironment
import de.gematik.ti.directory.util.KeyStoreVaultProvider
import de.gematik.ti.directory.util.VaultException
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("vault")
class VaultResource()

@Serializable
data class OpenVaultRequest(val vaultPassword: String)

fun Route.vaultRoute() {
    post<VaultResource> {
        val vaultPassword = call.receive<OpenVaultRequest>().vaultPassword
        try {
            val vault = KeyStoreVaultProvider().open(vaultPassword)

            val adminAPI = call.application.attributes[AdminAPIKey]

            vault.list().forEach { secret ->
                application.log.info("Logging in to: ${secret.variant}")
                adminAPI.login(AdminEnvironment.valueOf(secret.variant.uppercase()), secret.name, secret.secret)
            }

            call.respond(Outcome("Used vault to login to: ${vault.list().map { it.variant }.joinToString()}"))
        } catch (e: VaultException) {
            call.respond(status = HttpStatusCode.Forbidden, Outcome("Unauthorised"))
        }
    }
}
