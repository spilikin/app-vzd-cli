package de.gematik.ti.directory.cli.admin

import de.gematik.ti.directory.admin.DirectoryEntry
import de.gematik.ti.directory.admin.DirectoryEntryExtSerializer
import de.gematik.ti.directory.cli.toJsonPretty
import de.gematik.ti.directory.cli.toYamlNoDefaults
import de.gematik.ti.directory.pki.ExtendedCertificateDataDERSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.mamoe.yamlkt.Yaml

var YamlDirectoryEntryExt = Yaml {
    encodeDefaultValues = false
    serializersModule = SerializersModule {
        contextual(ExtendedCertificateDataDERSerializer)
    }
}
fun DirectoryEntry.toYamlExt(): String = YamlDirectoryEntryExt.encodeToString(DirectoryEntryExtSerializer, this)
fun List<DirectoryEntry>.toYamlExt(): String = YamlDirectoryEntryExt.encodeToString(ListSerializer(DirectoryEntryExtSerializer), this)

var JsonDirectoryEntryExt = Json {
    encodeDefaults = true
    prettyPrint = true
    serializersModule = SerializersModule {
        contextual(ExtendedCertificateDataDERSerializer)
    }
}
fun DirectoryEntry.toJsonExt(): String = JsonDirectoryEntryExt.encodeToString(DirectoryEntryExtSerializer, this)
fun List<DirectoryEntry>.toJsonExt(): String = JsonDirectoryEntryExt.encodeToString(ListSerializer(DirectoryEntryExtSerializer), this)

fun DirectoryEntry.toStringRepresentation(format: RepresentationFormat): String {
    return when (format) {
        RepresentationFormat.HUMAN -> this.toHuman()
        RepresentationFormat.YAML -> this.toYamlNoDefaults()
        RepresentationFormat.YAML_EXT -> this.toYamlExt()
        RepresentationFormat.JSON -> this.toJsonPretty()
        RepresentationFormat.JSON_EXT -> this.toJsonExt()
        else -> ""
    }
}

fun List<DirectoryEntry>.toStringRepresentation(format: RepresentationFormat): String {
    return when (format) {
        RepresentationFormat.HUMAN -> this.toHuman()
        RepresentationFormat.YAML -> this.toYamlNoDefaults()
        RepresentationFormat.YAML_EXT -> this.toYamlExt()
        RepresentationFormat.JSON -> this.toJsonPretty()
        RepresentationFormat.JSON_EXT -> this.toJsonExt()
        RepresentationFormat.TABLE -> this.toTable()
        RepresentationFormat.CSV -> this.toCsv()
    }
}
