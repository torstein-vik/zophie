import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Future, Promise }

import org.json4s._

import java.net._
import java.io._
import scala.io._

import io.zophie.api._
import io.zophie.api.event._
import io.zophie.api.event.Implicits._
import io.zophie.connection._
import io.zophie._


// Defining two dummy events and event data classes
class EventData1 (val str : String) extends EventData
class EventData2 (val num : Int)    extends EventData

case object Event1 extends Event[EventData1]("event1")
case object Event2 extends EventNoData("event2")
case object Event3 extends Event[EventData2]("event3")

class EventBusTest extends FunSuite with ScalaFutures {

    test ("empty eventbus does not have event handlers") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Make sure it does not have any event handlers
        assert(!eventbus.hasEventHandler(Event1))
        assert(!eventbus.hasEventHandler(Event2))

    }

    test ("eventbus hasEventHandler and addEventHandler works") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Add event handlers for Event1
        eventbus.addEventHandler(Event1)( _ => {});

        // Make sure it has Event1, but not Event2
        assert( eventbus.hasEventHandler(Event1))
        assert(!eventbus.hasEventHandler(Event2))

        // Add event handlers for Event2
        eventbus.addEventHandler(Event2)( _ => {});

        // Make sure it has all eventHandlers
        assert(eventbus.hasEventHandler(Event1))
        assert(eventbus.hasEventHandler(Event2))
    }

    test ("eventbus synchronous trigger works") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variable to be changed by event
        var x = "test1"

        // A handler on Event1, which changes x to event.str
        eventbus.addEventHandler(Event1)(event => x = event.str)

        // Nothing has changed by adding handler
        assert(x === "test1")

        // Nothing happends when Event2 is triggered
        eventbus.triggerEventSync(Event2)
        assert(x === "test1")

        // x changes to 'test2' when Event1 triggered with this data
        eventbus.triggerEventSync(Event1)(new EventData1("test2"))
        assert(x === "test2")

        // x changes to 'test3' when Event1 triggered with this data
        eventbus.triggerEventSync(Event1)(new EventData1("test3"))
        assert(x === "test3")

        // Nothing happends when Event2 is triggered
        eventbus.triggerEventSync(Event2)
        assert(x === "test3")
    }

    test ("eventbus asynchronous trigger works") {

        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variable to be changed by event
        var x = "test1"

        // A handler on Event1, which changes x to event.str
        eventbus.addEventHandler(Event1)(event => x = event.str)

        // Nothing happends when Event2 is triggered
        val eventTrigger1 = eventbus.triggerEvent(Event2)
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
        val eventTrigger4 = eventbus.triggerEvent(Event2)
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

        // Eventhandlers
        eventbus.addEventHandler(Event2)(_ => x += 1)
        eventbus.addEventHandler(Event2)(_ => y += 2)
        eventbus.addEventHandler(Event3)(data => x = data.num * 2 )
        eventbus.addEventHandler(Event3)(data => y = data.num )


        // Testing hasEventHandler
        assert(!eventbus.hasEventHandler(Event1))
        assert( eventbus.hasEventHandler(Event2))
        assert( eventbus.hasEventHandler(Event3))

        // Testing events
        eventbus.triggerEventSync(Event2)
        assert(x === 1 && y === 2)

        eventbus.triggerEventSync(Event2)
        assert(x === 2 && y === 4)

        eventbus.triggerEventSync(Event3)(new EventData2(num = 3))
        assert(x === 6 && y === 3)

        eventbus.triggerEventSync(Event2)
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

        // Eventhandlers
        eventbus.addEventHandler(Event2)(_ => x += 1)
        eventbus.addEventHandler(Event2)(_ => y += 2)
        eventbus.addEventHandler(Event3)(data => x = data.num * 2 )
        eventbus.addEventHandler(Event3)(data => y = data.num )


        // Testing events
        whenReady(eventbus.triggerEvent(Event2)) {
            _ => assert(x === 1 && y === 2)
        }

        whenReady(eventbus.triggerEvent(Event2)) {
            _ => assert(x === 2 && y === 4)
        }

        whenReady(eventbus.triggerEvent(Event3)(new EventData2(num = 3))) {
            _ => assert(x === 6 && y === 3)
        }

        whenReady(eventbus.triggerEvent(Event2)) {
            _ => assert(x === 7 && y === 5)
        }

        whenReady(eventbus.triggerEvent(Event3)(new EventData2(num = 4))) {
            _ => assert(x === 8 && y === 4)
        }
    }

    test ("eventbus multiple event order sync") {
        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variables to be tested
        var x = 0

        // Eventhandlers
        eventbus.addEventHandler(Event2)(_ => x = 1)
        eventbus.addEventHandler(Event2)(_ => x = 2)
        eventbus.addEventHandler(Event2)(_ => x = 3)

        // Testing events
        // Note that the last event handler should be run last
        eventbus.triggerEventSync(Event2)
        assert(x === 3)
    }

    test ("eventbus multiple event order async") {
        // The eventbus which is to be tested
        val eventbus : EventBus = new DefaultEventBus()

        // Variables to be tested
        var x = 0

        // Eventhandlers
        eventbus.addEventHandler(Event2)(_ => x = 1)
        eventbus.addEventHandler(Event2)(_ => x = 2)
        eventbus.addEventHandler(Event2)(_ => x = 3)

        // Testing events
        // Note that there is no order that these should be executed in, as they are execured in parallell
        whenReady(eventbus.triggerEvent(Event2)) {
            _ => assert(x === 1 || x === 2 || x === 3)
        }
    }
}

class APITest extends FunSuite with ScalaFutures {


    test ("API trigger works") {

        // The API which is to be tested
        val api : API = new API()

        // Variable to be changed by event
        var x = "test1"

        // A handler on Event1, which changes x to event.str
        api.on(Event1)(event => x = event.str)

        // Nothing happends when Event2 is triggered
        val eventTrigger1 = api.trigger(Event2)
        whenReady(eventTrigger1) {
            case _ => assert(x === "test1")
        }


        // x changes to 'test2' when Event1 triggered with this data
        val eventTrigger2 = api.trigger(Event1)(new EventData1("test2"))
        whenReady(eventTrigger2) {
            case _ => assert(x === "test2")
        }


        // x changes to 'test3' when Event1 triggered with this data
        val eventTrigger3 = api.trigger(Event1)(new EventData1("test3"))
        whenReady(eventTrigger3) {
            case _ => assert(x === "test3")
        }


        // Nothing happends when Event2 is triggered
        val eventTrigger4 = api.trigger(Event2)
        whenReady(eventTrigger4) {
            case _ => assert(x === "test3")
        }


    }

    test ("API multiple events") {

        // The API which is to be tested
        val api : API = new API()

        // Variables to be tested
        var x = 0
        var y = 0

        // Eventhandlers
        api.on(Event2)(_ => x += 1)
        api.on(Event2)(_ => y += 2)
        api.on(Event3)(data => x = data.num * 2 )
        api.on(Event3)(data => y = data.num )


        // Testing events
        whenReady(api.trigger(Event2)) {
            _ => assert(x === 1 && y === 2)
        }

        whenReady(api.trigger(Event2)) {
            _ => assert(x === 2 && y === 4)
        }

        whenReady(api.trigger(Event3)(new EventData2(num = 3))) {
            _ => assert(x === 6 && y === 3)
        }

        whenReady(api.trigger(Event2)) {
            _ => assert(x === 7 && y === 5)
        }

        whenReady(api.trigger(Event3)(new EventData2(num = 4))) {
            _ => assert(x === 8 && y === 4)
        }
    }

    test ("API multiple event order") {
        // The API which is to be tested
        val api : API = new API()

        // Variables to be tested
        var x = 0

        // Eventhandlers
        api.on(Event2)(_ => x = 1)
        api.on(Event2)(_ => x = 2)
        api.on(Event2)(_ => x = 3)

        // Testing events
        // Note that there is no order that these should be executed in, as they are execured in parallell
        whenReady(api.trigger(Event2)) {
            _ => assert(x === 1 || x === 2 || x === 3)
        }
    }
}

class SocketConnectionTest extends FunSuite with ScalaFutures {


    test ("Socket receives data") {

        // Message to be sent to socket
        val msg = "test1"

        // Port to use
        val port    = 29990
        // Server to test the connectionSocket
        val server  = new ServerSocket(port)

        // Promise for when socket has connected and received its data
        val promise = Promise[String]()
        // Setting up socket, and resolving promise with data when data is received
        val socket  = SocketConnection.setup(InetAddress.getByName("localhost"), port).setupConnection(promise.success)

        // Accept the socket session
        val session = server.accept
        // Output stream to send message to socket
        val out = new PrintStream( session.getOutputStream )

        // Sending and flushing message to socket
        out.println(msg)
        out.flush()

        // Closing session
        session.close()

        // When socket receives data, assert that it is equal to the message sent
        whenReady(promise.future) { data =>
            assert(data === msg)
        }

        // Close server
        server.close()

    }



    test ("Socket sends data") {

        // Message to be sent to server
        val msg = "test1"

        // Port to use
        val port    = 29990
        // Server for socket to connect to
        val server  = new ServerSocket(port)

        // Setting up socket, no input handler
        val socket  = SocketConnection.setup(InetAddress.getByName("localhost"), port).setupConnection(_ => {})

        // Sending data on socket to the server
        socket.push(msg)

        // Accepting session serverside
        val session = server.accept
        // Object to receive the data
        lazy val in = new BufferedSource( session.getInputStream ).getLines

        // Asserting that the message the server received is the message sent
        assert(in.next === msg)

        // Closing it all
        session.close()
        server.close()

    }

}

class ConfigTest extends FunSuite {
    test ("internet config") {

        // Tesing that the port and IP on defaultNetworkDetails is as exptected
        assert (DefaultNetworkDetails.defaultNetworkDetails.port === 29990)
        assert (DefaultNetworkDetails.defaultNetworkDetails.ip   === InetAddress.getByName("localhost"))
    }
}

class JSONTest extends FunSuite {
    // importing basic json functionallity
    import JSONConverter._
    import org.json4s.native.JsonMethods._
    
    // Some events without data
    case object EventA extends EventNoData("event_a")
    case object EventB extends EventNoData("event_b")
    case object EventC extends EventNoData("event_c")
    
    register(EventA)
    register(EventB)
    register(EventC)
    
    test ("NoData test") {
        
        
        // Rigorously defining {"event":"placeholder","data":{}} 
        val jsonString : String = {
            import org.json4s.JsonDSL._
            compact(render(("event" -> "event_a") ~ ("data" -> JObject())))
        }
        
        // toData and fromData work for Placeholder
        assert(toData(new EventDataComposite(EventA)(NoEventData)) === jsonString)
        assert(fromData(jsonString) == new EventDataComposite(EventA)(NoEventData))
    }
}
