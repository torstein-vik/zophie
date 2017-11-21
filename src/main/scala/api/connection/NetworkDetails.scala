package io.zophie.api.connection

import java.net._

// Datacontainer for IP and port
class NetworkDetails(val ip : InetAddress, val port : Int)

// Default Network w/ implicit chosen value
package object DefaultNetworkDetails {

    // localhost:29990 seems as good as any choice. 29990 does not seem to be reserved by anyone else
    implicit val defaultNetworkDetails = new NetworkDetails(InetAddress.getByName("localhost"), 29990)
}
