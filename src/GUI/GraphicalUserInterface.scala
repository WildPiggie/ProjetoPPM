package GUI

import QTrees.IO_Utils
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage



class GraphicalUserInterface extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Album")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerGridView.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()

    val itemController = fxmlLoader.getController[Controller]
    itemController.updateGrid()
  }

  override def stop(): Unit = {
    IO_Utils.writeToFile(FxApp.fileName, FxApp.container)
  }
}

  object FxApp {
    var currentImagePath:String =_
    val fileName = "Album.soa"
    var container: Container = IO_Utils.readFromFile(fileName)
    def main(args: Array[String]): Unit = {
      Application.launch(classOf[GraphicalUserInterface], args: _*)
    }

}
