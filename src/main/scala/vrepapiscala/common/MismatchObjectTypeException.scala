package vrepapiscala.common

/**
  * Created by trox on 04.04.16.
  */
class MismatchObjectTypeException(name: String, param: Any*)
  extends Exception(
    s"""Object with name "$name" does not fit the parameters.
       (${param.reduce(_ + " " + _)})""")
