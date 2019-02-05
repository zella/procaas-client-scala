package org.zella.procaas.client.internal

import java.io.{BufferedReader, IOException, InputStreamReader}

import com.typesafe.scalalogging.LazyLogging
import monix.eval.Task
import monix.execution.Cancelable
import monix.reactive.Observable
import okhttp3._
import org.zella.procaas.client.errors.ProcaasException

private object ClientCore extends LazyLogging {

  def execRequestT(client: OkHttpClient, request: Request): Task[Response] = {
    Task.create[Response] { (_, callback) =>
      val call = client.newCall(request)
      call.enqueue(new Callback {
        override def onFailure(call: Call, e: IOException): Unit = callback.onError(e)

        override def onResponse(call: Call, response: okhttp3.Response): Unit = {
          response.code() match {
            case c if c >= 200 && c <= 299 => callback.onSuccess(response)
            case c => callback.onError(new ProcaasException(s"Code: $c msg: ${response.body().string()}"))
          }
        }
      })
      new Cancelable {
        override def cancel(): Unit = {
          logger.debug("Canceling request...")
          call.cancel()
        }
      }
    }
  }


  def unbuffered(task: Task[Response]): Observable[String] = {
    Observable.fromTask(task)
      .flatMap(response => Observable.fromLinesReader(Task(new BufferedReader(new InputStreamReader(response.body().byteStream()))))
        .doOnNext(c => Task(logger.debug(c))))
  }

  def buffered(task: Task[Response]): Task[String] = task.map(_.body().string())

}
