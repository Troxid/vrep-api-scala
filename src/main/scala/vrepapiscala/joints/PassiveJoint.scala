package vrepapiscala.joints

import coppelia.remoteApi

/**
  * Created by trox on 02.02.16.
  */
class PassiveJoint private[vrepapiscala](remote: remoteApi, id: Int, handle: Int,
                                         val limit: Float, val range: Float){
  private val joint = new AnyJoint(remote, id, handle)

  def position_=(float: Float) = joint.setPosition(float)

  def position: Float = joint.getPosition
}
