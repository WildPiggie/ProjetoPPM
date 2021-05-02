package QTrees

import QTrees.QuadTree.{Coords, contrastEffect, noiseEffectWithState, sepiaEffect}
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


class Controller {

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
  @FXML
  private var textFieldPathPopUp: TextField=_
  @FXML
  private var textFieldInfoPopUp: TextField=_
  @FXML
  private var textFieldInfo: TextField=_
  @FXML
  private var textFieldScaleValue: TextField=_


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

  def onButtonRemoveClicked() = {
    if(imageView.getImage != null) {
      val path = urlToRelativePath(imageView.getImage.getUrl)
      try{
        val (nextPath,nextInfo) = FxApp.container.next(path)
        FxApp.container = FxApp.container.remove(path)
        imageView.setImage(new Image(urlToRelativePath(nextPath),true))
        textFieldInfo.setText(nextInfo)
      }
      catch {
        case e: IllegalArgumentException => imageView.setImage(null)
      }
    }
  }

  def onButtonSwitchClicked() = ???

  def onInfoTyped() = {
    if(imageView.getImage != null) {
      val path = urlToRelativePath(imageView.getImage.getUrl)
      val newInfo = textFieldInfo.getText
      FxApp.container = FxApp.container.editInfo(path,newInfo)
    }
  }

  def onButtonScaleClicked() = {
    if(imageView.getImage != null && !textFieldScaleValue.getText.isEmpty) {
      val path = urlToRelativePath(imageView.getImage.getUrl)
      val srcPath = srcRelativePath(path)
      val scaleValue = textFieldScaleValue.getText.toFloat
      if(scaleValue > 0) {
        val image = QuadTree(QuadTree(BitMap.makeQTree(srcPath)).scale(scaleValue)).makeBitMap()
        image.toImage(srcPath,srcPath.split('.')(1))
        imageView.setImage(new Image(path,true))

      }
    }
  }

  def applyEffects(f:QTree[Coords] => QTree[Coords]) = {
    if(imageView.getImage != null) {
      val path = urlToRelativePath(imageView.getImage.getUrl)
      val srcPath = srcRelativePath(path)
      val image = QuadTree(f(BitMap.makeQTree(srcPath))).makeBitMap()
      image.toImage(srcPath,srcPath.split('.')(1))
      imageView.setImage(null)
      imageView.setImage(new Image(path,true))
    }
  }

  def onButtonMirrorHClicked() = applyEffects(QuadTree.mirrorH)

  def onButtonMirrorVClicked() = applyEffects(QuadTree.mirrorV)

  def onButtonRotateRClicked() = applyEffects(QuadTree.rotateR)

  def onButtonRotateLClicked() = applyEffects(QuadTree.rotateL)

  def onButtonSepiaClicked() = applyEffects(QuadTree.mapColourEffect(sepiaEffect))

  def onButtonContrastClicked() = applyEffects(QuadTree.mapColourEffect(contrastEffect))

  def onButtonNoiseClicked() = applyEffects(QuadTree.mapColourEffectWithState(noiseEffectWithState))

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

  def onButtonNextClicked() = {
    if(imageView.getImage != null) {
      val path = urlToRelativePath(imageView.getImage.getUrl)
      try{
        val (nextPath,nextInfo) = FxApp.container.next(path)
        imageView.setImage(new Image(urlToRelativePath(nextPath),true))
        textFieldInfo.setText(nextInfo)
      }
      catch {
        case e: IllegalArgumentException => imageView.setImage(null)
      }
    }
  }

  def onButtonPreviousClicked() = {
    if(imageView.getImage != null){
      val path = urlToRelativePath(imageView.getImage.getUrl)
      try{
        val (nextPath,nextInfo) = FxApp.container.previous(path)
        imageView.setImage(new Image(urlToRelativePath(nextPath),true))
        textFieldInfo.setText(nextInfo)
      }
      catch {
        case e: IllegalArgumentException => imageView.setImage(null)
      }
    }
  }



  def onImageClicked() = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Image View")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerImageView.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()
    imageViewGrid.getScene.setRoot(imageViewRoot)

    val itemController = fxmlLoader.getController[Controller]
    val path = urlToRelativePath(imageViewGrid.getImage.getUrl)
    itemController.imageView.setImage(new Image(path,true))
    itemController.textFieldInfo.setText(FxApp.container.getInfo(path))
  }

  def onButtonAddPopUpClicked() = { //falta dar update na grid
    val path = textFieldPathPopUp.getText
    val info = textFieldInfoPopUp.getText
    FxApp.container =  FxApp.container.add(path, info)
    buttonAdd.getScene.getWindow.hide()

    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerImageView.fxml"))
    fxmlLoader.load()
    val itemController = fxmlLoader.getController[Controller]
    itemController.updateGrid()


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

  def srcRelativePath(path: String): String = {
    ("out//production/ProjetoPPM//" + path)
  }

}
