
# Overview

Platform for mathematical knowledge management, with an web-based conversation-like interface. 

## Philosophy guiding the design choices

Choices should be made with the goal of achieving a workable and useful human-machine interface.

We want to quickly include most of the main concepts of modern mathematics, rather than slowly building from the ground up.

## Mathematical content processing

### List of mathematical types

- Class: This is the most general form of collection. For every class, we may speak of its "members".
- Category: Consists of a class of objects, and for every ordered pair of objects, a class of morphisms.
- Set: A class which is is small enough to be a set. (We are implicitly assuming a choice of Grothendieck universe.) The word "element" means a member of set.

- Machine: This is the most general form of input-output procedure. Every machine has an input class and an output class.
- Functor. Every functor has a domain category and a target category. 
- Partially defined functor. Every PD functor has a domain category and a target category.
- Function: A machine where the input class and the output class are sets.


This is just the beginning...


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
- A few examples of cohomology functors. 
- The functor that takes a scheme to its functor of points. The target category here is the functor category Fun(CRing, Set)

Notes on cohomology functors:
- This is really complicated, due to the dependence on zero, one or two integers, a Grothendieck topology, and a sheaf. And this is just for sheaf cohomology, other cohomologies depend on other things. Should we introduce cohomology as a family of functors indexed by a natural number, or as a single functors into graded/bigraded abelian groups? There are several other issues to resolve here...

#### Various topological categories

- The category of topological spaces
- The category of metric spaces
- The category of simplicial sets


Add:
- Some properties that topological spaces can have, for example Hausdorff, compact, connected, metrizable
- Some properties that morphisms of topological spaces can have, for example being closed, being surjective, begin an epimorphism

#### Various categories of rings

1. The category of rngs
2. The category of rings
3. The category of commutative rings


#### Various categories of groups

1. The category of groups
2. The category of abelian groups (using additive notation)
3. The category of abelian groups (using multiplicative notation)
4. The category of finite groups (using multiplicative notation)
5. The category of finitely generated abelian groups (using additive notation)



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

This is just an example of an objects in which the elements have a long list of super-interesting properties. It is a slightly complicated example due to the multitude of operations and topologies, some of which are only partially defined.

# Project Architecture

A simple client-side with possibility to answer questions about mathematics. Should work with very simple html, because God knows some mathematicians use ancient technology. However, it should be enhanced with javascript and CSS (cross-comapitible).  

Server-side should contain all the interesting mathematics, with a database of everything. Should create intellegent questions. We can deploy using Firebase, and use a language with a strong typing system for computational ascpects.

