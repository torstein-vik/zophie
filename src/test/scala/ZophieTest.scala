import org.scalatest.FunSuite

import io.zophie.api.event._

class ZophieTest extends FunSuite {
    test ("eventbus") {

        // Defining two dummy events and event data classes
        class EventData1 (str : String) extends EventData

        case object Event1 extends Event {type eventData = EventData1}
        case object Event2 extends Event {type eventData = NoEventData}

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()



    }
}
