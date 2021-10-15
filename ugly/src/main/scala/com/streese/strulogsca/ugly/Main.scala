package com.streese.strulogsca.ugly

import akka.actor.typed.ActorSystem
import com.typesafe.scalalogging.StrictLogging
import org.slf4j.MDC
import com.typesafe.scalalogging.Logger
import com.streese.strulogsca.ugly.Context._

object Main extends App with StrictLogging {

  // START: Really ugly and not thread-safe
  MDC.put("foo", "bar")
  logger.info("message")
  MDC.clear()
  // END

  // START: Even uglier but thread-safe
  implicit val context = Context("bar")
  val loggerWithContext = Logger.takingImplicit[Context](logger.underlying)
  loggerWithContext.info("message")
  // END

  implicit val sys = ActorSystem(Guardian(), "loggingSystem")

  sys ! Guardian.LogSomething

}
