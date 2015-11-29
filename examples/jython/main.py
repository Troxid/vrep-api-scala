from vrepapiscala import VRepAPI


class Robot:

    def __init__(self, api):
        self._api = api
        self._speed = 2
        self._left_motor = api.getJoint('left_motor').get()
        self._right_motor = api.getJoint('right_motor').get()
        self._left_sensor = api.getProximitySensor('left_sonar').get()
        self._right_sensor = api.getProximitySensor('right_sonar').get()
        self.gps = api.getNavigationSensor('gps').get()

    def move_forward(self):
        self._left_motor.setTargetVelocity(self._speed)
        self._right_motor.setTargetVelocity(-self._speed)

    def move_backward(self):
        self._left_motor.setTargetVelocity(-self._speed)
        self._right_motor.setTargetVelocity(self._speed)

    def rotate_left(self):
        self._left_motor.setTargetVelocity(-self._speed)
        self._right_motor.setTargetVelocity(-self._speed)

    def rotate_right(self):
        self._left_motor.setTargetVelocity(self._speed)
        self._right_motor.setTargetVelocity(self._speed)

    def stop(self):
        self._left_motor.setTargetVelocity(0)
        self._right_motor.setTargetVelocity(0)

    def sensors(self):
        left_sensor_result = self._left_sensor.read().detectionState()
        right_sensor_result = self._right_sensor.read().detectionState()
        return left_sensor_result, right_sensor_result


api = VRepAPI.connect("127.0.0.1", 19997).get()
api.startSimulation()
r = Robot(api)

while True:
    if r.sensors()[0]:
        r.rotate_right()
    elif r.sensors()[1]:
        r.rotate_left()
    else:
        r.move_forward()
