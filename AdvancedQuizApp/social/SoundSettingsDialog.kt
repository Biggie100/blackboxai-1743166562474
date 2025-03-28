package com.advancedquiz.social

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundSettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SocialViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val soundPreferences by viewModel.soundPreferences.collectAsState()
    var volume by remember { mutableStateOf(soundPreferences.volume) }
    var soundsEnabled by remember { mutableStateOf(soundPreferences.enabled) }

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