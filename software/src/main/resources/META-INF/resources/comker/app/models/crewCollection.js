define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/models/crewObject',
], function($, _, Backbone, Coke, CrewObject) {

    var CrewCollection = Backbone.Collection.extend({
        model: CrewObject,
        url: '../ws/comker/api/crew/crud'
    });

    return CrewCollection;
});
