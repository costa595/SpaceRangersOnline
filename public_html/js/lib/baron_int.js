define(['jquery'], function ($) {
    window.dima = baron({
        root: '.wrapper_baron',
        scroller: '.scroller_baron',
        bar: '.scroller__bar',
        barOnCls: 'baron'
      })

   baron({
       scroller: '.scroller_baron',
       bar: '.scroller__bar',
       barOnCls: 'baron'
   }).fix({
       elements: '.header__title',
       outside: 'header__title_state_fixed',
       before: 'header__title_position_top',
       after: 'header__title_position_bottom'
   }).baron({
       barOnCls: 'baron_h',
       bar: '.scroller__bar_h'
   });

 });