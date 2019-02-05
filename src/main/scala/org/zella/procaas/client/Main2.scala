package org.zella.procaas.client

import java.io.{BufferedReader, IOException, InputStreamReader}

import dispatch.{Http, url}
import io.vertx.scala.core.{Vertx, http}
import io.vertx.scala.ext.web.Router
import monix.eval.Task
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import okhttp3._

object Main2 {

  val client: OkHttpClient = new OkHttpClient()

  def startT(): Task[http.HttpServer] = {
    Task.fromFuture {
      val vertx = Vertx.vertx()
      val router = Router.router(vertx)

      router.get("/test").blockingHandler(ctx => {
        ctx.response().setChunked(true)
        for (i <- 1 to 100000) {
          Thread.sleep(750)
          println("Try write " + i)
          ctx.response().write(s"LINE:${i.toString}\n")
        }
        ctx.response().end()
      })
      vertx
        .createHttpServer()
        .requestHandler(router.accept _)
        .listenFuture(9444, "0.0.0.0")
    }
  }


  def main(args: Array[String]): Unit = {
    startT().runSyncUnsafe()

    //    val source = makeRequest(new Request.Builder()
    //      .url("http://localhost:9444/test")
    //      .get()
    //      .build())
    //
    //    val body = source.runSyncUnsafe()
    //
    //    val obs: Observable[String] = Observable.fromLinesReader(Task(new BufferedReader(new InputStreamReader(body.source().inputStream())))).doOnNext(c => Task(println(c)))
    //    val subscription = obs.subscribe()
    //
    //    Thread.sleep(2000)
    //
    //    subscription.cancel()


    val svc = url(s"http://localhost:9444/test")
      .setMethod("GET")


    val resp = Task.fromFuture(Http.default(svc)).runSyncUnsafe()

    val obs: Observable[String] = Observable.fromLinesReader(Task(new BufferedReader(new InputStreamReader(resp.getResponseBodyAsStream)))).doOnNext(c => Task(println(c)))
    val subscription = obs.subscribe()

    Thread.sleep(2000)

    subscription.cancel()

  }


  def makeRequest(request: Request): Task[ResponseBody] = {
    Task.create[ResponseBody] { (scheduler, callback) =>
      val call = client.newCall(request)
      call.enqueue(new Callback {
        override def onFailure(call: Call, e: IOException): Unit = {
          println("ERRR")
          callback.onError(e)
        }

        override def onResponse(call: Call, response: okhttp3.Response): Unit = {
          println("SUUU")
          callback.onSuccess(response.body())
        }
      })

      new Cancelable {
        override def cancel(): Unit = {
          println("Canceling")
          call.cancel()
        }
      }
    }
  }


}
