import javax.script._

object RunJsWithArgs {
  def main(args: Array[String]): Unit = {
    val engine = new ScriptEngineManager(null).getEngineByName("javascript")
    val compilerEngine = engine match {
      case se: ScriptEngine with Invocable with Compilable => se
      case _ => throw new Exception("ScriptEngine not invocable and compilable")
    }
    compilerEngine.compile("""
        |function foo(bar){
        |  return bar*10+5
        |}
      """.stripMargin).eval

    val result = compilerEngine.invokeFunction("foo", "10")
    println(result)
  }
}
