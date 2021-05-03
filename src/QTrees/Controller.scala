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
import jdk.internal.org.jline.reader.Widget

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

  @FXML
  private var anchorPaneImageView: AnchorPane=_



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
    val path = FxApp.currentImagePath
    imageTransition(FxApp.container.next)
    FxApp.container = FxApp.container.remove(path)
  }

  def onButtonSwitchClicked() = ???

  def onInfoTyped() = {
    if(imageView.getImage != null) {
      val path = FxApp.currentImagePath
      val newInfo = textFieldInfo.getText
      FxApp.container = FxApp.container.editInfo(path,newInfo)
    }
  }

  def onButtonScaleClicked() = {
    if(imageView.getImage != null && !textFieldScaleValue.getText.isEmpty) {
      val path = FxApp.currentImagePath
      val scaleValue = textFieldScaleValue.getText.toFloat
      if(scaleValue > 0) {
        val image = QuadTree(QuadTree(BitMap.makeQTree(path)).scale(scaleValue)).makeBitMap()
        image.toImage(path,path.split('.')(1))
        imageView.setImage(new Image(new FileInputStream(path)))
      }
    }
  }

  def applyEffects(f:QTree[Coords] => QTree[Coords]) = {
    if(imageView.getImage != null) {
      val path = FxApp.currentImagePath
      println(path)
      val image = QuadTree(f(BitMap.makeQTree(path))).makeBitMap()
      image.toImage(path,path.split('.')(1))
      imageView.setImage(new Image(new FileInputStream(path)))
    }
  }

  def onButtonMirrorHClicked() = applyEffects(QuadTree.mirrorH)

  def onButtonMirrorVClicked() = applyEffects(QuadTree.mirrorV)

  def onButtonRotateRClicked() = applyEffects(QuadTree.rotateR)

  def onButtonRotateLClicked() = applyEffects(QuadTree.rotateL)

  def onButtonSepiaClicked() = applyEffects(QuadTree.mapColourEffect(sepiaEffect))

  def onButtonContrastClicked() = applyEffects(QuadTree.mapColourEffect(contrastEffect))

  def onButtonNoiseClicked() = applyEffects(QuadTree.mapColourEffectWithState(noiseEffectWithState))

  def onButtonImageViewClicked() = showImageView(0, buttonImageView)

  def showImageView(index: Int, widget: Node) ={
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Image View")
    val fxmlLoader = new FXMLLoader(getClass.getResource("ControllerImageView.fxml"))
    val imageViewRoot: Parent = fxmlLoader.load()
    widget.getScene.setRoot(imageViewRoot)

    val path = FxApp.container.data(index)._1
    val itemController = fxmlLoader.getController[Controller]
    itemController.imageView.setImage(new Image(new FileInputStream(path)))
    itemController.textFieldInfo.setText(FxApp.container.data(index)._2)
    FxApp.currentImagePath = path
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

  def onButtonNextClicked() = imageTransition(FxApp.container.next)

  def onButtonPreviousClicked() = imageTransition(FxApp.container.previous)

  def imageTransition(f: String => (String, String)) = {
    if(imageView.getImage != null){
      val path = FxApp.currentImagePath
      try{
        val (newPath,newInfo) = f(path)
        imageView.setImage(new Image(new FileInputStream(newPath)))
        textFieldInfo.setText(newInfo)
        FxApp.currentImagePath = newPath
      }
      catch {
        case e: IllegalArgumentException => imageView.setImage(null)
      }
    }
  }

  def onImageClicked() =
    showImageView(2*GridPane.getRowIndex(anchorPaneImageView) + GridPane.getColumnIndex(anchorPaneImageView), imageViewGrid)// número de colunas hardcoded

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

  def updateGrid() : Unit = {
    var (column, row) = (0,0)
    for(element <- FxApp.container.data){
      val fxmlLoader = new FXMLLoader(getClass.getResource("ControllerElementImage.fxml"))
      val anchorPane = fxmlLoader.load[AnchorPane]()
      val itemController = fxmlLoader.getController[Controller]
      itemController.imageViewGrid.setImage(new Image(new FileInputStream(element._1)))
      if(column == 2){
        column = 0
        row += 1
      }
      this.gridPane.add(anchorPane,column, row)
      column += 1
      GridPane.setMargin(anchorPane, new Insets(10))
    }
  }

}
