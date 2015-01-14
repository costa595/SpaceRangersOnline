define([
  'jquery',
  'backbone',
  'collections/score',
  'tmpl/scoreboard'
], function($, Backbone, scoreCollection, tmpl) {

  var ScoreboardView = Backbone.View.extend({
    tagName: 'div',
    collection: scoreCollection,
    initialize: function () {
      this.listenTo(this.collection, 'reset', this.insertInfo)
    },
    template: tmpl,
    render: function() {
      this.collection.fetch({reset: true});
      return this;
    },
    show: function () {
      this.trigger('show', this);
    },
    insertInfo: function () {
      this.$el.html(this.template(this.collection.toJSON()));
    }
  });

  return new ScoreboardView();
});
