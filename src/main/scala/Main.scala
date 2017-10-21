package io.zophie

import java.net._

import connection._
import api.event._
import api._

// Main object, performs all backend interfacing with the IO part of the architecture and is extended by Zophie object
class Main[S, T] (connection : ConnectionFactory[S, T]) {



    private object backend extends API {

    }

    private object io extends API {

    }

}
