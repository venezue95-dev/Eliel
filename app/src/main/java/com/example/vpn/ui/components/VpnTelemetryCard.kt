package com.example.vpn.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DangerRose
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.NeonEmerald
import com.example.vpn.model.VpnServer
import com.example.vpn.model.VpnState
import com.example.vpn.model.VpnStrings
import com.example.vpn.model.VpnTrafficStats
import kotlin.math.sin

@Composable
fun VpnTelemetryCard(
    stats: VpnTrafficStats,
    server: VpnServer?,
    vpnState: VpnState,
    strings: VpnStrings,
    modifier: Modifier = Modifier
) {
    val isConnected = vpnState == VpnState.CONNECTED

    val infiniteTransition = rememberInfiniteTransition(label = "traffic_wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
            .testTag("vpn_telemetry_card")
    ) {
        // IP and Connection Status Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
            ) {
                Text(
                    text = strings.virtualTunnelIp,
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (isConnected) "10.0.0.2 / 32 (WireGuard)" else strings.protectedModeOff,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Ping pill
            val pingColor = when {
                !isConnected -> Color.Gray
                stats.currentPingMs <= 40 -> NeonEmerald
                stats.currentPingMs <= 100 -> CyberCyan
                stats.currentPingMs <= 150 -> ElectricAmber
                else -> DangerRose
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(pingColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isConnected) "${stats.currentPingMs} ms" else "-- ms",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        // Real-Time Animated Sine-Wave Graph
        if (isConnected) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(horizontal = 4.dp)
            ) {
                val waveColor = MaterialTheme.colorScheme.primary
                val secondaryWaveColor = NeonEmerald
                Canvas(modifier = Modifier.fillMaxWidth().height(36.dp)) {
                    val width = size.width
                    val height = size.height
                    val midY = height / 2f

                    val path1 = Path()
                    val path2 = Path()

                    val points = 50
                    for (i in 0..points) {
                        val x = (i.toFloat() / points) * width
                        val angle = (i.toFloat() / points) * (4 * Math.PI.toFloat()) + waveOffset
                        val y1 = midY + sin(angle) * (height * 0.35f)
                        val y2 = midY + sin(angle + 1.2f) * (height * 0.25f)

                        if (i == 0) {
                            path1.moveTo(x, y1)
                            path2.moveTo(x, y2)
                        } else {
                            path1.lineTo(x, y1)
                            path2.lineTo(x, y2)
                        }
                    }

                    drawPath(
                        path = path1,
                        color = waveColor.copy(alpha = 0.7f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    drawPath(
                        path = path2,
                        color = secondaryWaveColor.copy(alpha = 0.5f),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Speed Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Download Speed Tile
            MetricTile(
                icon = Icons.Default.ArrowDownward,
                iconTint = NeonEmerald,
                label = strings.downloadSpeed.uppercase(),
                value = if (isConnected) stats.formattedDownloadSpeed() else "0.0 KB/s",
                modifier = Modifier.weight(1f)
            )

            // Upload Speed Tile
            MetricTile(
                icon = Icons.Default.ArrowUpward,
                iconTint = CyberCyan,
                label = strings.uploadSpeed.uppercase(),
                value = if (isConnected) stats.formattedUploadSpeed() else "0.0 KB/s",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Cumulative Data & Protocol Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Total Data Transferred Tile
            MetricTile(
                icon = Icons.Default.Storage,
                iconTint = ElectricAmber,
                label = strings.totalData.uppercase(),
                value = if (isConnected) stats.formattedTotalData() else "0.0 MB",
                modifier = Modifier.weight(1f)
            )

            // Packets count
            MetricTile(
                icon = Icons.Default.Speed,
                iconTint = MaterialTheme.colorScheme.primary,
                label = strings.packets.uppercase(),
                value = if (isConnected) "${stats.packetCount} ${strings.unitPackets}" else "0 ${strings.unitPackets}",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricTile(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
