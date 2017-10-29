package io.zophie.api.connection

import io.zophie.api.event._

import org.json4s._
import org.json4s.native.JsonMethods._

case class JSONConversionException(msg : String) extends Exception(msg)

// Implementation of JSON, to be passed to JSON converter 
trait EventDataJSONConverter[T <: EventData] {
    def toJSON   (data : T) : JObject
    def fromJSON (data : JObject) : T
}

trait EventDataJSONConverterRegistry {
    def register[T <: EventData] (event : Event[T])(implicit converter : EventDataJSONConverter[T]) : Unit
    
    def getEvent (name : String) : Option[Event[EventData]]
    def getEventDataJSONConverter[T <: EventData] (event : Event[T]) : Option[EventDataJSONConverter[T]]
}

class JSONEventConverter(implicit edconvreg : EventDataJSONConverterRegistry) extends EventConverter[String] {

    def toData[T <: EventData] (event : EventDataComposite[T]) : String = {
        val eventDataJSONConverter = edconvreg.getEventDataJSONConverter(event.event) match {
            case Some(value) => value
            case _ => throw JSONConversionException("No event-data jsonconverter found for " + event)
        }
        
        val eventstring = event.event.name
        val data = eventDataJSONConverter.toJSON(event.data)
        
        val json = {
            import org.json4s.JsonDSL._
            ("event" -> eventstring) ~
            ("data" -> data)
        }
        
        return compact(render(json))
    }
    
    // Parses JSON string into Event-EventData pair
    def fromData (data  : String) : EventDataComposite[EventData] = {
        // TODO: Add Exception on json syntax errors, or not fitting schema
        val json = {
            parse(data) 
        }
        
        val name : String = (json \ "event").extract[String](DefaultFormats, implicitly)
        val jsondata = (json \ "data").asInstanceOf[JObject] 
        
        
        val event = edconvreg.getEvent(name) match {
            case Some(value) => value
            case _ => throw JSONConversionException("No event found with name " + name)
        }
        
        val eventDataJSONConverter = edconvreg.getEventDataJSONConverter(event) match {
            case Some(value) => value
            case _ => throw JSONConversionException("No event-data jsonconverter found for " + event)
        }
        
        // TODO: implement with Option
        val eventData = eventDataJSONConverter.fromJSON(jsondata)
        
        return EventDataComposite(event)(eventData)
    }
}

// A converter that creates JSON-data from an Event-Event data pair
package object JSONConverter {

    def register[T <: EventData] (event : Event[T])(implicit converter : EventDataJSONConverter[T]) = mainEventJSONConverterRegistry.register[T](event)(converter)

    def toData[T <: EventData] (event : EventDataComposite[T]) : String = jsonc.toData(event)
    def fromData (data  : String) : EventDataComposite[EventData] = jsonc.fromData(data)
    

    implicit object edjsonNoEventData extends EventDataJSONConverter[NoEventData.type] {
        override def toJSON   (data : NoEventData.type) = JObject()
        override def fromJSON (data : JObject) = NoEventData
    }
    
    implicit object mainEventJSONConverterRegistry extends EventDataJSONConverterRegistry {
        private var eventRegistry : Map[String, Event[EventData]] = Map()
        private var eventDataRegistry : Map[Event[EventData], EventDataJSONConverter[_]] = Map()
        
        def register[T <: EventData] (event : Event[T])(implicit converter : EventDataJSONConverter[T]) = {
            eventRegistry += (event.name -> event)
            eventDataRegistry += (event -> converter)
        }
        
        override def getEvent (name : String) : Option[Event[EventData]] = eventRegistry.get(name)
        override def getEventDataJSONConverter[T <: EventData] (event : Event[T]) = eventDataRegistry.get(event).map(_.asInstanceOf[EventDataJSONConverter[T]])
    }
        
    // Implicit version, so it doesn't need to be passed
    implicit case object jsonc extends JSONEventConverter {
        
    }
}
