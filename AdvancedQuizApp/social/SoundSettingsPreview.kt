package com.advancedquiz.social

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.advancedquiz.core.ui.theme.AdvancedQuizTheme

@Preview(showBackground = true)
@Composable
fun SoundSettingsDialogPreview() {
    AdvancedQuizTheme {
        var showDialog by remember { mutableStateOf(true) }
        
        if (showDialog) {
            SoundSettingsDialog(
                onDismiss = { showDialog = false },
                viewModel = object : SocialViewModel() {
                    override val soundPreferences = MutableStateFlow(
                        SoundSettingsState(enabled = true, volume = 0.5f)
                    )
                    
                    override suspend fun updateSoundSettings(enabled: Boolean, volume: Float) {
                        soundPreferences.value = SoundSettingsState(enabled, volume)
                    }
                }
            )
        } else {
            Text("Click to show dialog", modifier = Modifier.clickable { showDialog = true })
        }
    }
}

@Preview
@Composable
fun SoundSettingsFeaturePreview() {
    AdvancedQuizTheme {
        SoundSettingsFeature(
            viewModel = object : SocialViewModel() {
                override val soundPreferences = MutableStateFlow(
                    SoundSettingsState(enabled = true, volume = 0.7f)
                )
                
                override suspend fun updateSoundSettings(enabled: Boolean, volume: Float) {
                    soundPreferences.value = SoundSettingsState(enabled, volume)
                }
            }
        )
    }
}