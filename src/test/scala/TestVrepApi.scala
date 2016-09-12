
import org.scalatest._
import vrepapiscala.VRepAPI
/**
  * Created by troxid on 12.09.16.
  */
class TestVrepApi extends FunSuite with Matchers with BeforeAndAfter {

  final val IpAddress = "127.0.0.1"
  final val Port = 19997
  val vrep = VRepAPI.connect(IpAddress, Port).get

  before{
    vrep.simulation.start()
  }

  test("joint with velocity control") {
    val joint = vrep.joint.withVelocityControl("joint_vc").get
    joint.setTargetVelocity(2f)
    sleep(5000)
  }

  test("joint with position control") {
    val targetPosition = Math.PI.toFloat / 2.0f
    val joint = vrep.joint.withPositionControl("joint_pc").get
    joint.setTargetPosition(targetPosition)
    sleep(5000)
  }

  after{
    vrep.simulation.stop()
  }

  def sleep(ms: Long) = Thread.sleep(ms)

}
