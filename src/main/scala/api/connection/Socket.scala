package io.zophie.api.connection

import java.net._
import java.io._
import scala.io._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import io.zophie.NetworkDetails

// An implementation of Connection (w. String as datatype) using java sockets, as a client (not server)
// Use factory to create, unless you want to pass your own socket
class SocketConnection (socket : Socket)(callback : ConnectionCallback[String]) extends Connection[String](callback) {

    // Input from socket, by line
    lazy val input = new BufferedSource( socket.getInputStream ).getLines

    // Output from socket, by line
    val output = new PrintStream( socket.getOutputStream )

    // Whether or not to continue loop
    var running = true

    // Close loop
    def close () = {
        running = false
        socket.close()
    }

    // Start running the callback loop on a separate thread
    Future {
        while(running) {

            // Callback with the next input
            callback.callback(input.next)
        }
    }

    // Send data to server
    override def push (data : String) = {
        output.println(data)
    }
}

// Factory for ConnectionFactory creating a SocketConnection
// Setup with ip and port, or with (implicit) configuration given in e.g. DefaultNetworkDetails
object SocketConnection {
    def setup (ip : InetAddress, port : Int) : ConnectionFactory[String, SocketConnection] = {
        return (callback => new SocketConnection(new Socket(ip, port))(callback)) : ConnectionFactory[String, SocketConnection]
    }

    // To use Networkdetails datacontainer instead of ip and port separately
    def setup (implicit config : NetworkDetails) : ConnectionFactory[String, SocketConnection] = setup(config.ip, config.port)
}
