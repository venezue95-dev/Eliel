package com.example.vpn.model

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.LocaleList
import java.util.Locale

enum class AppLanguage(
    val code: String,
    val title: String,
    val nativeName: String,
    val flag: String,
    val associatedCountryCodes: List<String>
) {
    AUTO("auto", "Automático (Ubicación / IP / Sistema)", "Automatic", "🌐", emptyList()),
    ES("es", "Español", "Español", "🇪🇸", listOf("ES", "CU", "MX", "AR", "CO", "CL", "PE", "VE", "UY", "CR", "PA", "DO", "PR", "GT", "HN", "SV", "NI", "EC", "BO", "PY")),
    EN("en", "English", "English", "🇺🇸", listOf("US", "GB", "CA", "AU", "NZ", "IE", "ZA", "SG")),
    PT("pt", "Português", "Português", "🇧🇷", listOf("BR", "PT", "AO", "MZ")),
    FR("fr", "Français", "Français", "🇫🇷", listOf("FR", "BE", "CH", "SN", "CI")),
    DE("de", "Deutsch", "Deutsch", "🇩🇪", listOf("DE", "AT")),
    IT("it", "Italiano", "Italiano", "🇮🇹", listOf("IT")),
    RU("ru", "Русский", "Русский", "🇷🇺", listOf("RU", "BY", "KZ"))
}

data class VpnStrings(
    // App Identity & Navigation
    val appName: String,
    val appSubTitle: String,
    val ultraBadge: String,
    val tabDashboard: String,
    val tabMap: String,
    val tabGateways: String,
    val themesTooltip: String,
    val diagnosticsTooltip: String,
    val securitySettingsTooltip: String,
    val languageTooltip: String,

    // Status & Power Button
    val statusConnected: String,
    val statusConnecting: String,
    val statusDisconnecting: String,
    val statusDisconnected: String,
    val statusError: String,
    val subConnected: String,
    val subConnecting: String,
    val subDisconnecting: String,
    val subDisconnected: String,
    val subError: String,
    val uptime: String,

    // Badges & IP
    val killSwitchOn: String,
    val killSwitchOff: String,
    val ipv6ShieldOn: String,
    val ipv6ShieldOff: String,
    val virtualTunnelIp: String,
    val protectedModeOff: String,
    val protectedModeOn: String,

    // Telemetry & Stats
    val telemetryTitle: String,
    val downloadSpeed: String,
    val uploadSpeed: String,
    val totalData: String,
    val duration: String,
    val latency: String,
    val packets: String,
    val unitPackets: String,

    // Gateways & Selection
    val targetGateway: String,
    val gatewaysTitle: String,
    val searchPlaceholder: String,
    val sortFastest: String,
    val sortLowLoad: String,
    val allRegions: String,
    val connectBtn: String,
    val disconnectBtn: String,
    val activeNode: String,
    val selectNode: String,
    val autoBadge: String,
    val loadLabel: String,
    val changeServer: String,

    // Map & Radar
    val mapRadarTitle: String,
    val mapTunnelActive: String,
    val mapRouting: String,
    val mapStandby: String,
    val mapHint: String,
    val nodesOnline: String,

    // Security Settings
    val settingsTitle: String,
    val killSwitchTitle: String,
    val killSwitchDesc: String,
    val ipv6Title: String,
    val ipv6Desc: String,
    val autoReconnectTitle: String,
    val autoReconnectDesc: String,
    val stealthTitle: String,
    val stealthDesc: String,
    val protocolSection: String,
    val mtuSection: String,
    val dnsSection: String,
    val cipherPrefix: String,
    val primaryDns: String,
    val secondaryDns: String,

    // Diagnostics
    val diagnosticsTitle: String,
    val diagnosticsSubtitle: String,
    val clearLogs: String,
    val noLogs: String,

    // Themes
    val themesTitle: String,
    val themesSubtitle: String,
    val themeSystemTitle: String,
    val themeSystemSub: String,
    val themeWhiteNavyTitle: String,
    val themeWhiteNavySub: String,
    val themeCyberDarkTitle: String,
    val themeCyberDarkSub: String,
    val themeAmoledTitle: String,
    val themeAmoledSub: String,
    val themeDeepNavyTitle: String,
    val themeDeepNavySub: String,
    val themeNeonMatrixTitle: String,
    val themeNeonMatrixSub: String,

    // Language Selector
    val languagesTitle: String,
    val languagesSubtitle: String,
    val autoDetectTitle: String,
    val autoDetectDesc: String,

    // Protocols
    val protoWireguardDesc: String,
    val protoOpenVpnUdpDesc: String,
    val protoOpenVpnTcpDesc: String,
    val protoIkev2Desc: String,
    val protoShadowsocksDesc: String,

    // MTU Options
    val mtu1500Label: String,
    val mtu1420Label: String,
    val mtu1380Label: String,
    val mtu1280Label: String,

    // Regions & Auto Server
    val regionAll: String = "All",
    val regionNorthAmerica: String = "North America",
    val regionEurope: String = "Europe",
    val regionAsiaPacific: String = "Asia-Pacific",
    val regionLatinAmerica: String = "Latin America",
    val regionMiddleEastAfrica: String = "Middle East & Africa",
    val autoServerName: String = "Optimal Node (Lowest Latency)",
    val autoServerCity: String = "Auto Routing",
    val autoServerCountry: String = "Automatic Optimal"
) {
    fun getLocalizedRegion(region: String): String {
        return when (region) {
            "All" -> regionAll
            "North America" -> regionNorthAmerica
            "Europe" -> regionEurope
            "Asia-Pacific" -> regionAsiaPacific
            "Latin America" -> regionLatinAmerica
            "Middle East & Africa" -> regionMiddleEastAfrica
            else -> region
        }
    }

    val globalGatewayRadar: String get() = mapRadarTitle
    val tapNodeToSwitch: String get() = mapHint
    val stateConnected: String get() = statusConnected
    val stateConnecting: String get() = statusConnecting
    val stateDisconnected: String get() = statusDisconnected
    val languageTitle: String get() = languagesTitle
    val languageSubtitle: String get() = languagesSubtitle
    val autoLanguageDesc: String get() = autoDetectDesc
    val themesVisualTitle: String get() = themesTitle
    val themesVisualSubtitle: String get() = themesSubtitle
}

object LocalizationManager {
    private const val PREFS_NAME = "vpn_locale_prefs"
    private const val KEY_LANGUAGE = "selected_app_language"

    fun getSavedLanguage(context: Context): AppLanguage {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedCode = prefs.getString(KEY_LANGUAGE, AppLanguage.AUTO.code) ?: AppLanguage.AUTO.code
        return AppLanguage.entries.find { it.code.equals(savedCode, ignoreCase = true) } ?: AppLanguage.AUTO
    }

    fun saveLanguage(context: Context, language: AppLanguage) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
        applySystemLocale(context, language)
    }

    private fun applySystemLocale(context: Context, language: AppLanguage) {
        try {
            val targetLocale = if (language == AppLanguage.AUTO) {
                Locale.getDefault()
            } else {
                Locale(language.code)
            }
            Locale.setDefault(targetLocale)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val localeManager = context.getSystemService(android.app.LocaleManager::class.java)
                if (language == AppLanguage.AUTO) {
                    localeManager?.applicationLocales = LocaleList.getEmptyLocaleList()
                } else {
                    localeManager?.applicationLocales = LocaleList.forLanguageTags(language.code)
                }
            }
        } catch (_: Exception) {
            // Graceful fallback
        }
    }

    fun resolveLanguage(selected: AppLanguage, serverCountryCode: String? = null): AppLanguage {
        if (selected != AppLanguage.AUTO) return selected

        // If connected or routed to a specific server country, auto-match if appropriate
        if (!serverCountryCode.isNullOrBlank()) {
            val code = serverCountryCode.uppercase(Locale.ROOT)
            val matchedByCountry = AppLanguage.entries.find { lang ->
                lang.associatedCountryCodes.contains(code)
            }
            if (matchedByCountry != null) return matchedByCountry
        }

        // Fallback to system locale
        val systemLang = Locale.getDefault().language.lowercase(Locale.ROOT)
        return when {
            systemLang.startsWith("es") -> AppLanguage.ES
            systemLang.startsWith("pt") -> AppLanguage.PT
            systemLang.startsWith("fr") -> AppLanguage.FR
            systemLang.startsWith("de") -> AppLanguage.DE
            systemLang.startsWith("it") -> AppLanguage.IT
            systemLang.startsWith("ru") -> AppLanguage.RU
            else -> AppLanguage.EN
        }
    }

    fun getStrings(language: AppLanguage, serverCountryCode: String? = null): VpnStrings {
        val effectiveLang = resolveLanguage(language, serverCountryCode)
        return when (effectiveLang) {
            AppLanguage.ES -> spanishStrings
            AppLanguage.PT -> portugueseStrings
            AppLanguage.FR -> frenchStrings
            AppLanguage.DE -> germanStrings
            AppLanguage.IT -> italianStrings
            AppLanguage.RU -> russianStrings
            else -> englishStrings
        }
    }

    private val spanishStrings = VpnStrings(
        appName = "ET VPN",
        appSubTitle = "Pasarelas Mundiales Activas",
        ultraBadge = "ULTRA",
        tabDashboard = "Panel",
        tabMap = "Mapa Global",
        tabGateways = "Pasarelas",
        themesTooltip = "Temas Visuales",
        diagnosticsTooltip = "Registros de Diagnóstico",
        securitySettingsTooltip = "Ajustes de Seguridad",
        languageTooltip = "Idioma & Localización",

        statusConnected = "CONECTADO",
        statusConnecting = "CONECTANDO...",
        statusDisconnecting = "DESCONECTANDO...",
        statusDisconnected = "NO CONECTADO",
        statusError = "ERROR DE CONEXIÓN",
        subConnected = "Túnel Encriptado: ",
        subConnecting = "Estableciendo interfaz virtual TUN...",
        subDisconnecting = "Cerrando sockets y rutas...",
        subDisconnected = "Toca para activar escudo seguro",
        subError = "Toca para reintentar conexión",
        uptime = "Tiempo Activo",

        killSwitchOn = "Kill Switch ACTIVO",
        killSwitchOff = "Kill Switch INACTIVO",
        ipv6ShieldOn = "Escudo IPv6",
        ipv6ShieldOff = "IPv6 Abierto",
        virtualTunnelIp = "IP DEL TÚNEL VIRTUAL",
        protectedModeOff = "Modo protegido desactivado",
        protectedModeOn = "Túnel Seguro WireGuard",

        telemetryTitle = "Telemetría de Red en Tiempo Real",
        downloadSpeed = "Descarga",
        uploadSpeed = "Subida",
        totalData = "Tráfico Total",
        duration = "Tiempo Activo",
        latency = "Latencia",
        packets = "Paquetes",
        unitPackets = "paquetes",

        targetGateway = "PASARELA DESTINO",
        gatewaysTitle = "Pasarelas Globales Seguras",
        searchPlaceholder = "Buscar país, ciudad o IP...",
        sortFastest = "Más Rápido",
        sortLowLoad = "Menor Carga",
        allRegions = "Todos",
        connectBtn = "Conectar",
        disconnectBtn = "Desconectar",
        activeNode = "Nodo Activo",
        selectNode = "Seleccionar",
        autoBadge = "AUTO",
        loadLabel = "Carga",
        changeServer = "Cambiar servidor",

        mapRadarTitle = "RADAR GLOBAL DE PASARELAS",
        mapTunnelActive = "TÚNEL ACTIVO",
        mapRouting = "ENRUTANDO...",
        mapStandby = "EN ESPERA",
        mapHint = "Toca nodos del mapa para inspeccionar y cambiar pasarela",
        nodesOnline = "Nodos en Línea",

        settingsTitle = "Seguridad y Protección de Red",
        killSwitchTitle = "Protección Kill Switch",
        killSwitchDesc = "Bloquea todo el tráfico de internet si el túnel VPN se desconecta accidentalmente",
        ipv6Title = "Escudo contra Fugas IPv6",
        ipv6Desc = "Desactiva y aísla canales IPv6 no cifrados para blindar tu identidad",
        autoReconnectTitle = "Reconexión Automática Continua",
        autoReconnectDesc = "Restablece el túnel de inmediato ante interrupciones de red o cambio celular",
        stealthTitle = "Ofuscación Sigilosa de Tráfico",
        stealthDesc = "Camufla los paquetes VPN haciéndolos indistinguibles de navegación web HTTPS",
        protocolSection = "PROTOCOLO DE TÚNEL Y CIFRADO",
        mtuSection = "OPTIMIZACIÓN DE TAMAÑO DE PAQUETE MTU",
        dnsSection = "RESOLVEDORES DNS CIFRADOS SIN REGISTROS",
        cipherPrefix = "Cifrado: ",
        primaryDns = "Primario: ",
        secondaryDns = "Secundario: ",

        diagnosticsTitle = "Registros de Diagnóstico",
        diagnosticsSubtitle = "Eventos de paquetes en vivo de VpnService TUN",
        clearLogs = "Limpiar registros",
        noLogs = "Sin eventos de túnel registrados aún. Conéctate a una pasarela para ver la telemetría en vivo.",

        themesTitle = "Temas Visuales",
        themesSubtitle = "Configuración de modo claro, oscuro y estilos de color",
        themeSystemTitle = "Automático (Sistema)",
        themeSystemSub = "Sigue automáticamente el modo claro u oscuro del dispositivo",
        themeWhiteNavyTitle = "Blanco & Azul #00012F",
        themeWhiteNavySub = "Modo claro blanco con elegante color principal #00012F",
        themeCyberDarkTitle = "Cyber Obsidian",
        themeCyberDarkSub = "Contraste cibernético sobre fondo oscuro de alta tecnología",
        themeAmoledTitle = "AMOLED Pitch Black",
        themeAmoledSub = "Negro absoluto (#000000) para máximo ahorro de batería OLED",
        themeDeepNavyTitle = "Deep Tactical Navy",
        themeDeepNavySub = "Tonalidades navales tácticas con acentos en cian",
        themeNeonMatrixTitle = "Matrix Terminal",
        themeNeonMatrixSub = "Terminal hacker con brillo esmeralda cibernético",

        languagesTitle = "Idioma & Localización",
        languagesSubtitle = "Selección manual o detección según tu ubicación, IP o sistema",
        autoDetectTitle = "Automático (Ubicación / IP / Sistema)",
        autoDetectDesc = "Detecta automáticamente según tu país de conexión, IP o sistema",

        protoWireguardDesc = "Túnel de última generación ChaCha20-Poly1305 curve25519 con latencia ultra baja y velocidad máxima",
        protoOpenVpnUdpDesc = "Estándar de la industria AES-256-GCM optimizado para streaming de alta velocidad",
        protoOpenVpnTcpDesc = "Flujo con corrección de errores para penetrar cortafuegos y redes restrictivas",
        protoIkev2Desc = "Protocolo MOBIKE con cambio transparente e instantáneo entre Wi-Fi y datos móviles",
        protoShadowsocksDesc = "Cifrado de flujo asimétrico que disfraza el tráfico VPN como navegación web HTTPS ordinaria",

        mtu1500Label = "1500 bytes (Estándar Ethernet y Fibra Óptica)",
        mtu1420Label = "1420 bytes (Recomendado para Redes Móviles 4G/5G)",
        mtu1380Label = "1380 bytes (Modo Seguro con Encapsulación)",
        mtu1280Label = "1280 bytes (Límite Mínimo de Fragmentación IPv6)",

        regionAll = "Todos",
        regionNorthAmerica = "América del Norte",
        regionEurope = "Europa",
        regionAsiaPacific = "Asia-Pacífico",
        regionLatinAmerica = "América Latina",
        regionMiddleEastAfrica = "Oriente Medio y África",
        autoServerName = "Nodo Óptimo (Menor Latencia)",
        autoServerCity = "Enrutamiento Automático",
        autoServerCountry = "Óptimo Automático"
    )

    private val englishStrings = VpnStrings(
        appName = "ET VPN",
        appSubTitle = "Worldwide Gateways Online",
        ultraBadge = "ULTRA",
        tabDashboard = "Dashboard",
        tabMap = "World Map",
        tabGateways = "Gateways",
        themesTooltip = "Visual Themes",
        diagnosticsTooltip = "Diagnostic Logs",
        securitySettingsTooltip = "Security Settings",
        languageTooltip = "Language & Region",

        statusConnected = "CONNECTED",
        statusConnecting = "CONNECTING...",
        statusDisconnecting = "DISCONNECTING...",
        statusDisconnected = "NOT CONNECTED",
        statusError = "CONNECTION FAILED",
        subConnected = "Tunnel Encrypted: ",
        subConnecting = "Establishing virtual TUN interface...",
        subDisconnecting = "Closing sockets and route tunnels...",
        subDisconnected = "Tap to activate secure shield",
        subError = "Tap to retry connection",
        uptime = "Uptime",

        killSwitchOn = "Kill Switch ON",
        killSwitchOff = "Kill Switch OFF",
        ipv6ShieldOn = "IPv6 Shield",
        ipv6ShieldOff = "IPv6 Open",
        virtualTunnelIp = "VIRTUAL TUNNEL IP",
        protectedModeOff = "Protected Mode Off",
        protectedModeOn = "WireGuard Secure Tunnel",

        telemetryTitle = "Real-Time Network Telemetry",
        downloadSpeed = "Download",
        uploadSpeed = "Upload",
        totalData = "Total Data",
        duration = "Uptime",
        latency = "Latency",
        packets = "Packets",
        unitPackets = "pkts",

        targetGateway = "TARGET GATEWAY",
        gatewaysTitle = "Global Secure Nodes",
        searchPlaceholder = "Search country, city, or IP...",
        sortFastest = "Fastest",
        sortLowLoad = "Low Load",
        allRegions = "All",
        connectBtn = "Connect",
        disconnectBtn = "Disconnect",
        activeNode = "Active Node",
        selectNode = "Select",
        autoBadge = "AUTO",
        loadLabel = "Load",
        changeServer = "Change Server",

        mapRadarTitle = "GLOBAL GATEWAY RADAR",
        mapTunnelActive = "TUNNEL ACTIVE",
        mapRouting = "ROUTING...",
        mapStandby = "STANDBY",
        mapHint = "Tap map nodes to inspect & switch gateway",
        nodesOnline = "Nodes Online",

        settingsTitle = "Security & Network Armor",
        killSwitchTitle = "Kill Switch Protection",
        killSwitchDesc = "Blocks all non-VPN internet traffic if tunnel connection drops unexpectedly",
        ipv6Title = "IPv6 Leak Shield",
        ipv6Desc = "Disables and isolates unencrypted IPv6 channels to prevent identity leaks",
        autoReconnectTitle = "Seamless Auto-Reconnect",
        autoReconnectDesc = "Re-establishes tunnel instantaneously upon disruptions or cellular handover",
        stealthTitle = "Stealth Traffic Obfuscation",
        stealthDesc = "Camouflages VPN handshake packets as standard TLS 1.3 HTTPS web traffic",
        protocolSection = "TUNNEL PROTOCOL & CIPHER",
        mtuSection = "MTU PACKET SIZING OPTIMIZATION",
        dnsSection = "ENCRYPTED ZERO-LOG DNS RESOLVERS",
        cipherPrefix = "Cipher: ",
        primaryDns = "Primary: ",
        secondaryDns = "Secondary: ",

        diagnosticsTitle = "Diagnostic Logs",
        diagnosticsSubtitle = "Live kernel VpnService TUN packet events",
        clearLogs = "Clear Logs",
        noLogs = "No tunnel events recorded yet. Connect to a gateway to view live telemetry.",

        themesTitle = "Visual Themes",
        themesSubtitle = "Light, dark mode and custom color profiles",
        themeSystemTitle = "Automatic (System)",
        themeSystemSub = "Follows device light or dark mode automatically",
        themeWhiteNavyTitle = "White & Navy #00012F",
        themeWhiteNavySub = "Pristine white mode with elegant #00012F navy primary",
        themeCyberDarkTitle = "Cyber Obsidian",
        themeCyberDarkSub = "Cybernetic contrast on high-tech dark background",
        themeAmoledTitle = "AMOLED Pitch Black",
        themeAmoledSub = "Absolute pitch black (#000000) for maximum OLED battery savings",
        themeDeepNavyTitle = "Deep Tactical Navy",
        themeDeepNavySub = "Tactical naval shades with cyber cyan accents",
        themeNeonMatrixTitle = "Matrix Terminal",
        themeNeonMatrixSub = "Hacker terminal with emerald cyber glow",

        languagesTitle = "Language & Region",
        languagesSubtitle = "Manual selection or smart detection based on location, IP or system",
        autoDetectTitle = "Automatic (Location / IP / System)",
        autoDetectDesc = "Detects automatically based on connected server country, IP or system",

        protoWireguardDesc = "Next-gen ChaCha20-Poly1305 curve25519 tunnel with ultra-low latency and peak throughput",
        protoOpenVpnUdpDesc = "Industry standard 256-bit AES-GCM data channel optimized for high-speed streaming",
        protoOpenVpnTcpDesc = "Error-correcting stream designed to reliably penetrate strict restrictive firewalls",
        protoIkev2Desc = "MOBIKE protocol providing seamless handover during Wi-Fi to cellular switches",
        protoShadowsocksDesc = "Asymmetric stream cipher that masks VPN traffic as ordinary HTTPS web traffic",

        mtu1500Label = "1500 bytes (Ethernet Standard & Fiber)",
        mtu1420Label = "1420 bytes (Recommended for 4G/5G Cellular)",
        mtu1380Label = "1380 bytes (Encapsulation Safe Mode)",
        mtu1280Label = "1280 bytes (IPv6 Minimum Fragment Boundary)"
    )

    private val portugueseStrings = VpnStrings(
        appName = "ET VPN",
        appSubTitle = "Servidores Globais Conectados",
        ultraBadge = "ULTRA",
        tabDashboard = "Painel",
        tabMap = "Mapa Global",
        tabGateways = "Servidores",
        themesTooltip = "Temas Visuais",
        diagnosticsTooltip = "Logs de Diagnóstico",
        securitySettingsTooltip = "Configurações de Segurança",
        languageTooltip = "Idioma & Localização",

        statusConnected = "CONECTADO",
        statusConnecting = "CONECTANDO...",
        statusDisconnecting = "DESCONECTANDO...",
        statusDisconnected = "NÃO CONECTADO",
        statusError = "FALHA NA CONEXÃO",
        subConnected = "Túnel Criptografado: ",
        subConnecting = "Configurando interface virtual TUN...",
        subDisconnecting = "Encerrando rotas e conexões...",
        subDisconnected = "Toque para ativar o escudo de segurança",
        subError = "Toque para tentar novamente",
        uptime = "Tempo Ativo",

        killSwitchOn = "Kill Switch ATIVADO",
        killSwitchOff = "Kill Switch DESATIVADO",
        ipv6ShieldOn = "Escudo IPv6",
        ipv6ShieldOff = "IPv6 Aberto",
        virtualTunnelIp = "IP DO TÚNEL VIRTUAL",
        protectedModeOff = "Modo protegido desativado",
        protectedModeOn = "Túnel Seguro WireGuard",

        telemetryTitle = "Telemetria de Rede em Tempo Real",
        downloadSpeed = "Download",
        uploadSpeed = "Upload",
        totalData = "Tráfego Total",
        duration = "Tempo Ativo",
        latency = "Latência",
        packets = "Pacotes",
        unitPackets = "pacotes",

        targetGateway = "SERVIDOR DESTINO",
        gatewaysTitle = "Servidores Globais Seguros",
        searchPlaceholder = "Buscar país, cidade ou IP...",
        sortFastest = "Mais Rápido",
        sortLowLoad = "Menor Carga",
        allRegions = "Todos",
        connectBtn = "Conectar",
        disconnectBtn = "Desconectar",
        activeNode = "Nó Ativo",
        selectNode = "Selecionar",
        autoBadge = "AUTO",
        loadLabel = "Carga",
        changeServer = "Mudar Servidor",

        mapRadarTitle = "RADAR GLOBAL DE SERVIDORES",
        mapTunnelActive = "TÚNEL ATIVO",
        mapRouting = "ROTEANDO...",
        mapStandby = "EM ESPERA",
        mapHint = "Toque nos nós do mapa para inspecionar e trocar de servidor",
        nodesOnline = "Nós Online",

        settingsTitle = "Segurança e Proteção de Rede",
        killSwitchTitle = "Proteção Kill Switch",
        killSwitchDesc = "Bloqueia o tráfego se o túnel VPN for interrompido",
        ipv6Title = "Proteção Contra Fuga IPv6",
        ipv6Desc = "Desativa e isola canais IPv6 não criptografados",
        autoReconnectTitle = "Reconexão Automática Contínua",
        autoReconnectDesc = "Restabelece o túnel instantaneamente em quedas de rede",
        stealthTitle = "Ofuscação de Tráfego Stealth",
        stealthDesc = "Disfarça o tráfego VPN como navegação HTTPS padrão",
        protocolSection = "PROTOCOLO DE TÚNEL E CIFRA",
        mtuSection = "OTIMIZAÇÃO DO TAMANHO DE PACOTE MTU",
        dnsSection = "SERVIDORES DNS CRIPTOGRAFADOS ZERO-LOG",
        cipherPrefix = "Cifra: ",
        primaryDns = "Primário: ",
        secondaryDns = "Secundário: ",

        diagnosticsTitle = "Logs de Diagnóstico",
        diagnosticsSubtitle = "Eventos de pacotes em tempo real do VpnService TUN",
        clearLogs = "Limpar Logs",
        noLogs = "Nenhum evento registrado. Conecte-se a um servidor para ver a telemetria.",

        themesTitle = "Temas Visuais",
        themesSubtitle = "Personalização de cores e temas",
        themeSystemTitle = "Automático (Sistema)",
        themeSystemSub = "Segue o modo claro ou escuro do dispositivo",
        themeWhiteNavyTitle = "Branco & Azul #00012F",
        themeWhiteNavySub = "Tema claro com elegante azul naval #00012F",
        themeCyberDarkTitle = "Cyber Obsidian",
        themeCyberDarkSub = "Contraste cibernético sobre fundo escuro",
        themeAmoledTitle = "AMOLED Pitch Black",
        themeAmoledSub = "Preto absoluto para máxima economia de bateria OLED",
        themeDeepNavyTitle = "Deep Tactical Navy",
        themeDeepNavySub = "Tons navais táticos com detalhes em ciano",
        themeNeonMatrixTitle = "Matrix Terminal",
        themeNeonMatrixSub = "Terminal hacker com brilho esmeralda",

        languagesTitle = "Idioma & Localização",
        languagesSubtitle = "Seleção manual ou detecção automática",
        autoDetectTitle = "Automático (Localização / IP / Sistema)",
        autoDetectDesc = "Detecta automaticamente conforme país do servidor, IP ou sistema",

        protoWireguardDesc = "Túnel ChaCha20-Poly1305 de última geração com latência ultra baixa",
        protoOpenVpnUdpDesc = "Padrão da indústria AES-256-GCM otimizado para alta velocidade",
        protoOpenVpnTcpDesc = "Fluxo com correção de erros para ultrapassar firewalls restritivos",
        protoIkev2Desc = "Protocolo MOBIKE com troca contínua entre Wi-Fi e dados móveis",
        protoShadowsocksDesc = "Cifra assimétrica que mascara o tráfego VPN como HTTPS comum",

        mtu1500Label = "1500 bytes (Padrão Ethernet e Fibra)",
        mtu1420Label = "1420 bytes (Recomendado para Redes Móveis 4G/5G)",
        mtu1380Label = "1380 bytes (Modo Seguro com Encapsulamento)",
        mtu1280Label = "1280 bytes (Limite Mínimo de Fragmentação IPv6)",

        regionAll = "Todos",
        regionNorthAmerica = "América do Norte",
        regionEurope = "Europa",
        regionAsiaPacific = "Ásia-Pacífico",
        regionLatinAmerica = "América Latina",
        regionMiddleEastAfrica = "Oriente Médio e África",
        autoServerName = "Nó Ideal (Menor Latência)",
        autoServerCity = "Roteamento Automático",
        autoServerCountry = "Automático Ideal"
    )

    private val frenchStrings = VpnStrings(
        appName = "ET VPN",
        appSubTitle = "Passerelles Mondiales Actives",
        ultraBadge = "ULTRA",
        tabDashboard = "Tableau",
        tabMap = "Carte Mondiale",
        tabGateways = "Passerelles",
        themesTooltip = "Thèmes Visuels",
        diagnosticsTooltip = "Journaux de Diagnostic",
        securitySettingsTooltip = "Paramètres de Sécurité",
        languageTooltip = "Langue & Région",

        statusConnected = "CONNECTÉ",
        statusConnecting = "CONNEXION...",
        statusDisconnecting = "DÉCONNEXION...",
        statusDisconnected = "NON CONNECTÉ",
        statusError = "ÉCHEC DE CONNEXION",
        subConnected = "Tunnel Chiffré : ",
        subConnecting = "Initialisation de l'interface TUN...",
        subDisconnecting = "Fermeture des routes et sockets...",
        subDisconnected = "Appuyez pour activer la protection",
        subError = "Appuyez pour réessayer",
        uptime = "Durée",

        killSwitchOn = "Kill Switch ACTIF",
        killSwitchOff = "Kill Switch INACTIF",
        ipv6ShieldOn = "Protection IPv6",
        ipv6ShieldOff = "IPv6 Non Protégé",
        virtualTunnelIp = "IP DU TUNNEL VIRTUEL",
        protectedModeOff = "Mode protégé désactivé",
        protectedModeOn = "Tunnel Sécurisé WireGuard",

        telemetryTitle = "Télémétrie Réseau en Temps Réel",
        downloadSpeed = "Téléchargement",
        uploadSpeed = "Téléversement",
        totalData = "Données Totales",
        duration = "Durée",
        latency = "Latence",
        packets = "Paquets",
        unitPackets = "paquets",

        targetGateway = "PASSERELLE CIBLE",
        gatewaysTitle = "Noeuds Sécurisés Mondiaux",
        searchPlaceholder = "Rechercher pays, ville ou IP...",
        sortFastest = "Plus Rapide",
        sortLowLoad = "Faible Charge",
        allRegions = "Tous",
        connectBtn = "Connecter",
        disconnectBtn = "Déconnecter",
        activeNode = "Noeud Actif",
        selectNode = "Sélectionner",
        autoBadge = "AUTO",
        loadLabel = "Charge",
        changeServer = "Changer de serveur",

        mapRadarTitle = "RADAR MONDIAL DES PASSERELLES",
        mapTunnelActive = "TUNNEL ACTIF",
        mapRouting = "ROUTAGE...",
        mapStandby = "EN VEILLE",
        mapHint = "Touchez les noeuds de la carte pour inspecter et changer de passerelle",
        nodesOnline = "Noeuds en Ligne",

        settingsTitle = "Sécurité & Blindage Réseau",
        killSwitchTitle = "Protection Kill Switch",
        killSwitchDesc = "Bloque tout le trafic internet non sécurisé en cas de coupure du VPN",
        ipv6Title = "Bouclier Anti-Fuite IPv6",
        ipv6Desc = "Désactive et isole les canaux IPv6 non chiffrés",
        autoReconnectTitle = "Reconnexion Automatique Fluide",
        autoReconnectDesc = "Rétablit instantanément le tunnel lors des coupures de réseau",
        stealthTitle = "Obscurcissement Furtif",
        stealthDesc = "Camoufle le trafic VPN en trafic web HTTPS standard",
        protocolSection = "PROTOCOLE DE TUNNEL ET CHIFFREMENT",
        mtuSection = "OPTIMISATION DE LA TAILLE DES PAQUETS MTU",
        dnsSection = "RÉSOLVEURS DNS CHIFFRÉS SANS JOURNAUX",
        cipherPrefix = "Chiffrement : ",
        primaryDns = "Primaire : ",
        secondaryDns = "Secondaire : ",

        diagnosticsTitle = "Journaux de Diagnostic",
        diagnosticsSubtitle = "Événements paquets TUN VpnService en direct",
        clearLogs = "Effacer les journaux",
        noLogs = "Aucun événement enregistré. Connectez-vous à une passerelle pour afficher la télémétrie.",

        themesTitle = "Thèmes Visuels",
        themesSubtitle = "Personnalisation du mode clair, sombre et des couleurs",
        themeSystemTitle = "Automatique (Système)",
        themeSystemSub = "Suit automatiquement le mode clair ou sombre de l'appareil",
        themeWhiteNavyTitle = "Blanc & Bleu #00012F",
        themeWhiteNavySub = "Mode clair élégant avec bleu marine #00012F",
        themeCyberDarkTitle = "Cyber Obsidian",
        themeCyberDarkSub = "Contraste cybernétique sur fond sombre haute technologie",
        themeAmoledTitle = "AMOLED Noir Absolu",
        themeAmoledSub = "Noir absolu pour une économie maximale de batterie OLED",
        themeDeepNavyTitle = "Deep Tactical Navy",
        themeDeepNavySub = "Nuances navales tactiques avec accents cyan",
        themeNeonMatrixTitle = "Matrix Terminal",
        themeNeonMatrixSub = "Terminal hacker avec lueur émeraude",

        languagesTitle = "Langue & Région",
        languagesSubtitle = "Sélection manuelle ou détection intelligente",
        autoDetectTitle = "Automatique (Emplacement / IP / Système)",
        autoDetectDesc = "Détecte automatiquement selon l'emplacement, l'IP ou le système",

        protoWireguardDesc = "Tunnel ChaCha20-Poly1305 nouvelle génération avec latence ultra faible",
        protoOpenVpnUdpDesc = "Norme industrielle AES-256-GCM optimisée pour le streaming haut débit",
        protoOpenVpnTcpDesc = "Flux avec correction d'erreurs conçu pour traverser les pare-feu stricts",
        protoIkev2Desc = "Protocole MOBIKE assurant une transition fluide entre Wi-Fi et réseau cellulaire",
        protoShadowsocksDesc = "Chiffrement de flux asymétrique masquant le trafic VPN en HTTPS ordinaire",

        mtu1500Label = "1500 octets (Norme Ethernet et Fibre)",
        mtu1420Label = "1420 octets (Recommandé pour Réseaux Mobiles 4G/5G)",
        mtu1380Label = "1380 octets (Mode Sécurisé avec Encapsulation)",
        mtu1280Label = "1280 octets (Limite Minimale de Fragmentation IPv6)",

        regionAll = "Tous",
        regionNorthAmerica = "Amérique du Nord",
        regionEurope = "Europe",
        regionAsiaPacific = "Asie-Pacifique",
        regionLatinAmerica = "Amérique Latine",
        regionMiddleEastAfrica = "Moyen-Orient et Afrique",
        autoServerName = "Nœud Optimal (Latence Minimale)",
        autoServerCity = "Routage Automatique",
        autoServerCountry = "Automatique Optimal"
    )

    private val germanStrings = VpnStrings(
        appName = "ET VPN",
        appSubTitle = "Weltweite Gateways Online",
        ultraBadge = "ULTRA",
        tabDashboard = "Dashboard",
        tabMap = "Weltkarte",
        tabGateways = "Gateways",
        themesTooltip = "Designs",
        diagnosticsTooltip = "Diagnoseprotokolle",
        securitySettingsTooltip = "Sicherheitseinstellungen",
        languageTooltip = "Sprache & Region",

        statusConnected = "VERBUNDEN",
        statusConnecting = "VERBINDUNG...",
        statusDisconnecting = "TRENNUNG...",
        statusDisconnected = "NICHT VERBUNDEN",
        statusError = "VERBINDUNGSFEHLER",
        subConnected = "Tunnel Verschlüsselt: ",
        subConnecting = "TUN-Schnittstelle wird initialisiert...",
        subDisconnecting = "Sockets und Routen werden geschlossen...",
        subDisconnected = "Tippen zum Aktivieren des Schildes",
        subError = "Tippen zum erneuten Versuchen",
        uptime = "Laufzeit",

        killSwitchOn = "Kill Switch EIN",
        killSwitchOff = "Kill Switch AUS",
        ipv6ShieldOn = "IPv6 Schutz",
        ipv6ShieldOff = "IPv6 Offen",
        virtualTunnelIp = "VIRTUELLE TUNNEL-IP",
        protectedModeOff = "Geschützter Modus aus",
        protectedModeOn = "WireGuard Sicherer Tunnel",

        telemetryTitle = "Echtzeit-Netzwerktelemetrie",
        downloadSpeed = "Download",
        uploadSpeed = "Upload",
        totalData = "Gesamtdaten",
        duration = "Laufzeit",
        latency = "Latenz",
        packets = "Pakete",
        unitPackets = "Pakete",

        targetGateway = "ZIEL-GATEWAY",
        gatewaysTitle = "Globale Sichere Knoten",
        searchPlaceholder = "Land, Stadt oder IP suchen...",
        sortFastest = "Schnellste",
        sortLowLoad = "Geringe Last",
        allRegions = "Alle",
        connectBtn = "Verbinden",
        disconnectBtn = "Trennen",
        activeNode = "Aktiver Knoten",
        selectNode = "Auswählen",
        autoBadge = "AUTO",
        loadLabel = "Last",
        changeServer = "Server wechseln",

        mapRadarTitle = "SERVER-STANDORTRADAR",
        mapTunnelActive = "TUNNEL AKTIV",
        mapRouting = "ROUTING...",
        mapStandby = "STANDBY",
        mapHint = "Knoten antippen, um Server zu prüfen und zu wechseln",
        nodesOnline = "Knoten Online",

        settingsTitle = "Sicherheit & Netzwerkschutz",
        killSwitchTitle = "Kill Switch Schutz",
        killSwitchDesc = "Blockiert den gesamten Internetverkehr, falls der VPN-Tunnel abbricht",
        ipv6Title = "IPv6 Leak-Schutz",
        ipv6Desc = "Deaktiviert und isoliert unverschlüsselte IPv6-Routen",
        autoReconnectTitle = "Nahtlose Automatische Wiederverbindung",
        autoReconnectDesc = "Stellt den Tunnel bei Netzwerkabbrüchen sofort wieder her",
        stealthTitle = "Stealth-Verkehrstarnung",
        stealthDesc = "Tarnt VPN-Pakete als gewöhnlichen HTTPS TLS 1.3 Webverkehr",
        protocolSection = "TUNNELPROTOKOLL & VERSCHLÜSSELUNG",
        mtuSection = "MTU-PAKETGRÖSSENOPTIMIERUNG",
        dnsSection = "VERSCHLÜSSELTE PROTOKOLLFREIE DNS-RESOLVER",
        cipherPrefix = "Chiffre: ",
        primaryDns = "Primär: ",
        secondaryDns = "Sekundär: ",

        diagnosticsTitle = "Diagnoseprotokolle",
        diagnosticsSubtitle = "Live VpnService TUN-Paketereignisse",
        clearLogs = "Protokolle löschen",
        noLogs = "Noch keine Ereignisse erfasst. Verbinde dich mit einem Gateway für Live-Telemetrie.",

        themesTitle = "Designs",
        themesSubtitle = "Helle, dunkle und individuelle Farbmodi",
        themeSystemTitle = "Automatisch (System)",
        themeSystemSub = "Folgt dem hellen oder dunklen Systemmodus",
        themeWhiteNavyTitle = "Weiß & Navy #00012F",
        themeWhiteNavySub = "Eleganter Weißmodus mit Marineblau #00012F",
        themeCyberDarkTitle = "Cyber Obsidian",
        themeCyberDarkSub = "Kybernetischer Kontrast auf dunklem Hightech-Hintergrund",
        themeAmoledTitle = "AMOLED Tiefschwarz",
        themeAmoledSub = "Absolutes Schwarz (#000000) für maximale OLED-Akkuschonung",
        themeDeepNavyTitle = "Deep Tactical Navy",
        themeDeepNavySub = "Taktische Marinetöne mit Cyan-Akzenten",
        themeNeonMatrixTitle = "Matrix Terminal",
        themeNeonMatrixSub = "Hacker-Terminal mit smaragdgrünem Cyber-Leuchten",

        languagesTitle = "Sprache & Region",
        languagesSubtitle = "Manuelle Auswahl oder intelligente Erkennung",
        autoDetectTitle = "Automatisch (Standort / IP / System)",
        autoDetectDesc = "Erkennt Sprache automatisch basierend auf Server, IP oder System",

        protoWireguardDesc = "Modernster ChaCha20-Poly1305 Curve25519-Tunnel mit extrem niedriger Latenz",
        protoOpenVpnUdpDesc = "Industriestandard AES-256-GCM optimiert für Highspeed-Streaming",
        protoOpenVpnTcpDesc = "Fehlerkorrigierender Datenstrom zur Überwindung restriktiver Firewalls",
        protoIkev2Desc = "MOBIKE-Protokoll für unterbrechungsfreie Übergabe zwischen WLAN und Mobilfunk",
        protoShadowsocksDesc = "Asymmetrische Stromchiffre, die VPN-Verkehr als normalen HTTPS-Webverkehr tarnt",

        mtu1500Label = "1500 Bytes (Standard Ethernet & Glasfaser)",
        mtu1420Label = "1420 Bytes (Empfohlen für 4G/5G Mobilfunk)",
        mtu1380Label = "1380 Bytes (Sicherer Kapselungsmodus)",
        mtu1280Label = "1280 Bytes (IPv6 Mindestfragmentgrenze)",

        regionAll = "Alle",
        regionNorthAmerica = "Nordamerika",
        regionEurope = "Europa",
        regionAsiaPacific = "Asien-Pazifik",
        regionLatinAmerica = "Lateinamerika",
        regionMiddleEastAfrica = "Naher Osten & Afrika",
        autoServerName = "Optimaler Knoten (Niedrigste Latenz)",
        autoServerCity = "Automatisches Routing",
        autoServerCountry = "Automatisch Optimal"
    )

    private val italianStrings = VpnStrings(
        appName = "ET VPN",
        appSubTitle = "Gateway Mondiali Online",
        ultraBadge = "ULTRA",
        tabDashboard = "Pannello",
        tabMap = "Mappa Globale",
        tabGateways = "Gateway",
        themesTooltip = "Temi Visivi",
        diagnosticsTooltip = "Registri Diagnostici",
        securitySettingsTooltip = "Impostazioni di Sicurezza",
        languageTooltip = "Lingua & Regione",

        statusConnected = "CONNESSO",
        statusConnecting = "CONNESSIONE...",
        statusDisconnecting = "DISCONNESSIONE...",
        statusDisconnected = "NON CONNESSO",
        statusError = "ERRORE CONNESSIONE",
        subConnected = "Tunnel Crittografato: ",
        subConnecting = "Configurazione interfaccia TUN...",
        subDisconnecting = "Chiusura tunnel e percorsi...",
        subDisconnected = "Tocca per attivare la protezione",
        subError = "Tocca per riprovare",
        uptime = "Tempo Attivo",

        killSwitchOn = "Kill Switch ATTIVO",
        killSwitchOff = "Kill Switch DISATTIVATO",
        ipv6ShieldOn = "Scudo IPv6",
        ipv6ShieldOff = "IPv6 Aperto",
        virtualTunnelIp = "IP DEL TUNNEL VIRTUALE",
        protectedModeOff = "Modalità protetta disattivata",
        protectedModeOn = "Tunnel Sicuro WireGuard",

        telemetryTitle = "Telemetria di Rete in Tempo Reale",
        downloadSpeed = "Download",
        uploadSpeed = "Upload",
        totalData = "Traffico Totale",
        duration = "Tempo Attivo",
        latency = "Latenza",
        packets = "Pacchetti",
        unitPackets = "pacchetti",

        targetGateway = "GATEWAY OBIETTIVO",
        gatewaysTitle = "Nodi Sicuri Globali",
        searchPlaceholder = "Cerca paese, città o IP...",
        sortFastest = "Più Veloce",
        sortLowLoad = "Basso Carico",
        allRegions = "Tutti",
        connectBtn = "Connetti",
        disconnectBtn = "Disconnetti",
        activeNode = "Nodo Attivo",
        selectNode = "Seleziona",
        autoBadge = "AUTO",
        loadLabel = "Carico",
        changeServer = "Cambia Server",

        mapRadarTitle = "RADAR POSIZIONE GATEWAY",
        mapTunnelActive = "TUNNEL ATTIVO",
        mapRouting = "INSTRADAMENTO...",
        mapStandby = "STANDBY",
        mapHint = "Tocca i nodi sulla mappa per esaminare e cambiare gateway",
        nodesOnline = "Nodi Online",

        settingsTitle = "Sicurezza e Protezione Rete",
        killSwitchTitle = "Protezione Kill Switch",
        killSwitchDesc = "Blocca il traffico internet se la connessione VPN si interrompe",
        ipv6Title = "Scudo Anti-Perdita IPv6",
        ipv6Desc = "Disattiva e isola canali IPv6 non crittografati",
        autoReconnectTitle = "Riconnessione Automatica Continua",
        autoReconnectDesc = "Ripristina istantaneamente il tunnel in caso di caduta della rete",
        stealthTitle = "Offuscamento Furtivo del Traffico",
        stealthDesc = "Mimetizza i pacchetti VPN come normale traffico web HTTPS",
        protocolSection = "PROTOCOLLO TUNNEL E CIFRARIO",
        mtuSection = "OTTIMIZZAZIONE DIMENSIONE PACCHETTI MTU",
        dnsSection = "RESOLVER DNS CIFRATI SENZA REGISTRI",
        cipherPrefix = "Cifrario: ",
        primaryDns = "Primario: ",
        secondaryDns = "Secondario: ",

        diagnosticsTitle = "Registri Diagnostici",
        diagnosticsSubtitle = "Eventi pacchetti TUN VpnService in tempo reale",
        clearLogs = "Cancella registri",
        noLogs = "Nessun evento registrato. Connettiti a un gateway per visualizzare la telemetria.",

        themesTitle = "Temi Visivi",
        themesSubtitle = "Personalizzazione modalità chiara, scura e colori",
        themeSystemTitle = "Automatico (Sistema)",
        themeSystemSub = "Segue automaticamente la modalità chiara o scura del dispositivo",
        themeWhiteNavyTitle = "Bianco & Blu Marino #00012F",
        themeWhiteNavySub = "Modalità chiara elegante con blu marino #00012F",
        themeCyberDarkTitle = "Cyber Obsidian",
        themeCyberDarkSub = "Contrasto cibernetico su sfondo scuro ad alta tecnologia",
        themeAmoledTitle = "AMOLED Nero Assoluto",
        themeAmoledSub = "Nero assoluto per il massimo risparmio di batteria OLED",
        themeDeepNavyTitle = "Deep Tactical Navy",
        themeDeepNavySub = "Tonalità navali tattiche con accenti ciano",
        themeNeonMatrixTitle = "Matrix Terminal",
        themeNeonMatrixSub = "Terminale hacker con bagliore smeraldo",

        languagesTitle = "Lingua & Regione",
        languagesSubtitle = "Selezione manuale o rilevamento intelligente",
        autoDetectTitle = "Automatico (Posizione / IP / Sistema)",
        autoDetectDesc = "Rileva automaticamente in base al paese del server, IP o sistema",

        protoWireguardDesc = "Tunnel ChaCha20-Poly1305 di nuova generazione con latenza bassissima",
        protoOpenVpnUdpDesc = "Standard di settore AES-256-GCM ottimizzato per streaming ad alta velocità",
        protoOpenVpnTcpDesc = "Flusso a correzione di errori per penetrare firewall restrittivi",
        protoIkev2Desc = "Protocollo MOBIKE per passaggio istantaneo tra Wi-Fi e rete cellulare",
        protoShadowsocksDesc = "Cifrario a flusso asimmetrico che maschera il traffico VPN come normale HTTPS",

        mtu1500Label = "1500 byte (Standard Ethernet e Fibra)",
        mtu1420Label = "1420 byte (Consigliato per Reti Cellulari 4G/5G)",
        mtu1380Label = "1380 byte (Modalità Sicura con Incapsulamento)",
        mtu1280Label = "1280 byte (Limite Minimo Frammentazione IPv6)",

        regionAll = "Tutti",
        regionNorthAmerica = "Nord America",
        regionEurope = "Europa",
        regionAsiaPacific = "Asia-Pacifico",
        regionLatinAmerica = "America Latina",
        regionMiddleEastAfrica = "Medio Oriente e Africa",
        autoServerName = "Nodo Ottimale (Latenza Minima)",
        autoServerCity = "Instradamento Automatico",
        autoServerCountry = "Automatico Ottimale"
    )

    private val russianStrings = VpnStrings(
        appName = "ET VPN",
        appSubTitle = "Глобальные Шлюзы Онлайн",
        ultraBadge = "ULTRA",
        tabDashboard = "Панель",
        tabMap = "Карта Мира",
        tabGateways = "Шлюзы",
        themesTooltip = "Темы Оформления",
        diagnosticsTooltip = "Журналы Диагностики",
        securitySettingsTooltip = "Настройки Безопасности",
        languageTooltip = "Язык и Регион",

        statusConnected = "ПОДКЛЮЧЕНО",
        statusConnecting = "ПОДКЛЮЧЕНИЕ...",
        statusDisconnecting = "ОТКЛЮЧЕНИЕ...",
        statusDisconnected = "НЕ ПОДКЛЮЧЕНО",
        statusError = "ОШИБКА ПОДКЛЮЧЕНИЯ",
        subConnected = "Туннель зашифрован: ",
        subConnecting = "Инициализация TUN интерфейса...",
        subDisconnecting = "Закрытие сокетов и маршрутов...",
        subDisconnected = "Нажмите для включения защиты",
        subError = "Нажмите для повтора",
        uptime = "Время работы",

        killSwitchOn = "Kill Switch ВКЛ",
        killSwitchOff = "Kill Switch ВЫКЛ",
        ipv6ShieldOn = "Защита IPv6",
        ipv6ShieldOff = "IPv6 Открыт",
        virtualTunnelIp = "IP ВИРТУАЛЬНОГО ТУННЕЛЯ",
        protectedModeOff = "Защищенный режим выключен",
        protectedModeOn = "Безопасный Туннель WireGuard",

        telemetryTitle = "Сетевая Телеметрия в Реальном Времени",
        downloadSpeed = "Загрузка",
        uploadSpeed = "Отдача",
        totalData = "Всего данных",
        duration = "Время работы",
        latency = "Задержка",
        packets = "Пакеты",
        unitPackets = "пакетов",

        targetGateway = "ЦЕЛЕВОЙ ШЛЮЗ",
        gatewaysTitle = "Глобальные Безопасные Узлы",
        searchPlaceholder = "Поиск страны, города или IP...",
        sortFastest = "Быстрые",
        sortLowLoad = "Низкая нагрузка",
        allRegions = "Все",
        connectBtn = "Подключить",
        disconnectBtn = "Отключить",
        activeNode = "Активный Узел",
        selectNode = "Выбрать",
        autoBadge = "AUTO",
        loadLabel = "Нагрузка",
        changeServer = "Сменить сервер",

        mapRadarTitle = "РАДАР ПОЛОЖЕНИЯ ШЛЮЗОВ",
        mapTunnelActive = "ТУННЕЛЬ АКТИВЕН",
        mapRouting = "МАРШРУТИЗАЦИЯ...",
        mapStandby = "ОЖИДАНИЕ",
        mapHint = "Нажмите на узлы карты для переключения шлюза",
        nodesOnline = "Узлов Онлайн",

        settingsTitle = "Безопасность и Сетевая Защита",
        killSwitchTitle = "Защита Kill Switch",
        killSwitchDesc = "Блокирует интернет-трафик при внезапном разрыве VPN туннеля",
        ipv6Title = "Защита от Утечек IPv6",
        ipv6Desc = "Отключает и изолирует незашифрованные IPv6 маршруты",
        autoReconnectTitle = "Плавное Автопереподключение",
        autoReconnectDesc = "Мгновенно восстанавливает туннель при сбоях сети",
        stealthTitle = "Маскировка Трафика (Stealth)",
        stealthDesc = "Маскирует пакеты VPN под обычный HTTPS TLS 1.3 веб-трафик",
        protocolSection = "ПРОТОКОЛ ТУННЕЛЯ И ШИФРОВАНИЕ",
        mtuSection = "ОПТИМИЗАЦИЯ РАЗМЕРА ПАКЕТОВ MTU",
        dnsSection = "ЗАШИФРОВАННЫЕ DNS БЕЗ ЛОГОВ",
        cipherPrefix = "Шифр: ",
        primaryDns = "Основной: ",
        secondaryDns = "Вторичный: ",

        diagnosticsTitle = "Журналы Диагностики",
        diagnosticsSubtitle = "События пакетов интерфейса VpnService TUN",
        clearLogs = "Очистить логи",
        noLogs = "Событий туннеля пока нет. Подключитесь к шлюзу для отображения телеметрии.",

        themesTitle = "Темы Оформления",
        themesSubtitle = "Настройка светлого, темного и пользовательских стилей",
        themeSystemTitle = "Автоматически (Система)",
        themeSystemSub = "Следует системной теме устройства",
        themeWhiteNavyTitle = "Белый и Синий #00012F",
        themeWhiteNavySub = "Светлая тема с элегантным темно-синим оттенком #00012F",
        themeCyberDarkTitle = "Cyber Obsidian",
        themeCyberDarkSub = "Кибернетический контраст на темном технологичном фоне",
        themeAmoledTitle = "AMOLED Абсолютно Черный",
        themeAmoledSub = "Черный цвет (#000000) для максимальной экономии батареи OLED",
        themeDeepNavyTitle = "Deep Tactical Navy",
        themeDeepNavySub = "Тактические морские оттенки с акцентами цвета циан",
        themeNeonMatrixTitle = "Matrix Terminal",
        themeNeonMatrixSub = "Хакерский терминал с изумрудным кибер-свечением",

        languagesTitle = "Язык и Регион",
        languagesSubtitle = "Ручной выбор или умное автоопределение",
        autoDetectTitle = "Автоматически (Локация / IP / Система)",
        autoDetectDesc = "Автоопределение по стране сервера, IP или системному языку",

        protoWireguardDesc = "Туннель нового поколения ChaCha20-Poly1305 Curve25519 с ультранизким пингом",
        protoOpenVpnUdpDesc = "Отраслевой стандарт AES-256-GCM, оптимизированный для высокой скорости",
        protoOpenVpnTcpDesc = "Поток с коррекцией ошибок для обхода строгих брандмауэров",
        protoIkev2Desc = "Протокол MOBIKE для плавного переключения между Wi-Fi и мобильной сетью",
        protoShadowsocksDesc = "Асимметричный потоковый шифр, маскирующий VPN под HTTPS веб-трафик",

        mtu1500Label = "1500 байт (Стандарт Ethernet и Оптоволокно)",
        mtu1420Label = "1420 байт (Рекомендуется для сетей 4G/5G)",
        mtu1380Label = "1380 байт (Безопасный режим инкапсуляции)",
        mtu1280Label = "1280 байт (Минимальный порог фрагментации IPv6)",

        regionAll = "Все",
        regionNorthAmerica = "Северная Америка",
        regionEurope = "Европа",
        regionAsiaPacific = "Азиатско-Тихоокеанский",
        regionLatinAmerica = "Латинская Америка",
        regionMiddleEastAfrica = "Ближний Восток и Африка",
        autoServerName = "Оптимальный Узел (Мин. Пинг)",
        autoServerCity = "Авто-маршрутизация",
        autoServerCountry = "Автоматический Оптимальный"
    )
}
