package vrepapiscala.sensors

import coppelia._
import vrepapiscala.{ObjectType, OpMode}

/**
  * Created by trox on 02.02.16.
  */
class Sensors private[vrepapiscala](remote: remoteApi, id: Int){
  def forceSensor(name: String): ForceSensor = {
    getObjectHandle(name) match {
      case Some(handle) if checkTypeObject(handle, ObjectType.ForceSensor) =>
        new ForceSensor(remote, id, handle)
      case None | Some(_) =>
        throw new Exception(s"Force sensor with name $name, not found.")
    }
  }

  def navigationSensor(name: String): NavigationSensor = {
    getObjectHandle(name) match {
      case Some(handle) if checkTypeObject(handle, ObjectType.Dummy) =>
        new NavigationSensor(remote, id, handle)
      case None | Some(_) =>
        throw new Exception(s"Navigation sensor with name $name, not found.")
    }
  }

  def proximitySensor(name: String): ProximitySensor = {
    getObjectHandle(name) match {
      case Some(handle) if checkTypeObject(handle, ObjectType.ProximitySensor) =>
        new ProximitySensor(remote, id, handle)
      case None | Some(_) =>
        throw new Exception(s"Proximity sensor with name $name, not found.")
    }
  }

  def visionSensor(name: String, resolution: (Int, Int)): VisionSensor = {
    getObjectHandle(name) match {
      case Some(handle) if checkTypeObject(handle, ObjectType.VisionSensor) =>
        new VisionSensor(remote, id, handle, resolution)
      case None | Some(_)=>
        throw new Exception(s"Vision sensor with name $name, not found.")
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

  private def checkTypeObject(handle: Int, objType: ObjectType): Boolean = {
    val handlesArr = new IntWA(256)
    remote.simxGetObjectGroupData(
      id, objType.rawCode, 0, handlesArr, new IntWA(256),
      new FloatWA(256), new StringWA(0), OpMode.OneShotWait.rawCode)
    val handles = handlesArr.getArray.toVector
    handles.contains(handle)
  }
}
