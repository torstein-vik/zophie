package io.zophie.api.event

sealed trait EventBus {
    def addEventListener(event : Event) : (EventHandler[event.eventData] => Unit)
    def hasEventListener(event : Event) : Boolean
    def triggerEvent(event : Event) : (event.eventData => Unit)
}

trait EventHandler[T <: EventData] {
    def handle (data : T) : Unit
}

class DefaultEventBus extends EventBus {

    override def addEventListener(event : Event) : (EventHandler[event.eventData] => Unit) = {
        return (handler : EventHandler[event.eventData]) => {

        }
    }

    override def hasEventListener(event : Event) : Boolean = {
        return false
    }

    override def triggerEvent(event : Event) : (event.eventData => Unit) = {
        return (data : event.eventData) => {

        }
    }

}
