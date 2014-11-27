/**
 * Created by dmitry on 09.10.14.
 */
define([
  'jquery',
  'backbone',
  'models/user',
  'tmpl/panel'
], function ($, Backbone, userModel, tmpl) {
  var PanelView = Backbone.View.extend({
    tagName: 'div',
    className: 'panel',
    model: userModel,
    events: {
      'click .js-logout': 'logout'
    },
    template: function () {
      return tmpl(this.model);
    },
    initialize: function () {
        this.listenTo(this.model, 'change', this.render);
        this.render();
    },
    render: function () {
        this.$el.html(this.template());
        if (this.model.isLogined()) {
          console.log('trueLog')
          this.$el.addClass('panel_logined');
        }
        else{
        console.log('falseLog')
          this.$el.removeClass('panel_logined');
        }
        return this;
    },
    logout: function () {
      userModel.logout();
    }
  });
  
  return new PanelView();
});