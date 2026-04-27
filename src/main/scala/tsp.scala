package org.expr.brkga

def createga(points: Array[Point]): BRKGA = 
    val costfn: ObjectiveFunction = (perm: Array[Int]) => 
        val tour = perm.map(points(_)) :+ points(perm(0))
        tour.sliding(2).map { case Array(p1, p2) => Point.distance(p1, p2) }.sum
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

