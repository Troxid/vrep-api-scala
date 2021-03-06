package vrepapiscala.joints

import coppelia.remoteApi
import vrepapiscala.OpMode

/**
  * Created by trox on 02.02.16.
  */
class PassiveJoint private[vrepapiscala](
  remote: remoteApi, id: Int, handle: Int,
  val limit: Float, val range: Float, opMode: OpMode){
  private val joint = new AnyJoint(remote, id, handle, opMode)

  def position_=(rad: Float): Unit = joint.setPosition(rad)

  def setPosition(rad: Float): Unit = joint.setPosition(rad)

  def position: Float = joint.getPosition
}
