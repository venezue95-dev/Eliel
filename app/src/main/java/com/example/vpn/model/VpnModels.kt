package com.example.vpn.model

enum class VpnState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    DISCONNECTING,
    ERROR
}

enum class VpnProtocol(
    val displayName: String,
    val description: String,
    val defaultPort: Int,
    val cipherInfo: String
) {
    WIREGUARD(
        displayName = "WireGuard Turbo",
        description = "Next-gen ChaCha20-Poly1305 curve25519 tunnel with ultra-low latency and peak throughput",
        defaultPort = 51820,
        cipherInfo = "ChaCha20-Poly1305 / Noise_IK"
    ),
    OPENVPN_UDP(
        displayName = "OpenVPN UDP",
        description = "Industry standard 256-bit AES-GCM data channel optimized for high-speed streaming",
        defaultPort = 1194,
        cipherInfo = "AES-256-GCM / SHA384"
    ),
    OPENVPN_TCP(
        displayName = "OpenVPN TCP",
        description = "Error-correcting stream designed to reliably penetrate strict restrictive firewalls",
        defaultPort = 443,
        cipherInfo = "AES-256-CBC / TLS 1.3"
    ),
    IKEV2(
        displayName = "IKEv2 Stealth",
        description = "MOBIKE protocol providing seamless handover during Wi-Fi to cellular switches",
        defaultPort = 500,
        cipherInfo = "AES-256-GCM / Diffie-Hellman 19"
    ),
    SHADOWSOCKS(
        displayName = "Shadowsocks Obfuscation",
        description = "Asymmetric stream cipher that masks VPN traffic as ordinary HTTPS web traffic",
        defaultPort = 8388,
        cipherInfo = "AEAD_CHACHA20_POLY1305"
    );

    fun getLocalizedDescription(strings: VpnStrings): String {
        return when (this) {
            WIREGUARD -> strings.protoWireguardDesc
            OPENVPN_UDP -> strings.protoOpenVpnUdpDesc
            OPENVPN_TCP -> strings.protoOpenVpnTcpDesc
            IKEV2 -> strings.protoIkev2Desc
            SHADOWSOCKS -> strings.protoShadowsocksDesc
        }
    }
}

enum class AppThemeMode(
    val defaultTitle: String,
    val defaultSubtitle: String,
    val iconEmoji: String
) {
    SYSTEM("Automático (Sistema)", "Sigue automáticamente el modo claro u oscuro del dispositivo", "📱"),
    WHITE_NAVY("Blanco & Azul #00012F", "Modo claro blanco con elegante color principal #00012F", "☀️"),
    CYBER_DARK("Cyber Obsidian", "Contraste cibernético sobre fondo oscuro de alta tecnología", "🌌"),
    AMOLED_BLACK("AMOLED Pitch Black", "Negro absoluto (#000000) para máximo ahorro de batería OLED", "🖤"),
    DEEP_NAVY("Deep Tactical Navy", "Tonalidades navales tácticas con acentos en cian", "⚓"),
    NEON_MATRIX("Matrix Terminal", "Terminal hacker con brillo esmeralda cibernético", "🟩");

    fun getTitle(strings: VpnStrings): String {
        return when (this) {
            SYSTEM -> strings.themeSystemTitle
            WHITE_NAVY -> strings.themeWhiteNavyTitle
            CYBER_DARK -> strings.themeCyberDarkTitle
            AMOLED_BLACK -> strings.themeAmoledTitle
            DEEP_NAVY -> strings.themeDeepNavyTitle
            NEON_MATRIX -> strings.themeNeonMatrixTitle
        }
    }

    fun getSubtitle(strings: VpnStrings): String {
        return when (this) {
            SYSTEM -> strings.themeSystemSub
            WHITE_NAVY -> strings.themeWhiteNavySub
            CYBER_DARK -> strings.themeCyberDarkSub
            AMOLED_BLACK -> strings.themeAmoledSub
            DEEP_NAVY -> strings.themeDeepNavySub
            NEON_MATRIX -> strings.themeNeonMatrixSub
        }
    }
}

data class VpnServer(
    val id: String,
    val name: String,
    val city: String,
    val country: String,
    val countryCode: String,
    val region: String, // "North America", "Europe", "Asia-Pacific", "Latin America", "Middle East & Africa"
    val flagEmoji: String,
    val ipAddress: String,
    val port: Int,
    val pingMs: Int,
    val loadPercent: Int,
    val latitude: Double,
    val longitude: Double,
    val supportedProtocols: List<VpnProtocol> = listOf(
        VpnProtocol.WIREGUARD,
        VpnProtocol.OPENVPN_UDP,
        VpnProtocol.IKEV2,
        VpnProtocol.SHADOWSOCKS
    ),
    val isAuto: Boolean = false
) {
    fun getLocalizedName(strings: VpnStrings): String = if (isAuto) strings.autoServerName else name
    fun getLocalizedCity(strings: VpnStrings): String = if (isAuto) strings.autoServerCity else city
    fun getLocalizedCountry(strings: VpnStrings): String = if (isAuto) strings.autoServerCountry else country

    fun formattedCoordinates(): String {
        val latDir = if (latitude >= 0) "N" else "S"
        val lonDir = if (longitude >= 0) "E" else "W"
        return String.format(java.util.Locale.US, "%.4f° %s, %.4f° %s", Math.abs(latitude), latDir, Math.abs(longitude), lonDir)
    }
}

data class VpnTrafficStats(
    val bytesIn: Long = 0L,
    val bytesOut: Long = 0L,
    val downloadSpeedBps: Long = 0L,
    val uploadSpeedBps: Long = 0L,
    val durationSeconds: Long = 0L,
    val currentPingMs: Int = 0,
    val packetCount: Long = 0L
) {
    fun formattedDownloadSpeed(): String {
        return formatSpeed(downloadSpeedBps)
    }

    fun formattedUploadSpeed(): String {
        return formatSpeed(uploadSpeedBps)
    }

    fun formattedTotalData(): String {
        val totalBytes = bytesIn + bytesOut
        return formatBytes(totalBytes)
    }

    fun formattedDuration(): String {
        val hours = durationSeconds / 3600
        val minutes = (durationSeconds % 3600) / 60
        val seconds = durationSeconds % 60
        return if (hours > 0) {
            String.format(java.util.Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds)
        }
    }

    private fun formatSpeed(bytesPerSec: Long): String {
        return when {
            bytesPerSec >= 1024 * 1024 -> String.format(java.util.Locale.US, "%.2f MB/s", bytesPerSec.toDouble() / (1024 * 1024))
            bytesPerSec >= 1024 -> String.format(java.util.Locale.US, "%.1f KB/s", bytesPerSec.toDouble() / 1024)
            else -> "$bytesPerSec B/s"
        }
    }

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 * 1024 -> String.format(java.util.Locale.US, "%.2f GB", bytes.toDouble() / (1024 * 1024 * 1024))
            bytes >= 1024 * 1024 -> String.format(java.util.Locale.US, "%.1f MB", bytes.toDouble() / (1024 * 1024))
            bytes >= 1024 -> String.format(java.util.Locale.US, "%.1f KB", bytes.toDouble() / 1024)
            else -> "$bytes B"
        }
    }
}

data class VpnConfig(
    val protocol: VpnProtocol = VpnProtocol.WIREGUARD,
    val killSwitchEnabled: Boolean = true,
    val dnsServer: String = "1.1.1.1",
    val secondaryDns: String = "1.0.0.1",
    val mtu: Int = 1500,
    val splitTunneling: Boolean = false,
    val bypassLocalSubnets: Boolean = true,
    val ipv6Protection: Boolean = true,
    val autoReconnect: Boolean = true,
    val stealthObfuscation: Boolean = false
)
