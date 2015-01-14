define([
  'jquery',
  'backbone',
  'views/home',
  'views/game',
  'views/login',
  'views/signup',
  // 'views/profile',
  'views/scoreboard',
  'views/createProfile',
  'views/rating',
  'views/app',
  'models/user',
  'models/ship'
], function($, Backbone, homeView, gameView, loginView, signupView, scoreboardView,createProfileView ,ratingView ,manager, userModel, ShipModel) {
  
  manager.subscribe([homeView, gameView, loginView, signupView, scoreboardView,createProfileView, ratingView]);
  
  var Router = Backbone.Router.extend({
    initialize: function () {
        this.listenTo(userModel, 'login:ok', this.toGame);
        this.listenTo(userModel, 'signup:ok', this.toLogin);
        this.listenTo(userModel, 'login:bad', this.toLogin);
        this.listenTo(userModel, 'logout', this.toMain);
        this.listenTo(ShipModel, 'login-bad', this.toLogin);
    },
    routes: {
      '': 'index',
      'game': 'game',
      'login': 'login',
      'signup': 'signup',
      // 'profile': 'profile',
      'scoreboard': 'scoreboard',
      'createProfile': 'createProfile',
      'rating': 'rating',
      '*other': 'default'
    },
    index: function() {
      homeView.show();
    },
    game: function() {
      gameView.show();
    },
    login: function() {
      loginView.show();
    },
    signup: function() {
      signupView.show();
    },
    // profile: function() {
    //   profileView.show();
    // },
    scoreboard: function() {
      scoreboardView.show();
    },
    rating: function() {
      ratingView.show();
    },
    createProfile: function() {
      console.log('createProfile')
      createProfileView.show();
    },
    default: function () {
      alert('404');
    },
    toGame: function () {
      this.navigate('game', {trigger: true});
    },
    toLogin: function () {
      this.navigate('login', {trigger: true});
    },
    toMain: function () {
      this.navigate('', {trigger: true});
    }
  });

  return new Router();
});
