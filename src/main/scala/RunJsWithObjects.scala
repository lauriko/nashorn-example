import javax.script._

object RunJsWithObjects {

  case class User(id: Long, name: String)

  def main(args: Array[String]): Unit = {
    val engine: ScriptEngine = new ScriptEngineManager(null).getEngineByName("nashorn")
    val compilerEngine: ScriptEngine with Invocable with Compilable = engine match {
      case se: ScriptEngine with Invocable with Compilable => se
      case _ => throw new Exception("ScriptEngine not invocable and compilable")
    }

    // use Scala type in JavaScript
    compilerEngine.compile("""
        |function foo(user){
        |  var User = Java.type("RunJsWithObjects.User");
        |  var id = user.id() + 1;
        |  var name = user.name().split("").reverse().join("");
        |  return new User(id, name);
        |};
      """.stripMargin).eval
    val result: Object = compilerEngine.invokeFunction("foo", User(1, "Bar"))
    result match {
      case u: User => println("result1: " + u)
      case _ => println("result1 type is not User")
    }

    // use JavaScript object
    compilerEngine.compile("""
        |function foo(user){
        |  var jsObj = {
        |    id: user.id() + 1,
        |    name: user.name().split("").reverse().join("")
        |  };
        |  return jsObj;
        |};
      """.stripMargin).eval
    val result2: Bindings = compilerEngine.invokeFunction("foo", User(1, "Bar")).asInstanceOf[Bindings]
    println("result2: " + User(
      result2.get("id").asInstanceOf[Double].toLong,
      result2.get("name").asInstanceOf[String]
    ))

  }
}
