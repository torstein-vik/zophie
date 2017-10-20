package io.zophie.api.event

// Used for DefaultEventBus eventhandler registry
import scala.collection.mutable.Map

// Used for async event trigger
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

// EventBus trait, a type of class which keeps track of EventHandlers on Events, and enacts those EventHandlers when an Event is triggered
// Also has a strong type system, which connects EventData to each Event
trait EventBus {

    // Add a new EventHandler
    def addEventHandler(event : Event)(handler : EventHandler[event.eventData]) : Unit

    // Test if an event has any handlers
    def hasEventHandler(event : Event) : Boolean

    // Trigger an event asynchronously (ie. invoke EventHandlers on seperate threads), and return a future object resolved when all EventHandlers are finished
    def triggerEvent(event : Event)(implicit data : event.eventData) : Future[Unit]

    // Trigger an event synchronously (ie. invoke all EventHandlers ,in the order which they were added, on this thread)
    def triggerEventSync(event : Event)(implicit data : event.eventData) : Unit

}

// Default implementation of EventBus trait
class DefaultEventBus extends EventBus {

    // Map of all the EventHandlers. Note that this is private, which means we can use the wildcard in EventHandler[_] while still preserving type safety
    // (as handlers is only affected by methods in this class, all of which are type safe)
    private var handlers : Map[Event, Seq[EventHandler[_]]] = Map()

    override def addEventHandler(event : Event)(handler : EventHandler[event.eventData]) = {
        // Append to handlers[event] if it exists, Seq(handler) otherwise
        handlers += event -> (handlers.getOrElse(event, Seq()) ++ Seq(handler))
    }

    override def hasEventHandler(event : Event) : Boolean = {
        // At least one EventHandler is registered on event
        return handlers.getOrElse(event, Seq()).length > 0
    }

    override def triggerEvent(event : Event)(implicit data : event.eventData) : Future[Unit] = {
        // Future.sequence takes a list of Futures and turns it into a Future of lists
        return Future.sequence(

            // For each handler on the event, invoke the handler in a Future, to run it on another thread
            handlers.getOrElse(event, Seq()).map(handler =>
                Future [Unit] {
                    // Note that becuase of the wildcard in handlers, we have to use _.asInstanceOf
                    handler.asInstanceOf[EventHandler[event.eventData]].handle(data)
                }
            )

        // Convert from Future[Seq[Unit]] to Future[Unit]
        ).map( _ => Unit )
    }

    override def triggerEventSync(event : Event)(implicit data : event.eventData) = {
        // For each element in handlers[event], run _.handle(data)
        // Note that becuase of the wildcard in handlers, we have to use _.asInstanceOf
        handlers.getOrElse(event, Seq()).foreach(_.asInstanceOf[EventHandler[event.eventData]].handle(data))
    }

}
