# Specification for build json file

The build.json specifies how the project should be built

## Using js-schema

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
    
## Explanation

build.json should be an object with three properties (all optional), "commands", "files", and "directories". "commands" is a list of commands to be run, "files" is a list of files to be copied from one place to another, and "directories" is a list of directories where each file is copied to another directory.

An element of "commands" may be either a string with the command (which will be run no matter what), or an object with two properties; "command" and "on" (optional). "command" is simply the command to be run. "on" specifies when this command should be run. {always: true} means always, {never: true} means never, and {filechange: "..."} means that if the file in the specified path has changed, then the command should be run.

An element of "files" is an object with three properties; "from", "to", and "on" (optional). "from" is the file to be copied, and "to" is the destination. "on" specifies the condition. "always" and "never" are as before, and "on" can be {filechange: "..."} as before. However, we can also have {filechange: true} which means that the file in from has to have changed.

An element of "directories" is an object with three properties; "from", "to", and "on" (optional). "from" is the directory to be copied from, and "to" is the destination directory. "on" specifies the condition. "always" and "never" are as before, and "on" can be {filechange: "..."} as before. However, we can also have {filechange: true} which means that for each file we only copy if it has changed.

