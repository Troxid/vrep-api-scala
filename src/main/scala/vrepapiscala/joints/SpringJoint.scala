package vrepapiscala.joints

import coppelia.remoteApi

/**
  * Created by trox on 02.02.16.
  */
class SpringJoint private[vrepapiscala](remote: remoteApi, id: Int, handle: Int,
                                        val limit: Float, val range: Float){
  private val joint = new AnyJoint(remote, id, handle)

  def setTargetPosition(target: Float) = joint.setTargetPosition(target)
  def setTargetVelocity(target: Float) = joint.setTargetVelocity(target)
  def setMaximumForce(force: Float) = joint.setMaximumForce(force)
  def position = joint.getPosition
  def force = joint.getForce
}