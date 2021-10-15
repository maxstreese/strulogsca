package com.streese.strulogsca.beautiful

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import net.logstash.logback.argument.StructuredArguments._

object Guardian {

  sealed trait Command
  case object  LogSomething extends Command

  def apply(): Behavior[Command] = Behaviors.receive {
    case (ctx, LogSomething) =>
      ctx.log.info("message", keyValue("foo", "bar"))
      // Well that was easy and clean, wasn't it?
      Behaviors.stopped
  }

}
