package QTrees

import javafx.application.Application
import javafx.fxml.{FXML, FXMLLoader}
import javafx.geometry.Insets
import javafx.scene.{Node, Parent, Scene}
import javafx.scene.control.{Button, Menu, MenuBar, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{AnchorPane, GridPane}
import javafx.stage.{Modality, Stage}

import java.io.{FileInputStream, IOException}
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
  private var imageViewGrid: ImageView=_
  @FXML
  private var buttonAdd: Button=_
  @FXML
  private var buttonAddImage: Button=_

  @FXML
  private var imageView: ImageView=_




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

    val itemController = fxmlLoader.getController[Controller]
    itemController.insertImageOnImageView(FxApp.container.data(0)._1)
  }

  def onButtonGridClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Grid")
    val fxmlLoader= new FXMLLoader(getClass.getResource("ControllerGrid.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()
    buttonGrid.getScene.setRoot(imageViewRoot)

    val itemController = fxmlLoader.getController[Controller]
    itemController.updateGrid()
  }

  def onButtonNextClicked() = ???

  def onButtonPreviousClicked() = ???



  def onImageClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Image View")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerImageView.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()
    imageViewGrid.getScene.setRoot(imageViewRoot)

    // meter algo
  }

  def onButtonAddClicked() = {
    // dar update no Container e na Grid
    buttonAdd.getScene.getWindow.hide()
  }

  def insertImageOnImageView(path: String): Unit = {
    imageView.setImage(new Image(path, true))
  }


  def updateGrid() : Unit = {
    var (column, row) = (0,0)
    for(element <- FxApp.container.data){
      val fxmlLoader = new FXMLLoader(getClass.getResource("ControllerElementImage.fxml"))
      val anchorPane = fxmlLoader.load[AnchorPane]()
      val itemController = fxmlLoader.getController[Controller]
      //itemController.imageViewGrid.setImage(new Image(new FileInputStream(element._1)))
      itemController.imageViewGrid.setImage(new Image(element._1, true))
      if(column == 2){
        column = 0
        row += 1
      }
      //val b:Node = scene.lookup("#gridPane")
      //val gridPane:GridPane =  b.asInstanceOf[GridPane]

      this.gridPane.add(anchorPane,column, row)
      column += 1
      GridPane.setMargin(anchorPane, new Insets(10))
    }

  }


  /**
   * Turns the URL into the relative path used on Container
   * @param url
   * @return
   */
  def urlToRelativePath(url: String) : String = {
    val tokens = url.split("img")
    ("img"+ tokens(1))
  }

}
