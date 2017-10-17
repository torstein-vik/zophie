package io.zophie.api.event

trait Event {
    type eventData <: EventData
}

trait EventHandler[T <: EventData] {
    def handle (data : T) : Unit
}

trait EventData {

}

class NoEventData () extends EventData
