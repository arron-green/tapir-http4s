package tapirpoc

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object models {

  /**
    * Barebones rfc7807 https://tools.ietf.org/html/rfc7807
    * @param `type`
    * @param title
    */
  case class Problem(`type`: String, title: String)
  object Problem {
    implicit val encoder = deriveEncoder[Problem]
    implicit val decoder = deriveDecoder[Problem]
  }

  case class Time(result: String)
  object Time {
    implicit val encoder = deriveEncoder[Time]
    implicit val decoder = deriveDecoder[Time]
  }

  case class TimeZone(result: String)
  object TimeZone {
    implicit val encoder = deriveEncoder[TimeZone]
    implicit val decoder = deriveDecoder[TimeZone]
  }
}
