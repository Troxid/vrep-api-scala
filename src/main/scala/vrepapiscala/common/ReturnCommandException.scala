package vrepapiscala.common

import vrepapiscala.CommandReturnCode

/**
  * Created by trox on 04.04.16.
  */
class ReturnCommandException(code: CommandReturnCode) extends Exception(code.msg)
