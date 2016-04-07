package vrepapiscala

import coppelia.remoteApi

/**
  * Created by trox on 05.04.16.
  */
sealed abstract class ScriptType(val rawCode: Int)
object ScriptType {
  case object MainScript extends ScriptType(remoteApi.sim_scripttype_mainscript)
  case object ChildScript extends ScriptType(remoteApi.sim_scripttype_childscript)
  case object JointControlCallBack extends ScriptType(remoteApi.sim_scripttype_jointctrlcallback)
  case object ContactCallBack extends ScriptType(remoteApi.sim_scripttype_contactcallback)
  case object CustomizationScript extends ScriptType(remoteApi.sim_scripttype_customizationscript)
  case object GeneralCallBack extends ScriptType(remoteApi.sim_scripttype_generalcallback)
}