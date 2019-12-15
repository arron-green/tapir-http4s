package tapirpoc

import cats.effect._
import sttp.tapir
import org.http4s.HttpRoutes
import sttp.model.StatusCode
import sttp.tapir.Endpoint
import sttp.tapir.server.http4s._
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.json.circe._
import sttp.tapir.swagger.http4s.SwaggerHttp4s

object api {
  import models._

  object Error {
    val body = tapir.jsonBody[Problem].description("Problem indication")
  }

  object Time {
    private val endpoint = tapir.endpoint.in("time")

    val get: Endpoint[Unit, Problem, Time, Nothing] = endpoint.get
      .errorOut(Error.body)
      .out(tapir.jsonBody[Time].description("ISO time"))
      .description("Retrieves the current time")

    val set: Endpoint[Time, Problem, Unit, Nothing] = endpoint.put
      .errorOut(Error.body)
      .in(tapir.jsonBody[Time].description("ISO time"))
      .description("Changes the current time")

    object Zone {
      private val endpoint = Time.endpoint.in("zone")

      val get: Endpoint[Unit, Problem, TimeZone, Nothing] = endpoint.get
        .errorOut(Error.body)
        .out(tapir.jsonBody[TimeZone].description("Zone ID"))
        .description("Retrieves the current time zone")

      val set: Endpoint[TimeZone, Problem, Unit, Nothing] = endpoint.put
        .errorOut(Error.body)
        .in(tapir.jsonBody[TimeZone].description("Zone ID"))
        .description("Changes the current time zone")
    }
  }

  private val openAPI: OpenAPI =
    Seq(Time.get, Time.set, Time.Zone.get, Time.Zone.set)
      .toOpenAPI(title = "Time API", version = "V1")

  private val docsPath = "docs"
  private val swagger = new SwaggerHttp4s(openAPI.toYaml, docsPath)

  /**
    * Serves swagger UI
    */
  def docs[F[_]: Effect: Http4sServerOptions: ContextShift]: HttpRoutes[F] =
    swagger.routes[F]

  /**
    * Maps default '/' route to redirect to '/docs'
    */
  def root[F[_]: Effect: Http4sServerOptions: ContextShift]: HttpRoutes[F] =
    tapir.endpoint.get
      .description("default route")
      .out(
        tapir
          .header("location", docsPath)
          .and(tapir.statusCode(StatusCode.SeeOther))
      )
      .toRoutes(_ => Effect[F].pure(Right(())))

}
