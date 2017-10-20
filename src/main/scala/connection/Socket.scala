package io.zophie.connection

class Socket (implicit callback : ConnectionCallback[String]) extends Connection[String](callback) {



    override def push (data : String) = {

    }
}

