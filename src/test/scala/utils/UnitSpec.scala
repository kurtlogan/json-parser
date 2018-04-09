package utils

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{ Matchers, WordSpec }

trait UnitSpec extends WordSpec with GeneratorDrivenPropertyChecks with Matchers
