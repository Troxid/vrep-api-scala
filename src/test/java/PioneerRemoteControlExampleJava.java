
import vrepapiscala.VRepAPI;
import vrepapiscala.joints.JointWithVelocityControl;
import vrepapiscala.sensors.ProximitySensor;


// load PioneerRemoteControlExampleScene.ttt scene in v-rep

public class PioneerRemoteControlExampleJava {
    public static void main(String[] args) throws Exception {
        VRepAPI api = VRepAPI.connect("127.0.0.1", 19997).get();

        PioneerP3dx robot = new PioneerP3dx(api);

        api.simulation().start();

        for(int i=0; i <= 500; i+=1){
            float leftDistance = robot.leftSensor().detectedPoint().length();
            float rightDistance = robot.rightSensor().detectedPoint().length();

            if(leftDistance < 0.5){
                robot.rotateRight();
            }else if(rightDistance < 0.5){
                robot.rotateLeft();
            }else if(leftDistance < 0.5 && rightDistance < 0.5){
                robot.moveBackward();
            }else{
                robot.moveForward();
            }
            Thread.sleep(10);
        }

        api.simulation().stop();
    }


    static class PioneerP3dx{
        final float speed = 2f;
        VRepAPI api;
        JointWithVelocityControl leftMotor;
        JointWithVelocityControl rightMotor;
        ProximitySensor[] sensors;

        public PioneerP3dx(VRepAPI api) {
            this.api = api;
            this.leftMotor = api.joint().withVelocityControl("Pioneer_p3dx_leftMotor").get();
            this.rightMotor= api.joint().withVelocityControl("Pioneer_p3dx_rightMotor").get();
            for(int i=1; i <= 8; i+=1){
                this.sensors[i] = api.sensor().proximity("Pioneer_p3dx_ultrasonicSensor" + i).get();
            }
        }

        public void moveForward() {
            leftMotor.setTargetVelocity(speed);
            rightMotor.setTargetVelocity(speed);
        }

        public void moveBackward(){
            leftMotor.setTargetVelocity(-speed);
            rightMotor.setTargetVelocity(-speed);
        }

        public void rotateLeft(){
            leftMotor.setTargetVelocity(-speed);
            rightMotor.setTargetVelocity(speed);
        }

        public void rotateRight(){
            leftMotor.setTargetVelocity(speed);
            rightMotor.setTargetVelocity(-speed);
        }

        public ProximitySensor.Values leftSensor() {
            return sensors[1].read();
        }

        public ProximitySensor.Values rightSensor(){
            return sensors[6].read();
        }
    }
}
