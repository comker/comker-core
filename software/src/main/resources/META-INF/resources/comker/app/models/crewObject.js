define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
], function($, _, Backbone, Coke) {
    var CrewObject = Backbone.Model.extend({
        defaults: {
            code: 'Unknown',
            name: 'Unknown',
            description: 'Unknown'
        },
        initialize: function(){
            Coke.log.debug("run CrewObject.initialize()");
        }
    });
    return CrewObject;
});


