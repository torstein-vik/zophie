package io.zophie.api.event

trait EventBus {
    def addEventListener(event : Event) : ((event.eventData => Unit) => Unit)
    def hasEventListener(event : Event) : Boolean
    def triggerEvent(event : Event) : ((event.eventData => Unit) => Unit)
}
