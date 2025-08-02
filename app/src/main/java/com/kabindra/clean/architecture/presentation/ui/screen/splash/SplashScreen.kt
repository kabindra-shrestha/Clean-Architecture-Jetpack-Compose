package com.kabindra.clean.architecture.presentation.ui.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kabindra.clean.architecture.data.request.LoginRefreshUserDetailsDataRequest
import com.kabindra.clean.architecture.domain.entity.LoginRefreshUserDetails
import com.kabindra.clean.architecture.domain.entity.User
import com.kabindra.clean.architecture.presentation.ui.component.AppIcon
import com.kabindra.clean.architecture.presentation.ui.component.LoadingIndicator
import com.kabindra.clean.architecture.presentation.ui.component.TextComponent
import com.kabindra.clean.architecture.presentation.viewmodel.remote.LoginViewModel
import com.kabindra.clean.architecture.presentation.viewmodel.room.AuthenticationRoomViewModel
import com.kabindra.clean.architecture.presentation.viewmodel.room.UserRoomViewModel
import com.kabindra.clean.architecture.utils.Connectivity
import com.kabindra.clean.architecture.utils.constants.ErrorType.Companion.ERROR_TITLE_VERSION_CHECK
import com.kabindra.clean.architecture.utils.constants.ErrorType.Companion.ERROR_VERSION_CHECK
import com.kabindra.clean.architecture.utils.constants.ResponseType
import com.kabindra.clean.architecture.utils.enums.subscribeToTopics
import com.kabindra.clean.architecture.utils.enums.unsubscribeFromTopics
import com.kabindra.clean.architecture.utils.error.GlobalErrorDialog
import com.kabindra.clean.architecture.utils.getPlatform
import com.kabindra.clean.architecture.utils.getToken
import com.kabindra.clean.architecture.utils.ktor.Result
import com.kabindra.clean.architecture.utils.ktor.ResultUIState
import com.kabindra.clean.architecture.utils.success.GlobalSuccessDialog
import com.kabindra.inappupdate.UpdateAvailableDialog
import com.kabindra.inappupdate.UpdateDownloadDialog
import com.kabindra.inappupdate.checkUpdate
import com.kabindra.inappupdate.completeUpdate
import com.kabindra.inappupdate.exitApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private var firebaseToken = ""

@Composable
fun SplashScreen(
    authenticationRoomViewModel: AuthenticationRoomViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel(),
    userRoomViewModel: UserRoomViewModel = koinViewModel(),
    innerPadding: PaddingValues,
    onNavigateLogin: () -> Unit,
    onNavigateDashboard: () -> Unit
) {

    val connectivity = remember { Connectivity() }
    val isConnected by connectivity.isConnectedState.collectAsState()
    val authenticationLoggedApiState by authenticationRoomViewModel.authenticationLoggedApiState.collectAsState()
    val loginRefreshUserDetailsState by loginViewModel.loginRefreshUserDetailsState.collectAsState()
    val userState by userRoomViewModel.userState.collectAsState()
    var resultsUIState by remember { mutableStateOf(ResultUIState()) }
    var isForcedUpdate by remember { mutableStateOf(false) }
    var isLoggedApi = false
    var userInfo: User? = null
    var userData by remember { mutableStateOf(userInfo) }

    var showUpdateAvailable by remember { mutableStateOf(false) }
    var updateAvailableAction by remember { mutableStateOf(false) }
    var updateAvailableTitle by remember { mutableStateOf("") }
    var updateAvailableMessage by remember { mutableStateOf("") }
    var showUpdateDownload by remember { mutableStateOf(false) }
    var updateDownloadAction by remember { mutableStateOf(false) }
    var updateDownloadMessage by remember { mutableStateOf("") }

    // Use DisposableEffect to reset states when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            // Reset the relevant states
            authenticationRoomViewModel.resetStates()
            loginViewModel.resetStates()

            resultsUIState = ResultUIState()
        }
    }

    println("isConnected: $isConnected")
    if (!isConnected) {
        GlobalErrorDialog(
            isVisible = true,
            statusCode = -1,
            title = "No Network Connection",
            message = "Please check you internet connection.\nPlease try again.",
            onDismiss = {
                resultsUIState = ResultUIState()
            },
        )
        return
    }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = getToken()
                if (token != null) {
                    println("Firebase Token: SplashScreen $token")
                    firebaseToken = token
                } else {
                    println("Firebase Token fetch failed")
                }
            } catch (e: Exception) {
                println("Firebase Error fetching token: ${e.message}")
            }
        }
    }

    LaunchedEffect(Unit) {
        checkUpdate(
            isForcedUpdate = isForcedUpdate,
            onReceiveVersionCode = { code: Int -> },
            onReceiveStalenessDays = { days: Int -> },
            onUpdateAvailable = {
                showUpdateAvailable = true
                updateAvailableAction = true
                updateAvailableTitle = ERROR_TITLE_VERSION_CHECK
                updateAvailableMessage = ERROR_VERSION_CHECK

                isForcedUpdate = it
            },
            onUpdateNotAvailable = {
                // Proceed app
                authenticationRoomViewModel.getIsLogged()
            },
            onCancelled = {
                if (isForcedUpdate) {
                    // Exit app
                    exitApp()
                } else {
                    // Proceed app
                    authenticationRoomViewModel.getIsLogged()
                }
            },
            onFailed = {
                if (isForcedUpdate) {
                    // Exit app
                    exitApp()
                } else {
                    // Proceed app
                    authenticationRoomViewModel.getIsLogged()
                }
            },
            onDownloadProgress = { bytesDownloaded: Long, totalBytes: Long -> },
            onDownloadStart = {
                showUpdateDownload = true
                updateDownloadAction = false
                updateDownloadMessage = "Please wait! Downloading update ....."
            },
            onDownloadComplete = {
                showUpdateDownload = true
                updateDownloadAction = true
                updateDownloadMessage = "An update has just been downloaded."
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppIcon(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .align(Alignment.Center)
        )

        if (resultsUIState.isLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .align(Alignment.Center)
                    .offset(y = 150.dp)
            )
        }

        TextComponent(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = (-75).dp),
            text = "Version: ${getPlatform().appVersion}",
            textAlign = TextAlign.Center
        )

        /*AppBrandIcon(
            modifier = Modifier.wrapContentWidth().wrapContentHeight().align(Alignment.BottomCenter)
        )*/
    }

    LaunchedEffect(authenticationLoggedApiState) {
        when (authenticationLoggedApiState) {
            is Result.Initial -> Unit

            is Result.Loading -> {
                resultsUIState = ResultUIState().copy(isLoading = true)
            }

            is Result.Success -> {
                resultsUIState = ResultUIState().copy(isSuccess = false)

                isLoggedApi =
                    (authenticationLoggedApiState as Result.Success<Boolean>).data

                if (isLoggedApi) {
                    user(userRoomViewModel)
                } else {
                    onNavigateLogin()
                }
            }

            is Result.Error -> {
                resultsUIState = ResultUIState().copy(
                    isError = true,
                    errorType = ResponseType.None,
                    errorStatusCode = (authenticationLoggedApiState as Result.Error).error.statusCode,
                    errorTitle = "",
                    errorMessage = (authenticationLoggedApiState as Result.Error).error.message
                )
            }
        }
    }

    LaunchedEffect(userState) {
        when (userState) {
            is Result.Initial -> Unit

            is Result.Loading -> {
                resultsUIState = ResultUIState().copy(isLoading = false)
            }

            is Result.Success -> {
                resultsUIState = ResultUIState().copy(isLoading = true)

                val user: User =
                    (userState as Result.Success<User>).data

                userInfo = user
                userData = userInfo
                userData?.firebase_topics?.takeIf { it.isNotEmpty() }?.let { topics ->
                    unsubscribeFromTopics(topics)
                }
                loginRefreshUserDetails(
                    loginViewModel,
                    firebaseToken
                )
            }

            is Result.Error -> {
                resultsUIState = ResultUIState().copy(
                    isError = true,
                    errorType = ResponseType.None,
                    errorStatusCode = (userState as Result.Error).error.statusCode,
                    errorTitle = "",
                    errorMessage = (userState as Result.Error).error.message
                )
            }
        }
    }

    LaunchedEffect(loginRefreshUserDetailsState) {
        when (loginRefreshUserDetailsState) {
            is Result.Initial -> Unit

            is Result.Loading -> {
                resultsUIState = ResultUIState().copy(isLoading = true)
            }

            is Result.Success -> {
                resultsUIState = ResultUIState().copy(isSuccess = false)

                val refreshUserDetails: LoginRefreshUserDetails =
                    (loginRefreshUserDetailsState as Result.Success<LoginRefreshUserDetails>).data

                refreshUserDetails.response?.user_details?.firebase_topics.takeIf { !it.isNullOrEmpty() }
                    ?.let { topics ->
                        subscribeToTopics(topics)
                    }

                val features = refreshUserDetails.response?.features
                val uses = refreshUserDetails.response?.featuresUsed

                if (features == null || uses == null) {
                    onNavigateDashboard()
                    return@LaunchedEffect
                }

                // Go To Home Screen
            }

            is Result.Error -> {
                resultsUIState = ResultUIState().copy(
                    isError = true,
                    errorType = ResponseType.None,
                    errorStatusCode = (loginRefreshUserDetailsState as Result.Error).error.statusCode,
                    errorTitle = "",
                    errorMessage = (loginRefreshUserDetailsState as Result.Error).error.message
                )
            }
        }
    }

    if (resultsUIState.isSuccess) {
        GlobalSuccessDialog(
            isVisible = true,
            isAction = true,
            message = resultsUIState.successMessage,
            onDismiss = { })
    }

    if (resultsUIState.isError) {
        GlobalErrorDialog(
            isVisible = true,
            isAction = true,
            statusCode = resultsUIState.errorStatusCode,
            title = resultsUIState.errorTitle,
            message = resultsUIState.errorMessage,
            onDismiss = {
                resultsUIState = ResultUIState()

                authenticationRoomViewModel.getIsLogged()
            },
            onNavigateLogin = { onNavigateLogin() })
    }

    if (showUpdateAvailable) {
        UpdateAvailableDialog(
            isVisible = showUpdateAvailable,
            isAction = updateAvailableAction,
            isForceUpdate = isForcedUpdate,
            title = updateAvailableTitle,
            message = updateAvailableMessage,
            onDismiss = {
                showUpdateAvailable = false
                updateAvailableAction = false
                updateDownloadMessage = ""
            },
            onUpdate = {
                showUpdateAvailable = false
                updateAvailableAction = false
                updateDownloadMessage = ""

                completeUpdate("", "")
            },
            onLater = {
                showUpdateAvailable = false
                updateAvailableAction = false
                updateDownloadMessage = ""

                isForcedUpdate = false

                authenticationRoomViewModel.getIsLogged()
            }
        )
    }

    if (showUpdateDownload) {
        UpdateDownloadDialog(
            isVisible = showUpdateDownload,
            isAction = updateDownloadAction,
            message = updateDownloadMessage,
            onDismiss = {
                showUpdateDownload = false
                updateDownloadAction = false
                updateDownloadMessage = ""
            },
            onInstall = {
                showUpdateDownload = false
                updateDownloadAction = false
                updateDownloadMessage = ""

                completeUpdate("", "")
            }
        )
    }
}

private fun loginRefreshUserDetails(
    loginViewModel: LoginViewModel,
    fcmToken: String
) {
    loginViewModel.getLoginRefreshUserDetails(LoginRefreshUserDetailsDataRequest(fcmToken))
}

private fun user(
    userRoomViewModel: UserRoomViewModel,
) {
    userRoomViewModel.getUser()
}