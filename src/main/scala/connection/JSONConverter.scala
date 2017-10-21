package io.zophie.connection

import io.zophie.api.event._

package object JSONConverter {
    implicit object jsonc extends EventConverter[String] {

        case object Placeholder extends Event  {type eventData = NoEventData.type}

        def toData   (event : EventDataComposite) : String = {
            return "placeholder"
        }

        def fromData (data  : String) : EventDataComposite = {
            return new EventDataComposite(Placeholder)(NoEventData)
        }
    }
}
