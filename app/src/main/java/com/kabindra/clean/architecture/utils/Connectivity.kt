package com.kabindra.clean.architecture.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface Connectivity {
    val isConnected: Boolean
    val currentNetworkConnection: NetworkConnection
    val isConnectedState: StateFlow<Boolean>
    val currentNetworkConnectionState: StateFlow<NetworkConnection>
}

fun Connectivity(): Connectivity {
    val connectivityManager: ConnectivityManager =
        appContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val connectivity = ConnectivityImpl(
        initialConnection = getCurrentNetworkConnection(connectivityManager)
    )
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val connection = getNetworkConnection(connectivityManager, network)
            connectivity.onNetworkConnectionChanged(connection)
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
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    return connectivity
}

private fun getCurrentNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection =
    postAndroidMNetworkConnection(connectivityManager)

private fun postAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection {
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return getNetworkConnection(capabilities)
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

        !(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) -> NetworkConnection.NONE

        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI

        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnection.CELLULAR

        else -> NetworkConnection.NONE
    }

internal class ConnectivityImpl(
    initialConnection: NetworkConnection = NetworkConnection.NONE,
    ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Connectivity {

    private val scope = CoroutineScope(ioDispatcher)

    private val state = MutableStateFlow(initialConnection)

    override val isConnected: Boolean
        get() = state.value != NetworkConnection.NONE

    override val currentNetworkConnection: NetworkConnection
        get() = state.value

    override val isConnectedState: StateFlow<Boolean> =
        state.asStateFlow()
            .map(scope) { it != NetworkConnection.NONE }

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