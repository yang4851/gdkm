var BYTE_UNITS = ['B', 'kB', 'MB', 'GB', 'TB', 'PB'];
var COUNT_UNITS = ['', 'K', 'M', 'G', 'T', 'P'];

/**
 * 공개여부 코드를 Label로 출력
 */
gdkmApp.filter('openYn', function() {
	
	var _DISPLAY_NAMES = {
		'Y' : 'Open',
		'N' : 'Close'
	}
	
	return function(openYn) {
		if(_DISPLAY_NAMES.hasOwnProperty(openYn)) {
			return _DISPLAY_NAMES[openYn]
		}
			
		return _DISPLAY_NAMES['N'];
	}
});

/**
 * 공개여부 코드를 Label로 출력
 */
gdkmApp.filter('registStatus', function() {
	
	var _DISPLAY_NAMES = {
		'ready' : 'Registration reception',
		'validating' : 'Verifying file',
		'validated' : 'Finishing Verification',
		'error' : 'File error',
		'submit' : 'Submitting',
		'failed' : 'Rejecting submission',
		'success' : 'Finishing registration'
	}
	
	return function(status) {
		if(_DISPLAY_NAMES.hasOwnProperty(status)) {
			return _DISPLAY_NAMES[status]
		}
			
		return _DISPLAY_NAMES['ready'];
	}
});

/**
 * Taxonomy 계층 순위 표시 
 */
gdkmApp.filter('taxonRank', function() {
	
	var _DISPLAY_NAMES = {
		"superkingdom" : "Super kingdom", 
		"subkingdom" : "Sub kingdom", 
		"kingdom" : "Kingdom", 
		"phylum" : "Phylum", 
		"class" : "Class", 
		"order" : "Order", 
		"family" : "Family", 
		"genus" : "Genus", 
		"species" : "Species"
	}
	
	return function(rank) {
		if(_DISPLAY_NAMES.hasOwnProperty(rank)) {
			return _DISPLAY_NAMES[rank]
		}
			
		return "No rank";
	}
});

/**
 * NGS 데이터 파일 형식 표시 
 */
gdkmApp.filter('fileFormat', function() {
	
	return function(file) {
		if(angular.isString(file))
			return Utils.capitalizeFirstLetter(file);
		
		try {
			if(angular.isObject(file) && angular.isString(file.fileType)) {
				if(FILE_TYPE.OTHER == file.fileType) {
					let extension = FileUtils.getExtension(file.fileName);
					return Utils.capitalizeFirstLetter(FILE_TYPE.OTHER) + " (" + extension + ")";
				}
				
				return Utils.capitalizeFirstLetter(file.fileType);
			}
		} catch(err) {
			console.error(err);
		}
		
		return Utils.capitalizeFirstLetter(FILE_TYPE.OTHER); 
	}
});

/**
 * byte형 파일 크기를 읽을 수 있는 파일크기로 출력
 * 
 * [parameters] 
 *   bytes : byte형 파일크기
 *   scale : 소수점 표시 자리수
 * 
 * 1. 기본사용 방법 
 * 
 * {{ 1500 | bytes }} ## 출력은 0.7KB
 * {{ 1500 | bytes:1 }} ## 출력은 1kB
 */
gdkmApp.filter('bytes', function() {
	return function(bytes, scale) {
		if (!angular.isNumber(bytes) || !isFinite(bytes) || bytes == 0) 
			return '0';
		
		var unit = Math.floor(Math.log(bytes) / Math.log(1024));
		scale = (unit == 0)? 0 : (angular.isNumber(scale)? scale : 1);
		
		return (bytes / Math.pow(1024, Math.floor(unit))).toFixed(scale) +  '' + BYTE_UNITS[unit];
	};
});

/**
 * 개수를 1000단위 크기로 변환해 출력 
 */
gdkmApp.filter('counts', function() {
	return function(count, scale) {
		if (!angular.isNumber(count) || isFinite(count) || bytes == 0) 
			return '0';
		
		var unit = Math.floor(Math.log(count) / Math.log(1000));
		scale = (unit == 0)? 0 : (angular.isNumber(scale)? scale : 1);
		
		return (count / Math.pow(1000, Math.floor(unit))).toFixed(scale) +  '' + COUNT_UNITS[unit];
	};
});

/**
 * 등록번호와 ID를 갖고 있는 목록을 받으면 "XXXX 외 1개" 이런 식으로 표시되도록 하기위한 directive  
 * 
 * (파라미터)
 * - models : 모델 목록 배열
 * - field : 모델의 변수명 
 * - expr : 모델 목록 표현 방식 
 *     > abbr = 약식으로 표현 (예시 : XX 외 1건)
 *     > pipe = 파이프로 연결해 모든 목록 표시 (예시 : XX|XY|XZ)
 * 
 * (사용예제) 
 * {{ item.attachments | models }}
 * {{ item.attachments | models :'name' }}
 * {{ item.attachments | models :'name':'pipe' }}
 * @returns
 */
gdkmApp.filter('models', function() {
	return function(models, field, expr) {
		if(Utils.isBlank(field))
			field = 'name';
		
		if(angular.isArray(models)){
			if(models.length == 0)
				return '-';
			
			if(expr == 'pipe') {
				let labels = [];
				for(let i=0; i < models.length ; i++) {
					let value = (angular.isObject(models[0]))? models[0][field] : models[0];
					if(labels.indexOf(value) < 0)
						labels.push(value);
				}
				
				return labels.join('|');
			} else {
				var value = (angular.isObject(models[0]))? models[0][field] : models[0];
				if(models.length == 2) { 
					value += ' and 1 other';
				} else if(models.length > 2) {
					value += ' and ' + (models.length-1) + ' others';
				}
				
				return value;
			}
		}
		
		return (angular.isObject(models))? models[field] : models;
	}
});

