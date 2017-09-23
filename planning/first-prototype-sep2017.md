
# 1st prototype for adding content (this is a "KLADD" produced on 18 Sep 2017, not at all finished)

## Section I: Mathematical content

### Fundamental internal semantic concepts for the mathematical content
- Concept
- Class
- Category
- Machine
- Specialization


### Link/button: Add new concept

A concept is any word that has a precise mathematical meaning, so in principle any word can be added here. But this button should only be used when none of the other buttons are applicable, and hopefully this will happen only on rare occasions.


### Link/button: Add new class
- Name of class: E.g. "The set of undirected graphs"
- Name of element, in singular: E.g. "Undirected graph"
- Name of element, in plural: E.g. "Undirected graphs"


### Link/button: Add new category
Fields: 
- Unique long description: E.g. "The category of topological spaces and continuous maps"
- Standard notation for this category (if applicable): E.g. "Top"
- Object name in singular form: E.g. "Topological space"
- Object name in plural form: E.g. "Topological spaces"
- Morphism name in singular form: E.g. "Continuous map"
- Morphism name in plural form: E.g. "Continuous maps"
- A list of property options, like "Is this an abelian category", with Yes/No/Open AFAWK/Unspecified
- A field for addition notes, for example a complete mathematical definition, or notes about things that are unknown?

Main challenge: Want to add for example the category of R-modules, where R is a commutative ring. 

Main question for implementation: Should we always require that a category is created based on an already added class (of objects)???


### Link/button: Add new machine
- Give name of machine in adjective form (if applicable)
- Give name of machine in noun form (if applicable)
- Give an ordered list of input types
- Give an ordered list of output types


### Link/button: Add new specialization
- Name of source, e.g. "$R$-modules, where $R$ is a commutative ring"
- Name of target, e.g. "Vector spaces over $k$, where $k$ is a field"
- Description of the specialization, e.g. "Specializing from a general commutative ring to a field"


------------

For each of the "creating" buttons, one could in principle ask lots of questions immediately, giving the user the options of adding known properties to the object just created. For example, when the user creates a new class, we could ask: 

- Question: Is this is a set? Y/N/OpenAFAIK/Skip for now
- Question: Is this a proper class: Y/N/OpenAFAIK/Skip for now

We can discuss how many such questions should be included in the standard template. We could perhaps generate these questions automatically from the list of all applicable properties known to Zophie, but this might eventually produce very large lists of questions.


-----------

### Important exampes of classes, just to illustrate the idea:
- Primes
- Prime powers
- Positive integers
- Non-negative integers
- Integers
- Rationals
- Reals
- Complex numbers
- Z/m for any positive integer m
- F_q for any prime power q
- Z_p for any prime p
- C_p for any prime p
- The adeles?

-------------


## Section II: Meta-mathematical content


### Add new publisher


### Add new journal

- Short name:
- Long name:
- Publisher:
- Hosting webpages: 


### Add new book series

- Publisher:
- Name of book series: 
- Webpage(s):


### Add new mathematician

- Surname:
- First name(s):
- MathSciNet Author ID:
- zbMATH Author ID
- arXiv Author ID:
- Webpages and online profiles:
- Last known physical working location:
- Year of last known physical working location:


### Add new blog

- Name of blog:
- Author(s):
- Webpage:


### Add new document

- Choose type of document: Book / Journal article / Survey series article / Stable preprint / Unpublished preprint or note / Dissertation / Slides / Blog post / StackExchange entry   
- Author(s): (Note: Must choose already existing authors)
- Link to hosting webpage:
- Link to pdf:
- Publisher (if applicable):
- Journal (if applicable):
- Book series (if applicable):
- Blog (if applicable): 
- Bibtex from MathSciNet (if applicable):
- Bibtex from zbMATH (if applicable): 


### Add new institute

- Long name:
- Short name:
- Location:


### Add new event:

Choose type: Seminar series / Seminar talk / Conference / Conference talk

Add more details here...


### Add new area of mathematics

Here we need to discuss the format and conventions. 

For example, all the subareas in the MSC2010 classification should be included, and Zophie should know when one area is a subarea of another, or when two are linked to each other because they are closely related.

http://www.ams.org/msc/pdfs/classifications2010.pdf
