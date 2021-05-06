package QTrees

import QTrees.QuadTree.{Coords, contrastEffect, noiseEffectWithState, sepiaEffect}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.geometry.Insets
import javafx.scene.{Node, Parent}
import javafx.scene.control.{Button, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{AnchorPane, GridPane}
import javafx.stage.{Modality, Stage}

import java.io.FileInputStream
import javafx.stage.FileChooser

import scala.annotation.tailrec


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
  private var buttonAddImageGV: Button=_
  @FXML
  private var buttonAddImageIV: Button=_
  @FXML
  private var imageView: ImageView=_
  @FXML
  private var textFieldInfo: TextField=_
  @FXML
  private var textFieldScaleValue: TextField=_
  @FXML
  private var anchorPaneImageView: AnchorPane=_


  def onButtonAddImageClickedGV(): Unit = addImage(buttonAddImageGV, updateGrid())

  def onButtonAddImageClickedIV(): Unit = addImage(buttonAddImageIV, updateImageView())

  def addImage( widget: Node, f: => Unit): Unit = {
    val stage: Stage = new Stage()
    stage.initModality(Modality.APPLICATION_MODAL) // ver isto
    stage.initOwner(widget.getScene.getWindow)

    val fileChooser = new FileChooser
    fileChooser.setTitle("Open Resource File")
    val file = fileChooser.showOpenDialog(stage)

    if(file != null) {
      val path = file.toString
      FxApp.container = FxApp.container.add(path, "")
      FxApp.currentImagePath = path
      f
    }
  }

  def updateImageView (): Unit = {
    imageView.setImage(new Image(new FileInputStream(FxApp.currentImagePath)))
    textFieldInfo.setText(FxApp.container.getInfo(FxApp.currentImagePath))
  }

  def onButtonRemoveClicked(): Unit = {
    val path = FxApp.currentImagePath
    if(FxApp.container.data.size > 1)
      imageTransition(FxApp.container.next)
    else {
      FxApp.currentImagePath = ""
      imageView.setImage(null)
      textFieldInfo.setText("")
    }
    FxApp.container = FxApp.container.remove(path)
  }

  def onButtonSwitchClicked(): Unit = ???

  def onInfoTyped(): Unit = {
    if(imageView.getImage != null) {
      val path = FxApp.currentImagePath
      val newInfo = textFieldInfo.getText
      FxApp.container = FxApp.container.editInfo(path,newInfo)
    }
  }

  def onButtonScaleClicked(): Unit = {
    if(imageView.getImage != null && textFieldScaleValue.getText.nonEmpty) {
      val path = FxApp.currentImagePath
      val scaleValue = textFieldScaleValue.getText.toFloat
      if(scaleValue > 0) {
        val bitmap = QuadTree(QuadTree(BitMap.makeQTree(path)).scale(scaleValue)).makeBitMap()
        bitmap.bitmapWriteImage(path)
        imageView.setImage(new Image(new FileInputStream(path)))
      }
    }
  }

  def onButtonMirrorHClicked(): Unit = applyEffects(QuadTree.mirrorH)

  def onButtonMirrorVClicked(): Unit = applyEffects(QuadTree.mirrorV)

  def onButtonRotateRClicked(): Unit = applyEffects(QuadTree.rotateR)

  def onButtonRotateLClicked(): Unit = applyEffects(QuadTree.rotateL)

  def onButtonSepiaClicked(): Unit = applyEffects(QuadTree.mapColourEffect(sepiaEffect))

  def onButtonContrastClicked(): Unit = applyEffects(QuadTree.mapColourEffect(contrastEffect))

  def onButtonNoiseClicked(): Unit = applyEffects(QuadTree.mapColourEffectWithState(noiseEffectWithState))

  def applyEffects(f:QTree[Coords] => QTree[Coords]): Unit = {
    if(imageView.getImage != null) {
      val path = FxApp.currentImagePath
      println(path)
      val bitmap = QuadTree(f(BitMap.makeQTree(path))).makeBitMap()
      bitmap.bitmapWriteImage(path)
      imageView.setImage(new Image(new FileInputStream(path)))
    }
  }

  def onButtonImageViewClicked(): Unit = showImageView(0, buttonImageView)

  def showImageView(index: Int, widget: Node): Unit ={
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

  def onButtonGridClicked(): Unit = {
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Grid")
    val fxmlLoader= new FXMLLoader(getClass.getResource("ControllerGridView.fxml"))

    val imageViewRoot: Parent = fxmlLoader.load()
    buttonGrid.getScene.setRoot(imageViewRoot)

    val itemController = fxmlLoader.getController[Controller]
    itemController.updateGrid()
  }

  def onButtonNextClicked(): Unit = imageTransition(FxApp.container.next)

  def onButtonPreviousClicked(): Unit = imageTransition(FxApp.container.previous)

  def imageTransition(f: String => (String, String)): Unit = {
    if(imageView.getImage != null){
      try{
        val newPath = f(FxApp.currentImagePath)._1
        FxApp.currentImagePath = newPath
        updateImageView()
      } catch {
        case e: IllegalArgumentException => imageView.setImage(null)
      }
    }
  }

  def onImageClicked(): Unit =
    showImageView(2*GridPane.getRowIndex(anchorPaneImageView) + GridPane.getColumnIndex(anchorPaneImageView), imageViewGrid)// número de colunas hardcoded

  def updateGrid() : Unit = {
    var (column, row) = (0,0)
    auxUpdate(FxApp.container.data)

    @tailrec
    def auxUpdate(lst: List[(String, String)]): Unit = {
     lst match {
      case Nil => ()
      case element::xs =>
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
        auxUpdate(xs)
      }
    }
  }


  def onDragImageViewDetected(): Unit = {
    println("Detected: "+imageViewGrid.getImage)
  }

  def onDragImageViewDone(): Unit = {
    println("Done: "+imageViewGrid.getImage)
  }

}
