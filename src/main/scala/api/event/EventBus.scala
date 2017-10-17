package io.zophie.api.event

import scala.collection.mutable.Map

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait EventBus {
    def addEventListener(event : Event) : (EventHandler[event.eventData] => Unit)
    def hasEventListener(event : Event) : Boolean
    def triggerEvent(event : Event) : (event.eventData => Future[Unit])
    def triggerEventSync(event : Event) : (event.eventData => Unit)
}

trait EventHandler[T <: EventData] {
    def handle (data : T) : Unit
}

class DefaultEventBus extends EventBus {

    private var listeners : Map[Event, Seq[EventHandler[_]]] = Map()

    override def addEventListener(event : Event) : (EventHandler[event.eventData] => Unit) = {
        return (handler : EventHandler[event.eventData]) => {
            listeners += (event -> Seq(handler))
        }
    }

    override def hasEventListener(event : Event) : Boolean = {
        return listeners.getOrElse(event, Seq()).length > 0
    }

    override def triggerEvent(event : Event) : (event.eventData => Future[Unit]) = {
        return (data : event.eventData) => Future.sequence(
            listeners.getOrElse(event, Seq()).map(handler =>
                Future [Unit] {
                    handler.asInstanceOf[EventHandler[event.eventData]].handle(data)
                }
            )
        ).map( _ => Unit )
    }

    override def triggerEventSync(event : Event) : (event.eventData => Unit) = {
        return (data : event.eventData) => {
            listeners.getOrElse(event, Seq()).foreach(_.asInstanceOf[EventHandler[event.eventData]].handle(data))
        }
    }

}
