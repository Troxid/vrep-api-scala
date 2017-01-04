package viewer

import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.{BoxLayout, ImageIcon, JFrame, JLabel}

/**
  * Created by troxid on 03.01.17.
  */
class Viewer extends JFrame("viewer.Viewer v-rep") {
  private val image = new ImageIcon(new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB))
  private val labelImg = new JLabel(image)
  private val labelSensor = new JLabel("Sensor: ")
  private val labelPos = new JLabel("Position: ")
  private val labelGps = new JLabel("GPS: ")
  labelImg.setVisible(true)
  getContentPane.setLayout(new BoxLayout(getContentPane, BoxLayout.Y_AXIS))
  add(labelImg)
  add(labelSensor)
  add(labelPos)
  setSize(400, 600)
  setVisible(true)
  pack()
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  def setCameraImage(img: Image): Unit ={
    image.setImage(img)
    revalidate()
    repaint()
  }

  def setSensorValue(v: Float): Unit ={
    labelSensor.setText("Sensor: " + v)
  }

  def setPositions(v1: Float, v2: Float): Unit ={
    labelPos.setText(s"Positions: $v1, $v2")
  }

  def setGps(x: Float, y: Float, z: Float): Unit ={
    labelGps.setText(s"GPS: $x, $y, $z")
  }
}
