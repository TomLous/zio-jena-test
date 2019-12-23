
import org.apache.jena.query._
import zio._
import zio.console._

object TestJena extends App {

  val selectAllQuery = SparQLQuery("SELECT ?s ?p ?o WHERE {?s ?p ?0 .}")


  override def run(args: List[String]) = program

  val logic: ZIO[Console with Config with PureRDFConnection, Nothing, Int] =
    (for {
      config <- Config.>.loadConfig
      _ <- PureRDFConnection.>.connect(config).use(c => IO {
        println(c)
        println(c.queryResultSet(selectAllQuery.render, ResultSetFormatter.outputAsJSON))

      })
      _ <- putStrLn(s"Hello from application with name ${config.destination}")

    } yield 0)
      .catchAll(e => putStrLn(s"Application run failed $e").as(1))

  private val program = logic
    .provideSome[Console] {
      c =>
        new Console with Config.Live with PureRDFConnection.Live {
          override val console: Console.Service[Any] = c.console
        }
    }


}
