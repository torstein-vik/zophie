# Detailed requirements for first implementation of content adding

This document gives details on the system for adding new concepts to Zophie's brain. 

It is divided into levels which describe the order of processing for a given input keyword (1st level, 2nd level, 3rd level, ...).

## An interface for adding any keyword to the database (1st level)

Page title: Adding new keywords

Zophie says: Hi, please give me a list of things to think about :-)

Description: This is a page where the user can add new keywords to Zophie's brain. These should be stored in a database for later sorting and processing.

1. The first input field should be a text field where the user can enter a list of keywords (separated by commas).
2. The second input field should be a text field where the user kan enter a list of keywords (separated by commas) that are known to be ambiguous in some sense. 
3. The third input field should be a text field where the user can enter a single expression in LaTeX math format. 
4. The fourth input field should be a text field where the user can enter a single expression in mixed math-and-text format.
5. The fifth input field should be a field for a URL where the user believes Zophie can find interesting material.
6. The sixth input field should be a text field where the user can leave a message if he/she wants to add something that doesn't yet fit.

### Automatic processing on this level:
* The entries from the first input field should be stored in a database called "keywords". Keywords which has already appeard in this database should be ignored, while never-seen-before keywords are added.
* The entries from the second input field should be stored in a database called "ambiguous-keywords". Keywords which has already appeared in this database before should NOT be ignored, but added again somehow, for manual processing.
* The entries from the third input field should be processed by rules not yet designed. For now, we could ignore them or store them in a temporary database.
* Fourth input field: Same as the third.
* Fifth input field: The entries should be stored in a database called "urls". 
* Anything in the sixth input field should be stored in a database called "wishlist-1st-level-input" or something similar (maybe automatically added to github issues?). A notification should be sent to the inbox of the developers. The user should also be given the option of leaving an email address for possible future updates, and this email address should then be stored together with the message just entered.

Note: In all cases, we might want to store the identity of the user who submitted the input AND the time stamp of the entry. A huge advantage of this is that (as an example) Andreas can later choose to help Zophie process urls submitted by Magnus Hellebust Haaland in October 2017. 

### Examples (for motivation and explanation):

1. In the first field, a user could add the list "zeta type, zeta types, Riemann hypothesis, the Gamma function, analytic continuation, topos, Gauss, Bill Allombert, Courant Institute of Mathematics, Annals of Mathematics, continuous, topos, Turing machine". Another user could add the list "Chelsea FC, Ronaldo, Messi, graphical, math". A third user could add "mathemattics, number theory,, problems. prime, derivative, linear algebr". A fourth user could add the list "R-module, adelic cohomology, Eichler-Shimura correspondence, Galois representations, Langlands program, Ising model". A fifth user could add "philosophy of mathematics, meaning of life, truth, Gödel, incompleteness, existence of God, Higgs particle". 

2. In the second field, a user could add the list "Schmidt, float, regular, natural numbers", motivated by the fact that several mathematicians are called Schmidt, the word float can mean several different things, etc.  

3. A user could add for example "\mathbb{R}^n". Another user could add "\sum_{k=1}^{\infty} \frac{1}{k}".

4. A user could add "$R$-module", or "$\ell$-adic $Gal(\mathbb{Q}/Q)$-representations".

5. A user could add "https://terrytao.wordpress.com/" or "http://wwwf.imperial.ac.uk/~buzzard/maths/research/notes/notes_on_weil_conjectures.pdf".

6. A user could add "I would like to add a commutative diagram used used in an explanation of the Brauer group". Another user could add "I would like to add a GeoGebra script that I wrote when I was 14 years old". 


### Additional notes for the future

We may consider adding fields for uploading of attachments to Zophie - pdfs, djvus, code written in various languages, images, all kinds of other files. We might also want to store snapshots of web pages Zophie has visited. But then we first need to solve the major challenges of storage and organising.

At some point in the future, all the above lists could perhaps be obtained by scraping books/articles/mathematical websites. The stuff found between dollar signs then automatically goes into the third input field, etc. 

Also in the future, one could perhaps add much more intelligent methods for automatic 1st-level processing, using advanced rules based on what Zophie already understands.

Processing mathematical expressions is hard but not impossible. We could learn things from [Springer LaTeX Search](http://latexsearch.com/) and from Ganesalingams thesis.


## An interface for keyword processing (2nd level)

Description: This is a page where the user helps Zophie to sort the keywords into high-level types.

1. The page should present an unprocessed (non-ambiguous) keyword to the user. 
2. The user then has to make a choice of ticking one or several boxes.

Boxes presented to the user (LIST STILL VERY INCOMPLETE):
* This keyword is an object in a category I want to create
* This keyword is an element of a known set
* This keyword calls for the creation of ...
* This is a meta-mathematical keyword (author, journal, etc)
* This keyword is a branch of mathematics
* I want to specify which branches of mathematics this keyword belongs to
* None of the above choices make sense logically
* I cannot or will not classify this keyword right now
* This keyword looks like spam


## An interface for the first step of ambiguous keyword processing (2nd level)

Description: This is a page where the user helps Zophie process keywords which have been entered as ambiguous.

This can be designed at a later stage; precise mechanisms must be discussed.


## An interface for meta-mathematical keyword processing (3rd level)

1. The user is presented with a keyword that has been marked as meta-mathematical.
2. The user then has to make a choice between various boxes.

Boxes presented to the user (LIST STILL INCOMPLETE)
* This is a journal
* This is a journal volume
* This is a single journal article
* This is an stable preprint
* This is an unstable preprint
* This is a note
* This is a set of slides
* This is a book series
* This is a book
* This is a publisher
* This is a blog
* This is a single blog post
* This is a mathematician
* This is an institution
* This is a conference
* This is a talk

