package com.example.vpn.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.NeonEmerald
import com.example.vpn.model.VpnServer
import com.example.vpn.model.VpnState
import com.example.vpn.model.VpnStrings
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun VpnServerLocationMapCard(
    activeServer: VpnServer,
    allServers: List<VpnServer>,
    vpnState: VpnState,
    strings: VpnStrings,
    onSelectServer: (VpnServer) -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onToggleExpand: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "map_radar_transition")

    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_pulse"
    )

    val beamAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "beam_flow"
    )

    var tappedServer by remember(activeServer) { mutableStateOf<VpnServer?>(null) }
    val displayServer = tappedServer ?: activeServer

    val isConnected = vpnState == VpnState.CONNECTED
    val isConnecting = vpnState == VpnState.CONNECTING

    val primaryAccent = when {
        isConnected -> NeonEmerald
        isConnecting -> ElectricAmber
        else -> CyberCyan
    }

    CardContainer(modifier = modifier.testTag("vpn_world_map_card")) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(primaryAccent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radar,
                            contentDescription = null,
                            tint = primaryAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.globalGatewayRadar,
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 1.2.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${displayServer.city}, ${displayServer.country}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = primaryAccent.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, primaryAccent.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { onToggleExpand?.invoke() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = displayServer.flagEmoji,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${displayServer.pingMs} ms",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            ),
                            color = primaryAccent,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Interactive Canvas Map
            val mapHeight = if (isExpanded) 340.dp else 220.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF060B14))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(allServers) {
                            detectTapGestures { tapOffset ->
                                val w = size.width
                                val h = size.height
                                var closest: VpnServer? = null
                                var minDistance = Float.MAX_VALUE

                                for (s in allServers) {
                                    val (sx, sy) = projectLatLon(s.latitude, s.longitude, w.toFloat(), h.toFloat())
                                    val dist = hypot(tapOffset.x - sx, tapOffset.y - sy)
                                    if (dist < 35f && dist < minDistance) {
                                        minDistance = dist
                                        closest = s
                                    }
                                }

                                if (closest != null) {
                                    tappedServer = closest
                                    onSelectServer(closest)
                                }
                            }
                        }
                ) {
                    val w = size.width
                    val h = size.height

                    // 1. Draw Grid Lines
                    drawCoordinateGrid(w, h)

                    // 2. Draw Vector Continents
                    drawWorldContinents(w, h)

                    // 3. Draw Inactive Server Nodes
                    allServers.forEach { server ->
                        if (server.id != activeServer.id && !server.isAuto) {
                            val (x, y) = projectLatLon(server.latitude, server.longitude, w, h)
                            drawCircle(
                                color = Color(0xFF64748B).copy(alpha = 0.6f),
                                radius = 2.5f,
                                center = Offset(x, y)
                            )
                        }
                    }

                    // 4. Client Virtual Location (e.g. Device node e.g. Madrid/Local origin)
                    val clientLat = 40.4168
                    val clientLon = -3.7038
                    val (cx, cy) = projectLatLon(clientLat, clientLon, w, h)

                    val (targetX, targetY) = projectLatLon(
                        displayServer.latitude,
                        displayServer.longitude,
                        w,
                        h
                    )

                    // Draw connection laser arc if connected or connecting
                    if (isConnected || isConnecting) {
                        drawConnectionBeam(
                            start = Offset(cx, cy),
                            end = Offset(targetX, targetY),
                            color = primaryAccent,
                            flowProgress = beamAnim,
                            isConnected = isConnected
                        )
                    }

                    // Draw Client Pin
                    drawCircle(
                        color = CyberCyan.copy(alpha = 0.3f),
                        radius = 8f,
                        center = Offset(cx, cy)
                    )
                    drawCircle(
                        color = CyberCyan,
                        radius = 4f,
                        center = Offset(cx, cy)
                    )

                    // 5. Active Target Server Radar Rings
                    val maxRadius = 36f
                    val ring1Radius = maxRadius * radarPulse
                    val ring1Alpha = (1f - radarPulse).coerceIn(0f, 1f)

                    drawCircle(
                        color = primaryAccent.copy(alpha = ring1Alpha * 0.7f),
                        radius = ring1Radius,
                        center = Offset(targetX, targetY),
                        style = Stroke(width = 2f)
                    )

                    val ring2Pulse = ((radarPulse + 0.5f) % 1f)
                    val ring2Alpha = (1f - ring2Pulse).coerceIn(0f, 1f)
                    drawCircle(
                        color = primaryAccent.copy(alpha = ring2Alpha * 0.5f),
                        radius = maxRadius * ring2Pulse,
                        center = Offset(targetX, targetY),
                        style = Stroke(width = 1.5f)
                    )

                    // Center Target Core
                    drawCircle(
                        color = primaryAccent.copy(alpha = 0.35f),
                        radius = 9f,
                        center = Offset(targetX, targetY)
                    )
                    drawCircle(
                        color = primaryAccent,
                        radius = 5f,
                        center = Offset(targetX, targetY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2.2f,
                        center = Offset(targetX, targetY)
                    )
                }

                // Coordinates & Telemetry Floating HUD inside map
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xCC030712))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PinDrop,
                                contentDescription = null,
                                tint = primaryAccent,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = displayServer.formattedCoordinates(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color(0xFFE2E8F0)
                            )
                        }
                        Text(
                            text = "IP: ${displayServer.ipAddress} • ${strings.loadLabel}: ${displayServer.loadPercent}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp
                            ),
                            color = Color(0xFF94A3B8),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                // Status Tag inside map top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isConnected) NeonEmerald.copy(alpha = 0.2f)
                            else if (isConnecting) ElectricAmber.copy(alpha = 0.2f)
                            else Color(0x991E293B)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isConnected) strings.stateConnected.uppercase() else if (isConnecting) strings.stateConnecting.uppercase() else strings.stateDisconnected.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = primaryAccent
                    )
                }
            }

            // Sub-info Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.tapNodeToSwitch,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )

                Text(
                    text = "${allServers.size} ${strings.nodesOnline}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

private fun DrawScope.drawCoordinateGrid(w: Float, h: Float) {
    val gridColor = Color(0xFF162032)

    // Latitudes: Equator, +/- 30, +/- 60
    val lats = listOf(0.0, 30.0, -30.0, 60.0, -60.0)
    for (lat in lats) {
        val y = ((90.0 - lat) / 180.0 * h).toFloat()
        drawLine(
            color = if (lat == 0.0) Color(0xFF1E2E48) else gridColor,
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = if (lat == 0.0) 1.2f else 0.8f,
            pathEffect = if (lat == 0.0) null else PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    }

    // Longitudes: Prime meridian, +/- 60, +/- 120
    val lons = listOf(-120.0, -60.0, 0.0, 60.0, 120.0)
    for (lon in lons) {
        val x = ((lon + 180.0) / 360.0 * w).toFloat()
        drawLine(
            color = if (lon == 0.0) Color(0xFF1E2E48) else gridColor,
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = if (lon == 0.0) 1.2f else 0.8f,
            pathEffect = if (lon == 0.0) null else PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )
    }
}

private fun DrawScope.drawWorldContinents(w: Float, h: Float) {
    val landColor = Color(0xFF131D2D)
    val outlineColor = Color(0xFF1F2F47)

    // Continent Polygons in Lat/Lon coordinates
    val continents = listOf(
        // North America
        listOf(
            70.0 to -160.0, 72.0 to -125.0, 60.0 to -85.0, 52.0 to -55.0,
            44.0 to -64.0, 30.0 to -81.0, 25.0 to -80.0, 20.0 to -87.0,
            15.0 to -90.0, 18.0 to -105.0, 32.0 to -118.0, 48.0 to -124.0,
            60.0 to -140.0, 65.0 to -168.0
        ),
        // South America
        listOf(
            12.0 to -72.0, 8.0 to -60.0, -5.0 to -35.0, -22.0 to -41.0,
            -35.0 to -55.0, -55.0 to -68.0, -50.0 to -75.0, -20.0 to -70.0,
            -5.0 to -81.0, 8.0 to -77.0
        ),
        // Europe
        listOf(
            71.0 to 28.0, 60.0 to 30.0, 55.0 to 20.0, 45.0 to 14.0,
            36.0 to -6.0, 43.0 to -9.0, 48.0 to -5.0, 58.0 to 5.0,
            65.0 to 12.0, 70.0 to 20.0
        ),
        // Africa
        listOf(
            37.0 to 10.0, 31.0 to 32.0, 12.0 to 51.0, -5.0 to 40.0,
            -25.0 to 33.0, -34.0 to 20.0, -20.0 to 12.0, 4.0 to 9.0,
            5.0 to -2.0, 15.0 to -17.0, 35.0 to -6.0
        ),
        // Asia
        listOf(
            75.0 to 100.0, 70.0 to 170.0, 60.0 to 165.0, 40.0 to 140.0,
            30.0 to 122.0, 22.0 to 108.0, 10.0 to 104.0, 10.0 to 76.0,
            24.0 to 68.0, 26.0 to 56.0, 40.0 to 35.0, 60.0 to 60.0,
            65.0 to 80.0
        ),
        // Australia
        listOf(
            -12.0 to 132.0, -15.0 to 145.0, -28.0 to 153.0, -38.0 to 148.0,
            -35.0 to 117.0, -22.0 to 114.0, -15.0 to 124.0
        )
    )

    continents.forEach { polygon ->
        val path = Path()
        polygon.forEachIndexed { index, (lat, lon) ->
            val (x, y) = projectLatLon(lat, lon, w, h)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path = path, color = landColor)
        drawPath(path = path, color = outlineColor, style = Stroke(width = 1.2f))
    }
}

private fun DrawScope.drawConnectionBeam(
    start: Offset,
    end: Offset,
    color: Color,
    flowProgress: Float,
    isConnected: Boolean
) {
    val controlX = (start.x + end.x) / 2
    val controlY = (start.y + end.y) / 2 - 45f // Arc upward

    val path = Path().apply {
        moveTo(start.x, start.y)
        quadraticTo(controlX, controlY, end.x, end.y)
    }

    // Base glowing path
    drawPath(
        path = path,
        color = color.copy(alpha = 0.35f),
        style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f)))
    )

    // Moving laser packet along the curve
    val t = flowProgress
    val packetX = (1 - t) * (1 - t) * start.x + 2 * (1 - t) * t * controlX + t * t * end.x
    val packetY = (1 - t) * (1 - t) * start.y + 2 * (1 - t) * t * controlY + t * t * end.y

    drawCircle(
        color = color,
        radius = 4f,
        center = Offset(packetX, packetY)
    )
    drawCircle(
        color = Color.White,
        radius = 2f,
        center = Offset(packetX, packetY)
    )
}

private fun projectLatLon(lat: Double, lon: Double, width: Float, height: Float): Pair<Float, Float> {
    val x = ((lon + 180.0) / 360.0 * width).toFloat().coerceIn(0f, width)
    val y = ((90.0 - lat) / 180.0 * height).toFloat().coerceIn(0f, height)
    return Pair(x, y)
}

@Composable
private fun CardContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(4.dp)
    ) {
        content()
    }
}
