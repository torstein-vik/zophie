package io.zophie.api.event

trait Event {
    type eventData <: EventData
}

trait EventData {
    def eventid
}
