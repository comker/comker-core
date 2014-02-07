define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke) {
    var SpotObject = Backbone.Model.extend({
        defaults: {
            code: 'Unknown',
            name: 'Unknown',
            description: 'Unknown'
        },
        initialize: function(){
            Coke.log.debug("run SpotObject.initialize()");
        }
    });
    return SpotObject;
});


