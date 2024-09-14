package com.kabindra.cleanarchitecture.utils

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.kabindra.cleanarchitecture.utils.NetworkConnection.NONE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/* Connectivity Implementation
val connectivity: Connectivity = Connectivity()

val isConnected: Boolean = connectivity.isConnected

val networkConnection: NetworkConnection = connectivity.currentNetworkConnection
when (networkConnection) {
    NetworkConnection.NONE -> "Not connected to the internet"
    NetworkConnection.WIFI -> "Connected to wifi"
    NetworkConnection.CELLULAR -> "Connected to cellular"
}

GlobalScope.launch {
    connectivity.isConnectedState.collect { isConnected ->
        // insert code
    }
}

GlobalScope.launch {
    connectivity.currentNetworkConnectionState.collect { connection ->
        when (connection) {
            NetworkConnection.NONE -> "Not connected to the internet"
            NetworkConnection.WIFI -> "Connected to wifi"
            NetworkConnection.CELLULAR -> "Connected to cellular"
        }
    }
}*/

interface Connectivity {
    val isConnected: Boolean
    val currentNetworkConnection: NetworkConnection
    val isConnectedState: StateFlow<Boolean>
    val currentNetworkConnectionState: StateFlow<NetworkConnection>
}

fun Connectivity(): Connectivity {
    val appContext = appContext!!
    val connectivityManager: ConnectivityManager =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val connectivity = ConnectivityImpl(
        initialConnection = getCurrentNetworkConnection(connectivityManager)
    )
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                connectivity.onNetworkConnectionChanged(
                    getNetworkConnection(connectivityManager, network)
                )
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val connection = getNetworkConnection(networkCapabilities)
            connectivity.onNetworkConnectionChanged(connection)
        }

        override fun onLost(network: Network) {
            connectivity.onNetworkConnectionChanged(NetworkConnection.NONE)
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    } else {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    return connectivity
}

private fun getCurrentNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        postAndroidMNetworkConnection(connectivityManager)
    } else {
        preAndroidMNetworkConnection(connectivityManager)
    }

@TargetApi(Build.VERSION_CODES.M)
private fun postAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection {
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return getNetworkConnection(capabilities)
}

@Suppress("DEPRECATION")
private fun preAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection =
    when (connectivityManager.activeNetworkInfo?.type) {
        null -> NetworkConnection.NONE
        ConnectivityManager.TYPE_WIFI -> NetworkConnection.WIFI
        else -> NetworkConnection.CELLULAR
    }

private fun getNetworkConnection(
    connectivityManager: ConnectivityManager,
    network: Network
): NetworkConnection {
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return getNetworkConnection(capabilities)
}

private fun getNetworkConnection(capabilities: NetworkCapabilities?): NetworkConnection =
    when {
        capabilities == null -> NetworkConnection.NONE
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                && !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> NetworkConnection.NONE

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) -> NetworkConnection.NONE

        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnection.CELLULAR
        else -> NetworkConnection.NONE
    }

internal class ConnectivityImpl(
    initialConnection: NetworkConnection = NONE,
    ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Connectivity {

    private val scope = CoroutineScope(ioDispatcher)

    private val state = MutableStateFlow<NetworkConnection>(initialConnection)

    override val isConnected: Boolean
        get() = state.value != NONE

    override val currentNetworkConnection: NetworkConnection
        get() = state.value

    override val isConnectedState: StateFlow<Boolean> =
        state.asStateFlow()
            .map(scope) { it != NONE }

    override val currentNetworkConnectionState: StateFlow<NetworkConnection> = state.asStateFlow()

    fun onNetworkConnectionChanged(connection: NetworkConnection) {
        state.value = connection
    }

    private fun <T, M> StateFlow<T>.map(
        coroutineScope: CoroutineScope,
        mapper: (value: T) -> M
    ): StateFlow<M> = map { mapper(it) }.stateIn(
        coroutineScope,
        SharingStarted.Eagerly,
        mapper(value)
    )
}