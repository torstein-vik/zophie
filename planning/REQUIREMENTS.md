# Requirements:

## Web app with pages for

* Asking questions to Zophie
* Answering questions from Zophie
* Visualizing knowledge from Zophie
* Adding new content to the Zophie database


### Functionality for adding new content
* Logged-in users with moderator status (i.e. Torstein and Andreas) can add content
* Each new piece of content much be approved by another moderator
* For the different types of data we would like to add, see the list under PLANNING.md

More specifically, for every main type, we want an easy interface for adding a new instance of that type.

To add a concept is the simplest possible thing, you just need to specify
- The name of the concept, for example "model category"

Any mathematical concept (even if you don't know what it means) can be added to the database in a quick-and-easy way. For example, we may add "lattice" or "operad" or "multiplicative function" or "partially ordered set". Hopefully, Zophie can learn more about the concept by asking questions to humans, such as "Is the concept XXX an object in some category?", or "Which binary operations are there on the class of XXXs?"?

To add a category, you need to specify:
- The name of the category, for example "the category of commutative rings"
- The latex symbol for the category, for example "\mathbf{CRing}".
- The name of an object, for example "commutative ring".
- The name of a morphism, for example "homomorphism of commutative rings".
- Other optional arguments, for example suggested notation for objects and morphisms.

To add a set, you need to specify:
- That you are adding an object in the category of sets
- The name of the set, for example "the set of all complex-valued multiplicative functions"
- The name of an element of the set, for example "complex-valued multiplicative function"
- The latex symbol for the set, for example "Mult(\mathbb{C})"
- Optional arguments, for example a shortened version of the name of an element, e.g. "multiplicative function"

To add a functor, you need to:
- Choose a domain category that is already added to the database (drop-down menu? search?)
- Choose a target category that is already added to the database
- Specify the name of the functor
- Specify the latex symbol for the functor

And so on...

### Functionality for asking questions 

* Long-term goal: Accept questions in natural language
* Short-term goal: Accept simple questions in specified formats

Examples of simple questions:
* Tell me about topic XXX
* Is it true that for morphisms of schemes, projective implies proper?


### Functionality for answering questions

Short-term goal: 
* The user can answer simple questions with buttons for the 4 alternatives Yes/No/I don't know/This question is nonsensical, together with a free-text box for an optional explanation.
* Every answer is saved, and is reviewed by a moderator before it is added to the knowledge base


### Functionality for visualizing knowledge

* Show all functors going into and out of a given category
* Show all operations and relations on a given set
* Show all known examples of objects in a given category
* Show all books on a given subject
* Show all articles from a given journal volume

