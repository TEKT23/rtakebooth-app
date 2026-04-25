package org.rtakebooth.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.navigation.Screen

@Composable
fun TopBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onLaunchBooth: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // App Title
            Text(
                text = "BoothMaster Pro",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.width(32.dp))
            
            // Navigation Links
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Screen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    Text(
                        text = screen.label.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier
                            .clickable { onNavigate(screen) }
                            .padding(vertical = 12.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Launch Booth Button
            Button(
                onClick = onLaunchBooth,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Launch Booth",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
