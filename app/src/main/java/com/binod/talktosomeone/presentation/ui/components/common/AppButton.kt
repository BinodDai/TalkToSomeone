import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.binod.talktosomeone.presentation.ui.theme.Shapes
import com.binod.talktosomeone.presentation.ui.theme.dimensions

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    disabledBackgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
    contentColor: Color = Color.White,
    disabledContentColor: Color = Color.White.copy(alpha = 0.7f),
    height: Dp = 56.dp,
    shape: Shape = Shapes.medium
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = shape,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = disabledBackgroundColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor
        )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary
        )
        if (icon != null) {
            Spacer(modifier = Modifier.width(dimensions.paddingSmall))
            Icon(
                icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
