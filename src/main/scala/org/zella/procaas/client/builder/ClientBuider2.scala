package org.zella.procaas.client.builder

import monix.eval.Task
import monix.reactive.Observable
import org.zella.procaas.client.Chef
import org.zella.procaas.client.builder.ClientBuilder.Read
import org.zella.procaas.client.builder.ClientBuilder.Read.NoRead


sealed trait OutputMode

case object Stdout extends OutputMode {
  override def toString: String = "stdout"
}

case object ChunkedStdout extends OutputMode {
  override def toString: String = "chunkedStdout"
}

case object SingleFile extends OutputMode {
  override def toString: String = "file"
}

case object ZipFile extends OutputMode {
  override def toString: String = "zip"
}

sealed trait Computation

case object Io extends Computation {
  override def toString: String = "io"
}

case object Cpu extends Computation {
  override def toString: String = "cpu"
}


case class CommonProcessInput(cmd: Seq[String],
                              zipInputMode: Option[Boolean] = None,
                              stdin: Option[String] = None,
                              envs: Option[Map[String, String]] = None,
                              outPutMode: Option[String] = None,
                              outputDir: Option[String] = None,
                              timeoutMillis: Option[Long] = None,
                              computation: Option[String] = None
                             ) {

}


//TODO input to request
//TODO request customization
//TODO okHttpClient customization

//abstract class ClientBuilder(cmd: Seq[String]) {
//
//
//  def withZipInput: ClientBuilder
//
//  def stdIn(stdin: String): ClientBuilder
//
//  def envs(envs: Map[String, String]): ClientBuilder
//
//  def outPutMode(mode: OutputMode): ClientBuilder
//
//  def outputDir(dir: String): ClientBuilder
//
//  def timeout(time: FiniteDuration): ClientBuilder
//
//  def computation(c: Computation): ClientBuilder
//
//  def build: Product
//}

object ClientBuilder {

  sealed trait Read

  sealed trait FilledRead extends Read

  sealed trait EmptyRead extends Read

  object Read {

    sealed trait Stdout extends FilledRead

    sealed trait Chunked extends FilledRead

    sealed trait File extends FilledRead

  }

}

class ClientBuilder[Read <: ClientBuilder.Read](input: CommonProcessInput) {

  import org.zella.procaas.client.builder.ClientBuilder._
  import org.zella.procaas.client.builder.ClientBuilder.Read._

  def outputBuffered(implicit ev: Read =:= EmptyRead): ClientBuilder[Read with Stdout] = new ClientBuilder(input.copy(outPutMode = Some("stdout")))

  //  def outputChunked(implicit ev: Read =:= EmptyRead): ClientBuilder[Read with Chunked] = new ClientBuilder(input.copy(outPutMode = Some("chunkedStdout")))
  //
  //  def outputZip(implicit ev: Read =:= EmptyRead): ClientBuilder[Read with Chunked] = new ClientBuilder(input.copy(outPutMode = Some("zip")))
  //
  //  def outputFile(implicit ev: Read =:= EmptyRead): ClientBuilder[Read with Chunked] = new ClientBuilder(input.copy(outPutMode = Some("file")))

  def withZipInput: ClientBuilder[Read] = new ClientBuilder(input.copy(zipInputMode = Some(true)))

  def stdIn(stdin: String): ClientBuilder[Read] = new ClientBuilder(input.copy(stdin = Some(stdin)))

  def output(implicit ev: Read =:= EmptyRead): Task[String] = {
    val input = this.input.copy(outPutMode = Some("stdout"))

    //input to Request

    //Core.exec request

  }

  def outputChunked(implicit ev: Read =:= EmptyRead): Observable[String] = {
    val input = this.input.copy(outPutMode = Some("chunkedStdout"))

    //input to Request

    //Core.exec request

  }

  def outputZip(dir:java.io.File)(implicit ev: Read =:= EmptyRead): Task[java.io.File] = {
    val input = this.input.copy(outPutMode = Some("zip"))
  }

  def outputFile(dir:java.io.File)(implicit ev: Read =:= EmptyRead):  Task[java.io.File]  = new ClientBuilder(input.copy(outPutMode = Some("file"))){
    val input = this.input.copy(outPutMode = Some("file"))
  }
  //
  //  def envs(envs: Map[String, String]): ClientBuilder[_] = {
  //    input = input.copy(envs = Some(envs))
  //    this
  //  }
  //
  ////  def outPutMode(mode: OutputMode): ClientBuilder[_] = {
  ////    input = input.copy(outPutMode = Some(mode.toString))
  ////    this
  ////  }
  //  def readAsStdout: ClientBuilder[Stdout.type ] = {
  //    input = input.copy(outPutMode = Some(Stdout.toString))
  //    this
  //  }
  //
  //
  //  def outputDir(dir: String): ClientBuilder[_] = {
  //    input = input.copy(outputDir = Some(dir))
  //    this
  //  }
  //
  //  def timeout(time: FiniteDuration): ClientBuilder[_] = {
  //    input = input.copy(zipInputMode = Some(true))
  //    this
  //  }
  //
  //  def computation(c: Computation): ClientBuilder[_] = {
  //    input = input.copy(zipInputMode = Some(true))
  //    this
  //  }
  //
  //  def build: CommonProcessInput = input
}


object ScalaClient {

  def main(args: Array[String]): Unit = {
    new ClientBuilder[NoRead](null)
      .readAsChunked
    new Chef[Chef.Pizza.EmptyPizza]()
      .addCheese("mozzarella")
      .addDough
      .addTopping("olives")
      .build
    println("done")

    new ClientBuilder[NoRead](null)
      .stdIn("sd")
      .withZipInput
      .readAsChunked

    //
    //    Procaas.clientBuilder(Seq())
    //      .stdIn("sds").
    //      readAsStdout
  }
}

//
object Procaas {
  def clientBuilder(cmd: Seq[String]): ClientBuilder[NoRead] = new ClientBuilder[NoRead](CommonProcessInput(cmd))
}