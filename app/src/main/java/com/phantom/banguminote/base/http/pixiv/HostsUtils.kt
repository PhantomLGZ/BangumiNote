package com.phantom.banguminote.base.http.pixiv

import java.net.InetAddress
import java.net.UnknownHostException


fun String.needBlock(): Boolean {
    return this.contains("www.google-analytics.com")
            || this.contains("www.googletagmanager.com")
            || this.contains("a.pixiv.org")
            || this.contains("analytics.twitter.com")
            || this.contains("c.amazon-adsystem.com")
            || this.contains("static.ads-twitter.com")
            || this.contains("pixon.ads-pixiv.net")
            || this.contains(".doubleclick.net")
            || this.contains("connect.facebook.net")
            || this.contains(".rubiconproject.com")
}

fun toInetAddress(host: String?, ip: String?): InetAddress? {
    if (!isValidHost(host)) {
        return null
    }
    if (ip == null) {
        return null
    }
    var bytes = parseV4(ip)
    if (bytes == null) {
        bytes = parseV6(ip)
    }
    return if (bytes == null) {
        null
    } else try {
        InetAddress.getByAddress(host, bytes)
    } catch (e: UnknownHostException) {
        null
    }
}

/**
 * Returns true if the host is valid.
 */
private fun isValidHost(host: String?): Boolean {
    if (host == null) {
        return false
    }
    if (host.length > 253) {
        return false
    }
    var labelLength = 0
    var i = 0
    val n = host.length
    while (i < n) {
        val ch = host[i]
        if (ch == '.') {
            if (labelLength < 1 || labelLength > 63) {
                return false
            }
            labelLength = 0
        } else {
            labelLength++
        }
        if ((ch < 'a' || ch > 'z') && (ch < '0' || ch > '9') && ch != '-' && ch != '.') {
            return false
        }
        i++
    }
    return !(labelLength < 1 || labelLength > 63)
}

/**
 * Returns true if the ip is valid.
 */
fun isValidIp(ip: String?): Boolean {
    return ip != null && (parseV4(ip) != null || parseV6(ip) != null)
}

// org.xbill.DNS.Address.parseV4
private fun parseV4(s: String): ByteArray? {
    var numDigits = 0
    var currentOctet = 0
    var currentValue = 0
    val values = ByteArray(4)
    val length = s.length
    for (i in 0 until length) {
        val c = s[i]
        if (c in '0'..'9') {
            /* Can't have more than 3 digits per octet. */
            if (numDigits == 3) return null
            /* Octets shouldn't start with 0, unless they are 0. */
            if (numDigits > 0 && currentValue == 0) return null
            numDigits++
            currentValue *= 10
            currentValue += c.code - '0'.code
            /* 255 is the maximum value for an octet. */
            if (currentValue > 255) return null
        } else if (c == '.') {
            /* Can't have more than 3 dots. */
            if (currentOctet == 3) return null
            /* Two consecutive dots are bad. */
            if (numDigits == 0) return null
            values[currentOctet++] = currentValue.toByte()
            currentValue = 0
            numDigits = 0
        } else return null
    }
    /* Must have 4 octets. */
    if (currentOctet != 3) return null
    /* The fourth octet can't be empty. */
    if (numDigits == 0) return null
    values[currentOctet] = currentValue.toByte()
    return values
}

// org.xbill.DNS.Address.parseV6
private fun parseV6(s: String): ByteArray? {
    var range = -1
    val data = ByteArray(16)
    val tokens = s.split(":".toRegex()).toTypedArray()
    var first = 0
    var last = tokens.size - 1
    if (tokens[0].isEmpty()) {
        // If the first two tokens are empty, it means the string
        // started with ::, which is fine.  If only the first is
        // empty, the string started with :, which is bad.
        if (last - first > 0 && tokens[1].isEmpty())
            first++
        else return null
    }
    if (tokens[last].isEmpty()) {
        // If the last two tokens are empty, it means the string
        // ended with ::, which is fine.  If only the last is
        // empty, the string ended with :, which is bad.
        if (last - first > 0 && tokens[last - 1].isEmpty()) last-- else return null
    }
    if (last - first + 1 > 8) return null
    var i = first
    var j = 0
    while (i <= last) {
        if (tokens[i].isEmpty()) {
            if (range >= 0) return null
            range = j
            i++
            continue
        }
        if (tokens[i].indexOf('.') >= 0) {
            // An IPv4 address must be the last component
            if (i < last) return null
            // There can't have been more than 6 components.
            if (i > 6) return null
            val v4addr = parseV4(tokens[i]) ?: return null
            for (k in 0..3) data[j++] = v4addr[k]
            break
        }
        try {
            for (k in 0 until tokens[i].length) {
                val c = tokens[i][k]
                if ((c.digitToIntOrNull(16) ?: -1) < 0) return null
            }
            val x = tokens[i].toInt(16)
            if (x > 0xFFFF || x < 0) return null
            data[j++] = (x ushr 8).toByte()
            data[j++] = (x and 0xFF).toByte()
        } catch (e: NumberFormatException) {
            return null
        }
        i++
    }
    if (j < 16 && range < 0) return null
    if (range >= 0) {
        val empty = 16 - j
        System.arraycopy(data, range, data, range + empty, j - range)
        i = range
        while (i < range + empty) {
            data[i] = 0
            i++
        }
    }
    return data
}
