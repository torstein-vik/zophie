package io.zophie.api.event

trait Event {
    type eventData <: EventData
}

trait EventHandler[T <: EventData] {
    def handle (data : T) : Unit
}

trait EventData

case object NoEventData extends EventData
