import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global


import io.zophie.api.event._

class APITest extends FunSuite with ScalaFutures {

    // Defining two dummy events and event data classes
    class EventData1 (val str : String) extends EventData

    case object Event1 extends Event {type eventData = EventData1 }
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
        eventbus.addEventListener(Event1)( (data : EventData1) => {});

        // Make sure it has Event1, but not Event2
        assert( eventbus.hasEventListener(Event1))
        assert(!eventbus.hasEventListener(Event2))

        // Add event listener for Event2
        eventbus.addEventListener(Event2)( (data : NoEventData) => {});

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
        eventbus.addEventListener(Event1)((event : EventData1) => { x = event.str })

        // Nothing has changed by adding listener
        assert(x === "test1")

        // Nothing happends when Event2 is triggered
        eventbus.triggerEventSync(Event2)(new NoEventData())
        assert(x === "test1")

        // x changes to 'test2' when Event1 triggered with this data
        eventbus.triggerEventSync(Event1)(new EventData1("test2"))
        assert(x === "test2")

        // x changes to 'test3' when Event1 triggered with this data
        eventbus.triggerEventSync(Event1)(new EventData1("test3"))
        assert(x === "test3")

        // Nothing happends when Event2 is triggered
        eventbus.triggerEventSync(Event2)(new NoEventData())
        assert(x === "test3")
    }

    test ("eventbus asynchronous trigger works") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variable to be changed by event
        var x = "test1"

        // A listener on Event1, which changes x to event.str
        eventbus.addEventListener(Event1)((event : EventData1) => { x = event.str })

        // Nothing happends when Event2 is triggered
        val eventTrigger1 = eventbus.triggerEvent(Event2)(new NoEventData())
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
        val eventTrigger4 = eventbus.triggerEvent(Event2)(new NoEventData())
        whenReady(eventTrigger4) {
            case _ => assert(x === "test3")
        }


    }
}
