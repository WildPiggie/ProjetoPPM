package QTrees

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, Menu, MenuBar, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.GridPane
import javafx.stage.{Modality, Stage}

import java.io.FileInputStream
import scala.reflect.io.Path


//import scala.reflect.io.File


class Controller { // extends Initializable
  @FXML
  private var gridPane: GridPane =_
  @FXML
  private var buttonImageView: Button=_
  @FXML
  private var buttonGrid: Button=_
  @FXML
  private var imageView: ImageView=_
  @FXML
  private var buttonAdd: Button=_
  @FXML
  private var buttonAddImage: Button=_




  /*def onButtonClicked() = {
    val image:Image = new Image(new FileInputStream("img//a.png"))
    imageView2.setImage(image)
   }*/



  def onButtonAddImageClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Add Image")
    val fxmlLoader = new FXMLLoader(getClass.getResource("ControllerAddNewImage.fxml"))

    val secondViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(secondViewRoot)

    secondStage.initModality(Modality.APPLICATION_MODAL)
    secondStage.initOwner(buttonAddImage.getScene.getWindow)

    secondStage.setScene(scene)
    secondStage.show()
  }

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

  def onButtonImageViewClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Image View")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerImageView.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()

    buttonImageView.getScene.setRoot(imageViewRoot)

  }

  def onButtonGridClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Grid")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerGrid.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()

    buttonGrid.getScene.setRoot(imageViewRoot)

    // fazer o start() para dar upload as fotos ?
  }

  def onButtonNextClicked() = ???

  def onButtonPreviousClicked() = ???

  def setData(path: String) = {
    imageView.setImage(new Image(new FileInputStream(path)))
  }

  def onImageClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Image View")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerImageView.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()

    imageView.getScene.setRoot(imageViewRoot)
  }

  def onButtonAddClicked() = {
    // dar update no Container e na Grid
    buttonAdd.getScene.getWindow.hide()
  }












  //override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = ???


}
