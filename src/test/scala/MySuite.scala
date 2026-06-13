import org.expr.brkga._

class TspTest extends munit.FunSuite {
  test("Traveling Salesman with 5 points") {
    val points = Array(
      Point(0, 0),
      Point(0, 10),
      Point(10, 10),
      Point(20, 10),
      Point(20, 0)
    )
    val ga = createga(points)
    val finalpop = iterate(ga, 500)
    val bestchromosome = best(finalpop)
    val obtained = bestchromosome.cost
    val expected = 60.0
    println(s"Best cost found: $obtained")
    println(s"Chromosome genes: ${bestchromosome.genes.mkString(", ")}")
    println(
      s"Decoded tour: ${decode(bestchromosome).mkString(" -> ")}"
    )
    assertEquals(expected, obtained)
  }

  test("Traveling Salesman with 10 points") {
    val points = Array(
      Point(0, 0),
      Point(0, 10),
      Point(0, 20),
      Point(10, 20),
      Point(20, 20),
      Point(20, 10),
      Point(20, 5),
      Point(20, 0),
      Point(15, 0),
      Point(10, 0)
    )
    val ga = createga(points)
    val finalpop = iterate(ga, 500)
    val bestchromosome = best(finalpop)
    val obtained = bestchromosome.cost
    val expected = 80.0
    println(s"Best cost found: $obtained")
    println(s"Chromosome genes: ${bestchromosome.genes.mkString(", ")}")
    println(
      s"Decoded tour: ${decode(bestchromosome).mkString(" -> ")}"
    )
    assertEquals(expected, obtained)
  }
}
