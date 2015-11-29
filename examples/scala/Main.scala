/**
  * Created by troxid on 09.06.2015.
  */

import vrepapiscala._

object Main extends App {
  val api = VRepAPI.connect("127.0.0.1", 19997).get
  val robot = new Robot(api)

  api.startSimulation()

  for(_ <- 0 to 500){
    Thread.sleep(10)
    println(robot.gps.position)
    if (robot.sensors._1) {
      robot.rotateRight()
    } else if (robot.sensors._2) {
      robot.rotateLeft()
    } else {
      robot.moveForward()
    }
  }

  api.stopSimulation()
}

class Robot(api: VRepAPI) {
  private val speed = 2f
  private val leftMotor = api.getJoint("left_motor").get
  private val rightMotor = api.getJoint("right_motor").get
  private val leftSensor = api.getProximitySensor("left_sonar").get
  private val rightSensor = api.getProximitySensor("right_sonar").get
  val gps = api.getNavigationSensor("gps").get

  def moveForward(): Unit = {
    leftMotor.setTargetVelocity(speed)
    rightMotor.setTargetVelocity(-speed)
  }

  def moveBackward(): Unit = {
    leftMotor.setTargetVelocity(-speed)
    rightMotor.setTargetVelocity(speed)
  }

  def rotateLeft(): Unit = {
    leftMotor.setTargetVelocity(-speed)
    rightMotor.setTargetVelocity(-speed)
  }

  def rotateRight(): Unit = {
    leftMotor.setTargetVelocity(speed)
    rightMotor.setTargetVelocity(speed)
  }

  def stop(): Unit = {
    leftMotor.setTargetVelocity(0)
    rightMotor.setTargetVelocity(0)
  }

  def sensors: (Boolean, Boolean) =
    (leftSensor.read.detectionState, rightSensor.read.detectionState)
}

