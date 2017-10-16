import org.scalatest.FunSuite

import io.zophie.api.event._

class APITest extends FunSuite {
    test ("eventbus") {

        // Defining two dummy events and event data classes
        class EventData1 (str : String) extends EventData

        case object Event1 extends Event {type eventData = EventData1}
        case object Event2 extends Event {type eventData = NoEventData}

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Make sure it does not have any event listeners
        assert(!eventbus.hasEventListener(Event1))
        assert(!eventbus.hasEventListener(Event2))

    }
}
