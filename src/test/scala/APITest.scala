import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global


import io.zophie.api.event._

class APITest extends FunSuite with ScalaFutures {

    // Defining two dummy events and event data classes
    class EventData1 (val str : String) extends EventData
    class EventData2 (val num : Int)    extends EventData

    case object Event1 extends Event {type eventData = EventData1}
    case object Event2 extends Event {type eventData = NoEventData.type}
    case object Event3 extends Event {type eventData = EventData2}


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
        eventbus.addEventListener(Event1)( _ => {});

        // Make sure it has Event1, but not Event2
        assert( eventbus.hasEventListener(Event1))
        assert(!eventbus.hasEventListener(Event2))

        // Add event listener for Event2
        eventbus.addEventListener(Event2)( _ => {});

        // Make sure it has all eventListeners
        assert(eventbus.hasEventListener(Event1))
        assert(eventbus.hasEventListener(Event2))
    }

    test ("eventbus synchronous trigger works") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variable to be changed by event
        var x = "test1"

        // A listener on Event1, which changes x to event.str
        eventbus.addEventListener(Event1)(event => x = event.str)

        // Nothing has changed by adding listener
        assert(x === "test1")

        // Nothing happends when Event2 is triggered
        eventbus.triggerEventSync(Event2)(NoEventData)
        assert(x === "test1")

        // x changes to 'test2' when Event1 triggered with this data
        eventbus.triggerEventSync(Event1)(new EventData1("test2"))
        assert(x === "test2")

        // x changes to 'test3' when Event1 triggered with this data
        eventbus.triggerEventSync(Event1)(new EventData1("test3"))
        assert(x === "test3")

        // Nothing happends when Event2 is triggered
        eventbus.triggerEventSync(Event2)(NoEventData)
        assert(x === "test3")
    }

    test ("eventbus asynchronous trigger works") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variable to be changed by event
        var x = "test1"

        // A listener on Event1, which changes x to event.str
        eventbus.addEventListener(Event1)(event => x = event.str)

        // Nothing happends when Event2 is triggered
        val eventTrigger1 = eventbus.triggerEvent(Event2)(NoEventData)
        whenReady(eventTrigger1) {
            case _ => assert(x === "test1")
        }


        // x changes to 'test2' when Event1 triggered with this data
        val eventTrigger2 = eventbus.triggerEvent(Event1)(new EventData1("test2"))
        whenReady(eventTrigger2) {
            case _ => assert(x === "test2")
        }


        // x changes to 'test3' when Event1 triggered with this data
        val eventTrigger3 = eventbus.triggerEvent(Event1)(new EventData1("test3"))
        whenReady(eventTrigger3) {
            case _ => assert(x === "test3")
        }


        // Nothing happends when Event2 is triggered
        val eventTrigger4 = eventbus.triggerEvent(Event2)(NoEventData)
        whenReady(eventTrigger4) {
            case _ => assert(x === "test3")
        }


    }

    test ("eventbus multiple events sync") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variables to be tested
        var x = 0
        var y = 0

        // Eventlisteners
        eventbus.addEventListener(Event2)(_ => x += 1)
        eventbus.addEventListener(Event2)(_ => y += 2)
        eventbus.addEventListener(Event3)(data => x = data.num * 2 )
        eventbus.addEventListener(Event3)(data => y = data.num )


        // Testing hasEventListener
        assert(!eventbus.hasEventListener(Event1))
        assert( eventbus.hasEventListener(Event2))
        assert( eventbus.hasEventListener(Event3))

        // Testing events
        eventbus.triggerEventSync(Event2)(NoEventData)
        assert(x === 1 && y === 2)

        eventbus.triggerEventSync(Event2)(NoEventData)
        assert(x === 2 && y === 4)

        eventbus.triggerEventSync(Event3)(new EventData2(num = 3))
        assert(x === 6 && y === 3)

        eventbus.triggerEventSync(Event2)(NoEventData)
        assert(x === 7 && y === 5)

        eventbus.triggerEventSync(Event3)(new EventData2(num = 4))
        assert(x === 8 && y === 4)
    }

    test ("eventbus multiple events async") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variables to be tested
        var x = 0
        var y = 0

        // Eventlisteners
        eventbus.addEventListener(Event2)(_ => x += 1)
        eventbus.addEventListener(Event2)(_ => y += 2)
        eventbus.addEventListener(Event3)(data => x = data.num * 2 )
        eventbus.addEventListener(Event3)(data => y = data.num )


        // Testing events
        whenReady(eventbus.triggerEvent(Event2)(NoEventData)) {
            _ => assert(x === 1 && y === 2)
        }

        whenReady(eventbus.triggerEvent(Event2)(NoEventData)) {
            _ => assert(x === 2 && y === 4)
        }

        whenReady(eventbus.triggerEvent(Event3)(new EventData2(num = 3))) {
            _ => assert(x === 6 && y === 3)
        }

        whenReady(eventbus.triggerEvent(Event2)(NoEventData)) {
            _ => assert(x === 7 && y === 5)
        }

        whenReady(eventbus.triggerEvent(Event3)(new EventData2(num = 4))) {
            _ => assert(x === 8 && y === 4)
        }
    }
}
