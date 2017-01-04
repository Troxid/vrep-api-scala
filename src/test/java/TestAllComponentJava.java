
import scala.util.Try;
import viewer.Viewer;
import vrepapiscala.*;
import vrepapiscala.common.AxisMatrix;
import vrepapiscala.common.EulerAngles;
import vrepapiscala.joints.*;
import vrepapiscala.sensors.PositionSensor;
import vrepapiscala.sensors.ProximitySensor;
import vrepapiscala.sensors.VisionSensor;

// load TestAllComponentsScene.ttt scene in v-rep

public class TestAllComponentJava {
    public static void main(String[] args) throws Exception {
        Viewer viewer = new Viewer();
        VRepAPI vrep = VRepAPI.connect("127.0.0.1", 19997).get();

        vrep.simulation().start();

        JointWithVelocityControl vel = vrep.joint().withVelocityControl("joint_velocity").get();
        JointWithPositionControl pos = vrep.joint().withPositionControl("joint_position").get();
        PassiveJoint   pas = vrep.joint().passive("joint_passive").get();
        SphericalJoint sph = vrep.joint().spherical("sp_joint").get();
        SpringJoint    spr = vrep.joint().spring("joint_spring").get();

        // example of getting component with error handling
        ProximitySensor sensor;
        Try<ProximitySensor> mayBeSensor = vrep.sensor().proximity("sensor");
        if(mayBeSensor.isSuccess()){
            sensor = mayBeSensor.get();
        }else{
            throw new Exception("Not found");
        }

        // example of getting component with specific mode
        VisionSensor vision = vrep.sensor().vision("vision", 256, 256, OpMode.Buffer$.MODULE$).get();

        PositionSensor gps = vrep.sensor().position("gps").get();

        vel.setTargetVelocity(2);
        spr.setTargetPosition((float)Math.PI / 2.0f);

        for(int i=0; i <= 50; i += 1){
            // spherical joint
            float s = (float) Math.sin(i / 50.0 * 10) * i / 50.0f;
            sph.setMatrix(new AxisMatrix(
                    new EulerAngles(0, 0, s),
                    new EulerAngles(0, 0, 0),
                    new EulerAngles(0, 0, 0)
            ));
            // or
            sph.setRawMatrix(new float[]{
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    s, 0, 0, 0
            });

            // joint with position control
            float b = (float) Math.PI / 9f;
            pos.setTargetPosition(b);

            // passive joint
            float v = (float)Math.sin(i / 50.0);
            pas.setPosition(v);

            // viewer
            viewer.setPositions(
                   pos.position(),
                   pas.position()
            );
            viewer.setCameraImage(vision.colorImg());
            viewer.setSensorValue(sensor.read().detectedPoint().length());
            viewer.setGps(
                    gps.orientation().alpha(),
                    gps.orientation().beta(),
                    gps.orientation().gamma()
            );

            Thread.sleep(100);
            System.out.println(pos.position());
        }

        vrep.simulation().stop();
        System.exit(0);
    }

}
