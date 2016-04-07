package vrepapiscala

import coppelia.remoteApi

/**
  * Created by trox on 05.04.16.
  */
sealed abstract class ScriptReturnCode{
  case object NoError extends ScriptReturnCode
  case object MainScriptNonExistent extends ScriptReturnCode
  case object MainScriptNotCalled extends ScriptReturnCode
  case object ReentranceError extends ScriptReturnCode
  case object LuaError extends ScriptReturnCode
  case object ScriptCallError extends ScriptReturnCode

  def fromRawCode(rawCode: Int): ScriptReturnCode = rawCode match {
    case remoteApi.sim_script_no_error => NoError
    case remoteApi.sim_script_main_script_nonexistent => MainScriptNonExistent
    case remoteApi.sim_script_main_script_not_called => MainScriptNotCalled
    case remoteApi.sim_script_reentrance_error => ReentranceError
    case remoteApi.sim_script_lua_error => LuaError
    case remoteApi.sim_script_call_error => ScriptCallError
  }
}