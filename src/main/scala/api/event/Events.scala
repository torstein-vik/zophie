package io.zophie.api.event

trait Event {
    type eventData <: EventData
}

trait EventData {

}

class NoEventData () extends EventData
