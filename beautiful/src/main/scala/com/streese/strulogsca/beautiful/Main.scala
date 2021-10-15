package com.streese.strulogsca.beautiful

import akka.actor.typed.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import net.logstash.logback.argument.StructuredArguments._

object Main extends App with StrictLogging {

  logger.info("message", keyValue("foo", "bar"))
  // Well that was easy and clean, wasn't it?

  implicit val sys = ActorSystem(Guardian(), "loggingSystem")

  sys ! Guardian.LogSomething

}
