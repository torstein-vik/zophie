package io.zophie.connection

import io.zophie.api.event._

// A converter that creates JSON-data from an Event-Event data pair
package object JSONConverter {

    // Implicit version, so it doesn't need to be passed
    implicit object jsonc extends EventConverter[String] {

        // For now, just a placeholder event
        case object Placeholder extends EventNoData

        // Stringifies the JSON for the pair
        def toData   (event : EventDataComposite) : String = {

            // For now just returns placeholder
            return "placeholder"
        }

        // Parses JSON string into Event-EventData pair
        def fromData (data  : String) : EventDataComposite = {

            // Just return placeholder
            return new EventDataComposite(Placeholder)(NoEventData)
        }
    }
}
