package GUI

case class Container(name:String, data : List[(String, String)]){
  def add(path:String, info:String): Container = Container.add(path,info)(this)
  def remove(path:String): Container =  Container.remove(path)(this)
  def next(path: String): (String, String) = Container.next(path)(this)
  def previous(path: String): (String, String) = Container.previous(path)(this)
  def switch(path1: String, path2: String): Container = Container.switch(path1, path2)(this)
  def editInfo(path:String,newInfo:String): Container = Container.editInfo(path, newInfo)(this)
  def getInfo(path: String): String = Container.getInfo(path)(this)
  def getAbsolutePath(name: String): String = Container.getAbsolutePath(name)(this)
}

object Container {

  /**
   * Adds a new element (path and info) to the list of the container.
   * @param path the path name of the image
   * @param info the information associated to the image
   * @param container the container
   * @return
   */
  def add(path: String, info: String)(container: Container): Container = {
    if(path.contains(">") || info.contains(">")) {
      throw new IllegalArgumentException("Invalid character: '>'")
    }
    if(info.length > 64)
      throw  new IllegalArgumentException("Character limit exceeded.")
    val index = container.data.indexWhere(x => x._1 == path)
    if(index != -1)
      throw new IllegalArgumentException("There already exists an image with this path associated.")
    new Container(container.name, container.data :+ (path, info))

  }

  /**
   * Removes an element from the list of the container given a path name.
   * @param path the path name of the image
   * @param container the container
   * @return
   */
  def remove(path: String)(container: Container): Container = {
    val index = container.data.indexWhere(x => x._1 == path)
    if(index == -1)
      throw new IllegalArgumentException("There doesn't exist an image with this path associated.")
    val lst = container.data.filter(x => x._1 != path)
    new Container(container.name, lst)

  }

  /**
   * Returns the next element of the list of the container given a path that represents the actual element of the list.
   * @param path the path name of the image
   * @param container the container
   * @return
   */
  def next(path: String)(container: Container): (String, String) = {
    if(container.data.isEmpty)
      throw new IllegalArgumentException("Album is empty.")
    val index = container.data.indexWhere(x => x._1 == path)
    if(index == container.data.size-1)
      return container.data.head
    container.data(index+1)
  }

  /**
   * Returns the previous element of the list of the container given a path that represents the actual element of the list.
   * @param path the path name of the image
   * @param container the container
   * @return
   */
  def previous(path: String)(container: Container): (String,String) = {
    if(container.data.isEmpty)
      throw new IllegalArgumentException("Album is empty.")
    val index = container.data.indexWhere(x => x._1 == path)
    if(index == 0)
      return container.data.last
    container.data(index-1)
  }

  /**
   * Returns a new container with 2 elements of the list of the container switched.
   * @param path1 the path name of the 1st image to switch
   * @param path2 the path name of the 2nd image to switch
   * @param container the container
   * @return
   */
  def switch(path1: String, path2:String)(container: Container) : Container = {
    val index1 = container.data.indexWhere(x => x._1 == path1)
    val index2 = container.data.indexWhere(x => x._1 == path2)
    new Container(container.name, container.data.updated(index1,container.data(index2)).updated(index2,container.data(index1)))
  }

  /**
   * Returns a new container with the information of one image updated.
   * @param path the path name of the image
   * @param newInfo the new information of the image
   * @param container the container
   * @return
   */
  def editInfo(path: String, newInfo: String)(container: Container): Container = {
    if(newInfo.length > 64)
      throw  new IllegalArgumentException("Character limit exceeded.")
    if(newInfo.contains(">"))
      throw  new IllegalArgumentException("Invalid character: '>'")
    val index = container.data.indexWhere(x => x._1 == path)
    if(index == -1)
      throw new IllegalArgumentException("There doesn't exist an image with this path associated.")
    new Container(container.name, container.data.updated(index,(path, newInfo)))
  }

  /**
   * Returns the info associated to the image given the path of that image.
   * @param path the path name of the image
   * @param container the container
   * @return
   */
  def getInfo(path: String)(container: Container) : String = {
    val index = container.data.indexWhere(x => x._1 == path)
    if(index == -1)
      throw new IllegalArgumentException("There doesn't exist an image with this path associated.")
    container.data(index)._2
  }

  /**
   * Returns the absolute path of an image inside the list of the container given the image name.
   * @param name the name of the image
   * @param container the container
   * @return
   */
  def getAbsolutePath(name: String)(container: Container): String = {
    val lst = container.data filter (x => x._1.endsWith(name))
    if(lst.nonEmpty)
      lst.head._1
    else
      throw new IllegalArgumentException("Incorrect image name.")
  }
}