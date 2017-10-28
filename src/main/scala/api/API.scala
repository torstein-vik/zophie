package io.zophie.api

import event._


// Class for Main to use, some shorthands for nicer code
class API extends DefaultEventBus {

    // Shorthands for addEventHandler and triggerEvent
    def trigger[T <: EventData](event : Event[T])(implicit data : T) = triggerEvent(event)(data)
    def on[T <: EventData](event : Event[T])(handler : EventHandler[T]) = addEventHandler(event)(handler)

}
