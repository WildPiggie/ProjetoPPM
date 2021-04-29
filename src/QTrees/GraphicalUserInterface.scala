package QTrees

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import scala.io.Source


class GraphicalUserInterface extends Application{
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("First Window")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("Controller.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

object FxApp {
  var container = IO_Utils.readFromFile("Album.soa")
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[GraphicalUserInterface], args: _*)
  }
}
