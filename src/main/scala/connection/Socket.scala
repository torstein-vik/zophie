package io.zophie.connection

import java.net._
import java.io._
import scala.io._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class SocketConnection (socket : Socket)(callback : ConnectionCallback[String]) extends Connection[String](callback) {

    lazy val input = new BufferedSource( socket.getInputStream ).getLines
    val output = new PrintStream( socket.getOutputStream )

    var running = true

    def close () = {
        running = false
        socket.close()
    }

    Future {
        while(running) {
            callback.callback(input.next)
        }
    }

    override def push (data : String) = {
        output.println(data)
    }
}

object SocketConnection {
    def setup (ip : InetAddress, port : Int) : ConnectionFactory[String, SocketConnection] = {
        return (callback => new SocketConnection(new Socket(ip, port))(callback)) : ConnectionFactory[String, SocketConnection]
    }
}
