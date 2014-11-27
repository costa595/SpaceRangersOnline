define([
  'backbone',
  'tmpl/createProfile',
  'models/createProfile'
], function (Backbone, tmpl, positionCreateProfile) {
  var createProfileView = Backbone.View.extend({
    initialize: function() {
      last_choose_race = 'race_malok';
      last_choose_class = 'class_war';
      this.render();
      this.setCenter();
      this.listenTo(positionCreateProfile, 'window:resize', this.setCenter);
      this.listenTo(positionCreateProfile, 'race:malok', this.chooseRace);
    },
    tagName : 'div',
    className : 'createProfile',
    model : positionCreateProfile,
    template: tmpl,
    render: function() {
      this.$el.html(this.template());
      return this;
    },
    events: {
      "click #race_malok,#race_peleng,#race_human,#race_fainin,#race_galec": "chooseRace",
      "click #class_war,#class_mercenary,#class_trade,#class_corsar,#class_pirate": "chooseClass",
      "click #apply_name_person" : "chooseResult"
    },
    show: function () {
      //console.log('showfunction',this);
      this.trigger('show', this);
    },
    setCenter: function () {
      var windows = this.model.sizeWindow();
      var windowPosition = this.model.position(windows);
      var marginCount = this.model.changePosition(windowPosition);
      marginCount = marginCount.toJSON();
      //console.log(marginCount);
      //console.log(this.$('#windowCreateProfile'));
      this.$('#windowCreateProfile').css({'left':marginCount.marginleft,'top':marginCount.margintop});
    },
    chooseRace: function(e){
      $('#'+last_choose_race).removeClass('choose_race');
      this.model.changeModelPerson('race',$(event.target).attr('id'))
      $('#'+$(event.target).attr('id')).addClass('choose_race');
      last_choose_race = $(event.target).attr('id');
      this.infoAboutRaceAndClass('race',last_choose_race);
    },
    chooseClass: function(e){
//      console.log($(event.target).attr('id'))
      $('#'+last_choose_class).removeClass('choose_class');
      this.model.changeModelPerson('class',$(event.target).attr('id'))
      $('#'+$(event.target).attr('id')).addClass('choose_class');
      last_choose_class = $(event.target).attr('id');
      this.infoAboutRaceAndClass('class',last_choose_class);
    },
    chooseResult: function(){
      this.model.changeModelPerson('name',$('#input_new_name_peson').val())
      if (result.name != ''){
        console.log('отправить на сервер=',result)
      }
      else{
        alert('введите имя')
      }
    },
    infoAboutRaceAndClass: function(type,currentChoose){
       if (type == 'race'){
        if(currentChoose == 'race_malok'){
          $('#info_about_person').html('race_malok')
        }
        if(currentChoose == 'race_peleng'){
          $('#info_about_person').html('race_peleng')
        }
        if(currentChoose == 'race_human'){
          $('#info_about_person').html('race_human')
        }
        if(currentChoose == 'race_fainin'){
          $('#info_about_person').html('race_fainin')
        }
        if(currentChoose == 'race_galec'){
           $('#info_about_person').html('race_galec')
        }
       }
       else{
        if(currentChoose == 'class_war'){
          $('#info_about_person').html('class_war')
        }
        if(currentChoose == 'class_mercenary'){
          $('#info_about_person').html('class_mercenary')
        }
        if(currentChoose == 'class_trade'){
          $('#info_about_person').html('class_trade')
        }
        if(currentChoose == 'class_corsar'){
          $('#info_about_person').html('class_corsar')
        }
        if(currentChoose == 'class_pirate'){
           $('#info_about_person').html('class_pirate')
        }
       }
    }
  });
  return new createProfileView();
});
