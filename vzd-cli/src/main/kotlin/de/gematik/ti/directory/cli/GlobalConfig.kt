package de.gematik.ti.directory.cli

import de.gematik.ti.directory.cli.util.FileObjectStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.nio.file.Path

@Serializable
data class HttpProxyConfig(
    var proxyURL: String,
    var enabled: Boolean,
)

@Serializable
data class UpdatesConfig(
    var preReleasesEnabled: Boolean,
    var lastCheck: Long,
    var latestRelease: String,
    var enabled: Boolean = true,
)

@Serializable
data class GlobalConfig(
    var httpProxy: HttpProxyConfig,
    var updates: UpdatesConfig,
)

internal class GlobalConfigFileStore(
    customConfigPath: Path? = null
) : FileObjectStore<GlobalConfig>(
        "directory-global.yaml",
        {
            GlobalConfig(
                httpProxy =
                    HttpProxyConfig(
                        proxyURL = "http://192.168.110.10:3128/",
                        enabled = false,
                    ),
                updates =
                    UpdatesConfig(
                        preReleasesEnabled = false,
                        lastCheck = -1,
                        latestRelease = BuildConfig.APP_VERSION,
                        enabled = true,
                    ),
            )
        },
        { yaml, stringValue -> yaml.decodeFromString(stringValue) },
        customConfigPath,
    )
