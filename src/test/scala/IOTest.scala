import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import java.net._
import java.io._
import scala.io._

import mockupio.IO

import io.zophie._

val port = 29990
val ip = InetAddress.getByName("localhost")

class InternetTest extends FunSuite {
    
    test ("mockup IO may start") {        
        val IO = new IO(port)
        
        new Socket(ip, port).close()
        
        IO.close
    }
    
}

class IOTest extends FunSuite {
    
    val IO = new IO(port)
    
    test ("zophie starts") {
        Zophie.connection
    }
    
    
    test ("server is closed properly") {
        IO.close
        assertThrows[ConnectException] {
            new Socket(ip, port)
        }
    }
} 