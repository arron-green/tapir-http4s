package tapirpoc

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneId}

import cats.data.{Kleisli, OptionT}
import cats.effect._
import cats.implicits._
import org.http4s._
import sttp.tapir.server.http4s._

class ApiService[F[_]](
    implicit F: Effect[F],
    opts: Http4sServerOptions[F],
    ctx: ContextShift[F]
) {
  import models._

  def docs: Kleisli[OptionT[F, *], Request[F], Response[F]] =
    api.docs <+> api.root

  def timeRoutes: Kleisli[OptionT[F, *], Request[F], Response[F]] =
    api.Time.get.toRoutes(getTime.run) <+>
      api.Time.set.toRoutes(setTime.run)

  def timeZoneRoutes: Kleisli[OptionT[F, *], Request[F], Response[F]] =
    api.Time.Zone.get.toRoutes(getTimeZone.run) <+>
      api.Time.Zone.set.toRoutes(setTimeZone.run)

  // impl
  private[tapirpoc] val getTime = Kleisli[F, Unit, Either[Problem, Time]] {
    case _: Unit =>
      F.delay {
        val inst = Instant.now()
        if (inst.getEpochSecond % 2 == 0) {
          Problem("oops", "have problem").asLeft[Time]
        } else {
          val now = LocalDateTime.ofInstant(inst, ZoneId.systemDefault())
          Time(now.format(DateTimeFormatter.ISO_TIME)).asRight[Problem]
        }
      }
  }

  private[tapirpoc] val setTime = Kleisli[F, Time, Either[Problem, Unit]] {
    case input: Time =>
      for {
        _ <- F.delay(println(s"setting time to ${input}"))
      } yield ().asRight[Problem]
  }

  private[tapirpoc] val getTimeZone =
    Kleisli[F, Unit, Either[Problem, TimeZone]] {
      case _: Unit =>
        for {
          tz <- TimeZone("zone").pure[F]
        } yield tz.asRight[Problem]
    }

  private[tapirpoc] val setTimeZone =
    Kleisli[F, TimeZone, Either[Problem, Unit]] {
      case input: TimeZone =>
        for {
          _ <- F.delay(println(s"set timezone for ${input}"))
        } yield ().asRight[Problem]
    }

}
