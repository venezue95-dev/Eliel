package com.example.vpn.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EnhancedEncryption
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonEmerald
import com.example.vpn.model.AppLanguage
import com.example.vpn.model.VpnState
import com.example.vpn.ui.components.VpnDiagnosticsSheet
import com.example.vpn.ui.components.VpnGatewaysView
import com.example.vpn.ui.components.VpnLanguageSelectorSheet
import com.example.vpn.ui.components.VpnPowerButton
import com.example.vpn.ui.components.VpnSecuritySettingsSheet
import com.example.vpn.ui.components.VpnServerBottomSheet
import com.example.vpn.ui.components.VpnServerLocationMapCard
import com.example.vpn.ui.components.VpnServerSelectorCard
import com.example.vpn.ui.components.VpnTelemetryCard
import com.example.vpn.ui.components.VpnThemeSelectorSheet

enum class ScreenTab {
    DASHBOARD,
    WORLD_MAP,
    GLOBAL_NODES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VpnMainScreen(
    viewModel: VpnViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vpnState by viewModel.vpnState.collectAsState()
    val trafficStats by viewModel.trafficStats.collectAsState()
    val servers by viewModel.servers.collectAsState()
    val selectedServer by viewModel.selectedServer.collectAsState()
    val vpnConfig by viewModel.vpnConfig.collectAsState()
    val diagnosticLogs by viewModel.diagnosticLogs.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val strings by viewModel.strings.collectAsState()
    val vpnPrepareIntent by viewModel.vpnPrepareIntent.collectAsState()

    var activeTab by remember { mutableStateOf(ScreenTab.DASHBOARD) }
    var showServerSheet by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showDiagnosticsSheet by remember { mutableStateOf(false) }

    // System VPN Permission Consent Launcher
    val vpnConsentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.onVpnPermissionGranted(context)
        } else {
            viewModel.onVpnPermissionDenied()
        }
    }

    // Android 13+ Notification Permission Launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* Permission handled gracefully */ }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // React to system VPN consent requirement
    LaunchedEffect(vpnPrepareIntent) {
        vpnPrepareIntent?.let { intent ->
            vpnConsentLauncher.launch(intent)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                                .border(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "ET VPN Logo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = strings.appName,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = strings.ultraBadge,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                            Text(
                                text = "${servers.size} ${strings.appSubTitle}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Language Selector Button
                    IconButton(
                        onClick = { showLanguageSheet = true },
                        modifier = Modifier.testTag("language_selector_button")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentLanguage.flag,
                                fontSize = 16.sp
                            )
                        }
                    }

                    // Theme Button
                    IconButton(
                        onClick = { showThemeSheet = true },
                        modifier = Modifier.testTag("theme_selector_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = strings.themesTooltip,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Diagnostics Terminal
                    IconButton(
                        onClick = { showDiagnosticsSheet = true },
                        modifier = Modifier.testTag("diagnostics_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = strings.diagnosticsTooltip,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Security Settings
                    IconButton(
                        onClick = { showSettingsSheet = true },
                        modifier = Modifier.testTag("security_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = strings.securitySettingsTooltip,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == ScreenTab.DASHBOARD,
                    onClick = { activeTab = ScreenTab.DASHBOARD },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = strings.tabDashboard) },
                    label = { Text(strings.tabDashboard) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                    ),
                    modifier = Modifier.testTag("tab_dashboard")
                )
                NavigationBarItem(
                    selected = activeTab == ScreenTab.WORLD_MAP,
                    onClick = { activeTab = ScreenTab.WORLD_MAP },
                    icon = { Icon(Icons.Default.Map, contentDescription = strings.tabMap) },
                    label = { Text(strings.tabMap) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                    ),
                    modifier = Modifier.testTag("tab_world_map")
                )
                NavigationBarItem(
                    selected = activeTab == ScreenTab.GLOBAL_NODES,
                    onClick = { activeTab = ScreenTab.GLOBAL_NODES },
                    icon = { Icon(Icons.Default.Public, contentDescription = strings.tabGateways) },
                    label = { Text(strings.tabGateways) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                    ),
                    modifier = Modifier.testTag("tab_servers")
                )
            }
        }
    ) { innerPadding ->
        when (activeTab) {
            ScreenTab.GLOBAL_NODES -> {
                // Dedicated, Full Embedded Gateways View with independent scrolling
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    VpnGatewaysView(
                        servers = servers,
                        selectedServer = selectedServer,
                        vpnState = vpnState,
                        strings = strings,
                        onSelectServer = { viewModel.selectServer(it) }
                    )
                }
            }

            ScreenTab.DASHBOARD, ScreenTab.WORLD_MAP -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 18.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Security Status Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(
                            icon = Icons.Default.VpnKey,
                            label = vpnConfig.protocol.displayName
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        StatusBadge(
                            icon = Icons.Default.GppGood,
                            label = if (vpnConfig.killSwitchEnabled) strings.killSwitchOn else strings.killSwitchOff,
                            tint = if (vpnConfig.killSwitchEnabled) NeonEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        StatusBadge(
                            icon = Icons.Default.EnhancedEncryption,
                            label = if (vpnConfig.ipv6Protection) strings.ipv6ShieldOn else strings.ipv6ShieldOff,
                            tint = if (vpnConfig.ipv6Protection) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (activeTab == ScreenTab.DASHBOARD) {
                        // Central Power Button with Upgraded Dual-Orbit Animation & Multi-Language Status
                        VpnPowerButton(
                            vpnState = vpnState,
                            uptimeText = trafficStats.formattedDuration(),
                            strings = strings,
                            onClick = { viewModel.toggleConnection(context) }
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // Selected Server Card
                        VpnServerSelectorCard(
                            selectedServer = selectedServer,
                            vpnState = vpnState,
                            strings = strings,
                            onClick = { showServerSheet = true }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interactive Server Location Radar Map
                        VpnServerLocationMapCard(
                            activeServer = selectedServer,
                            allServers = servers,
                            vpnState = vpnState,
                            strings = strings,
                            onSelectServer = { viewModel.selectServer(it) },
                            isExpanded = false,
                            onToggleExpand = { activeTab = ScreenTab.WORLD_MAP }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Bandwidth & Telemetry Card with Animated Traffic Sine-Wave Visualizer
                        VpnTelemetryCard(
                            stats = trafficStats,
                            server = selectedServer,
                            vpnState = vpnState,
                            strings = strings
                        )
                    } else {
                        // Full Expanded World Map View (Stay on Map, do not force jump to Dashboard)
                        VpnServerLocationMapCard(
                            activeServer = selectedServer,
                            allServers = servers,
                            vpnState = vpnState,
                            strings = strings,
                            onSelectServer = { viewModel.selectServer(it) },
                            isExpanded = true,
                            onToggleExpand = null
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        VpnServerSelectorCard(
                            selectedServer = selectedServer,
                            vpnState = vpnState,
                            strings = strings,
                            onClick = { showServerSheet = true }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        VpnTelemetryCard(
                            stats = trafficStats,
                            server = selectedServer,
                            vpnState = vpnState,
                            strings = strings
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Modal Sheets
    if (showServerSheet) {
        VpnServerBottomSheet(
            servers = servers,
            selectedServer = selectedServer,
            strings = strings,
            onServerSelected = { viewModel.selectServer(it) },
            onDismiss = {
                showServerSheet = false
            }
        )
    }

    if (showLanguageSheet) {
        VpnLanguageSelectorSheet(
            currentLanguage = currentLanguage,
            strings = strings,
            onLanguageSelected = { viewModel.setLanguage(it, context) },
            onDismiss = { showLanguageSheet = false }
        )
    }

    if (showSettingsSheet) {
        VpnSecuritySettingsSheet(
            config = vpnConfig,
            strings = strings,
            onProtocolChange = { viewModel.setProtocol(it) },
            onKillSwitchToggle = { viewModel.toggleKillSwitch(it) },
            onIpv6Toggle = { viewModel.toggleIpv6Protection(it) },
            onAutoReconnectToggle = { viewModel.toggleAutoReconnect(it) },
            onStealthToggle = { viewModel.toggleStealthObfuscation(it) },
            onMtuChange = { viewModel.setMtu(it) },
            onDnsChange = { dns1, dns2 -> viewModel.setDnsServer(dns1, dns2) },
            onDismiss = { showSettingsSheet = false }
        )
    }

    if (showThemeSheet) {
        VpnThemeSelectorSheet(
            currentTheme = currentTheme,
            strings = strings,
            onThemeSelected = { viewModel.setTheme(it) },
            onDismiss = { showThemeSheet = false }
        )
    }

    if (showDiagnosticsSheet) {
        VpnDiagnosticsSheet(
            logs = diagnosticLogs,
            strings = strings,
            onClearLogs = { viewModel.clearLogs() },
            onDismiss = { showDiagnosticsSheet = false }
        )
    }
}

@Composable
private fun StatusBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
