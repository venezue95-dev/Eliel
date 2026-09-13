package com.example.vpn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DangerRose
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.NeonEmerald
import com.example.vpn.model.VpnState
import com.example.vpn.model.VpnStrings

@Composable
fun VpnPowerButton(
    vpnState: VpnState,
    uptimeText: String,
    strings: VpnStrings,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (vpnState == VpnState.CONNECTED || vpnState == VpnState.CONNECTING) 1.25f else 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = if (vpnState == VpnState.CONNECTED || vpnState == VpnState.CONNECTING) 0.55f else 0.18f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    val reverseRotationAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "reverse_spin_angle"
    )

    val primaryColor by animateColorAsState(
        targetValue = when (vpnState) {
            VpnState.CONNECTED -> NeonEmerald
            VpnState.CONNECTING -> ElectricAmber
            VpnState.DISCONNECTING -> ElectricAmber
            VpnState.ERROR -> DangerRose
            VpnState.DISCONNECTED -> MaterialTheme.colorScheme.primary
        },
        label = "button_color"
    )

    val statusTitle = when (vpnState) {
        VpnState.CONNECTED -> strings.statusConnected
        VpnState.CONNECTING -> strings.statusConnecting
        VpnState.DISCONNECTING -> strings.statusDisconnecting
        VpnState.ERROR -> strings.statusError
        VpnState.DISCONNECTED -> strings.statusDisconnected
    }

    val statusSubtitle = when (vpnState) {
        VpnState.CONNECTED -> "${strings.subConnected}$uptimeText"
        VpnState.CONNECTING -> strings.subConnecting
        VpnState.DISCONNECTING -> strings.subDisconnecting
        VpnState.ERROR -> strings.subError
        VpnState.DISCONNECTED -> strings.subDisconnected
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(204.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer Aura Glow Ring
            Box(
                modifier = Modifier
                    .size(196.dp)
                    .scale(pulseScale)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = pulseAlpha),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Outer Orbit Border Ring 1 (Rotating forward)
            Box(
                modifier = Modifier
                    .size(178.dp)
                    .rotate(if (vpnState == VpnState.CONNECTING) rotationAngle else 0f)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        brush = Brush.sweepGradient(
                            listOf(
                                primaryColor.copy(alpha = 0.85f),
                                primaryColor.copy(alpha = 0.1f),
                                primaryColor.copy(alpha = 0.85f)
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Inner Orbit Border Ring 2 (Rotating reverse for extra high-tech flair)
            Box(
                modifier = Modifier
                    .size(156.dp)
                    .rotate(if (vpnState == VpnState.CONNECTING) reverseRotationAngle else 45f)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.sweepGradient(
                            listOf(
                                primaryColor.copy(alpha = 0.2f),
                                primaryColor.copy(alpha = 0.7f),
                                primaryColor.copy(alpha = 0.1f)
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Core Power Button
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .border(
                        width = 3.dp,
                        color = primaryColor,
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = primaryColor),
                        onClick = onClick
                    )
                    .testTag("vpn_power_button"),
                contentAlignment = Alignment.Center
            ) {
                when (vpnState) {
                    VpnState.CONNECTED -> {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "VPN Active",
                            tint = NeonEmerald,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                    VpnState.CONNECTING, VpnState.DISCONNECTING -> {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Connecting",
                            tint = ElectricAmber,
                            modifier = Modifier
                                .size(54.dp)
                                .rotate(rotationAngle)
                        )
                    }
                    VpnState.ERROR -> {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Error",
                            tint = DangerRose,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                    VpnState.DISCONNECTED -> {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Connect VPN",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Monospace Status Ticker Badge if connected
        if (vpnState == VpnState.CONNECTED) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = NeonEmerald.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.4f)),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(NeonEmerald)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${strings.uptime.uppercase()}: $uptimeText",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        ),
                        color = NeonEmerald
                    )
                }
            }
        }

        Text(
            text = statusTitle,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
            ),
            color = primaryColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = statusSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
