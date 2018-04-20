package utils

object Pipe {
  implicit class implicits[T](val v: T) extends AnyVal {
    def |>[S](f: T ⇒ S): S = f(v)
  }
}
