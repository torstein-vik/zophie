const fs   = require("fs-extra");
const path = require("path");
const execSync = require('child_process').execSync;
const schema   = require('js-schema');

// Specification of schema
const onAlways = schema({
    always: true
});

const onNever = schema({
    never: true
});

const onFileChange = schema({
    filechange: true
});

const onFileChangeWithPath = schema({
    filechange: String
});

const jsonformat = schema({
    '?files'   : Array.of({
        'from' : String,
        'to'   : String,
        '?on'   : [onAlways, onNever, onFileChange, onFileChangeWithPath]
    }),
    '?directories'   : Array.of({
        'from' : String,
        'to'   : String,
        '?on'   : [onAlways, onNever, onFileChange, onFileChangeWithPath]
    }),
    '?commands' : Array.of([
        String,
        {
            'command' : String,
            '?on'      : [onAlways, onNever, onFileChangeWithPath]
        }
    ])
});


// Load build setup
var build = require("./build.json");


// Match with schema
if(!jsonformat(build)){
    console.log("build.json does not follow the specified schema!");
    process.exit(1);
}

// Compile
build = compileBuildScript(build);

/*
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
*/

// Compiles the json file
function compileBuildScript(build){

    // directories are turned into files
    let newbuild = {
        files: [],
        commands: []
    };

    let types = ['always', 'filechange', 'never'];

    if (build.files){
        build.files.forEach((file) => {
            let on = file.on || {always: true};
            let ontype, onvalue;

            types.forEach((type) => on[type] ? ontype = type : false);

            onvalue = on[ontype];

            if (ontype === types[1] && onvalue === true){
                onvalue = file.from;
            }

            newbuild.files.push({
                from: file.from,
                to: file.to,
                ontype: ontype,
                onvalue: onvalue
            });
        });
    }

    if (build.directories){
        build.directories.forEach((directory) => {
            fs.readdirSync(directory).filter((filename) => fs.statSync(filename).isFile()).forEach((file) => {
                let on = file.on || {always: true};
                let ontype, onvalue;

                types.forEach((type) => on[type] ? ontype = type : false);

                onvalue = on[ontype];

                if (ontype === types[1] && onvalue === true){
                    onvalue = file.from;
                }

                newbuild.files.push({
                    from: file.from,
                    to: file.to,
                    ontype: ontype,
                    onvalue: onvalue
                });
            });
        });
    }

    return newbuild;
}

function hasChanged(){

}

function updateHashes(){

}
