var gdkmApp = angular.module('gdkmApp', [ 'ngCookies','ngRoute', 'ngResource', 'ngAnimate', 'ngSanitize',
		'ui.bootstrap', 'ui.bootstrap.accordion', 'ui.bootstrap.typeahead', 'moment-picker',
		'angular-bootstrap-select', 'checklist-model', 'ngFileUpload', 'jlareau.pnotify', 
		'googlechart','ngMaterial','ivh.treeview', 
		'ngTouch', 'ui.grid', 'ui.grid.edit', 'ui.grid.resizeColumns', 'ui.grid.pinning', 'ui.grid.autoResize'], function($mdThemingProvider) {
    $mdThemingProvider.theme('docs-dark', 'default')
    .primaryPalette('yellow')
    .dark();
});
var grid = null;
gdkmApp.config(function (ivhTreeviewOptionsProvider) {
	ivhTreeviewOptionsProvider.set({
		twistieCollapsedTpl: '<span class="fa fa-arrow-circle-right icon-spin"></span>',
		twistieExpandedTpl: '<span class="fa fa-arrow-circle-down"></span>',
		twistieLeafTpl: '<span class="fa" ng-class="(node.type==\'DEL\')? \'fa-spinner animate-spin\': \'fa-link\'"></span>'
	});
});

gdkmApp.directive('validateForm', function() {
  return {
    restrict: 'A',
    controller: function($scope, $element) {
      var $elem = $($element);
      if($.fn.parsley)
        $elem.parsley();
    }
  };
});

/**
 * 폼(Form)요소 포커스 설정 지시어(ng-focus)
 * 
 * [지시어와 엔터키 지시어 사용 예제] 
 * <input type="text" ng-model="userId" ng-enter="passwdInput=true">
 * <input type="password" ng-model="passwd" ng-focus="passwdInput" ng-enter="procLogin()">
 */
gdkmApp.directive('ngFocus', function($timeout) {
	return {
		scope: { trigger: '=ngFocus' },
		link: function(scope, element) {
			scope.$watch('trigger', function(value) {
				if(value === true) {
					element[0].focus();
					scope.trigger = false;
				}
			});
		}
	};
});

/**
 * AngularJS text enter key event process directive
 * 
 * [Usage] 
 * 
 * <input type="text" ng-model="userId" ng-enter="anyFunc()">
 * 
 */
gdkmApp.directive('ngEnter', function () {
	return function(scope, element, attrs) {
		element.bind("keydown keypress", function (event) {
			if(event.which === 13) {
				scope.$apply(function (){
					scope.$eval(attrs.ngEnter);
				});
				
				event.preventDefault();
            }
        });
	};
});

/**
 * grid
 * 
 */
gdkmApp.directive('uiGridResize', function(gridUtil, uiGridConstants) {
	return {
		restrict: 'A',
		require: 'uiGrid',
		link: function($scope, $elm, $attrs, uiGridCtrl) {
			$scope.$watch($attrs.uiGrid + '.minRowsToShow', val => {
				var grid = uiGridCtrl.grid;
				uiGridCtrl.scrollbars = [];
				
				var contentHeight = grid.options.minRowsToShow * grid.options.rowHeight;
				var headerHeight = grid.options.hideHeader? 0 : grid.options.headerRowHeight;
				var footerHeight = grid.options.showFooter? grid.options.footerRowHeight : 0; 
				var scrollbarHeight = grid.optionsenableScrollbars? gridUtil.getScrollbarWidth() : 0; 
				
				var maxNumberOfFilters = 0; 
				
				angular.forEach(grid.options.columnDefs, function(col) {
					if(col.hasOwnProperty('filter')) {
						maxNumberOfFilter = Math.max(1, maxNumberOfFilters);
					} else if (col.hasOwnProperty('filters')) {
						maxNumberOfFilters = Math.max(maxNumberOfFilters, col.filters.length);
					}
				});
				
				var filterHeight = maxNumberOfFilters * headerHeight;
				var newHeight = headerHeight + contentHeight + footerHeight + scrollbarHeight + filterHeight + 19;
				
				$elm.css('height', newHeight + "px");
				grid.gridHeight = $scope.gridHeight = gridUtil.elementHeight($elm);
				
				grid.refreshCanvas();
			});
		}
	};
});

/**
 * AngularJS Checkbox List Settings.
 */
gdkmApp.directive('iCheck', ['$timeout', '$parse', function ($timeout, $parse) {
	return {
		require: 'ngModel',
		link: function ($scope, element, $attrs, ngModel) {
			return $timeout(function () {
				var value = $attrs.value;
				var $element = $(element);
				    
				// Instantiate the iCheck control.
				$element.iCheck({
					checkboxClass: 'icheckbox_minimal',
					radioClass: 'iradio_minimal',
					checkedClass: 'checked',
					increaseArea: '20%'
				});
			
				// If the model changes, update the iCheck control.
				$scope.$watch($attrs.ngModel, function (newValue) {
					$element.iCheck('update');
				});
				
				// If the iCheck control changes, update the model.
				$element.on('ifChanged', function (event) {
					if ($element.attr('type') === 'radio' && $attrs.ngModel) {
						$scope.$apply(function () {
						ngModel.$setViewValue(value);
						});
					} else if ($element.attr('type') === 'checkbox' && $attrs.ngModel) {
						$scope.$apply(function () {
						ngModel.$setViewValue(event.target.checked);
						});
					}
				});
			});
		}
	};
}]);