package io.zophie.api

import event._


// Class for Main to use, some shorthands for nicer code
class API extends DefaultEventBus {

    // Shorthands for addEventHandler and triggerEvent
    def trigger(event : Event)(implicit data : event.eventData) = triggerEvent(event)(data)
    def on(event : Event)(handler : EventHandler[event.eventData]) = addEventHandler(event)(handler)

}
