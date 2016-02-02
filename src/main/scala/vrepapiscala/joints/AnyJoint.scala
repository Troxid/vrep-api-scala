package vrepapiscala.joints

import coppelia.{remoteApi, FloatWA, FloatW}
import vrepapiscala.OpMode

/**
  * Created by trox on 01.02.16.
  */
private[vrepapiscala] class AnyJoint private[vrepapiscala](remote: remoteApi, id: Int, handle: Int) {

  /** Sets the intrinsic target velocity of a non-spherical joint.
    * This command makes only sense when the joint mode is:
    * (a) motion mode: the joint's motion handling feature must be enabled
    * (simHandleJoint must be called (is called by default in the main script),
    * and the joint motion properties must be set in the joint settings dialog),
    * (b) torque/force mode: the dynamics functionality and the joint motor have
    * to be enabled (position control should however be disabled)
    *
    * @param target target velocity of the joint (linear or angular velocity depending on the joint-type)
    */
  def setTargetVelocity(target: Float): Unit = {
    remote.simxSetJointTargetVelocity(id, handle, target, OpMode.OneShotWait.rawCode)
  }

  /** Sets the target position of a joint if the joint is in
    * torque/force mode (also make sure that the joint's motor and position control are enabled)
    *
    * @param target target position of the joint (angular or linear value depending on the joint type)
    */
  def setTargetPosition(target: Float): Unit = {
    remote.simxSetJointTargetPosition(id, handle, target, OpMode.OneShotWait.rawCode)
  }

  /** Retrieves the force or torque applied to a joint along/about its active axis. */
  /**
    * Retrieves the force or torque applied to a joint along/about its active axis.
    * This function retrieves meaningful information only if the joint is prismatic or revolute,
    * and is dynamically enabled. With the Bullet engine, this function returns the force or torque
    * applied to the joint motor (torques from joint limits are not taken into account).
    * With the ODE or Vortex engine, this function returns the total force or torque
    * applied to a joint along/about its z-axis.
    *
    * @return the force or the torque applied to the joint along/about its z-axis
    */
  def getForce: Float = {
    val h = new FloatW(-1)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    remote.simxGetJointForce(id, handle, h, OpMode.OneShotWait.rawCode)
    h.getValue
  }

  /** Sets the maximum force or torque that a joint can exert.
    *
    * @note This function has no effect when the joint is not dynamically enabled, or when it is a spherical joint
    * @param force maximum force or torque that the joint can exert
    */
  def setMaximumForce(force: Float): Unit = {
    remote.simxSetJointForce(id, handle, force, OpMode.OneShotWait.rawCode)
  }

  /** Sets the intrinsic position of a joint.
    * May have no effect depending on the joint mode.
    * This function cannot be used with spherical joints (use simxSetSphericalJointMatrix instead).
    * If you want to set several joints that should be applied at the exact same time on the V-REP side,
    * then use simxPauseCommunication.
    *
    * @param position position of the joint (angular or linear value depending on the joint type)
    */
  def setPosition(position: Float): Unit = {
    remote.simxSetJointPosition(id, handle, position, OpMode.OneShotWait.rawCode)
  }

  /** Retrieves the intrinsic position of a joint.
    * This function cannot be used with spherical joints (use getMatrix instead)
    *
    * @return intrinsic position of the joint (output). This is a one-dimensional value: if the joint is revolute,
    *         the rotation angle is returned, if the joint is prismatic, the translation amount is returned, etc.
    */
  def getPosition: Float ={
    val h = new FloatW(-1)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    remote.simxGetJointPosition(id, handle, h, OpMode.OneShotWait.rawCode)
    h.getValue
  }

  /** Retrieves the intrinsic transformation matrix of a joint (the transformation caused by the joint movement).
    *
    * @return matrix: 12 values (output).
    */
  def getMatrix: Array[Float] = {
    val h = new FloatWA(12)
    //TODO: simx_opmode_streaming (the first call) and simx_opmode_buffer (the following calls)
    remote.simxGetJointMatrix(id, handle, h, OpMode.OneShotWait.rawCode)
    h.getArray
  }

  /** Sets the intrinsic orientation matrix of a spherical joint object.
    * This function cannot be used with non-spherical joints (use simxSetJointPosition instead).
    *
    *  @param matrix array of 12 values.
    *  @throws IllegalArgumentException matrix must be contain 12 values
    */
  def setSphericalMatrix(matrix: Array[Float]) = {
    require(matrix.length == 12, "matrix must be contain 12 values")
    val h = new FloatWA(12)
    matrix.copyToArray(h.getArray)
    remote.simxSetSphericalJointMatrix(id, handle, h, OpMode.OneShotWait.rawCode)
  }
}
