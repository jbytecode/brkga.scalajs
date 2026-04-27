package org.expr.brkga

type ObjectiveFunction = Array[Int] => Double

case class BRKGA(
    popsize: Int, 
    chlen: Int, 
    alpha: Double, numelites: Int,
    nummutants: Int,
    costfn: ObjectiveFunction
)

case class Chromosome(genes: Array[Double], cost: Double)

type Population = Array[Chromosome]

def copy(ch: Chromosome): Chromosome = 
    Chromosome(ch.genes.clone(), ch.cost)

def selectgene(g1: Double, g2: Double, alpha: Double): Double = 
    if (Math.random() < alpha) g1 else g2

def cross(c1: Chromosome, c2: Chromosome, alpha: Double): Chromosome = 
    val genes = c1.genes.zip(c2.genes).map((g1, g2) => selectgene(g1, g2, alpha))
    Chromosome(genes, Double.MaxValue)

def createrandom(chlen: Int): Chromosome = 
    val genes = Array.fill(chlen)(Math.random())
    Chromosome(genes, Double.MaxValue)

def createpopulation(popsize: Int, chlen: Int): Population = 
    Array.fill(popsize)(createrandom(chlen))

def evaluate(ch: Chromosome, ga: BRKGA): Chromosome = 
    // Sort perm 
    val perm = ch.genes.zipWithIndex.sortBy(_._1).map(_._2)
    val cost = ga.costfn(perm)
    Chromosome(ch.genes, cost)
    

def generation(pop: Population, brkga: BRKGA): Population = 
    val evaluatedpop = pop.map(ch => evaluate(ch, brkga))
    val sortedpop = evaluatedpop.sortBy(ch => ch.cost)
    val elites = sortedpop.take(brkga.numelites)
    val mutants = Array.fill(brkga.nummutants)(createrandom(brkga.chlen))
    var newpop = elites ++ mutants 
    while (newpop.length < brkga.popsize){
        val index_of_elite = (Math.random() * brkga.numelites).toInt
        val index_of_nonelite = (Math.random() * (brkga.popsize - brkga.numelites)).toInt + brkga.numelites
        val elite = sortedpop(index_of_elite)
        val nonelite = sortedpop(index_of_nonelite)
        newpop :+= cross(elite, nonelite, brkga.alpha)
    }
    newpop

