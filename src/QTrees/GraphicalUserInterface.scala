package QTrees

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import scala.io.Source


class GraphicalUserInterface extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Album") //meter aqui o nome do album ?
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("ControllerGrid.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()

  }
}

  /*
  override def init(): Unit = {
    //Controller.
  }

  override def stop(): Unit = super.stop()



   */
  object FxApp {
    //val fileName = "Album.soa"
    //var container = IO_Utils.readFromFile(fileName)
    def main(args: Array[String]): Unit = {
      Application.launch(classOf[GraphicalUserInterface], args: _*)
    }
    //IO_Utils.writeToFile(fileName, container)

}
