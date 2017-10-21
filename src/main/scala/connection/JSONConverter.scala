package io.zophie.connection

import io.zophie.api.event._

package object JSONConverter {
    implicit object jsonc extends EventConverter[String] {

        case object Placeholder extends EventNoData

        def toData   (event : EventDataComposite) : String = {
            return "placeholder"
        }

        def fromData (data  : String) : EventDataComposite = {
            return new EventDataComposite(Placeholder)(NoEventData)
        }
    }
}
