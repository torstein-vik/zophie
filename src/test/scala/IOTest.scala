import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import java.net._
import java.io._
import scala.io._

import mockupio.IO

import io.zophie._
import io.zophie.api.connection.DefaultNetworkDetails._

class InternetTest extends FunSuite {
    
    test ("mockup IO may start") {        
        val IO = new IO(defaultNetworkDetails.port)
        
        new Socket(defaultNetworkDetails.ip, defaultNetworkDetails.port).close()
        
        IO.close
    }
    
}

class IOTest extends FunSuite {
    
    val IO = new IO(defaultNetworkDetails.port)
    
    test ("zophie starts") {
        Zophie.connection
    }
    
    
    test ("server is closed properly") {
        IO.close
        assertThrows[ConnectException] {
            new Socket(defaultNetworkDetails.ip, defaultNetworkDetails.port)
        }
    }
} 
