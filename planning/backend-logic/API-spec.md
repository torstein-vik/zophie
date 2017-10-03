The Zophie API essentially does all the computation within the project, while the server purely handles IO.

The API needs to have both input and output. 

Output is dealt with through the 'on'-function, which accepts an event string and a callback function. These are the event strings:
* 'add_lexeme',   json object representing the lexeme
* 'add_class',    json object representing the class
* 'add_machine',  json object representing the machine
* 'list_lexemes', callback function which should be passed the list of all lexeme objects
* 'list_class',   callback function which should be passed the list of all class objects
* 'list_machine', callback function which should be passed the list of all machine objects

Input is dealt with through the 'trigger'-function, which launches an event that is picked up on the API side. It accepts an event string and a json-object to passed on. These are the event strings:
_to be added_