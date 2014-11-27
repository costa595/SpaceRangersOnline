define([
  'backbone',
  'tmpl/profile',
  'models/profile'
], function (Backbone, tmpl, profileModel) {
  var ProfileView = Backbone.View.extend({
    initialize: function() {
      this.render();
    },
    tagName: 'div',
    className: 'wrap_profile',
    template: tmpl,
    render: function() {
      this.$el.html(this.template);
      return this;
    },
    show: function () {
      this.trigger('show', this);
    }
  });

  return new ProfileView();
});
