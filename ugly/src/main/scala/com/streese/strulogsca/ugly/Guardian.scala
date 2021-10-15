package com.streese.strulogsca.ugly

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.MDC
import com.typesafe.scalalogging.Logger
import com.streese.strulogsca.ugly.Context._

object Guardian {

  sealed trait Command
  case object  LogSomething extends Command

  def apply(): Behavior[Command] = Behaviors.receive {
    case (ctx, LogSomething) =>
      // START: Really ugly but might actually be thread-safe because of how Akka scheduling works?
      MDC.put("foo", "bar")
      ctx.log.info("message")
      MDC.clear()
      // END
      // START: Even uglier but definitely thread-safe
      implicit val context = Context("bar")
      val loggerWithContext = Logger.takingImplicit[Context](ctx.log)
      loggerWithContext.info("message")
      // END
      Behaviors.stopped
  }

}
