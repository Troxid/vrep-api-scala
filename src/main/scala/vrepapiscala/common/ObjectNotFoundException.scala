package vrepapiscala.common

/**
  * Created by trox on 04.04.16.
  */
class ObjectNotFoundException(name: String)
  extends Exception(s"""Object with name "$name", not found.""")