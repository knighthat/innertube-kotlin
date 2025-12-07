package me.knighthat.internal.util

import io.ktor.util.sha1


internal fun getSapisidHash( cookies: String, host: String ): String {
    val cookieMap: Map<String, String> =
        cookies.split( ";" )
               .map( String::trim )
               .mapNotNull {
                   val parts = it.split("=", limit = 2)
                   if (parts.size == 2) parts[0] to parts[1] else null
               }
               .toMap()
    val currentTime = System.currentTimeMillis() / 1000
    val sapisidHash: String
    "%d %s %s".format( currentTime, cookieMap["SAPISID"], host )
              .toByteArray()
              .let( ::sha1 )
              .joinToString("") { "%02x".format(it) }
              .also { sapisidHash = it }

    return "${currentTime}_${sapisidHash}_u"
}