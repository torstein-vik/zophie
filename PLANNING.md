
# Overview

Platform for mathematical knowledge management, with an web-based conversation-like interface. 

## Philosophy guiding the design choices

Choices should be made with the goal of achieving a natural human-machine interface.

## Mathematical content processing

### List of mathematical types

- Category (every category has objects and morphisms)
- Functor (every functor has a domain category and a target category)
- Partially defined functor (every one has a domain category and a target category)
- Machine??? (every machine has an input class and an output class)
- Set (every set has elements)
- Class (every class has members)




### List of meta-mathematical types

- Author
- Institute
- Journal
- MSC2010 label


### Examples for prototype building

#### Various categories of schemes

Some design choices to make here:

- What exactly is a variety? There are many conflicting conventions in the literature. We could follow some modern authoritative source, like Ravi Vakil's book project, or perhaps the Stacks project.
- How many different categories do we introduce, as opposed to adding properties inside a big category of schemes? 
- How do we deal with relative schemes (a special case of an over-category)?

Here is a suggestion for the prototype:

- The category of schemes
- The category of schemes of finite type over Z
- The category of varieties over Q
- The category of varieties over C

Functors from schemes include:

- The functor to Top which gives the underlying Zariski topology of a scheme
- The functor to Top which gives the complex topology on the complex points (for schemes where this makes sense)
- The global sections functor to rings
- A few examples of cohomology functors. This is really complicated, due to the dependence on zero, one or two integers, a Grothendieck topology, and a sheaf

#### Various topological categories

- The category of topological spaces
- The category of metric spaces
- The category of simplicial sets

- Some properties that topological spaces can have, for example Hausdorff, compact, connected, metrizable
- Some properties that morphisms of topological spaces can have, for example being closed, being surjective

#### Various categories of rings

1. The category of rngs
2. The category of rings
3. The category of commutative rings


#### Various categories of groups

1. The category of abelian groups (using additive notation)

#### Various categories of monoids

Here we have a really difficult design choice - should we work with monoids only, or with separate categories for "additive" monoids and "multiplicative" monoids?

One requirement is that the choices we make here should work well with various forgetful functors from commutative and general rings.

One suggestion: First we use three separate categories as below. 

1. The category of monoids (using multiplicative notation)
2. The category of commutative monoids (using multiplicative notation)
3. The category of commutative monoids (using additive notation)

Secondly, we add a few functors between them:

- The inclusion functor from 2 to 1.
- The "change of notation functor" from 2 to 3, and another one from 3 to 2.


#### Various categories of sets

- The category of sets (assuming some fixed Grothendieck universe)
- The category of finite sets


#### The set of partially defined functions from the reals to the reals


# Project Architecture





