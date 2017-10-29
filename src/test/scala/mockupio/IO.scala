package mockupio

import java.net._
import java.io._
import scala.io._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Future, Promise }

class IO (port : Int) {
    
    val server = new ServerSocket(port)
    
    var running = true
    
    Future {
        val session = server.accept()
        
        val out = new PrintStream( session.getOutputStream )
        val in  = new BufferedSource( session.getInputStream ).getLines
        
        while (running) {
            
        }
        
        session.close()
    }
    
    def close = {
        server.close()
        running = false
    }
    
}
