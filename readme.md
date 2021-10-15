# StruLogSca

## Introduction

This repository aims to demonstrate how to do structured logging in Scala in two different styles: Beautiful and ugly.
Both the beautiful as well as the ugly style utilize the _Logstash Logback Encoder (LLE)_ project to encode their
JSON log messages. The beautiful style however utilizes LLE's `StructuredArguments` to do structured logging, whereas
the ugly style abuses the _Mapped Diagnostic Context (MDC)_. The repository hopes to convince the reader, that using
`StructuredArguments` is the way to go.

## Beautiful

### How To Log

In order to log a structured message with `StructuredArguments`, all you have to do is the following:

```scala
import net.logstash.logback.argument.StructuredArguments._
// Assume that logger is coming from e.g. extending `StrictLogging` here
logger.info("message", keyValue("foo", "bar"))
```

The great thing is that this will work regardless of the `logger` being used, as long as the logging backend being
used is _Logback_. If you therefore want to do the above but through an Akka actors context logger that works just
the same:

```scala
import net.logstash.logback.argument.StructuredArguments._
// Assume we are in some actor here and ctx is of type `ActorContext[_]`
ctx.log.info("message", keyValue("foo", "bar"))
```

### Output

As you can see in the output below, MDC information coming from Akka is logged separate from our structured arguments.

```json
{
  "timestamp" : "2021-10-15T13:21:23.346Z",
  "message" : "message",
  "logger" : "com.streese.strulogsca.beautiful.Main$",
  "thread" : "main",
  "level" : "INFO",
  "arguments" : {
    "foo" : "bar"
  }
}
{
  "timestamp" : "2021-10-15T13:21:23.770Z",
  "message" : "Slf4jLogger started",
  "logger" : "akka.event.slf4j.Slf4jLogger",
  "thread" : "loggingSystem-akka.actor.default-dispatcher-3",
  "level" : "INFO"
}
{
  "timestamp" : "2021-10-15T13:21:23.845Z",
  "message" : "message",
  "logger" : "com.streese.strulogsca.beautiful.Guardian$",
  "thread" : "loggingSystem-akka.actor.default-dispatcher-5",
  "level" : "INFO",
  "arguments" : {
    "foo" : "bar"
  },
  "mdc" : {
    "akkaAddress" : "akka://loggingSystem",
    "akkaSource" : "akka://loggingSystem/user",
    "sourceActorSystem" : "loggingSystem"
  }
}
```

## Ugly

### How To Log

It's too ugly to be put into writing here. Just check out the ugly project. We only log four statements so the project
is still small, even though the ugly way of logging is like 1.000.000 times more verbose / contains more boilerplate
than the beautiful way.

### Output

We did log two more statements here than in the beautiful case to show two ugly ways of logging instead of just one.
Note how the structured argument is in the MDC section and not in the arguments section (which does not exist).

```json
{
  "timestamp" : "2021-10-15T13:47:42.575Z",
  "message" : "message",
  "logger" : "com.streese.strulogsca.ugly.Main$",
  "thread" : "main",
  "level" : "INFO",
  "mdc" : {
    "foo" : "bar"
  }
}
{
  "timestamp" : "2021-10-15T13:47:42.581Z",
  "message" : "message",
  "logger" : "com.streese.strulogsca.ugly.Main$",
  "thread" : "main",
  "level" : "INFO",
  "mdc" : {
    "foo" : "bar"
  }
}
{
  "timestamp" : "2021-10-15T13:47:42.981Z",
  "message" : "Slf4jLogger started",
  "logger" : "akka.event.slf4j.Slf4jLogger",
  "thread" : "loggingSystem-akka.actor.default-dispatcher-3",
  "level" : "INFO"
}
{
  "timestamp" : "2021-10-15T13:47:43.059Z",
  "message" : "message",
  "logger" : "com.streese.strulogsca.ugly.Guardian$",
  "thread" : "loggingSystem-akka.actor.default-dispatcher-3",
  "level" : "INFO",
  "mdc" : {
    "akkaAddress" : "akka://loggingSystem",
    "akkaSource" : "akka://loggingSystem/user",
    "sourceActorSystem" : "loggingSystem",
    "foo" : "bar"
  }
}
{
  "timestamp" : "2021-10-15T13:47:43.060Z",
  "message" : "message",
  "logger" : "com.streese.strulogsca.ugly.Guardian$",
  "thread" : "loggingSystem-akka.actor.default-dispatcher-3",
  "level" : "INFO",
  "mdc" : {
    "akkaAddress" : "akka://loggingSystem",
    "akkaSource" : "akka://loggingSystem/user",
    "sourceActorSystem" : "loggingSystem",
    "foo" : "bar"
  }
}
```

## Conclusion

I think it should become clear from the ugly example(s) that the motivation behind MDC was to allow contextual logging
(it's in the name after all) and not structured logging. These two share similarities but are not ultimately different.

Contextual logging assumes that you have some context in your code, in which you always want to put the same additional
stuff into your log statements. This is evident in the amount of required setup. This is definitely not something you
want to do for individual log statements but instead across multiple. The motivation behind MDC is clearly not to make
it easy to provide structured information to individual log statements.

Structured logging on the other hand is for providing additional structure to logs that make the individual logs better
to analyze. As such, any approach to structured logging should make it easy to provide additional information to
individual log statements.

In a way I think that structured logging is the logical progression over contextual logging the way it is done with
MDC. Both want to put more context into log messages to provide more analytical power to the user. But MDC restricts
the freedom one has to put more context into individual log statements by assuming that the context the user would
like to put into a log statement is always equal to the codes execution context (e.g. `Thread`/`ExecutionContext`).
Structured logging on the other hand does not assume this and instead says: Put whatever additional context you want
into any message you want.
