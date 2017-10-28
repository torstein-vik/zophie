package io.zophie.api.event

// An event is something that an EventBus can invoke. Should be defined using case object, to be used as an Enum. 
// Includes the type of the data which is passed alongside it, and a name
abstract class Event[T <: EventData](val name : String) {
    override def toString = name
}

// Extend this to have an event with NoEventData
abstract class EventNoData(name : String) extends Event[NoEventData.type](name)

// An EventHandler takes the eventData and performs some task. Takes the type of the EventData as a type parameter
trait EventHandler[T <: EventData] {
    def handle (data : T) : Unit
}

// Datacontainer which may be passed through an EventBus
trait EventData

// Container for both the event and the data
class EventDataComposite[T <: EventData] (val event : Event[T])(implicit val data : T) {
    def ==(that : EventDataComposite[T]) = (event == that.event) && (data == that.data)
    
    override def toString = event + " with data: " + data
}

// An object which convert an EventDataComposite into some data S, to be used for interfacing with IO
trait EventConverter[S] {
    // 'Stringify' into S
    def toData[T <: EventData] (event : EventDataComposite[T]) : S

    // Parse from S
    def fromData (data : S) : EventDataComposite[EventData]
}

// Specific case of no event data
case object NoEventData extends EventData

package object Implicits {

    // Implicit NoEventData so we don't need to specify it all the time
    implicit val NoEventDataImplicit = NoEventData
}
