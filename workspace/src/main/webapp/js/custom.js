//== onWindowScroll
//
//## (스크롤 시) onWindowScrollClass 클래스 토글링
function onWindowScroll() {
    'use strict';

    var bodyEl = document.querySelector('body'),
        scrollToTopBtn = document.querySelector('.scroll-to-top');
    var onWindowScrollClass = 'onWindowScroll',
        showClass = 'show';

    var currentScroll = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
    if (currentScroll> 100) {
        bodyEl.classList.add(onWindowScrollClass);
        scrollToTopBtn.classList.add(showClass);
    } else if (currentScroll < 10) {
        bodyEl.classList.remove(onWindowScrollClass);
        scrollToTopBtn.classList.remove(showClass);
    }
}
window.onscroll = function() {
    //onWindowScroll();
};

//== scrollToTop
//
//## onWindowScrollClass 클래스 토글링
function scrollToTop() {
    'use strict';

    (function smoothscroll() {
        var currentScroll = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
        if (currentScroll > 0) {
            window.requestAnimationFrame(smoothscroll);
            window.scrollTo(0, currentScroll - (currentScroll / 5));
        }
    })();
}


//== check has child ul
//
//## 서브메뉴있는 리스트에 has_child 클래스 추가
function hasList(lis) {
    var lis = document.querySelectorAll(lis);

    for(var i = 0; i < lis.length; i++) {
        var li = lis.item(i);

        if(li.nodeType == 1 && li.childNodes[1]) {
            //console.log(li);
            li.className += ' has_child';
        }
    }
}
window.onload = function() {
    hasList('ul.nav_h > li');
}

/**
 * IE의 경우 Array의 fill 함수를 지원하지 않아 코드 추가 
 */
if(!Array.prototype.fill) {
	Object.defineProperty(Array.prototype, 'fill', {
		value: function(value) {
			if (this == null) {
				throw new TypeError('this is null or not defined');
			}
			
			var O = Object(this);
			var len = O.length >>> 0;
			var start = arguments[1];
			var relativeStart = start >> 0;
			var k = relativeStart < 0 ?
					Math.max(len + relativeStart, 0) : 
						Math.min(relativeStart, len);
					
			var end = arguments[2];
			var relativeEnd = end === undefined ? len : end >> 0;
			var final = relativeEnd < 0 ?
					Math.max(len + relativeEnd, 0) :
						Math.min(relativeEnd, len);
					
			while (k < final) {
				O[k] = value;
				k++;
			}
			
			return O;
		}
	});
}


/**
 * IE의 경우 Array의 findIndex 함수를 지원하지 않아 코드 추가
 */
if(!Array.prototype.findIndex) {
	Object.defineProperty(Array.prototype, 'findIndex', {
		value: function(predicate) {
			if (this == null) {
				throw new TypeError('"this" is null or not defined');
			}
			
			var o = Object(this);
			var len = o.length >>> 0;
			if (typeof predicate !== 'function') {
				throw new TypeError('predicate must be a function');
			}
			
			var thisArg = arguments[1];
			var k = 0;
			
			while (k < len) {
				var kValue = o[k];
				if (predicate.call(thisArg, kValue, k, o)) {
					return k;
				}
				
				k++;
			}
			
			return -1;
		}
	});
}


//== library
//
//## bootstrap, bootstrap-select, iCheck...
$(function() {
    'use strict';

    // Bootstrap > collapse
    function openAllPanels(aId) {
        //console.log('setAllPanelOpen');
        $(aId + ' .panel-collapse:not(".in")').collapse('show');
    }
    function closeAllPanels(aId) {
        //console.log('setAllPanelclose');
        $(aId + ' .panel-collapse.in').collapse('hide');
    }

    function resetAllPanels() {
        $('.toggle_panels')
            .removeClass('active')
    }
    function toggleAllPanels(e) {
        var targetId = $(e.target).attr('target-id'),
            numPanelOpen = $(targetId + ' .collapse.in').length;

        // $(this).toggleClass('active');
        if (numPanelOpen == 0) {
            openAllPanels(targetId);
            $(this).addClass('active');
        } else {
            closeAllPanels(targetId);
            $(this).removeClass('active');
        }
    }
    // Bootstrap Panel Collapse 화살표 방향 바꾸기
    function toggleActivePanels(e) {
        $(e.target)
            .prev('.panel_h')
            .toggleClass('active')
            .find('.toggle_panel')
            .toggleClass('active')
    }

    $('.toggle_panels').on('click', toggleAllPanels);
    $('.toggle_panel').on('click', resetAllPanels);

    $('.panel-collapse').on('hide.bs.collapse', toggleActivePanels);
    $('.panel-collapse').on('show.bs.collapse', toggleActivePanels);


    // Bootstrap-select(2018.03.07/view template작업시작하면서, 아래코드로인해 dropbox가 2개생성되는 현상을 막기위해 제거)
    /*if ( typeof $.fn.selectpicker != 'undefined' ) {
        $('.selectpicker').selectpicker()
    } else {
        return false;
    }*/

    // Bootstrap-datepicker
    if ( typeof $.fn.datepicker != 'undefined' ) {
        $('.input-daterange input').each(function() {
            $(this).datepicker('clearDates');
        });
    } else {
        return false;
    }

    // iCheck
    if ( typeof $.fn.iCheck != 'undefined' ) {
        $('.minimal').iCheck({
            checkboxClass: 'icheckbox_minimal',
            radioClass: 'iradio_minimal',
            checkedClass: 'checked',
            increaseArea: '20%'
        });
        $( '.square_grey' ).iCheck({
            checkboxClass: 'icheckbox_square-grey',
            radioClass: 'iradio_square-grey',
            checkedClass: 'checked',
            increaseArea: '20%'
        });
        $( '.square_green' ).iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
            checkedClass: 'checked',
            increaseArea: '20%'
        });
    } else {
        return false;
    }
})

//# sourceMappingURL=maps/custom.js.map
