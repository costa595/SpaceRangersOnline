define([
    'backbone',
    'tmpl/rating',
    'models/rating',
    ], function (Backbone, tmpl, ratingModel) {
    var ratingView = Backbone.View.extend({
        tagName : 'div',
        className : 'rating',
        model : ratingModel,
        events: {
            'click .close_rating': 'closeRating',
            'click .table_rating': 'showStatistic'
        },
        template: function () {
          return tmpl(this.model);
        },
        initialize: function() {
            this.listenTo(this.model, 'change', this.render);
            this.render();
        },
        render: function() {
            if(!$.isEmptyObject(this.model.attributes)) {
                this.$el.html(this.template());
                return this;
            }
        },
        showStatistic: function(event) {

            var elem = $(event.target);
            var num = elem.parent().attr('num');
            var id = '#' + elem.parent().attr('id');

            if(num == null) {
                num = elem.parent().parent().attr('num');
                id = '#' + elem.parent().parent().attr('id');
            }

            this.statisticSet(num, id);

        },
        statisticSet: function(num, elementId) {
            
            $('.rating_open').css('background', 'url(/images/rating/background' + this.model.attributes.mainObj[num].race + '.png)');
            // $(".table_rating_info").attr('num', num);

            var src = '/images/profile/races/' + this.model.attributes.mainObj[num].race + '/' + this.model.attributes.mainObj[num].avatar + '.jpg';
            $('.rating_info_ava').css('background', 'url(' + src + ') no-repeat');

            $('.mobs').html(this.model.attributes.mainObj[num].killmobs);
            $('.players').html(this.model.attributes.mainObj[num].killplayers);
            $('.sum').html(parseInt(this.model.attributes.mainObj[num].killmobs) + parseInt(this.model.attributes.mainObj[num].killplayers));

            var choosedTab = $(".table_rating_info");
            choosedTab.slideUp('fast');
            choosedTab.delay(100).insertAfter(elementId).slideToggle();
        },
        closeRating: function() {
            this.$el.hide();
        },
        show: function () {
            this.trigger('show', this);
        }
    })

    return new ratingView();
});