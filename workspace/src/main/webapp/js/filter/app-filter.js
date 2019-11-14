gdkmApp.filter('openStatus', function() {
	var _DISPLAY_NAMES = {
		'Y' : 'Open',
		'N' : 'Close'
	}
	return function(openYn) {
		if(_DISPLAY_NAMES.hasOwnProperty(openYn)) {
			return _DISPLAY_NAMES[openStatus]
		}
			
		return _DISPLAY_NAMES['N'];
	}
});