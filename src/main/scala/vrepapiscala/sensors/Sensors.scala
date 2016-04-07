package vrepapiscala.sensors

import coppelia._
import vrepapiscala.common.ObjectNotFoundException
import vrepapiscala.{ObjectType, OpMode}

import scala.util.{Failure, Success, Try}

/**
  * Created by trox on 02.02.16.
  */
class Sensors private[vrepapiscala](remote: remoteApi, id: Int){

  def force(name: String, opMode: OpMode = OpMode.OneShotWait): Try[ForceSensor] = {
    getObjectHandle(name) match {
      case Some(handle) if checkTypeObject(handle, ObjectType.ForceSensor) =>
        Success(new ForceSensor(remote, id, handle, opMode))
      case None | Some(_) =>
        Failure(new ObjectNotFoundException(name))
    }
  }

  def position(name: String, opMode: OpMode = OpMode.OneShotWait): Try[PositionSensor] = {
    getObjectHandle(name) match {
      case Some(handle) if checkTypeObject(handle, ObjectType.Dummy, ObjectType.Shape) =>
        Success(new PositionSensor(remote, id, handle, opMode))
      case None | Some(_) =>
        Failure(new ObjectNotFoundException(name))
    }
  }

  def proximity(name: String, opMode: OpMode = OpMode.OneShotWait): Try[ProximitySensor] = {
    getObjectHandle(name) match {
      case Some(handle) if checkTypeObject(handle, ObjectType.ProximitySensor) =>
        Success(new ProximitySensor(remote, id, handle, opMode))
      case None | Some(_) =>
        Failure(new ObjectNotFoundException(name))
    }
  }

  def vision(name: String, resolution: (Int, Int), opMode: OpMode = OpMode.OneShotWait): Try[VisionSensor] = {
    getObjectHandle(name) match {
      case Some(handle) if checkTypeObject(handle, ObjectType.VisionSensor) =>
        Success(new VisionSensor(remote, id, handle, resolution, opMode))
      case None | Some(_)=>
        Failure(new ObjectNotFoundException(name))
    }
  }

  private def getObjectHandle(name: String): Option[Int] = {
    val h = new IntW(-1)
    val res = remote.simxGetObjectHandle(id, name, h, OpMode.OneShotWait.rawCode)
    if (res == remoteApi.simx_return_ok) {
      Some(h.getValue)
    } else {
      None
    }
  }

  private def checkTypeObject(handle: Int, objTypes: ObjectType*): Boolean = {
    val allResult = for(typ <- objTypes) yield {
      val handlesArr = new IntWA(256)
      remote.simxGetObjectGroupData(
        id, typ.rawCode, 0, handlesArr, new IntWA(256),
        new FloatWA(256), new StringWA(0), OpMode.OneShotWait.rawCode)
      val handles = handlesArr.getArray.toVector
      handles.contains(handle)
    }
    allResult.reduce(_ || _)
  }
}
