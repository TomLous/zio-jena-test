import Config.JenaApiConfig
import org.apache.jena.rdfconnection._
import zio._
import console._
import stream._
import blocking.{Blocking, effectBlocking}
import org.apache.jena.query.ResultSetFormatter

trait PureRDFConnection extends Serializable {
  val rdfConnection: PureRDFConnection.Service[Any]
}

object PureRDFConnection {

  trait Service[R] {
//    def runQuery(query: SparQLQuery): ZIO[R with RDFConnection, Throwable, SparQLResult]

    def connect(conf: JenaApiConfig): ZManaged[R, Throwable, RDFConnection]
  }

    object > extends Service[PureRDFConnection] {
//      override def runQuery(query: SparQLQuery): ZIO[PureRDFConnection, Throwable, SparQLResult] = ZIO
//        .environment[PureRDFConnection]
//        .flatMap(_.rdfConnection.runQuery(query))

      override def connect(conf: JenaApiConfig): ZManaged[PureRDFConnection, Throwable, RDFConnection] =
        ZManaged
          .environment[PureRDFConnection]
          .flatMap(_.rdfConnection.connect(conf))
    }

  trait Live extends PureRDFConnection {
    val rdfConnection: Service[Any] = new Service[Any] {
//      override def runQuery(query: SparQLQuery): ZIO[Any, Throwable, SparQLResult] =
//        ???
//        for {
//        conn <-  ZIO.environment[RDFConnection]
//        res <- conn.queryResultSet(query.query,ResultSetFormatter.out)
//        json <- SparQLResult.parse(res)
//      } yield json

      override def connect(conf: JenaApiConfig): ZManaged[Any, Throwable, RDFConnection] = Managed
        .makeEffect(
          RDFConnectionRemote
            .create()
            .destination(conf.destination)
            .queryEndpoint(conf.queryEndpoint)
            .build()
        )(
          _.close()
        )
    }

  }


}


