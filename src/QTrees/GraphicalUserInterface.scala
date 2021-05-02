package QTrees

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{AnchorPane, GridPane}
import javafx.scene.{Node, Parent, Scene}
import javafx.stage.Stage
import javafx.scene.layout.GridPane
import javafx.scene.layout.Region

import java.io.{FileInputStream, IOException}
import scala.io.Source


class GraphicalUserInterface extends Application {
  var scene2: Scene =_

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Album") //meter aqui o nome do album ?
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerGrid.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    scene2 = new Scene(mainViewRoot)
    primaryStage.setScene(scene2)
    primaryStage.show()

    val itemController = fxmlLoader.getController[Controller]
    itemController.updateGrid()

  }

  override def stop(): Unit = {
    IO_Utils.writeToFile(FxApp.fileName, FxApp.container)
  }

}


  object FxApp {
    val fileName = "Album.soa"
    var container = IO_Utils.readFromFile(fileName)
    def main(args: Array[String]): Unit = {
      Application.launch(classOf[GraphicalUserInterface], args: _*)
    }

}
