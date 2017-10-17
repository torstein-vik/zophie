import org.scalatest.FunSuite

import io.zophie.api.event._

class APITest extends FunSuite {

    // Defining two dummy events and event data classes
    class EventData1 (val str : String) extends EventData

    case object Event1 extends Event {type eventData = EventData1}
    case object Event2 extends Event {type eventData = NoEventData}


    test ("eventbus does not have event listeners") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Make sure it does not have any event listeners
        assert(!eventbus.hasEventListener(Event1))
        assert(!eventbus.hasEventListener(Event2))

    }

    test ("eventbus.hasEventListener and addEventListener works") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Add event listener for Event1
        eventbus.addEventListener(Event1)(new EventHandler [EventData1] { def handle ( data : EventData1 ) { } });

        // Make sure it has Event1, but not Event2
        assert(eventbus.hasEventListener(Event1))
        assert(!eventbus.hasEventListener(Event2))

        // Add event listener for Event2
        eventbus.addEventListener(Event2)(new EventHandler [NoEventData] { def handle ( data : NoEventData ) { } });

        // Make sure it has all eventListeners
        assert(eventbus.hasEventListener(Event1))
        assert(eventbus.hasEventListener(Event2))
    }
}
