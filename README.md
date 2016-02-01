# v-rep scala
Simple Scala binding for
[Coppelia Robotics V-REP simulator](http://www.coppeliarobotics.com/) ([remote API](http://www.coppeliarobotics.com/helpFiles/en/remoteApiOverview.htm))

## Getting started
1. Add the library in your sbt project by simply adding the following dependency to your build file:
```scala
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "com.github.troxid" %% "vrepapiscala" % "0.1-SNAPSHOT"
```
2. Copy platform-specific native library from
    `V-REP/programming/remoteApiBindings/java/lib/`
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


