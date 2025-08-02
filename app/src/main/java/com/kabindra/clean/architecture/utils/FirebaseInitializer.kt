package com.kabindra.clean.architecture.utils

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun initializeFirebase() {
    FirebaseApp.initializeApp(appContext!!)
}

suspend fun getToken(): String? {
    return FirebaseMessaging.getInstance().token.await()
}

suspend fun deleteToken() {
    FirebaseMessaging.getInstance().deleteToken().await()
}

suspend fun subscribeToTopic(topic: String) {
    FirebaseMessaging.getInstance().subscribeToTopic(topic).await()
}

suspend fun unsubscribeFromTopic(topic: String) {
    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).await()
}

object NavigationManager {
    private val _navigationState = MutableSharedFlow<NavigationEvent>(replay = 1)
    val navigationState: SharedFlow<NavigationEvent> = _navigationState.asSharedFlow()

    suspend fun handleNavigation(pageId: String) {
        _navigationState.emit(NavigationEvent(pageId))
    }

    suspend fun resetNavigationState() {
        _navigationState.emit(NavigationEvent(""))
    }
}

fun handleNavigationRedirection(pageId: String) {
    CoroutineScope(Dispatchers.Main).launch {
        NavigationManager.handleNavigation(
            pageId = pageId
        )
    }
}

data class NavigationEvent(
    val pageId: String?
)


