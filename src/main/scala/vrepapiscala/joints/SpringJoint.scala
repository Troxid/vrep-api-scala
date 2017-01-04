package vrepapiscala.joints

import coppelia.remoteApi
import vrepapiscala.OpMode


/**
  * Created by trox on 02.02.16.
  */
class SpringJoint private[vrepapiscala](
  remote: remoteApi, id: Int, handle: Int,
  val limit: Float, val range: Float, opMode: OpMode){
  private val joint = new AnyJoint(remote, id, handle, opMode)

  def setTargetPosition(rad: Float): Unit = joint.setTargetPosition(rad)
  def setTargetVelocity(radInSec: Float): Unit = joint.setTargetVelocity(radInSec)
  def setMaximumForce(force: Float): Unit = joint.setMaximumForce(force)
  def position: Float = joint.getPosition
  def force: Float = joint.getForce
}