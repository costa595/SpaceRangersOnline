define([
  'jquery',
  'backbone',
  'tmpl/toolbar',
  'models/user',
  'views/profile',
  'views/game',
  'views/rating'
], function ($, Backbone, tmpl, userModel, profileView, gameView, ratingView) {

  var HeaderView = Backbone.View.extend({
    model: userModel,
    template : tmpl, 
    className: 'toolbar',
    profile: profileView,
    rating: ratingView,
    profileShowed: false,
    ratingShowed: false,
    events: {
      'click .toolbar_ship': 'openProfile',
      'click .toolbar_raiting': 'openRating'
    },
    initialize: function () {
      this.$container = $('#toolbar');
      this.listenTo(this.model, 'change', this.render);
      this.render();
    },
    render: function () {
      this.$el.html(this.template(this.model));
      return this;
    },
    openProfile: function() {
      if (this.ratingShowed) {
        ratingView.$el.hide();
      }

      if (this.profileShowed) {
        profileView.$el.show();
      } else {
        gameView.$el.append(profileView.render().$el);
        this.profileShowed = true;
      }
    },
    openRating: function() {
      if (this.profileShowed) {
        profileView.$el.hide();
      }

      if (this.ratingShowed) {
        ratingView.$el.show();
      } else {
        gameView.$el.append(ratingView.render().$el);
        this.ratingShowed = true;
        $('.background_rating').css('z-index', '100');
      }
    }
  });

  return new HeaderView();

});
