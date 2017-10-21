package io.zophie.connection

import io.zophie.api.event._

package object JSONConverter {
    implicit object jsonc extends EventConverter[String] {
        def toData   (event : EventDataComposite) : String = {

        }

        def fromData (data  : String) : EventDataComposite = {

        }
    }
}
