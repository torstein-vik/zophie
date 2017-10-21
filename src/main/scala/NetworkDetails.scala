package io.zophie

import java.net._

class NetworkDetails(val ip : InetAddress, val port : Int)

package object DefaultNetworkDetails {
    implicit val defaultNetworkDetails = new NetworkDetails(InetAddress.getByName("localhost"), 29990)
}
