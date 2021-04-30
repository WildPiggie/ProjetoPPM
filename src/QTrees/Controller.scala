package QTrees

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, Menu, MenuBar}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.{Modality, Stage}

import java.io.FileInputStream


//import scala.reflect.io.File


class Controller { // extends Initializable
  @FXML
  private var menuBar: MenuBar =_

  @FXML
  private var menuAlbum: Menu =_
  @FXML
  private var menuEdit: Menu =_
  @FXML
  private var menuView: Menu =_
  @FXML
  private var imageView1: ImageView =_

  @FXML
  private var imageView2: ImageView =_
  @FXML
  private var imageView3: ImageView =_
  @FXML
  private var imageView4: ImageView =_
  @FXML
  private var imageView5: ImageView =_
  @FXML
  private var imageView6: ImageView =_
  @FXML
  private var gridPane: GridPane =_
  @FXML
  private var buttonImageView: Button=_
  @FXML
  private var buttonGrid: Button=_



 /*def onButtonClicked() = {
   val image:Image = new Image(new FileInputStream("img//a.png"))
   imageView2.setImage(image)
  }*/



  def onButtonAddClicked() = ???

  def onButtonRemoveClicked() = ???

  def onButtonSwitchClicked() = ???

  def onInfoTyped() = ???

  def onButtonScaleClicked() = ???

  def onButtonMirrorHClicked() = ???

  def onButtonMirrorVClicked() = ???

  def onButtonRotateRClicked() = ???

  def onButtonRotateLClicked() = ???

  def onButtonSepiaClicked() = ???

  def onButtonContrastClicked() = ???

  def onButtonNoiseClicked() = ???

  def onImageViewClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Image View")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerImageView.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()

    buttonImageView.getScene.setRoot(imageViewRoot)

  }

  def onGridClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Grid")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerGrid.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()

    buttonGrid.getScene.setRoot(imageViewRoot)
  }

  def onButtonNextClicked() = ???

  def onButtonPreviousClicked() = ???













  //override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = ???


}
