package com.kabindra.clean.architecture.presentation.ui.screen.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.kabindra.clean.architecture.presentation.ui.screen.splash.DashboardScreen
import com.kabindra.clean.architecture.presentation.ui.screen.splash.LoginScreen
import com.kabindra.clean.architecture.presentation.ui.screen.splash.LoginVerifyOTPScreen
import com.kabindra.clean.architecture.presentation.ui.screen.splash.RegisterScreen
import com.kabindra.clean.architecture.presentation.ui.screen.splash.SplashScreen
import org.koin.compose.koinInject

@Composable
fun MainScreen() {
    val snackBarHostState: SnackbarHostState = koinInject()

    val backStack = rememberNavBackStack(SplashRoute)

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }) { innerPadding ->

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<SplashRoute> {
                    SplashScreen(
                        innerPadding = innerPadding,
                        onNavigateLogin = {
                            backStack.add(LoginRoute)
                        },
                        onNavigateDashboard = {
                            backStack.add(DashboardRoute)
                        }
                    )
                }
                entry<RegisterRoute> {
                    RegisterScreen(
                        innerPadding = innerPadding,
                        onNavigateLogin = {
                            backStack.add(LoginRoute)
                        }
                    )
                }
                entry<LoginRoute> {
                    LoginScreen(
                        innerPadding = innerPadding,
                        onNavigateLogin = {
                            backStack.add(LoginRoute)
                        }
                    )
                }
                entry<LoginVerifyOTPRoute> {
                    LoginVerifyOTPScreen(
                        innerPadding = innerPadding,
                        onNavigateLogin = {
                            backStack.add(LoginRoute)
                        }
                    )
                }
                entry<DashboardRoute>(
                    metadata = NavDisplay.transitionSpec {
                        // Slide new content up, keeping the old content in place underneath
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(1000)
                        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                    } + NavDisplay.popTransitionSpec {
                        // Slide old content down, revealing the new content in place underneath
                        EnterTransition.None togetherWith
                                slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(1000)
                                )
                    } + NavDisplay.predictivePopTransitionSpec {
                        // Slide old content down, revealing the new content in place underneath
                        EnterTransition.None togetherWith
                                slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(1000)
                                )
                    }
                ) {
                    DashboardScreen(
                        innerPadding = innerPadding,
                        onNavigateLogin = {
                            backStack.add(LoginRoute)
                        }
                    )
                }
            },
            transitionSpec = {
                // Slide in from right when navigating forward
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(1000)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(1000)
                )
            },
            popTransitionSpec = {
                // Slide in from left when navigating back
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(1000)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(1000)
                )
            },
            predictivePopTransitionSpec = {
                // Slide in from left when navigating back
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(1000)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(1000)
                )
            }
        )
    }
}