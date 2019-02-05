import org.zella.procaas.client.Chef

case class Food(ingredients: Seq[String])

case class VeganFood(ingredients: Seq[String])

object Chef {

  sealed trait Tacos
  object Tacos {
    sealed trait Empty extends Tacos
    sealed trait Cheese extends Tacos
    sealed trait Meat extends Tacos

    type MeatTacos = Empty with Meat
    type CheeseTacos = Empty with Cheese

  }
}

class Chef[Tacos <: Chef.Tacos](ingredients: Seq[String] = Seq()) {
  import Chef.Tacos._

  def addCheese(cheeseType: String)(implicit ev: Tacos =:= Empty): Chef[Tacos with Cheese] = new Chef(ingredients :+ cheeseType)

  def addMeat(implicit ev: Tacos =:= Empty): Chef[Tacos with Meat] = new Chef(ingredients :+ "meat")

  def addChilli: Chef[Tacos] = new Chef(ingredients :+ "chilli")

  def cook(implicit ev: Tacos =:= MeatTacos): Food = Food(ingredients)
  //Here!
  def cook2(implicit ev: Tacos =:= CheeseTacos): VeganFood = VeganFood(ingredients)

}

object Main{
  def main(args: Array[String]): Unit = {
    new Chef[Chef.Tacos.Empty]()
      .addCheese("mozzarella")
      .addChilli
      .cook2
  }
}