define([
  'jquery',
  'backbone'
], function($, Backbone) {
	var ratingModel = Backbone.Model.extend({
		url: '/api/v1/rating',
	    initialize: function() {
	    	var fetchedObj = this.fetch();
	    	// this.set(fetchedObj.responseJSON)
	    }
	});

    return new ratingModel();
});