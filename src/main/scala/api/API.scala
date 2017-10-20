package io.zophie.api

import event._

class API extends DefaultEventBus {
    def trigger(event : Event)(data : event.eventData) = triggerEvent(event)(data)
    def on(event : Event)(handler : EventHandler[event.eventData]) = addEventHandler(event)(handler)
}
