load().done(() => {

});

function load(){
    let DOMLoaded = new $.Deferred((def) => $(def.resolve));
    return DOMLoaded;
}
