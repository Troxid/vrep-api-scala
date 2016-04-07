package vrepapiscala.joints

import coppelia.remoteApi
import vrepapiscala.OpMode

/**
  * Created by trox on 02.02.16.
  */
class JointWithPositionControl private[vrepapiscala](
  remote: remoteApi, id: Int, handle: Int,
  val limit: Float, val range: Float, opMode: OpMode){
  private val joint = new AnyJoint(remote, id, handle, opMode)

  def setTargetPosition(target: Float) = joint.setTargetPosition(target)
  def setMaximumForce(force: Float) = joint.setMaximumForce(force)
  def position = joint.getPosition
  def force = joint.getForce
}