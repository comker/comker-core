define([
    'jquery',
    'underscore',
    'backbone',
    'utils',
    'app/models/spotObject',
], function($, _, Backbone, Coke, SpotObject) {

    var SpotCollection = Backbone.Collection.extend({
        model: SpotObject,
        url: '../ws/comker/api/spot/crud'
    });

    return SpotCollection;
});
