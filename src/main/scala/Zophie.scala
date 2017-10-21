package io.zophie

import java.net._

import connection._
import connection.JSONConverter._
import DefaultNetworkDetails._

// Primary object, where it all begins (extends App)
// Also extends Main for API-access, with a string-based SocketConnection using setup from DefaultNetworkDetails
object Zophie extends Main[String, SocketConnection](SocketConnection.setup) with App {

}
