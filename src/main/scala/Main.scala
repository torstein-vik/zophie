package io.zophie

import java.net._

import connection._
import api.event._
import api._

// Main object, performs all backend interfacing with the IO part of the architecture and is extended by Zophie object
class Main[S, T <: Connection[S]] (connectionFactory : ConnectionFactory[S, T])(implicit converter : EventConverter[S]) {

    // Connection with IO, unspecified exactly what. Use backend object as ConnectionCallback
    val connection : T = connectionFactory.setupConnection(backend)

    // API for the scala backend (this codebase), this is what 'on' uses to addEventListener
    // Events dispatched (primarily) by ConnectionCallback
    private object backend extends API with ConnectionCallback[S] {

        // Callback from whatever Connection is
        def callback (data : S) {

        }

    }

    // API for the IO codebase, this is what 'trigger' starts up
    // In addition to working like a normal IO, dispatches events to
    private object io extends API {

        // Asynchronously trigger event both here and in the IO
        override def triggerEvent (event : Event)(implicit data : event.eventData) = {

            // Basic API functionality preserved
            super.triggerEvent(event)(data)
        }

        // Synchronously trigger event both here and in the IO
        override def triggerEventSync (event : Event)(implicit data : event.eventData) = {

            // Basic API functionality preserved
            super.triggerEventSync(event)(data)
        }

    }

    // Trigger event on IO-side
    def trigger(event : Event)(implicit data : event.eventData) = io.trigger(event)(data)

    // Listen for events sent here from IO-side
    def on(event : Event)(handler : EventHandler[event.eventData]) = backend.on(event)(handler)

}
