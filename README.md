# zophie
***A Mathematical Oracle***
## Overview

## Installation

1. Make sure you have installed Node.js and the firebase-tools package
2. Clone this repo to your computer
3. Login to Firebase (with 'firebase login')
4. Update .firebaserc to accommodate whatever your Firebase project is
5. In the firebase directory, use 'firebase serve' to run locally and 'firebase deploy' to deploy online.

Please tell us if this doesn't work, because that means something is wrong with our instructions.

## Contributors

_Ask Torstein ([torsteinv64@gmail.com](torsteinv64@gmail.com)) to add you here if you contribute to this project_
* Torstein Vik, Design & Full stack
* Andreas Holmstrøm, Design

## Copyright


This framework is and will remain completely open source, under the GNU General Public License version 3+:

    Copyright (C) 2017, Andreas Holmstrom, Torstein Vik.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

## Architecture

Mostly still in planning, but see Languages/Frameworks for some ideas about the architecture.

## Languages/Frameworks

* Hosting/Maintenance: Firebase
* Server-side: Node.js which recieves all events from the client-side, and delegates computational aspects to other services. Another idea could be to implement everything in javascript, but this seems slow and difficult. 
* Client-side: Possibly Angular, but maybe React or Vue as well.
* Displaying math: MathJAX, hopefull server-side but maybe just client-side.

#### Note

This is very subject to change, and only specifices the current plans.

## Folder structure

* /planning -- Files for the planning of the project
* /planning/ui-sketch -- Sketch of the UI

* /firebase/ -- The root directory for the firebase project. NOTE: FILES ARE NOT EDITED HERE, BUT BUILT FROM /src/
* /firebase/public -- Public directory with all the hosted files
* /firebase/function -- Directory with the server-side logic

