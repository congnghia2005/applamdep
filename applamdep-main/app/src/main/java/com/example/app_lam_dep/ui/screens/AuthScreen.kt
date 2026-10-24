package com.example.app_lam_dep.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_lam_dep.ui.icons.GlowBrandIcon
import com.example.app_lam_dep.ui.icons.GlowIcon
import com.example.app_lam_dep.ui.icons.GlowIcons
import com.example.app_lam_dep.ui.icons.IconMedallion
import com.example.app_lam_dep.ui.theme.ChampagneGold
import com.example.app_lam_dep.ui.theme.DustyMerlot
import com.example.app_lam_dep.ui.theme.TextMuted

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFFBF7E8) // Warm White canvas
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Crest & Logo
            IconMedallion(
                icon = GlowIcons.Diamond,
                size = 64.dp,
                iconSize = 32.dp,
                tint = ChampagneGold,
                contentDescription = "Glow Prestige Crest"
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "GLOW PRESTIGE",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = DustyMerlot,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(28.dp))

            // Subtitle
            Text(
                text = "Create an account",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF24090F)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Enter your email to sign up for this app",
                fontSize = 13.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("email@domain.com", color = TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DustyMerlot,
                    unfocusedBorderColor = Color(0xFFEBDCD3),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Continue Button
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DustyMerlot)
            ) {
                Text(text = "Continue", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider "or"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFEBDCD3))
                Text(
                    text = "  or  ",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFEBDCD3))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Logins
            OutlinedButton(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlowBrandIcon(icon = GlowIcons.Google, size = 18.dp)
                    Text(text = "Continue with Google", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF24090F))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlowIcon(icon = GlowIcons.Apple, tint = Color.Black, size = 18.dp)
                    Text(text = "Continue with Apple", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF24090F))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Terms
            Text(
                text = "By clicking continue, you agree to our Terms of Service and Privacy Policy",
                fontSize = 11.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
