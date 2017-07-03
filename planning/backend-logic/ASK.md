# Interpretation and answering of questions

## Overview

First we need to tokenize the query using two types of tokens: concept-tokens and grammatical tokens. As a starter, we can use the grammatical tokens to fit the query with some kind of template. Code assosiated to such a template will then be executed (with the concept-tokens passed) and the result will be sent back.

## Tokenization

We split this into three stages; pretokenization, lexical matching, and final tokenization. The pretokenization splits the query into very fundamental grammatical tokens, while lexical matching matches these with known vocabulary. The final tokenization refines these into concept-tokens and grammatical tokens. 

### Pretokenization
The primary tokenization has the purpose of effictivising the remaining tokenization. Primary tokenization recognizes the following tokens:

#### Words
A word is any finite sequence of permitted characters. The characters consist of the normal alphabet A-Z, both upper and lower case. More caracters will be added later.
#### Spaces
A space is... well... a space. As in " ". [https://en.wikipedia.org/wiki/Space_(punctuation)](https://en.wikipedia.org/wiki/Space_(punctuation))
#### Punctuation
Punctuation consists of "?" and "," and ".".

### Lexical matching
Lexical matching is the stage where Levenshtein-like metrics will later be used to match the word-tokens with known vocabulary. For now we assume Levenshtein distance 0. 'Known vocabulary' is scraped from the set of concepts and grammatical words. 

### Final tokenization
Final tokenization recognizes the following tokens:

#### Concepts

Words appearing in the concept-database. These may contain spaces.

#### Grammatical words

These are words that will appear in grammar. At first, we will hand-pick these for some simple examples, but later we might want to scrape a dictionary or similar.

## Template matching
At this point we have little more than a list of tokens. This state is a temporary solution for recognizing the syntax.

### Template specification

Specified in JSON and compiled from that. Example:
    {
        "syntax": [
            {"text": "Could you give me an example of "},
            {"concept": {restrictions:[{"hasrealization": "class"}], name:"@1"}}
        ],
        "answer": [
            {"selectconcept": {restrictions:[{"hasrealization": {"from": "@1"}}], "select": "random"}},
            {"text": " is an example."}
        ]
    }


#### Formal Specification

Object with "syntax" and "answer" attribute. "syntax" is the syntax of a question, and "answer" is the answer given that a sentence matches the syntax. Both are arrays of token-objects.



### Template code interface

### Template storage

## Alternatives to templates

Templates are easy to implement, but the big problem is that the set of 'legal' templates will be hard to maintain and grow, even with recursive templates. Another idea is to parse the grammatical syntax of the query, but from here it is hard to see exactly how the queries will be answered. One possibility is to use machine learing, but it will take a long to to train Zophie to answer with even just somwhat related information.