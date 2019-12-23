import pureconfig._
import pureconfig.generic.auto._
import zio._

trait Config {
  val config: Config.Service[Any]
}

object Config {

  case class ConfigError(message: String) extends RuntimeException(message)
  case class JenaApiConfig(destination:String, queryEndpoint: String)

  trait Service[R] {
    def loadConfig: ZIO[R, Throwable, JenaApiConfig]
  }

  trait Live extends Config {
    val config: Config.Service[Any] = new Service[Any] {
      override def loadConfig: Task[JenaApiConfig] =
        ZIO
          .fromEither(ConfigSource.default.load[JenaApiConfig])
          .mapError(e => ConfigError(e.toList.mkString(", ")))
    }
  }

  object > extends Config.Service[Config] {
    override def loadConfig: ZIO[Config, Throwable, JenaApiConfig] = ZIO.accessM[Config](_.config.loadConfig)
  }
}