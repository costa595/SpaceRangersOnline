define([
  'dragLib',
  'backbone',
  'tmpl/profile',
  'models/user'
], function (dragLib, Backbone, tmpl, userModel) {
  var weaponFromHoldToInv = false;
  // var weaponOnWeaponInv = false;
  var swapWeapon = false;
  var anotherElement = false;
  var setId = '';
  var weaponSetId = '';
  var setSrc = '';
  var cellClicked = false;
  var invClick = false;

  var weaponClicked = false;
  var otherClicked = false;

  var swapItemsInv = false;
  var itemToInv = false;

  var itemDeleted = false;

  var itemSettings = {
    speed: '',
    blockGenerator: ''
  }

  var itemSettingsInv = {
    speedInv: '', 
    blockGeneratorInv: ''
  }

  var ProfileView = Backbone.View.extend({
    url: '/api/v1/profile',
    tagName: 'div',
    className: 'wrap_profile',
    model: userModel,
    events: {
      'click .object_in_inventory': 'inventoryClick',
      'click .object_in_cell': 'cellClick',
      'click .close_profile': 'closeProfile',
      'click .wrapGate': 'deleteItem',

      'mousedown .draggable': 'detectDrag',
      'dragstart .draggable': 'dragstart',
      'dragend .draggable': 'dragend',

      'dragenter .dropable': 'dragenter',
      'dragleave .dropable': 'dragleave',
      'drop .dropable': 'drop'
    },
    detectDrag: function (event) {
      $(event.currentTarget).detectDrag();
    },
    dragstart: function (event, data, clone, element) {
      data.someProperty = 'someValue';
      data.set('someValue');
    },
    dragenter: function (event, clone, element) {
      var dropContainer = $(event.currentTarget);
      dropContainer.animate({opacity: 1}, 'fast');
    },
    dragleave: function (event, clone, element) {
      var dropContainer = $(event.currentTarget);
      dropContainer.animate({opacity: 0.5}, 'fast');
    },
    drop: function (event, data, clone, element) {
      console.log(data.someProperty);
      console.log(data);
    },
    dragend: function (event, clone, element) {

    },
    stopDrag: function (event) { 
      var $target = $(event.target),
      $window = $(window); 

      if (isDragging) { 
        $clone.remove(); 
        if ($target.attr('dropable')) { 
          $target.trigger('drop', [data, $clone, $el]); 
        } else { 
          $el.trigger('dragend', [$clone, $el]); 
        } 
      } 

      isDragging = false; 
      $el.off('selectstart', preventSelection); 
      $('body').css('user-select', 'auto');
      $window.off('mousemove', handleDrag).off('mouseup', stopDrag); 
    },
    template: function () {
      return tmpl(this.model);
    },
    initialize: function() {
      this.listenTo(this.model, 'change', this.render);
      this.render();
    },
    inventoryClick: function(event) {

      if(!weaponClicked && !itemToInv) {
        var elem =  $(event.target),
            path = '/images/profile/res/',
            format = '.png',
            id = "",
            that = this,
            itemIsWeapon = false;
            if(!swapItemsInv) {
              invClick = true;
            }

        var holdsArray = new Array();
        for(var i = 1; i < 7; i++) {
          holdsArray.push('#object_in_inventory_' + i);
        }

        var holdStr = holdsArray.join(', ');

        $('.background_profile').find('.active').addClass('use').removeClass('active');
        var invType = elem.parent().attr('inv_got_type');
        var invHoldNum = elem.parent().attr('inv_hold_num');
        var elemId = '#' + elem.parent().attr('id');
        var invId = elem.parent().attr('inv_got_id');

        if(invType == 'engine') {
          itemSettingsInv.speedInv = elem.parent().attr('speed');
          elem.parent().attr('speed', '');
        } else if(invType == 'generator') {
          itemSettingsInv.blockGeneratorInv = elem.parent().attr('block');
          elem.parent().attr('block', '');
        }

        setId = elemId;

        if (['w1','w2','w3','w4','w5','gun'].indexOf(invType) > -1) {
          id = "cell_weapon_1_1, #cell_weapon_1_2, #cell_weapon_2_1, #cell_weapon_2_2, #cell_weapon_2_3";
          itemIsWeapon = true;
        } else {
          switch(invType) {
            case 'engine':
              id = "cell_engine"
              break;
            case 'radar':
              id = "cell_radar";
              break;
            case 'scanner':
              id = "cell_scanner";
              break;
            case 'fuel':
              id = "cell_fuel";
              break;
            case 'generator':
              id = "cell_generator";
              break;
            case 'hook':
              id = "cell_hook";
              break;
            case 'droid':
              id = "cell_droid";
              break;
          }
        }

        var element = $('#' + id);
        var newImage = path + elem.attr('src').match(/\d+/) + format;

        if(!swapWeapon && !swapItemsInv) {
          element.addClass('active').removeClass('use');
          $('.app').css('cursor','url(' + newImage + '), auto');
          elem.remove();
        }
        //---------------------------------------------клетки-------------------
        element.bind("click", function() { 

          if(itemIsWeapon) {
            invType = that.compare('#' + $(this).attr('id'));
          }

          if( $(this).children().attr('src') == null ) {
            var img = $('<img class="object_in_cell">');
            img.attr('src', newImage);

            img.appendTo(this);
            element.removeClass('active');
            elem.parent().attr('inv_got_type', '');
            elem.parent().attr('inv_got_id', 0);
            element.unbind();
            $(holdStr).unbind();
            that.sendChangedInfo({
              'action' : 'swap',
              'setItem' : invType,
              'unSetItem' : invHoldNum
            });

          } else {

            weaponFromHoldToInv = true;
            var oldSrc = $(this).children().attr('src');
            $(this).children().attr('src', newImage);
            element.removeClass('active');
            element.unbind();
            $(holdStr).unbind();
            var img = $('<img class="object_in_inventory">');
            img.attr('src', oldSrc);
            img.appendTo(elemId);

            that.sendChangedInfo({
              'action' : 'swap',
              'setItem' : invHoldNum,
              'unSetItem' : invType
            });

          }

          if($(elemId).attr('speed') != null) {
            $('#speed_static_count').text(itemSettingsInv.speedInv);
          } else if($(elemId).attr('block') != null) {
            var defence = $('#defence_static_count');
            var oldVal = defence.html();
            oldVal = oldVal.split('+')[0];
            defence.text(oldVal + '+' + itemSettingsInv.blockGeneratorInv + '%');
          }

          element.each(function() {
            if ($(this).children().attr('src') != null) {
              $(this).addClass('use');
            }
          });
          invClick = false;
          $('.app').css('cursor','default');

        });
        
        //----------------------------------------------ТРЮМ--------------------
        $(holdStr).bind('click', function() {
          if(!swapItemsInv) {
            var oldSrc = $(this).children().attr('src');
            var OldInvHoldNum = $(this).attr('inv_hold_num');

            if( oldSrc == null ) {
              var img = $('<img class="object_in_inventory">');
              img.attr('src', newImage);
              img.appendTo(this);

              $(elemId).attr('inv_got_type', '');
              $(elemId).attr('inv_got_id', 0);
              if(itemSettingsInv.speedInv != '') {
                $(this).attr('speed', itemSettingsInv.speedInv);
              } else if(itemSettingsInv.blockGeneratorInv != '') {
                $(this).attr('block', itemSettingsInv.blockGeneratorInv);
              }
              $(this).attr('inv_got_type', invType);
              $(this).attr('inv_got_id', invId);
              $('.cell').unbind();
              $(holdStr).unbind();
              that.sendChangedInfo({
                'action' : 'swap',
                'setItem' : invHoldNum,
                'unSetItem' : OldInvHoldNum
              });

            } else {

              swapItemsInv = true;
              var old = this;
              var OldInvType = $(old).attr('inv_got_type');
              var OldInvId = $(old).attr('inv_got_id');

              if(OldInvType == 'engine'){
                var OldSpeedInv = $(old).attr('speed');
                $(old).attr('speed', '');
              } else if(OldInvType == 'generator') {
                var OldBlockGeneratorInv = $(old).attr('block');
                $(old).attr('block', '');
              }

              var img = $('<img class="object_in_inventory">');
              img.attr('src', oldSrc);
              img.appendTo(elemId);

              $(old).children().attr('src', newImage);
              $(old).attr('inv_got_type', invType);
              $(old).attr('inv_got_id', invId);

              if(itemSettingsInv.speedInv != '') {
                $(old).attr('speed', itemSettingsInv.speedInv);
              } else if(itemSettingsInv.blockGeneratorInv != '') {
                $(old).attr('block', itemSettingsInv.blockGeneratorInv);
              }

              $(elemId).attr('inv_got_type', OldInvType);
              $(elemId).attr('inv_got_id', OldInvId);

              if(OldSpeedInv != '') {
                $(elemId).attr('speed', OldSpeedInv);
              } else if(OldBlockGeneratorInv != '') {
                $(elemId).attr('block', OldBlockGeneratorInv);
              }

              $('.cell').unbind();
              $(holdStr).unbind();

            }
            element.removeClass('active');

            element.each(function() {
              if ($(old).children().attr('src') != null) {
                $(old).addClass('use');
              }
            });

            $('.app').css('cursor','default');

            invClick = false;
          } else {
            swapItemsInv = false;
            invClick = false;
            $('.cell').unbind();
            $(holdStr).unbind();
          }
        });
      }
    },
    cellClick: function(event) {

      if(!invClick) {
        console.log('***************** cellClick ************')
        var elem =  $(event.target),
            path = '/images/profile/res/',
            format = '.png',
            that = this;
        cellClicked = true;

        var weaponsArray = ['#cell_weapon_1_1', '#cell_weapon_1_2', '#cell_weapon_2_1', '#cell_weapon_2_2', '#cell_weapon_2_3'];
        var tmpArr = new Array();

        var holdsArray = new Array();
        for(var i = 1; i < 7; i++) {
          holdsArray.push('#object_in_inventory_' + i);
        }

        var choosedItem = elem.attr('src');

        var holdStr = holdsArray.join(', ');
        if(!otherClicked) {
          var weaponId = '#' + elem.parent().attr('id');
        }
        //--------------------------------------------оружие-----------------------------------------------
        if (weaponsArray.indexOf(weaponId) > -1) {
          
          var weaponsId = weaponsArray.join(',');
          var setWeapon = 0;
          // var weaponId = weaponId;
          weaponSetId = id;
          setWeapon = that.compare(weaponId);

            // console.log(weaponOnWeaponInv)
            if(!otherClicked) { //----если замена оружия, то не забираем его, а свопаем
              weaponClicked = true;
              $('.app').css('cursor','url(' + choosedItem + '), auto');
              elem.remove();
              $(weaponsId).addClass('active').removeClass('use');
            } 
            //   weaponFromHoldToInv = false;
              // weaponOnWeaponInv = false;
            // }
          
            //---------------------меняем одетые оружия местами
            $(weaponsId).bind("click", function() {
              // if(!weaponOnWeaponInv) {
                if(!otherClicked) {
                var oldSrc = $(this).children().attr('src');
                var element = $(weaponsId);

                if(oldSrc == null) {
                  var img = $('<img class="object_in_cell">');
                  img.attr('src', choosedItem);
                  img.appendTo(this);
                } else {
                  $(this).children().attr('src', choosedItem);
                  var img = $('<img class="object_in_cell">');
                  img.attr('src', oldSrc);
                  img.appendTo(weaponId);
                  // weaponOnWeaponInv = true;
                  // console.log(weaponOnWeaponInv)
                }

                element.removeClass('active');

                element.each(function() {
                  if ($(this).children().attr('src') != null) {
                    $(this).addClass('use');
                  }
                });
                
                element.unbind();
                $(holdStr).unbind();

                unSetWeapon = that.compare('#' + this.id);

                that.sendChangedInfo({
                  'action' : 'swap',
                  'setItem' : setWeapon,
                  'unSetItem' : unSetWeapon
                });
                weaponId = 0;
                $('.app').css('cursor','default');
                weaponClicked = false;
               }
              // } else {
              //   // weaponOnWeaponInv = false;
              // }
            });

            //---------------------ставим оружие в трюм
            $(holdStr).bind('click', function() {

              if(!otherClicked) {
                var oldInvSrc = $(this).children().attr('src');
                var invType = $(this).attr('inv_got_type');
                var index = choosedItem.match(/\d+/);

                if(oldInvSrc == null) {
                  var img = $('<img class="object_in_inventory">');
                  img.attr('src', choosedItem);
                  img.appendTo(this);
                  $(this).attr('inv_got_type', setWeapon);
                  $(this).attr('inv_got_id', index);

                  that.sendChangedInfo({
                    'action' : 'swap',
                    'setItem' : setWeapon,
                    'unSetItem' : $(this).attr('inv_hold_num')
                  });

                  $(weaponId).children().remove();
                  $(weaponsId).removeClass('active');
                  $(weaponsId).each(function() {
                    if ($(this).children().attr('src') != null) {
                      $(this).addClass('use');
                    }
                  });
                  weaponClicked = false;
                  $(weaponsId).unbind();
                  $(holdStr).unbind();
                  weaponId = 0;
                  $('.app').css('cursor','default');

                } else {

                  if (['w1','w2','w3','w4','w5','gun'].indexOf(invType) > -1) {
                    swapWeapon = true;
                    $(this).children().attr('src', choosedItem);
                    $(this).attr('inv_got_id', index);
                    var img = $('<img class="object_in_cell">');
                    img.attr('src', oldInvSrc);
                    img.appendTo(weaponId);

                    $(weaponsId).removeClass('active');
                    $(weaponsId).each(function() {
                      if ($(this).children().attr('src') != null) {
                        $(this).addClass('use');
                      }
                    });

                    that.sendChangedInfo({
                      'action' : 'swap',
                      'setItem' : setWeapon,
                      'unSetItem' : $(this).attr('inv_hold_num')
                    });
                    weaponClicked = false;
                    $(weaponsId).unbind();
                    $(holdStr).unbind();
                    weaponId = 0;
                    $('.app').css('cursor','default');
                  }

                }
              }
            });

        } else {
          //--------------------------------------------остальное-----------------------------------------------

            if(!anotherElement && !weaponClicked) {
              var id = '#' + elem.parent().attr('id');
              setId = id;
              setSrc = elem.attr('src');
              otherClicked = true;
              $(id).addClass('active').removeClass('use').addClass('clicked');

              $('.app').css('cursor','url(' + choosedItem + '), auto');
              $(id).children().remove();
              if(id == '#cell_engine') {
                itemSettings.speed = $(id).attr('speed');
                $('#speed_static_count').text('0');
              } else if(id == '#cell_generator') {
                itemSettings.blockGenerator = $(id).attr('block');
                var defence = $('#defence_static_count');
                var oldVal = defence.html();
                oldVal = oldVal.split('+')[0];
                defence.text(oldVal + '+0%');
              }
            } 

            //---------------------клики по клеткам
            $('.cell').bind("click", function() {
              if(!weaponClicked) {
                if(setId == '#' + $(this).attr('id')) {
                  if(setId == '#cell_engine') {
                    $('#speed_static_count').text(itemSettings.speed);
                  } if(setId == '#cell_generator') {
                    var defence = $('#defence_static_count');
                    var oldVal = defence.html();
                    oldVal = oldVal.split('+')[0];
                    defence.text(oldVal + '+' + itemSettings.blockGenerator + '%');
                  }
                  var oldSrc = $(this).children().attr('src');
                  var element = $('.cell');

                  var img = $('<img class="object_in_cell">');
                  img.attr('src', setSrc);
                  img.appendTo(this);

                  element.removeClass('active');
                  element.each(function() {
                    if ($(this).children().attr('src') != null) {
                      $(this).addClass('use');
                    }
                  });
                  
                  element.unbind();
                  $(holdStr).unbind();
                  $('.cell').unbind();
                  id = 0;
                  $('.app').css('cursor','default');
                  otherClicked = false;
                  anotherElement = false;
                  itemToInv = false;
                } else {
                  if(!itemDeleted) {
                    anotherElement = true;
                  } else {
                    itemDeleted = false;
                  }
                  $('.cell').unbind();
                }
              }
            });

            //----------------------------------ставим в трюм
            $(holdStr).bind('click', function() {
              if(!weaponClicked) {
                var oldInvSrc = $(this).children().attr('src');
                var index = choosedItem.match(/\d+/);
                var invType = $(this).attr('inv_got_type');

                if(oldInvSrc == null) {
                  var img = $('<img class="object_in_inventory">');
                  img.attr('src', choosedItem);
                  img.appendTo(this);
                  var itemType = id.replace('#cell_', '');
                  $(this).attr('inv_got_type', itemType);
                  $(this).attr('inv_got_id', index);

                  if(itemType == 'engine') {
                    $(this).attr('speed', itemSettings.speed);
                  } else if(itemType == 'generator') {
                    $(this).attr('block', itemSettings.blockGenerator);
                  }

                  that.sendChangedInfo({
                    'action' : 'swap',
                    'setItem' : id.replace('#cell_',''),
                    'unSetItem' : $(this).attr('inv_hold_num')
                  });

                  $(id).removeClass('active');
                  anotherElement = false;
                  $('.app').css('cursor','default');
                  $('.cell').unbind();
                  $(holdStr).unbind();
                  $(id).removeClass('active').removeClass('clicked');
                  id = 0;
                  otherClicked = false;
                } else {
                  itemToInv = true;
                  if(invType == id.replace('#cell_','')) {
                    $(this).children().attr('src', choosedItem);
                    $(this).attr('inv_got_id', index);
                    elem.attr('src', oldInvSrc);

                    that.sendChangedInfo({
                      'action' : 'swap',
                      'putItem' : id.replace('#cell_',''),
                      'setItem' : invType
                    });

                    itemToInv = false;
                    $('.app').css('cursor','default');
                    $('.cell').unbind();
                    $(holdStr).unbind();
                    $(id).removeClass('active').removeClass('clicked');
                    id = 0;
                    otherClicked = false;
                  }
                  anotherElement = false;
                }
              }
            });
        }
      }
    },
    deleteItem: function() {
      var that = this;
      var elements = $('.cell');
      var element = $(setId);

      elements.removeClass('active');
      elements.each(function() {
        if ($(this).children().attr('src') != null) {
          $(this).addClass('use');
        }
      });
      $(setId).children().remove('img');
            
      elements.unbind();
      $('.place_in_inventory').unbind();

      if(weaponSetId == 0) {
        var deletedItem = setId.replace('#cell_', '');
      } else {
        var deletedItem = weaponSetId;
      }

      $('.app').css('cursor','default');

      if(setId.indexOf('cell') == 1) {
        if(setId == '#cell_engine') {
          element.removeAttr('speed');
        } else if(setId == '#cell_generator') {
          element.removeAttr('block');
        }
      } else if (setId.indexOf('object') == 1) {
        element.attr('inv_got_id', 0);
        if(element.attr('inv_got_type') == 'engine') {
          element.removeAttr('speed');
        } else if(element.attr('inv_got_type') == 'generator') {
          element.removeAttr('block');
        }
        element.attr('inv_got_type', '');
      }

      weaponFromHoldToInv = false;
      // weaponOnWeaponInv = false;
      swapWeapon = false;
      anotherElement = false;
      setId = '';
      weaponSetId = '';
      setSrc = '';
      cellClicked = false;
      invClick = false;
      weaponClicked = false;
      otherClicked = false;

      swapItemsInv = false;
      itemToInv = false;

      // that.sendChangedInfo({
      //   'action' : 'delete',
      //   'deletedItem' : deletedItem
      // });

      itemDeleted = true;
          
    },
    sendChangedInfo: function(data) {
      $.ajax({
        url: this.url,
        type: 'POST',
        data: data,
        success: function(resp) {
          if (resp.status === 200) {
            console.log('resp', resp)
            console.log("200");
          } else if (resp.status === 403) {
            console.log("403");
          }
          console.log("success")
        },
        error: function() {
          console.log("sending data error");
        }
      });
    },
    compare: function(dataId) {
      var returnedWeapon = '';
      switch(dataId) {
        case '#cell_weapon_1_1':
          returnedWeapon = 'w1';
          break;
        case '#cell_weapon_1_2':
          returnedWeapon = 'w2';
          break;
        case '#cell_weapon_2_1':
          returnedWeapon = 'w3';
          break;
        case '#cell_weapon_2_2':
          returnedWeapon = 'w4';
          break;
        case '#cell_weapon_2_3':
          returnedWeapon = 'w5';
          break;
      }
      return returnedWeapon;
    },
    render: function() {
      if(!$.isEmptyObject(this.model.attributes)) {
        this.$el.html(this.template());
        return this;
      }
    },
    show: function () {
      this.trigger('show', this);
    },
    closeProfile: function() {
      var cursorType = $('.app').css('cursor');
      if(cursorType.indexOf("url") > -1) {

        cursorType = cursorType.replace('8000', '');
        var elementId = cursorType.match(/\d+/);

        if(cellClicked) {
          var img = $('<img class="object_in_cell">');
          cellClicked = false;
        } else if(invClick) {
          var img = $('<img class="object_in_inventory">');
          invClick = false;
        }
        img.attr('src', '/images/profile/res/' + elementId + '.png');
        if(weaponSetId != null) {
          img.appendTo(weaponSetId);
        }
        if(setId != null) {
          img.appendTo(setId);
        }
        var cell = $('.cell');
        cell.removeClass('active');
        cell.each(function() {
          if ($(this).children().attr('src') != null) {
            $(this).addClass('use');
          }
        });
        $('.app').css('cursor', 'default');
      }

      this.$el.hide();
    }
  });

  return new ProfileView();
});
