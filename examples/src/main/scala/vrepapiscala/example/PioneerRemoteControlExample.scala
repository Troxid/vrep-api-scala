/**
  * Created by troxid on 09.06.2015.
  */
package vrepapiscala.example

import vrepapiscala.VRepAPI

object PioneerRemoteControlExample extends App {
  val api = VRepAPI.connect("127.0.0.1", 19997).get
  val robot = new PioneerP3dx(api)

  api.simulation.start()

  for(_ <- 0 to 500){
    val resLS = robot.leftSensor.read
    val resRS = robot.rightSensor.read
    if(resLS.detectionState && resLS.detectedPoint.length < 0.5){
      robot.rotateRight()
    }else if(resRS.detectionState && resRS.detectedPoint.length < 0.5){
      robot.rotateLeft()
    } else {
      robot.moveForward()
    }
    Thread.sleep(10)
  }

  api.simulation.stop()
}

class PioneerP3dx(api: VRepAPI) {
  private val speed = 2f
  private val leftMotor = api.joint.withVelocityControl("Pioneer_p3dx_leftMotor").get
  private val rightMotor = api.joint.withVelocityControl("Pioneer_p3dx_rightMotor").get
  private val frontSensors =
    for(i <- 1 to 8)
      yield api.sensor.proximity("Pioneer_p3dx_ultrasonicSensor" + i).get

  def moveForward(): Unit = {
    leftMotor.setTargetVelocity(speed)
    rightMotor.setTargetVelocity(speed)
  }

  def moveBackward(): Unit = {
    leftMotor.setTargetVelocity(-speed)
    rightMotor.setTargetVelocity(-speed)
  }

  def rotateLeft(): Unit = {
    leftMotor.setTargetVelocity(-speed)
    rightMotor.setTargetVelocity(speed)
  }

  def rotateRight(): Unit = {
    leftMotor.setTargetVelocity(speed)
    rightMotor.setTargetVelocity(-speed)
  }

  def stop(): Unit = {
    leftMotor.setTargetVelocity(0)
    rightMotor.setTargetVelocity(0)
  }

  def leftSensor = frontSensors(1)

  def rightSensor = frontSensors(6)
}

