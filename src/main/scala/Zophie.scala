package io.zophie

import connection._
import java.net._
import connection.JSONConverter._
import DefaultNetworkDetails._

object Zophie extends Main[String, SocketConnection](SocketConnection.setup) with App {

}
