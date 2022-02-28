package vzd.admin.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import net.mamoe.yamlkt.Yaml
import org.bouncycastle.util.encoders.Base64
import vzd.admin.client.CertificateDataDER
import vzd.admin.client.UserCertificate
import vzd.admin.client.VZDResponseException
import vzd.admin.client.toCertificateInfo
import kotlin.io.path.inputStream

class AddCertCommand : CliktCommand(name = "add-cert", help = "Add certificate to existing DirectoryEntry") {
    private val logger = KotlinLogging.logger {}

    //val inputFormat by option("--inform", "-i").choice("der", "pem")
    private val files by argument().path(mustBeReadable = true).multiple()
    private val ignore by option("--ignore", "-i", help = "Ignore Error 409 (certificate exists).").flag()


    private val context by requireObject<CommandContext>()
    override fun run() = catching {
        files.forEach {
            val certB64 = Base64.toBase64String(it.inputStream().readBytes())
            val userCertificate = UserCertificate(userCertificate = CertificateDataDER(certB64))
            val certificateInfo = userCertificate.userCertificate!!.toCertificateInfo()
            logger.info { "Adding Certificate ${Yaml.encodeToString(userCertificate.userCertificate?.toCertificateInfo())}" }

            val entries = runBlocking {
                context.client.readDirectoryEntry(mapOf("telematikID" to certificateInfo.admissionStatement.registrationNumber))
            }

            entries?.first()?.let {
                logger.info { "Found matching Entry: ${it.directoryEntryBase.dn?.uid} ${it.directoryEntryBase.displayName}" }
                runBlocking {
                    try {
                        context.client.addDirectoryEntryCertificate(it.directoryEntryBase.dn?.uid!!,
                            userCertificate)
                    } catch (e: VZDResponseException) {
                        if (!ignore || e.response.status != HttpStatusCode.Conflict) {
                            throw e
                        }
                        logger.warn { "Certificate with serialNumber=${userCertificate.userCertificate?.toCertificateInfo()?.serialNumber} already exists. Ignoring conflict." }
                    }

            }
            }
                ?: run { throw CliktError("Entry with telematikID ${certificateInfo.admissionStatement.registrationNumber} not found.") }
        }

    }
}
