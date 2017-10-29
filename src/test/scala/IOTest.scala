import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import java.net._
import java.io._
import scala.io._

import mockupio.IO

import io.zophie._

class IOTest extends FunSuite {
    
    val port = 29990
    val ip = InetAddress.getByName("localhost")
    
    test ("mockup IO may start") {        
        val IO = new IO(port)
        
        new Socket(ip, port).close()
        
        IO.close
    }
    
    
    val IO = new IO(port)
    
    IO.close
    
    test ("server is closed properly") {
        assertThrows[ConnectException] {
            new Socket(ip, port)
        }
    }
} 