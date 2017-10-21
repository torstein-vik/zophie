package io.zophie

import java.net._

import connection._
import api.event._
import api._

// Main object, performs all backend interfacing with the IO part of the architecture and is extended by Zophie object
class Main[S, T <: Connection[S]] (connectionFactory : ConnectionFactory[S, T])(implicit converter : EventConverter[S]) {

    val connection : T = connectionFactory.setupConnection(backend)

    private object backend extends API with ConnectionCallback[S] {

        def callback (data : S) {

        }

    }

    private object io extends API {

        override def triggerEvent (event : Event)(implicit data : event.eventData) = {
            super.triggerEvent(event)(data)
        }

        override def triggerEventSync (event : Event)(implicit data : event.eventData) = {
            super.triggerEventSync(event)(data)
        }

    }

    def trigger(event : Event)(implicit data : event.eventData) = io.trigger(event)(data)
    def on(event : Event)(handler : EventHandler[event.eventData]) = backend.on(event)(handler)

}
