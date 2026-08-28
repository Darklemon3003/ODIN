package com.odin.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.odin.core.ui.theme.OdinBackground
import com.odin.core.ui.theme.OdinBorder
import com.odin.core.ui.theme.OdinDisabled
import com.odin.core.ui.theme.OdinError
import com.odin.core.ui.theme.OdinInfo
import com.odin.core.ui.theme.OdinPrimary
import com.odin.core.ui.theme.OdinPrimarySoft
import com.odin.core.ui.theme.OdinSurface
import com.odin.core.ui.theme.OdinSurfaceSoft
import com.odin.core.ui.theme.OdinSurfaceVariant
import com.odin.core.ui.theme.OdinTextMuted
import com.odin.core.ui.theme.OdinTextPrimary
import com.odin.core.ui.theme.OdinTextSecondary
import com.odin.core.ui.theme.OdinWarning

enum class OdinStatusTone {
    Safe,
    Warning,
    Error,
    Neutral,
    Planned,
    Unavailable
}

@Composable
fun OdinCard(
    modifier: Modifier = Modifier,
    alert: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (alert) OdinSurfaceVariant else OdinSurface
        ),
        border = BorderStroke(1.dp, if (alert) OdinWarning else OdinBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
fun OdinStatusChip(
    text: String,
    tone: OdinStatusTone,
    modifier: Modifier = Modifier
) {
    val colors = when (tone) {
        OdinStatusTone.Safe -> OdinPrimary to OdinPrimarySoft
        OdinStatusTone.Warning -> OdinWarning to OdinSurfaceVariant
        OdinStatusTone.Error -> OdinError to OdinSurfaceVariant
        OdinStatusTone.Neutral -> OdinTextSecondary to OdinSurfaceSoft
        OdinStatusTone.Planned -> OdinInfo to OdinSurfaceVariant
        OdinStatusTone.Unavailable -> OdinDisabled to OdinSurfaceSoft
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colors.second),
        border = BorderStroke(1.dp, colors.first)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = colors.first
        )
    }
}

@Composable
fun OdinPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = OdinPrimary,
            contentColor = OdinBackground,
            disabledContainerColor = OdinSurfaceSoft,
            disabledContentColor = OdinTextMuted
        )
    ) {
        OdinButtonContent(text = text, icon = icon)
    }
}

@Composable
fun OdinSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (enabled) OdinBorder else OdinDisabled),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = OdinSurfaceVariant,
            contentColor = OdinTextPrimary,
            disabledContainerColor = OdinSurfaceSoft,
            disabledContentColor = OdinTextMuted
        )
    ) {
        OdinButtonContent(text = text, icon = icon)
    }
}

@Composable
fun OdinDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = OdinError,
            contentColor = Color.White,
            disabledContainerColor = OdinSurfaceSoft,
            disabledContentColor = OdinTextMuted
        )
    ) {
        OdinButtonContent(text = text, icon = icon)
    }
}

@Composable
fun OdinSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = OdinTextPrimary
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = OdinTextSecondary
            )
        }
    }
}

@Composable
fun OdinMetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    tone: OdinStatusTone = OdinStatusTone.Neutral
) {
    OdinCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = OdinTextSecondary
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = odinToneColor(tone)
                )
            }
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = odinToneColor(tone)
                )
            }
        }
    }
}

@Composable
fun OdinEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Info
) {
    OdinCard(modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OdinInfo
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = OdinTextPrimary
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = OdinTextSecondary
        )
    }
}

@Composable
fun OdinBlockedFeatureCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    status: String = "No disponible"
) {
    OdinCard(modifier = modifier, alert = true) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = null,
                    tint = OdinWarning
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = OdinTextPrimary
                )
            }
            OdinStatusChip(text = status, tone = OdinStatusTone.Unavailable)
        }
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = OdinTextSecondary
        )
    }
}

@Composable
private fun OdinButtonContent(text: String, icon: ImageVector?) {
    if (icon != null) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
    }
    Text(text = text)
}

private fun odinToneColor(tone: OdinStatusTone): Color = when (tone) {
    OdinStatusTone.Safe -> OdinPrimary
    OdinStatusTone.Warning -> OdinWarning
    OdinStatusTone.Error -> OdinError
    OdinStatusTone.Neutral -> OdinTextSecondary
    OdinStatusTone.Planned -> OdinInfo
    OdinStatusTone.Unavailable -> OdinDisabled
}
