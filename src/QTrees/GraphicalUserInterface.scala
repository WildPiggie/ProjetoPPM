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

    var column = 0
    var row = 0

    try{
      //var element = 0
      for(element <- FxApp.container.data){
        //val fxmlloader = new FXMLLoader()
        //fxmlloader.setLocation(getClass.getResource("ControllerElementImage.fxml"))
        val fxmlLoader =
        new FXMLLoader(getClass.getResource("ControllerElementImage.fxml"))
        val anchorPane = fxmlLoader.load[AnchorPane]()
        val itemController = fxmlLoader.getController[Controller]
        itemController.setData(element._1)

        if(column == 2){
          column = 0
          row += 1
          println(row)
        }
        val b:Node = scene2.lookup("#gridPane")
        val gridPane:GridPane =  b.asInstanceOf[GridPane]

        gridPane.add(anchorPane,column, row)
        column += 1


       /* gridPane.setMinWidth(Region.USE_COMPUTED_SIZE)
        gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE)
        gridPane.setMaxWidth(Region.USE_COMPUTED_SIZE)
        gridPane.setMinHeight(Region.USE_COMPUTED_SIZE)
        gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE)
        gridPane.setMaxHeight(Region.USE_COMPUTED_SIZE)

        */

        GridPane.setMargin(anchorPane, new Insets(10))



      }
    } catch {
      case e: IOException => e.printStackTrace
    }

    /*
    val b:Node = scene.lookup("#gridPane")
    val gridPane:GridPane =  b.asInstanceOf[GridPane]
    for(element <- FxApp.container.data) {
      //val imageView1:ImageView = b.asInstanceOf[ImageView]
      gridPane.add(new ImageView(new Image(new FileInputStream(element._1))),0,0)
      //imageView1.setImage(new Image(new FileInputStream(element._1)))
    }
     */

  }

  /*override def init(): Unit = {
    println("comecei no init")
    var column = 0
    var row = 0

    try{
      //var element = 0
      for(element <- FxApp.container.data){
        //val fxmlloader = new FXMLLoader()
        //fxmlloader.setLocation(getClass.getResource("ControllerElementImage.fxml"))
        val fxmlLoader =
        new FXMLLoader(getClass.getResource("ControllerElementImage.fxml"))
        val anchorPane = fxmlLoader.load[AnchorPane]()
        val itemController = fxmlLoader.getController[Controller]
        itemController.setData(element._1)

        if(column == 2){
          column = 0
          row += 1
        }
        val b:Node = scene2.lookup("#gridPane")
        val gridPane:GridPane =  b.asInstanceOf[GridPane]

        gridPane.add(anchorPane,column, row)
        column += 1


        gridPane.setMinWidth(Region.USE_COMPUTED_SIZE)
        gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE)
        gridPane.setMaxWidth(Region.USE_PREF_SIZE)
        gridPane.setMinHeight(Region.USE_COMPUTED_SIZE)
        gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE)
        gridPane.setMaxHeight(Region.USE_PREF_SIZE)

        GridPane.setMargin(anchorPane, new Insets(10))



      }
    } catch {
      case e: IOException => e.printStackTrace
    }

  }

   */

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
    //IO_Utils.writeToFile(fileName, container)


}
