package com.example.vpn.ui

import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.lifecycle.ViewModel
import com.example.vpn.SecureVpnService
import com.example.vpn.data.VpnServerProvider
import com.example.vpn.model.AppLanguage
import com.example.vpn.model.AppThemeMode
import com.example.vpn.model.LocalizationManager
import com.example.vpn.model.VpnConfig
import com.example.vpn.model.VpnProtocol
import com.example.vpn.model.VpnServer
import com.example.vpn.model.VpnState
import com.example.vpn.model.VpnStrings
import com.example.vpn.model.VpnTrafficStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VpnViewModel : ViewModel() {

    val vpnState: StateFlow<VpnState> = SecureVpnService.vpnState
    val trafficStats: StateFlow<VpnTrafficStats> = SecureVpnService.trafficStats
    val activeServer: StateFlow<VpnServer?> = SecureVpnService.currentServer
    val diagnosticLogs: StateFlow<List<String>> = SecureVpnService.diagnosticLogs

    private val _servers = MutableStateFlow(VpnServerProvider.SERVERS)
    val servers: StateFlow<List<VpnServer>> = _servers.asStateFlow()

    private val _selectedServer = MutableStateFlow(VpnServerProvider.AUTO_SERVER)
    val selectedServer: StateFlow<VpnServer> = _selectedServer.asStateFlow()

    private val _vpnConfig = MutableStateFlow(VpnConfig())
    val vpnConfig: StateFlow<VpnConfig> = _vpnConfig.asStateFlow()

    private val _currentTheme = MutableStateFlow(AppThemeMode.SYSTEM)
    val currentTheme: StateFlow<AppThemeMode> = _currentTheme.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.AUTO)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _strings = MutableStateFlow(LocalizationManager.getStrings(AppLanguage.AUTO))
    val strings: StateFlow<VpnStrings> = _strings.asStateFlow()

    private val _vpnPrepareIntent = MutableStateFlow<Intent?>(null)
    val vpnPrepareIntent: StateFlow<Intent?> = _vpnPrepareIntent.asStateFlow()

    fun selectServer(server: VpnServer) {
        _selectedServer.value = server
        // If language mode is AUTO, adapt strings if country matches
        updateStrings()
        SecureVpnService.addLog("Selected target node: ${server.name} (${server.city}, ${server.country}) [${server.formattedCoordinates()}]")
    }

    fun initLocale(context: Context) {
        val savedLang = LocalizationManager.getSavedLanguage(context)
        _currentLanguage.value = savedLang
        updateStrings()
    }

    fun setLanguage(language: AppLanguage, context: Context? = null) {
        _currentLanguage.value = language
        updateStrings()
        context?.let {
            LocalizationManager.saveLanguage(it, language)
        }
        SecureVpnService.addLog("Application language set to ${language.title}")
    }

    private fun updateStrings() {
        val serverCode = if (_selectedServer.value.isAuto) null else _selectedServer.value.countryCode
        _strings.value = LocalizationManager.getStrings(_currentLanguage.value, serverCode)
    }

    fun setTheme(theme: AppThemeMode) {
        _currentTheme.value = theme
        SecureVpnService.addLog("UI Theme set to ${theme.defaultTitle}")
    }

    fun cycleTheme() {
        val allThemes = AppThemeMode.entries
        val nextIndex = (allThemes.indexOf(_currentTheme.value) + 1) % allThemes.size
        setTheme(allThemes[nextIndex])
    }

    fun setProtocol(protocol: VpnProtocol) {
        _vpnConfig.value = _vpnConfig.value.copy(protocol = protocol)
        SecureVpnService.addLog("Switched tunnel protocol to ${protocol.displayName} (${protocol.cipherInfo})")
    }

    fun toggleKillSwitch(enabled: Boolean) {
        _vpnConfig.value = _vpnConfig.value.copy(killSwitchEnabled = enabled)
        SecureVpnService.addLog("Kill Switch ${if (enabled) "ENABLED" else "DISABLED"}")
    }

    fun toggleIpv6Protection(enabled: Boolean) {
        _vpnConfig.value = _vpnConfig.value.copy(ipv6Protection = enabled)
        SecureVpnService.addLog("IPv6 Leak Shield ${if (enabled) "ENABLED" else "DISABLED"}")
    }

    fun toggleAutoReconnect(enabled: Boolean) {
        _vpnConfig.value = _vpnConfig.value.copy(autoReconnect = enabled)
        SecureVpnService.addLog("Auto-Reconnect ${if (enabled) "ENABLED" else "DISABLED"}")
    }

    fun toggleStealthObfuscation(enabled: Boolean) {
        _vpnConfig.value = _vpnConfig.value.copy(stealthObfuscation = enabled)
        SecureVpnService.addLog("Stealth Obfuscation ${if (enabled) "ENABLED" else "DISABLED"}")
    }

    fun setMtu(mtu: Int) {
        _vpnConfig.value = _vpnConfig.value.copy(mtu = mtu)
        SecureVpnService.addLog("MTU tuned to $mtu bytes")
    }

    fun setDnsServer(dns: String, secondaryDns: String = "1.0.0.1") {
        _vpnConfig.value = _vpnConfig.value.copy(dnsServer = dns, secondaryDns = secondaryDns)
        SecureVpnService.addLog("DNS resolver changed to $dns / $secondaryDns")
    }

    fun clearLogs() {
        SecureVpnService.clearLogs()
    }

    fun toggleConnection(context: Context) {
        when (vpnState.value) {
            VpnState.CONNECTED, VpnState.CONNECTING -> {
                disconnect(context)
            }
            VpnState.DISCONNECTED, VpnState.ERROR -> {
                connect(context)
            }
            VpnState.DISCONNECTING -> {
                // Wait for disconnection to finish
            }
        }
    }

    fun connect(context: Context) {
        val prepareIntent = VpnService.prepare(context)
        if (prepareIntent != null) {
            _vpnPrepareIntent.value = prepareIntent
        } else {
            startVpnService(context)
        }
    }

    fun onVpnPermissionGranted(context: Context) {
        _vpnPrepareIntent.value = null
        startVpnService(context)
    }

    fun onVpnPermissionDenied() {
        _vpnPrepareIntent.value = null
        SecureVpnService.addLog("System VPN dialog permission was declined by user")
    }

    private fun startVpnService(context: Context) {
        val server = _selectedServer.value
        val config = _vpnConfig.value
        SecureVpnService.startVpn(context, server, config)
    }

    fun disconnect(context: Context) {
        SecureVpnService.stopVpn(context)
    }
}
