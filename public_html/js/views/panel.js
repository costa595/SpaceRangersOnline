define([
  'jquery',
  'backbone',
  'models/user',
  'tmpl/panel'
], function ($, Backbone, userModel, tmpl) {
  var PanelView = Backbone.View.extend({
    tagName: 'div',
    className: 'panel_block',
    model: userModel,
    events: {
      'click .js-logout': 'logout'
    },
    template: function () {
      return tmpl(this.model);
    },
    initialize: function () {
      console.log(this.model)
        this.listenTo(this.model, 'change', this.render);
        this.render();
    },
    render: function () {
        this.$el.html(this.template());
        if (this.model.isLogined()) {
          // console.log('trueLog')
          this.$el.find('#panel_logined').addClass('panel_logined');
          this.$el.find('#panel_logined').removeClass('panel_unlogined');
        }
        else{
        // console.log('falseLog')
            this.$el.find("#panel_logined").addClass('panel_unlogined');
          this.$el.find("#panel_logined").removeClass('panel_logined');
        }
        return this;
    },
    logout: function () {
      userModel.logout();
    }
  });
  
  return new PanelView();
});