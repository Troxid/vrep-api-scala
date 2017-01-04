
import viewer.Viewer
import vrepapiscala.{OpMode, VRepAPI}
import vrepapiscala.common.{AxisMatrix, EulerAngles}

import scala.util.{Failure, Success}

// load TestAllComponentsScene.ttt scene in v-rep

object TestAllComponents extends App {
  val viewer = new Viewer()
  val vrep = VRepAPI.connect("127.0.0.1", 19997).get

  vrep.simulation.stop()
  Thread.sleep(100)
  vrep.simulation.start()

  val jointVel = vrep.joint.withVelocityControl("joint_velocity").get
  val jointPos = vrep.joint.withPositionControl("joint_position").get
  val jointPas = vrep.joint.passive("joint_passive").get
  val jointSph = vrep.joint.spherical("sp_joint").get
  val jointSpr = vrep.joint.spring("joint_spring").get

  // example of getting component with error handling
  val sensor = vrep.sensor.proximity("sensor") match {
    case Failure(exception) => throw new Exception("not found")
    case Success(value) => value
  }

  // example of getting component with specific mode
  val vision = vrep.sensor.vision("vision", 256, 256, OpMode.Buffer).get

  val gps = vrep.sensor.position("gps").get

  jointVel.setTargetVelocity(2)
  jointSpr.setTargetPosition(2)

  for(i <- 0 to 50){

    // spherical joint
    val s = (Math.sin(i / 50.0 * 10) * i / 50.0f).toFloat
    jointSph.setMatrix(AxisMatrix(
      xAxis = EulerAngles(0, 0, s),
      yAxis = EulerAngles(0, 0, 0),
      zAxis = EulerAngles(0, 0, 0))
    )

    // joint with position control
    val b = (Math.PI / 9.0).toFloat
    jointPos.setTargetPosition(b * i + 0.2f)

    // passive joint
    val v = Math.sin(i / 50.0).toFloat
    jointPas.setPosition(v)

    // viewer
    viewer.setPositions(
      jointPos.position,
      jointPas.position
    )
    viewer.setCameraImage(vision.colorImg)
    viewer.setSensorValue(sensor.read.detectedPoint.length)
    viewer.setGps(
      gps.orientation.alpha,
      gps.orientation.beta,
      gps.orientation.gamma)
    Thread.sleep(100)
  }

  vrep.simulation.stop()
  System.exit(0)
}

