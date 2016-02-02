package vrepapiscala.joints

import coppelia._
import vrepapiscala.{OpMode, ObjectType}

/**
  * Created by trox on 02.02.16.
  */
class Joints private[vrepapiscala](remote: remoteApi, id: Int){

  def spherical(name: String): SphericalJoint = {
    val validType = JointType.Spherical
    val validMode = JointMode.Passive
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`validType`, `validMode`, _, _))  =>
          new SphericalJoint(remote, id, h)
        case None | Some(_) =>
          throw new Exception(s"Joint does not fit the parameters. " +
            s"Must be $validType.")
      }
      case None =>
        throw new Exception(s"Component with name: $name does't exist")
    }
  }

  def passive(name: String): PassiveJoint = {
    val fstValidType = JointType.Prismatic
    val sndValidType = JointType.Revolute
    val validMode = JointMode.Passive
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`fstValidType` | `sndValidType`, `validMode`, limit, range))  =>
          new PassiveJoint(remote, id, h, limit, range)
        case None | Some(_) =>
          throw new Exception(s"Joint does not fit the parameters. " +
            s"Must be $fstValidType or $sndValidType and in $validMode mode.")
      }
      case None =>
        throw new Exception(s"Component with name: $name does't exist")
    }
  }

  def spring(name: String): SpringJoint = {
    val fstValidType = JointType.Prismatic
    val sndValidType = JointType.Revolute
    val validMode = JointMode.Force
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`fstValidType` | `sndValidType`, `validMode`, limit, range))  =>
          new SpringJoint(remote, id, h, limit, range)
        case None | Some(_) =>
          throw new Exception(s"Joint does not fit the parameters. " +
            s"Must be $fstValidType or $sndValidType and in $validMode mode.")
      }
      case None =>
        throw new Exception(s"Component with name: $name does't exist")
    }
  }

  def withPositionControl(name: String): JointWithPositionControl = {
    val fstValidType = JointType.Prismatic
    val sndValidType = JointType.Revolute
    val validMode = JointMode.Force
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`fstValidType` | `sndValidType`, `validMode`, limit, range))  =>
          new JointWithPositionControl(remote, id, h, limit, range)
        case None | Some(_) =>
          throw new Exception(s"Joint does not fit the parameters. " +
            s"Must be $fstValidType or $sndValidType and in $validMode mode.")
      }
      case None =>
        throw new Exception(s"Component with name: $name does't exist")
    }
  }

  def withVelocityControl(name: String): JointWithVelocityControl = {
    val fstValidType = JointType.Prismatic
    val sndValidType = JointType.Revolute
    val validMode = JointMode.Force
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`fstValidType` | `sndValidType`, `validMode`, limit, range))  =>
          new JointWithVelocityControl(remote, id, h, limit, range)
        case None | Some(_) =>
          throw new Exception(s"Joint does not fit the parameters. " +
            s"Must be $fstValidType or $sndValidType and in $validMode mode.")
      }
      case None =>
        throw new Exception(s"Component with name: $name does't exist")
    }
  }

  private def getInformationAboutJoint(handle: Int): Option[(JointType, JointMode, Float, Float)] = {
    val jointPropertiesCode = 16
    val handlesArr = new IntWA(256)
    val typesAndModesArr = new IntWA(256)
    val limitsAndRangesArr = new FloatWA(256)
    remote.simxGetObjectGroupData(
      id, ObjectType.Joint.rawCode, jointPropertiesCode, handlesArr, typesAndModesArr,
      limitsAndRangesArr, new StringWA(0), OpMode.OneShotWait.rawCode)
    val handles = handlesArr.getArray.toVector
    val typesAndModes = typesAndModesArr.getArray.grouped(2).toVector
    val limitsAndRanges = limitsAndRangesArr.getArray.grouped(2).toVector
    if(handles.contains(handle)){
      val i = handles.indexOf(handle)
      val jtype = JointType.fromRawCode(typesAndModes(i)(0))
      val jmode = JointMode.fromRawCode(typesAndModes(i)(1))
      val limit = limitsAndRanges(i)(0)
      val range = limitsAndRanges(i)(1)
      Some((jtype, jmode, limit, range))
    } else {
      None
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
}
