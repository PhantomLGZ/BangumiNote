package com.phantom.banguminote.base.http.pixiv

import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory


class PixivSSLSocketFactory : SSLSocketFactory() {
    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        val address = s?.inetAddress
        return (getDefault().createSocket(address, port) as SSLSocket)
    }

    override fun createSocket(host: String?, port: Int): Socket {
        return getDefault().createSocket(host, port)
    }

    override fun createSocket(
        host: String?,
        port: Int,
        localHost: InetAddress?,
        localPort: Int
    ): Socket {
        return getDefault().createSocket(host, port, localHost, localPort)
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        return getDefault().createSocket(host, port)
    }

    override fun createSocket(
        address: InetAddress?,
        port: Int,
        localAddress: InetAddress?,
        localPort: Int
    ): Socket {
        return getDefault().createSocket(address, port, localAddress, localPort)
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return (getDefault() as SSLSocketFactory).defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return (getDefault() as SSLSocketFactory).supportedCipherSuites
    }
}