# Zophie
***A Mathematical Oracle***
https://zophie-508e8.firebaseapp.com/
## Overview

## Installation

1. Make sure you have installed Node.js and the firebase-tools package
2. Clone this repo to your computer
3. Run 'npm install' in the zophie directory to install packages for the build script
4. Run 'npm install' in 'firebase/functions/'
5. Make sure you have SASS installed, and in path
6. Run 'node build.js' to build the program
7. Login to Firebase (with 'firebase login')
8. Update .firebaserc to accommodate whatever your Firebase project is
9. In the firebase directory, use 'firebase serve' to run locally and 'firebase deploy' to deploy online.
10. Add 'resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"' to ~/.sbt/1.0/global.sbt
11. Run 'sbt' in the zophie directory to start SBT
12. Inside SBT, run 'test' to test the zophie codebase 

Please tell us if this doesn't work, because that means something is wrong with our instructions.

## Contributors

_Ask Torstein ([torsteinv64@gmail.com](mailto:torsteinv64@gmail.com)) to add you here if you contribute to this project_
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

Handled almost entirely by Firebase. At a later stage, firebase will use CoCalc's API to run computation in something more sophisticated than js.

## Languages/Frameworks

* Hosting/Maintenance: Firebase
* Server-side: Node.js which recieves all events from the client-side, and delegates computational aspects to other services. 
* 'Other services': Coded in scala, which can be compliled to js, and called upon within Firebase (this way the project can continue to be no-cost a while longer)
* Client-side: Possibly Angular, but maybe React or Vue as well. Stylesheets using SASS/SCSS
* Displaying math: MathJAX, hopefull server-side but maybe just client-side.
* Markdown: Showdown 1.7.3

#### Note

This is very subject to change, and only specifices the current plans.

## Folder structure

* /planning -- Files for the planning of the project
* ~~/planning/ui-sketch -- Sketch of the UI~~
* /planning/backend-logic -- Logic of the backend
* /planning/content -- Content on the webpage, such as Home and About
* /planning/examples -- Various examples of questions/answers etc...

* /project/ -- Part of SBT

* /firebase/ -- The root directory for the firebase project. NOTE: FILES ARE NOT EDITED HERE, BUT BUILT FROM /src/
* /firebase/public -- Public directory with all the hosted files
* /firebase/functions -- Directory with the server-side logic

* /src/ -- Source directory, where code is edited.
* /src/client/ -- The HTML, javascript, and scss general for all the website
* /src/client/templates -- HTML templates
* /src/client/json -- Json data or js-files that define a json object (since async can be very inconvenient)
* /src/server/ -- Code for the server. Note, this only serves as an API for the backend-logic.
* /src/main/scala -- API for the backend logic. Called upon by 'native' js-code in /src/server. Foundatioanlly independent from the webpage
* /src/test/scala -- Test for the backend API
