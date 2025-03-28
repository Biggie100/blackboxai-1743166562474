package com.advancedquiz.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupTypingManager @Inject constructor() {
    private val typingStatus = mutableMapOf<String, MutableStateFlow<Set<String>>>()
    private val typingTimers = mutableMapOf<String, Job>()

    fun getTypingMembers(groupId: String): StateFlow<Set<String>> {
        return typingStatus.getOrPut(groupId) { MutableStateFlow(emptySet()) }.asStateFlow()
    }

    fun setTypingStatus(groupId: String, userId: String, isTyping: Boolean) {
        viewModelScope.launch {
            val statusFlow = typingStatus.getOrPut(groupId) { MutableStateFlow(emptySet()) }
            
            // Cancel any existing timer for this user
            typingTimers["$groupId-$userId"]?.cancel()

            if (isTyping) {
                // Add user to typing list if not already present
                if (!statusFlow.value.contains(userId)) {
                    statusFlow.value = statusFlow.value + userId
                }

                // Set timer to remove typing status after 3 seconds of inactivity
                typingTimers["$groupId-$userId"] = viewModelScope.launch {
                    delay(3000)
                    statusFlow.value = statusFlow.value - userId
                }
            } else {
                // Remove user from typing list
                statusFlow.value = statusFlow.value - userId
            }
        }
    }

    fun clearAll() {
        typingTimers.values.forEach { it.cancel() }
        typingStatus.clear()
    }
}