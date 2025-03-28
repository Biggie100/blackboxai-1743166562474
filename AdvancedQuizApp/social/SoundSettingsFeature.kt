package com.advancedquiz.social

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun SoundSettingsFeature(
    viewModel: SocialViewModel = hiltViewModel()
) {
    var showSettings by remember { mutableStateOf(false) }
    
    // Settings button that can be placed in any screen
    IconButton(onClick = { showSettings = true }) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Sound Settings",
            tint = Color.White
        )
    }
    
    if (showSettings) {
        SoundSettingsDialog(
            onDismiss = { showSettings = false },
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoundSettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SocialViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val soundState by viewModel.soundPreferences.collectAsState()
    
    var volume by remember { mutableStateOf(soundState.volume) }
    var soundsEnabled by remember { mutableStateOf(soundState.enabled) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sound Settings") },
        text = {
            Column {
                SwitchRow(
                    checked = soundsEnabled,
                    onCheckedChange = { soundsEnabled = it },
                    label = "Enable sounds"
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Volume",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Slider(
                        value = volume,
                        onValueChange = { volume = it },
                        valueRange = 0f..1f,
                        steps = 4,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.updateSoundSettings(
                            enabled = soundsEnabled,
                            volume = volume
                        )
                    }
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        }
    )
}

@Composable
private fun SwitchRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

data class SoundSettingsState(
    val enabled: Boolean,
    val volume: Float
)