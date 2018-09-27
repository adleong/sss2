/** Simple scala server */

import $ivy.`com.twitter::finagle-http:6.44.0`
import $ivy.`io.buoyant::finagle-h2:1.1.0`
import com.twitter.concurrent.AsyncQueue
import com.twitter.conversions.time._
import com.twitter.finagle.{Status => _, _}
import com.twitter.finagle.buoyant.h2._
import com.twitter.finagle.buoyant.H2
import com.twitter.util._
import com.twitter.finagle.util.DefaultTimer
import com.twitter.logging.{Level, Logger, Logging}
import scala.util.Random

@main
def main(port: Int, word: String) = {

implicit val timer = DefaultTimer.twitter

Logger.get("").setLevel(Level.TRACE)

val server = H2.serve(s":$port", Service.mk { req: Request =>
  println(toString(req))
  for( (k,v) <- req.headers.toSeq ) {
    println(s"$k: $v")
  }
  println()

  val rsp = Response(Headers(":status" -> "500", "grpc-status" -> "3"), Stream.empty())
    
  Future.value(rsp)
})

Await.ready(server)

}

def toString(req: Request): String = {
  val method = req.headers.get(Headers.Method).getOrElse("???")
  val authority = req.headers.get(Headers.Authority).getOrElse("???")
  val path = req.headers.get(Headers.Path).getOrElse("???")
  s"$method $authority$path"
}

