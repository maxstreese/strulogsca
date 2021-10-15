package com.streese.strulogsca.ugly

import com.typesafe.scalalogging.CanLog
import org.slf4j.MDC

case class Context(foo: String)

object Context {

  implicit case object CanLogContext extends CanLog[Context] {
    override def logMessage(originalMsg: String, a: Context): String = {
      MDC.put("foo", a.foo)
      originalMsg
    }
    override def afterLog(a: Context): Unit = MDC.remove("foo")
  }

}
