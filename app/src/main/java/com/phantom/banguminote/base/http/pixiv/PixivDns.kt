package com.phantom.banguminote.base.http.pixiv

import android.util.Log
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.net.InetAddress
import java.net.UnknownHostException

object PixivDns : Dns {

    private val builtInHosts: Map<String, List<InetAddress>>
    private var dnsOverHttps: DnsOverHttps? = null

    init {
        val map = hashMapOf<String, List<InetAddress>>()
        put(map, "accounts.pixiv.net", "104.18.42.239", "172.64.145.17")
        put(map, "pixiv.net", "210.140.92.187", "210.140.92.181", "210.140.92.183")
        put(
            map,
            "i.pximg.net",
            "210.140.139.133",
            "210.140.139.134",
            "210.140.139.129",
            "210.140.139.135",
            "210.140.139.132",
            "210.140.139.136",
            "210.140.139.130",
            "210.140.139.131"
        )
        builtInHosts = map

        val builder = DnsOverHttps.Builder()
            .client(OkHttpClient.Builder().build())
            .url("https://1.0.0.1/dns-query".toHttpUrl())
        dnsOverHttps = builder.post(true).build()
    }

    private fun put(
        map: MutableMap<String, List<InetAddress>>,
        host: String,
        vararg ips: String
    ) {
        val addresses = arrayListOf<InetAddress>()
        for (ip in ips) {
            toInetAddress(host, ip)?.let { addresses.add(it) }
        }
        map[host] = addresses
    }

    @Throws(UnknownHostException::class)
    override fun lookup(hostname: String): List<InetAddress> {
        var host = hostname
        if (host.contains(".pixiv.net")) {
            host = "pixiv.net"
        }
        val inetAddresses = builtInHosts[host]
        if (inetAddresses != null) {
            Log.i("PixivDns", "Dns find $inetAddresses")
            return inetAddresses
        }
        try {
            return InetAddress.getAllByName(host).toList()
        } catch (e: Exception) {
            val unknownHostException = UnknownHostException(
                "Broken system behaviour for dns lookup of $host"
            )
            unknownHostException.initCause(e)
            throw unknownHostException
        }
    }
}