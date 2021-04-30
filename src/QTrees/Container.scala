package QTrees

import scala.annotation.tailrec
import scala.reflect.io.Path

case class Container(name:String, data : List[(String, String)]){

}

object Container {

  def add(path: String, info: String)(container: Container): Container = {
    if(path.contains(":") || info.contains(":")) {
      throw new IllegalArgumentException("Invalid character: ':'")
    }
    if(info.size > 64)
      throw  new IllegalArgumentException("Character limit exceeded.")
    val index = container.data.indexWhere(x => x._1 == path)
    if(index != -1)
      throw new IllegalArgumentException("There already exists an image with this path associated.")
    new Container(container.name, container.data :+ (path, info))

  }

  def remove(path: String)(container: Container): Container = {
    val index = container.data.indexWhere(x => x._1 == path)
    if(index == -1)
      throw new IllegalArgumentException("There doesn't exist an image with this path associated.")
    val lst = container.data.filter(x => x._1 != path)
    new Container(container.name, lst)

  }

  def next(path: String)(container: Container): (Container, (String, String)) = {
    if(container.data == Nil)
      throw new IllegalArgumentException("Album is empty.")
    val index = container.data.indexWhere(x => x._1 == path) // problema se houver paths repetidos
    if(index == container.data.size-1)
      (container, container.data(0))
    (container, container.data(index+1))
  }

  def previous(path: String)(container: Container): (Container, (String,String)) = {
    if(container.data == Nil)
      throw new IllegalArgumentException("Album is empty.")
    val index = container.data.indexWhere(x => x._1 == path)
    if(index == 0)
      (container, container.data(container.data.size-1))
    (container, container.data(index-1))
  }

  def switch(path1: String, path2:String)(container: Container) : Container = {
    val index1 = container.data.indexWhere(x => x._1 == path1)
    val index2 = container.data.indexWhere(x => x._1 == path2)
    new Container(container.name, container.data.updated(index1,container.data(index2)).updated(index2,container.data(index1)))
  }

  def editInfo(path: String, newInfo: String)(container: Container): Container = {
    if(newInfo.size > 64)
      throw  new IllegalArgumentException("Character limit exceeded.")
    val index = container.data.indexWhere(x => x._1 == path)
    if(index == -1)
      throw new IllegalArgumentException("There doesn't exist an image with this path associated.")
    new Container(container.name, container.data.updated(index,(path, newInfo)))
  }




}