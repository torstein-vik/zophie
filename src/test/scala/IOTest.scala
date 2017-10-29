import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import mockupio.IO

class IOTest extends FunSuite {
    
    val IO = new IO(29990)
    
    IO.close
    
} 