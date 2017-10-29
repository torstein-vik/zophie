import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import java.net._
import java.io._
import scala.io._

import mockupio.IO

class IOTest extends FunSuite {
    
    val port = 29990
    val ip = InetAddress.getByName("localhost")
    
    val IO = new IO(port)
    
    IO.close
    
} 