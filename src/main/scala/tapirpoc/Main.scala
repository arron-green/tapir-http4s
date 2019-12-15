package tapirpoc

import cats.effect._
import cats.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import sttp.tapir.server.http4s._
import pureconfig.generic.auto._
import pureconfig.module.catseffect._

object Main extends IOApp {
  implicit val opts: Http4sServerOptions[IO] = Http4sServerOptions.default[IO]

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      conf <- loadConfigF[IO, models.ServerConfig]
      api <- IO(new ApiService[IO])
      server <- IO {
        BlazeServerBuilder[IO]
          .bindHttp(port = conf.port, host = "0.0.0.0")
          .withHttpApp(
            Router(
              "/" -> (api.timeRoutes <+> api.timeZoneRoutes <+> api.docs),
            ).orNotFound
          )
          .serve
          .compile
          .drain
      }
      exitcode <- server.map(_ => ExitCode.Success)
    } yield exitcode
  }
}
