package io.zophie.api.event

// An event is something that an EventBus can invoke. Should be defined using case object, to be used as an Enum. Includes the type of the data which is passed alongside it
trait Event {
    type eventData <: EventData
}

// An EventHandler takes the eventData and performs some task. Takes the type of the EventData as a type parameter
trait EventHandler[T <: EventData] {
    def handle (data : T) : Unit
}

// Datacontainer which may be passed through an EventBus
trait EventData


package object ImplicitNoEventData {

    // Specific case of no event data
    implicit case object NoEventData extends EventData
}
