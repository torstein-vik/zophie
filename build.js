const fs   = require("fs-extra");
const path = require("path");
const execSync = require('child_process').execSync;


// Load build setup
var build = require("./build.json");

// Copy files & directories
build.copies.forEach(({from: src, to: target}) => {
    copy(path.join(src),path.join(target));
});

// Run commands
build.commands.forEach((command) => {
    var res = execSync(command);
    console.log("Ran > " + command);
    console.log("With output > " + res);
});

function copy(src, target){
    try{
        fs.copySync(src, target);
        console.log("copied " + src + " to " + target);
    } catch (err) {
        console.log(err);
    }
}
