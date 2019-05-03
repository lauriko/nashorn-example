import javax.script._

object RunJsWithoutArgs {

  def main(args: Array[String]) {
    val engine = new ScriptEngineManager(null).getEngineByName("javascript")

    val compilerEngine = engine match {
      case c: Compilable => Some(c)
      case _ => None  // See if the engine supports compilation.
    }

    // If the engine supports compilation, compile and run the program.
    val result = compilerEngine.map {ce =>
      ce.compile("[1,2,3].length").eval()
    }
    println(result.fold("Script not compilable")(_.toString))
  }
}
