package org.expr.brkga

def createga(points: Array[Point]): BRKGA = 
    val distancematrix = Array.tabulate(points.length, points.length) { (i, j) => 
        Point.distance(points(i), points(j))
    }
    val costfn: ObjectiveFunction = (perm: Array[Int]) => 
        val tour = perm :+ perm.head // Return to the starting point
        tour.sliding(2).map { case Array(i, j) => distancematrix(i)(j) }.sum
    BRKGA(
        popsize = 100,
        chlen = points.length,
        alpha = 0.7,
        numelites = 20,
        nummutants = 10,
        costfn = costfn
    )

def iterate(ga: BRKGA, generations: Int): Population = 
    var pop = createpopulation(ga.popsize, ga.chlen)
    for (i <- 1 to generations) {
        pop = generation(pop, ga)
    }
    pop

def best(pop: Population): Chromosome = 
    pop.sortBy(_.cost).head

