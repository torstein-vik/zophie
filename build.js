const fs       = require("fs-extra");
const path     = require("path");
const execSync = require('child_process').execSync;
const schema   = require('js-schema');
const crypto   = require('crypto');

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

// Filter build object by whether or not to do something, and update hashes
build = filterByEvents(build);


// Copy files & directories
build.files.forEach(({from: src, to: target}) => {
    copy(path.join(src),path.join(target));
});

// Run commands
build.commands.forEach(({command: command}) => {
    try{
        console.log("$ " + command);
        execSync(command);
    } catch (err) {
        console.trace();
    }
});

// Copy src to target
function copy(src, target){
    try{
        fs.copySync(src, target);
        console.log("copied " + src + " to " + target);
    } catch (err) {
        console.trace();
    }
}


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
                onvalue = path.join(file.from);
            }

            newbuild.files.push({
                from: path.join(file.from),
                to: path.join(file.to),
                ontype: ontype,
                onvalue: onvalue
            });
        });
    }

    if (build.directories){
        build.directories.forEach((directory) => {
            fs.readdirSync(directory.from).filter((filename) => fs.statSync(path.join(directory.from, filename)).isFile()).forEach((filename) => {
                let on = directory.on || {always: true};
                let ontype, onvalue;

                let frompath = path.join(directory.from, filename);
                let topath   = path.join(directory.to  , filename);

                types.forEach((type) => on[type] ? ontype = type : false);

                onvalue = on[ontype];

                if (ontype === types[1] && onvalue === true){
                    onvalue = frompath;
                }

                newbuild.files.push({
                    from: frompath,
                    to: topath,
                    ontype: ontype,
                    onvalue: onvalue
                });
            });
        });
    }

    if (build.commands){
        build.commands.forEach((command) => {
            command = typeof command === 'string' ? {command: command} : command;

            let on = command.on || {always: true};
            let ontype, onvalue;

            types.forEach((type) => on[type] ? ontype = type : false);

            onvalue = ontype === types[1] ? path.join(on[ontype]) : on[ontype];

            newbuild.commands.push({
                command: command.command,
                ontype: ontype,
                onvalue: onvalue
            });
        });
    }

    return newbuild;
}


function filterByEvents(build){
    let hashes = loadHashes();
    let newhashes = {};

    let types = {
        always:     (value) => true,
        never:      (value) => false,
        filechange: (value) => testFileHash(value, hashes, newhashes)
    }

    let newbuild = {
        files:    build.files.filter(({ontype: ontype, onvalue: onvalue}) => types[ontype](onvalue)),
        commands: build.commands.filter(({ontype: ontype, onvalue: onvalue}) => types[ontype](onvalue))
    }

    fs.writeFile("./hashes.json", JSON.stringify(newhashes), function(err) {
        if (err) console.log(err);

        console.log("New hashes saved!");
    });

    return newbuild;
}

function testFileHash(file, hashes, newhashes){

    let content = fs.readFileSync(file);
    let hash = crypto.createHash('sha1').update(content).digest('hex');

    newhashes[file] = hash;

    return hash !== hashes[file];

}

function loadHashes(){
    if(fs.existsSync('./hashes.json')){
        return JSON.parse(fs.readFileSync('./hashes.json'));
    } else {
        return {};
    }
}
