package GUI

import QTrees.QuadTree.{Coords, contrastEffect, noiseEffectWithState, sepiaEffect}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.geometry.Insets
import javafx.scene.{Node, Parent}
import javafx.scene.control.{Label, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{AnchorPane, GridPane}
import javafx.stage.{Modality, Stage}

import java.io.{File, FileInputStream}
import javafx.stage.FileChooser

import scala.annotation.tailrec
import QTrees.{BitMap, QTree, QuadTree}
import javafx.embed.swing.JFXPanel

import javax.swing.JOptionPane


class Controller {

  @FXML
  private var gridPane: GridPane =_
  @FXML
  private var buttonImageView: ImageView=_
  @FXML
  private var buttonGrid: ImageView=_
  @FXML
  private var imageViewGrid: ImageView=_
  @FXML
  private var buttonAddImageGV: ImageView=_
  @FXML
  private var buttonAddImageIV: ImageView=_
  @FXML
  private var buttonSwitchOrder: ImageView=_
  @FXML
  private var imageView: ImageView=_
  @FXML
  private var labelImageNameGV: Label=_
  @FXML
  private var labelImageNameIV: Label=_
  @FXML
  private var textFieldInfo: TextField=_
  @FXML
  private var textFieldImage1: TextField=_
  @FXML
  private var textFieldImage2: TextField=_
  @FXML
  private var textFieldScaleValue: TextField=_
  @FXML
  private var anchorPaneImageView: AnchorPane=_


  def onButtonAddImageClickedGV(): Unit = addImage(buttonAddImageGV, updateGrid())

  def onButtonAddImageClickedIV(): Unit = addImage(buttonAddImageIV, updateImageView())

  /**
   * Adds the Image to the container and sets it to be the current image.
   * @param widget a widget in the current scene
   * @param f function to execute after the image has been added to the container
   */
  def addImage( widget: Node, f: => Unit): Unit = {
    val stage: Stage = new Stage()
    stage.initOwner(widget.getScene.getWindow)
    stage.initModality(Modality.WINDOW_MODAL)

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

  /**
   * Refreshes the Image View.
   */
  def updateImageView (): Unit = {
    imageView.setImage(new Image(new FileInputStream(FxApp.currentImagePath)))
    labelImageNameIV.setText(getImageNameFromPath(FxApp.currentImagePath))
    textFieldInfo.setText(FxApp.container.getInfo(FxApp.currentImagePath))
  }

  /**
   * Removes the current image from the container and updates the view accordingly.
   */
  def onButtonRemoveClicked(): Unit = {
    val path = FxApp.currentImagePath
    if(FxApp.container.data.size > 1) {
      imageTransition(FxApp.container.next)
      FxApp.container = FxApp.container.remove(path)
    }
    else {
      FxApp.currentImagePath = ""
      FxApp.container = FxApp.container.remove(path)
      onButtonGridClicked()
    }
  }

  /**
   * Switches the order of the two images specified in the respective text fields and updates the GridView.
   */
  def onButtonSwitchClicked(): Unit = {
    if(textFieldImage1.getText.nonEmpty && textFieldImage2.getText.nonEmpty) {
      try{
        val path1 = FxApp.container.getAbsolutePath(textFieldImage1.getText)
        val path2 = FxApp.container.getAbsolutePath(textFieldImage2.getText)
        FxApp.container = FxApp.container.switch(path1, path2)

        val secondStage: Stage = new Stage()
        secondStage.setTitle("Grid")
        val fxmlLoader= new FXMLLoader(getClass.getResource("ControllerGridView.fxml"))

        val imageViewRoot: Parent = fxmlLoader.load()
        buttonSwitchOrder.getScene.setRoot(imageViewRoot)

        val itemController = fxmlLoader.getController[Controller]
        itemController.updateGrid()

      } catch {
        case e: IllegalArgumentException => popUpErrorMessage(e.getMessage)
      }
    }
  }

  /**
   * Updates the info of a given image when the user enters new info into the text field and presses enter.
   */
  def onInfoTyped(): Unit = {
    if(imageView.getImage != null) {
      val path = FxApp.currentImagePath
      val newInfo = textFieldInfo.getText
      FxApp.container = FxApp.container.editInfo(path,newInfo)
    }
  }

  //The next functions apply an effect to an image and display it.
  def onButtonScaleClicked(): Unit = {
    if(imageView.getImage != null && textFieldScaleValue.getText.nonEmpty) {
      try{
        val path = FxApp.currentImagePath
        val scaleValue = textFieldScaleValue.getText.toFloat
        val bitmap = QuadTree(QuadTree(BitMap.makeQTree(path)).scale(scaleValue)).makeBitMap()
        bitmap.bitmapWriteImage(path)
        imageView.setImage(new Image(new FileInputStream(path)))
      }
      catch {
        case e: IllegalArgumentException => popUpErrorMessage(e.getMessage)
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

  /**
   * Funtion responsible for applying a give effect f to an image and displaying it.
   * @param f function (QTree[Coords] => QTree[Coords]) to be applied to the image
   */
  def applyEffects(f:QTree[Coords] => QTree[Coords]): Unit = {
    if(imageView.getImage != null) {
      val path = FxApp.currentImagePath
      val bitmap = QuadTree(f(BitMap.makeQTree(path))).makeBitMap()
      bitmap.bitmapWriteImage(path)
      imageView.setImage(new Image(new FileInputStream(path)))
    }
  }

  def onButtonImageViewClicked(): Unit = {
    if(FxApp.container.data.isEmpty)
      popUpErrorMessage("There are no images available in your album to view.")
    else
      showImageView(0, buttonImageView)
  }

  /**
   * Shows the Image View, requires an index of the image to display and a widget from the current scene.
   * @param index index of the image to be displayed
   * @param widget widget in the current scene
   */
  def showImageView(index: Int, widget: Node): Unit ={
    val secondStage: Stage = new Stage()
    secondStage.setTitle("Image View")
    val fxmlLoader = new FXMLLoader(getClass.getResource("ControllerImageView.fxml"))
    val imageViewRoot: Parent = fxmlLoader.load()
    widget.getScene.setRoot(imageViewRoot)

    val path = FxApp.container.data(index)._1
    val itemController = fxmlLoader.getController[Controller]
    itemController.imageView.setImage(new Image(new FileInputStream(path)))
    itemController.labelImageNameIV.setText(getImageNameFromPath(path))
    itemController.textFieldInfo.setText(FxApp.container.data(index)._2)
    FxApp.currentImagePath = path
  }

  /**
   * Shows the Grid View
   */
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

  /**
   * Displays the next/previous image depending on the f function supplied.
   * @param f transition function (String=>(String, String))
   */
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
    showImageView(3*GridPane.getRowIndex(anchorPaneImageView) + GridPane.getColumnIndex(anchorPaneImageView), imageViewGrid)

  /**
   * Refreshes the Grid View.
   */
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
        itemController.labelImageNameGV.setText(getImageNameFromPath(element._1))
        if(column == 3){
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

  def getImageNameFromPath(path: String): String = {
    val file = new File(path)
    //necessary due to an error when using the '\' character
    file.getName
  }

  def popUpErrorMessage(msg: String): Unit = {
    JOptionPane.showMessageDialog(new JFXPanel(), msg, "Error", JOptionPane.ERROR_MESSAGE)
  }

}
