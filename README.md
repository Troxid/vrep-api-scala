# v-rep scala
Simple Scala binding for
[Coppelia Robotics V-REP simulator](http://www.coppeliarobotics.com/) ([remote API](http://www.coppeliarobotics.com/helpFiles/en/remoteApiOverview.htm))

## Getting started
1. Add the library to the project one of several ways:
    1. Assembly jar by `sbt assembly`
    (jar is located in `vrepapi/target/scala-2.11/vrepapiscala-assembly-x.x.jar`)
    2. Download pre-compiled jar
    3. Add src files in your project
2. Copy platform-specific native library from
    `V-REP/programming/remoteApiBindings/lib/lib/`
    into the root folder of your project (or configure `java.library.path`)
3. Find the socket port number in `V-REP/remoteApiConnections.txt` and use
    it to connect to the server simulator

## Currently implemented things
In the current version is not implemented features such as remote management GUI,
additional configuration properties of objects and shapes, etc.
Basically implemented those components that are required to control the robot:
* Joint
* Proximity sensor
* Vision sensor
* Force sensor
* Navigation sensor (used for that dummy object)

## Example
```scala
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
```


