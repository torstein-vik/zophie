package io.zophie.api.event

sealed trait EventBus {
    def addEventListener(event : Event) : ((event.eventData => Unit) => Unit)
    def hasEventListener(event : Event) : Boolean
    def triggerEvent(event : Event) : (event.eventData => Unit)
}

class DefaultEventBus extends EventBus {

    override def addEventListener(event : Event) : ((event.eventData => Unit) => Unit) = {
        return (f : (event.eventData => Unit)) => {

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
