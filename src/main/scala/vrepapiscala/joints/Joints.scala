package vrepapiscala.joints

import coppelia._
import vrepapiscala.common.{MismatchObjectTypeException, ObjectNotFoundException}
import vrepapiscala.{ObjectType, OpMode}

import scala.util.{Failure, Success, Try}

/**
  * Created by trox on 02.02.16.
  */
class Joints private[vrepapiscala](remote: remoteApi, id: Int){

  /**
    * Retrieves the joint with next parameters:
    * <ul>
    *   <li> Joint type: Spherical </li>
    *   <li> Joint mode: Force </li>
    * </ul>
    */
  def spherical(name: String, opMode: OpMode): Try[SphericalJoint] = {
    //FIXME: bug in v-rep: return revolute, instead spherical
    val validType = JointType.Spherical
    val validMode = JointMode.Passive
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`validType`, `validMode`, _, _))  =>
          Success(new SphericalJoint(remote, id, h, opMode))
        case None | Some(_) =>
          Failure(new MismatchObjectTypeException(name, validType, validMode))
      }
      case None =>
        Failure(new ObjectNotFoundException(name))
    }
  }

  def spherical(name: String): Try[SphericalJoint] = {
    spherical(name, OpMode.OneShotWait)
  }

  /**
    * Retrieves the joint(kinematic mode) with next parameters:
    * <ul>
    *   <li> Joint type: Revolute or Prismatic </li>
    *   <li> Joint mode: Passive </li>
    * </ul>
    */
  def passive(name: String, opMode: OpMode): Try[PassiveJoint] = {
    val fstValidType = JointType.Prismatic
    val sndValidType = JointType.Revolute
    val validMode = JointMode.Passive
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`fstValidType` | `sndValidType`, `validMode`, limit, range))  =>
          Success(new PassiveJoint(remote, id, h, limit, range, opMode))
        case None | Some(_) =>
          Failure(new MismatchObjectTypeException(name, fstValidType, sndValidType,validMode))
      }
      case None =>
        Failure(new ObjectNotFoundException(name))
    }
  }

  def passive(name: String): Try[PassiveJoint] = {
    passive(name, OpMode.OneShotWait)
  }

  /**
    * Retrieves the joint with next parameters:
    * <ul>
    *   <li> Joint type: Revolute or Prismatic </li>
    *   <li> Joint mode: Force </li>
    *   <li> Motor enabled: True </li>
    *   <li> Control loop enabled: True </li>
    *   <li> Spring-damper mode </li>
    * </ul>
    */
  def spring(name: String, opMode: OpMode): Try[SpringJoint] = {
    val fstValidType = JointType.Prismatic
    val sndValidType = JointType.Revolute
    val validMode = JointMode.Force
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`fstValidType` | `sndValidType`, `validMode`, limit, range))  =>
          Success(new SpringJoint(remote, id, h, limit, range, opMode))
        case None | Some(_) =>
          Failure(new MismatchObjectTypeException(name, fstValidType, sndValidType,validMode))
      }
      case None =>
        Failure(new ObjectNotFoundException(name))
    }
  }

  def spring(name: String): Try[SpringJoint] = {
    spring(name, OpMode.OneShotWait)
  }

  /**
    * Retrieves the joint (like servo) with next parameters:
    * <ul>
    *   <li> Joint type: Revolute or Prismatic </li>
    *   <li> Joint mode: Force </li>
    *   <li> Motor Enabled: True </li>
    *   <li> Control loop enabled: True </li>
    *   <li> Position control (PID) </li>
    * </ul>
    */
  def withPositionControl(name: String, opMode: OpMode): Try[JointWithPositionControl]= {
    val fstValidType = JointType.Prismatic
    val sndValidType = JointType.Revolute
    val validMode = JointMode.Force
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`fstValidType` | `sndValidType`, `validMode`, limit, range))  =>
          Success(new JointWithPositionControl(remote, id, h, limit, range, opMode))
        case None | Some(_) =>
          Failure(new MismatchObjectTypeException(name, fstValidType, sndValidType,validMode))
      }
      case None =>
        Failure(new ObjectNotFoundException(name))
    }
  }

  def withPositionControl(name: String): Try[JointWithPositionControl] = {
    withPositionControl(name, OpMode.OneShotWait)
  }

  /**
    * Retrieves the joint (like DC motor) with next parameters::
    * <ul>
    *   <li> Joint type: Revolute or Prismatic </li>
    *   <li> Joint mode: Force </li>
    *   <li> Motor Enabled: True </li>
    * </ul>
    */
  def withVelocityControl(name: String, opMode: OpMode): Try[JointWithVelocityControl]= {
    val fstValidType = JointType.Prismatic
    val sndValidType = JointType.Revolute
    val validMode = JointMode.Force
    getObjectHandle(name) match {
      case Some(h) => getInformationAboutJoint(h) match {
        case Some((`fstValidType` | `sndValidType`, `validMode`, limit, range))  =>
          Success(new JointWithVelocityControl(remote, id, h, limit, range, opMode))
        case None | Some(_) =>
          Failure(new MismatchObjectTypeException(name, fstValidType, sndValidType,validMode))
      }
      case None =>
        Failure(new ObjectNotFoundException(name))
    }
  }

  def withVelocityControl(name: String): Try[JointWithVelocityControl]= {
    withVelocityControl(name, OpMode.OneShotWait)
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
