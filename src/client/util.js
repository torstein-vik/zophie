// Escape HTML
String.prototype.escapeHTML = function() {
    const replacements = {
        '<': '&lt;',
        '>': '&gt;',
        '&': '&amp;'
    };

    return this.replace(/[<>&]/g, function(c){
        return replacements[c] || c;
    });
}
