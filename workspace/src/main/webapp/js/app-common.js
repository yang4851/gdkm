function init_uicomponents() {
	if ($('input.flat')[0]) {
		$('input.flat').iCheck({
			checkboxClass: 'icheckbox_minimal',
			radioClass: 'iradio_minimal',
			checkedClass: 'checked',
			increaseArea: '20%'
		});
	}
	
	$('[data-toggle="tooltip"]').tooltip({ container: 'body' });
	$('[data-toggle="popover"]').popover();
}

/*
 * change dateTypeFormat
 * filter적용시, datePicker오작동으로 인해 설정. 
 */
const dateFormat = (function(){
	return {
		yyyyMMdd : function(longTypeDate) {
			return new Date(longTypeDate).toISOString().slice(0,10);
		}
	};
})();

const FILE_TYPE = {
	FASTA : 'fasta',
	FASTQ : 'fastq', 
	GFF : 'gff',
	GBK : 'gbk',
	ZIPPED : 'zipped',
	OTHER : 'other'
}

/**
 * 페이지 데이터 종류 
 * 
 * RAWDATA = 원시데이터
 * PROCESSED = 가공데이터
 * TAXONOMY = 생물종 계통분류
 * USER = 사용자
 * CODE = 공통코드
 * STATISTICS = 통계
 */
const PAGE_DATA = {
	RAWDATA : 'rawdata',
	PROCESSED : 'processed',
	RESEARCH : 'research',
	RESEARCH_ATTACHMENT : 'research_attachment',
	RESEARCH_OMICS : 'research_omics',
	TAXONOMY : 'taxonomy',
	USER : 'user',
	CODE : 'code',
	STATISTICS : 'statistics',
};

const TREE_NODE_TYPE = {
	RAWDATA : 'rawdata',
	PROCESSED : 'processed',
	TAXONOMY : 'taxonomy',
	ARCHIVE : 'archive'
}


const MESSAGE_SECTION = {
	RAWDATA : 'Raw Data',
	PROCESSED : 'Processed Data',
	RESEARCH : 'Research',
	TAXONOMY : 'Taxonomy',
	USER_KR : '사용자',
	CODE_KR : '코드',
}
/**
 * 페이지 구분
 * 
 * REGIST = 데이터 등록 관리 페이지
 * REVIEW = 데이터 제출 관리 페이지
 * PUBLIC = 공개 데이터 조회 페이지
 * SYSMTE = 시스템 관리 페이지
 */
const PAGE_SECTION = {
	REGIST : 'regist',
	REVIEW : 'review',
	PUBLIC : 'public',
	SYSTEM : 'system',
};


/**
 * 승인상태 
 * 
 * ACCEPT = 승인상태
 * REJECT = 반려상태
 */
const APPROVAL_STATUS = {
	ACCEPT : 'accept',
	REJECT : 'reject',
}

/**
 * Registration reception	등록 접수
 * Verifying file			파일 검증 진행
 * File error				파일 오류
 * Finishing verification	파일 검증 완료
 * Submitting				제출 진행
 * Accepting submission	제출 승낙
 * Rejecting submission	제출 거절
 * Finishing registration	등록 완료
 */
const REGIST_STATUS = {
	READY : 'ready',
	RUNNING : 'running',
	VALIDATING : 'validating', 
	VALIDATED : 'validated',
	ERROR : 'error',
	SUBMIT : 'submit', 
	FAILED : 'failed',
	SUCCESS : 'success'
};

const CACHE_PROP = {
	SEARCH_PARAMS : 'searchParams'
};

const NODE_RANK_LIST = [
	{ id: "superkingdom", name: 'Super kingdom'},
	{ id: "subkingdom", name: 'Sub kingdom'},
	{ id: "kingdom", name: 'Kingdom'},
	{ id: "superphylum", name: 'Super phylum'},
	{ id: "phylum", name: 'Phylum'},
	{ id: "subphylum", name: 'Sub phylum'},
	{ id: "superclass", name: 'Super class'},
	{ id: "class", name: 'Class'},
	{ id: "subclass", name: 'Sub class'},
	{ id: "infraclass", name: 'Infra class'},
	{ id: "superorder", name: 'Super order'},
	{ id: "order", name: 'Order'},
	{ id: "suborder", name: 'Sub order'},
	{ id: "infraorder", name: 'Infra order'},
	{ id: "parvorder", name: 'Parv order'},
	{ id: "superfamily", name: 'Super family'},
	{ id: "family", name: 'Family'},
	{ id: "tribe", name: 'Tribe'},
	{ id: "subtribe", name: 'Sub tribe'},
	{ id: "genus", name: 'Genus'},
	{ id: "subgenus", name: 'Sub genus'},
	{ id: "species", name: 'Species'},
	{ id: "subspecies", name: 'Sub species'},
	{ id: "forma", name: 'Forma'},
	{ id: "species group", name: 'Species group'},
	{ id: "species subgroup", name: 'Species sub group'},
	{ id: "varietas", name: 'Varietas'},
	{ id: "no rank", name: 'Unknown'},
];

const MESSAGE = (function(){
	 function __construct_handler__(title){
		 return {
			 NOTICE001_KR : title + " 이(가) 등록되었습니다.",
			 NOTICE002_KR : title + " 이(가) 수정되었습니다.",
			 NOTICE003_KR : title + " 이(가) 조회되었습니다.",
			 NOTICE004_KR : title + " 이(가) 삭제되었습니다.",
			 NOTICE005_KR : "검증요청이 완료되었습니다.",     
			 NOTICE006_KR : "접수요청이 완료되었습니다.",     
			 NOTICE007_KR : "승인 되었습니다.",   
			 NOTICE008_KR : "반려 되었습니다.",          
			 NOTICE009_KR : "선택한 정보가 공개되었습니다.",   
			 NOTICE010_KR : "연계데이터 호출에 성공하였습니다.", 
			 NOTICE011_KR : "파일 업로드에 성공하였습니다.",
			 NOTICE012_KR : "파일 다운로드를 실행 중 입니다.", 
			 NOTICE013_KR : "로그인 되었습니다.",
			 NOTICE014_KR : title + " 이(가) 유효하지 않습니다.",
			 
			 NOTICE021_KR : "선택된 정보가 잘 못 되었습니다.",
			 
			 NOTICE001_EN : "Successfully create " + title + " ",
			 NOTICE002_EN : "Successfully change " + title + " ",
			 NOTICE003_EN : "Successfully search " + title + " ",
			 NOTICE004_EN : "Successfully delete " + title + " ",
			 NOTICE005_EN : "Successfully launch verification " + title + "(s). - ",
			 NOTICE006_EN : "Successfully request for ",
			 
			 NOTICE014_EN : "Invalid " + title,
			 NOTICE015_EN : "No verified raw data " + title + "(s).",
			 NOTICE016_EN : "Failed to verify" + title + "(s).",
			 NOTICE017_EN : "No selected " + title + " ",
			 NOTICE018_EN : "It is already verfieid.",
			 NOTICE019_EN : "Are you sure launch verification " + title + "(s)?",
			 NOTICE020_EN : "It is only possible submitted data.",
			 NOTICE021_EN : "It is incorrect.",
			 NOTICE022_EN : "There is no information selected."
		 }
	 }
	 return {
		 create : function(title) {
			return __construct_handler__(title); 
		}
	 }
})();

const Utils = (function() {
	return {
		/**
		 * 객체의 Null 체크. 선언되지 않은 경우 true 반환
		 */
		isNull : function(obj) {
			return (angular.isUndefined(obj) || obj == null);
		}, 
		
		isNotNull : function(obj) {
			return !this.isNull(obj);
		},
		
		/**
		 * 객체가 Null이거나 빈 값을 확인 
		 * 문자열의 경우 공백만 있는 문자는 빈값으로 인식하고 true 반환
		 * 숫자인 경우는 무조건 값이 있는 것으로 인식
		 * Object의 경우는 변수(=속성)이 하나도 없으면 빈값으로 인식하고 true 반환
		 * 배열의 경우 배열 길이가 0인경우 true 반환
		 */
		isBlank : function(obj) {
			if(this.isNull(obj)) 
				return true;

			if(angular.isNumber(obj))
				return false;
			
			if(angular.isString(obj)) 
				return (String(obj).replace(/ /g,'').length == 0);
			
			if(angular.isDate(obj))
				return false;
				
			if(angular.isArray(obj))
				return (obj.length === 0);
			
			return (Object.keys(obj).length == 0); 
		}, 
		
		isNotBlank : function(obj) {
			return (!this.isBlank(obj));
		},
		
		/** 
		 * 입력값이 숫자형인지 확인하고 숫자형인 경우 true 반환
		 */
		isNumber : function(obj) {
			return (angular.isNumber(obj) && !isNaN(obj));
		}, 
		
		isArray : function(obj) {
			return angular.isArray(obj);
		},
		
		isDate : function(obj) {
			return angular.isDate(obj);
		}, 
		
		isDateFormat : function(obj) {
			if(this.isBlank(obj))
				return false;
			
			if(angular.isDate(obj))
				return false;
			
			return (new Date(obj) != 'Invalid Date');
		},
		
		capitalizeFirstLetter : function(string) {
			if(angular.isString(string)) {
				let capitalize = string.charAt(0).toUpperCase();
				if(string.length > 1)
					capitalize += string.slice(1).toLowerCase();
				
				return capitalize;
			}
			
			return string;
		},
		
		/**
		 * 등록 데이터의 공개 날짜 반환
		 * 기본은 1년 후를 공개일로 설정하여 timestamp 값을 반환.
		 */
		getDefaultOpenDate : function() {
			var openDate = new Date();
			openDate.setHours(0,0,0);
			return openDate.setYear(openDate.getFullYear()+1);
		},
		
		getStartOfDay : function(date) {
			var from = (Utils.isBlank(date)) ? new Date() : new Date(date);
			return new Date(from.setHours(0,0,0));
		},
		
		getEndOfDay : function(date) {
			var from = (Utils.isBlank(date)) ? new Date() : new Date(date);
			return new Date(from.setHours(23,59,59));
		},
		
		/**
		 * 서비스 중인 컨택스트 위치를 반환
		 * ex) http://localhost:8089/AlzNAVi/views/dash.html 에서 호출하는 경우 /AlzNAVi 를 반환
		 * usage) 
		 *   Utils.getContextPath();
		 */
		getContextPath : function() {
			var context = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
			context = (this.isBlank(context)) ? "" : context ;
			return context;
		}, 
		
		/**
		 * 서비스 중인 컨텍스 전체 주소를 반환
		 * ex) http://localhost:8089/AlzNAVi/views/dash.html 에서 호출하는 경우 
		 *     http://localhost:8089/AlzNAVi 를 반환
		 */
		getContextURL : function() {
			var baseUrl = window.location.origin;
			return baseUrl + this.getContextPath();
		},
		
		getCache : function($cacheFactory) {
			if(angular.isUndefined($cacheFactory))
				throw "cache factory is null";
			
			var cache = $cacheFactory.get('gdkm');
			if(angular.isUndefined(cache))
				cache = $cacheFactory('gdkm');
			
			return cache;
		},
		
		isRawData : function(type) {
			if(angular.isString(type)) {
				return (type == PAGE_DATA.RAWDATA); 
			} else if (angular.isObject(type)) {
				return (type['dataType'] == PAGE_DATA.RAWDATA);
			}
			
			return false;
		}, 
		
		isProcessedData : function(type) {
			if(angular.isString(type)) {
				return (type == PAGE_DATA.PROCESSED); 
			} else if (angular.isObject(type)) {
				return (type['dataType'] == PAGE_DATA.PROCESSED);
			}
			
			return false;
		},
	};
})();

const FileUtils = (function() {
	return {
		
		getExtension : function(filename) {
			if(Utils.isBlank(filename))
				return '';
			
			var lastIndex = filename.lastIndexOf(".") + 1;
			if(lastIndex == 0 || lastIndex == filename.length)
				return '';
			
			return filename.substring(lastIndex).toLowerCase();
		},
		
		getBaseName : function(filename) {
			if(Utils.isBlank(filename))
				return '';
			
			var lastIndex = filename.lastIndexOf(".");
			if(lastIndex < 0)
				return filename.trim();
			
			return filename.substring(0, lastIndex);
		}, 
		
		isZipData : function(model) {
			if(angular.isObject(model) && angular.isString(model['fileType'])) {
				return (FILE_TYPE.ZIPPED == model.fileType)
			} else if(angular.isString(model)) {
				return this.isZipFile(model);
			}
			
			return false;
		},
		
		isZipFile : function(filename) {
			return /\.(zip|gz)$/.test(filename);
		},
		
		isFastaData : function(model) {
			if(angular.isObject(model) && angular.isString(model['fileType'])) {
				return (FILE_TYPE.FASTA == model.fileType)
			} else if(angular.isString(model)) {
				return this.isFastaFile(model);
			}
			
			return false;
		},
		
		isFastaFile : function(filename) {
			return /\.(fasta|fa|fna)$/.test(filename)
		},
		
		isFastqData : function(model) {
			if(angular.isObject(model) && angular.isString(model['fileType'])) {
				return (FILE_TYPE.FASTQ == model.fileType)
			} else if(angular.isString(model)) {
				return this.isFastqFile(model);
			}
			
			return false;
		},
		
		isFastqFile : function(filename) {
			return /\.(fastq|fq)$/.test(filename);
		},
		
		isGffData : function(model) {
			if(angular.isObject(model) && angular.isString(model['fileType'])) {
				return (FILE_TYPE.GFF == model.fileType)
			} else if(angular.isString(model)) {
				return this.isGffFile(model);
			}
			
			return false;
		},
		
		isGffFile : function(filename) {
			return /\.(gff|gff3)$/.test(filename);
		},

		isExcelFile : function(filename) {
			return /\.(xls|xlsx)$/.test(filename);
		}, 

		isGbkData : function(model) {
			if(angular.isObject(model) && angular.isString(model['fileType'])) {
				return (FILE_TYPE.GBK == model.fileType)
			} else if(angular.isString(model)) {
				return this.isGbkFile(model);
			}
			
			return false;
		},
		
		isGbkFile : function(filename) {
			return /\.(gbk|gbff|gb)$/.test(filename);
		},
		
		isNgsFile : function(filename) {
			return /\.(bam|sam)$/.test(filename);
		},
		
		isH5File : function(filename) {
			return /\.h5$/.test(filename);
		},
		
		isHtmlFile : function(filename) {
			return /\.(html|htm|HTML|HTM)$/.test(filename);
		},

		isOtherData : function(model) {
			if(angular.isObject(model) && angular.isString(model['fileType'])) {
				return (FILE_TYPE.OTHER == model.fileType)
			}
			
			return false;
		},
		
	};
})(); 

const ContentTypeUtils = (function() {
	const self = {
		isCompressed : function(type) {
			if(Utils.isBlank(type)) 
				return false;
			
			return /(zip|gz|bzip|rar|compressed)/.test(type);
		},
		
		isImage : function(type) {
			if(Utils.isBlank(type)) 
				return false;
			
			return /(image)/.test(type);
		},
		
		isPPT : function(type) {
			if(Utils.isBlank(type)) 
				return false;
			
			return /(powerpoint|presentationml)/.test(type);
		},
		
		isExcel : function(type) {
			if(Utils.isBlank(type)) 
				return false;
			
			return /(excel|spreadsheetml)/.test(type);
		},
		
		isDoc : function(type) {
			if(Utils.isBlank(type)) 
				return false;
			
			return /(msword|wordprocessingml)/.test(type);
		},
		
		isPdf : function(type) {
			if(Utils.isBlank(type)) 
				return false;
			
			return /(pdf)/.test(type);
		},
		
		isHtml : function(type) {
			if(Utils.isBlank(type)) 
				return false;
			
			return /(html)/.test(type);
		},
		
		isHwp : function(type) {
			if(Utils.isBlank(type)) 
				return false;
			
			return /(hwp)/.test(type);
		},
		
		getType : function(type) {
			
			console.info(type);
			
			if(self.isCompressed(type))
				return "Zip";
			
			if(self.isImage(type))
				return "Image";
			
			if(self.isPPT(type))
				return "PPT";
			
			if(self.isExcel(type))
				return "Excel";
			
			if(self.isDoc(type))
				return "Doc";
			
			if(self.isPdf(type))
				return "PDF";
			
			if(self.isHtml(type))
				return "Html";
			
			if(self.isHwp(type))
				return "Hwp";
			
			return "Other";
		},
	};
	
	return self;
})();

const ModelListHandler = (function() {
	
	var _getModelId = function(model) {
		if(angular.isObject(model)) {
			if(Utils.isNotNull(model['id']))
				return model.id;
			if(Utils.isNotNull(model['code']))
				return model.code;
			if(Utils.isNotNull(model['codeGroup']))
				return model.codeGroup;
			if(Utils.isNotNull(model['taxonId']))
				return model.taxonId;
		}
		
		return model;
	};
	
	function __construct_handler__($timeout) {
		return {
			checkedAll : false,	// 모든 목록의 아이템을 선택했는지에 대한 선택 상태
			
			selectedIdList : [],	// 현재 선택된 객체의 식별자
			
			models : [],			// 대상 목록 객체
			
			initialize : function(props) {
				if(Utils.isBlank(props) || (typeof props) != 'object')
					return;
				
				try {
					this.models = props.models;
					this.selectedIdList = props.selectedIdList;
					this.checkedAll = props.checkedAll;
				} catch(err) {
					console.error(err);
				}
			},
			
			getItemIdList : function() {
				return this.getItems().map(function(item) {
					return _getModelId(item);
				});
			},
			
			getItems : function() {
				if(Utils.isNull(this.models)) 
					this.models = [];				
				return this.models;
			},
			
			setItems : function(items) {
				this.models = (Utils.isNull(items))? [] : items;
				this.setSelectedItems(null);
			},
			
			/**
			 * id 에 해당하는 노드를 반환하거나 파라미터 id가 없는 경우 가장 마지막에 선택된 요소를 반환함.
			 */
			getItem : function(id) {
				if(Utils.isNull(id) && this.hasSelectedItems()) {
					 id = this.selectedIdList[this.selectedIdList.length-1];
				}
				
				if(Utils.isNull(id)) 
					return null;
				
				id = _getModelId(id);
				var findList = this.models.filter(function(item) { return _getModelId(item) == id; });
				if(Utils.isBlank(findList))
					return null; 
				
				return findList[0];
			},
			
			setItem : function(item) {
				if(Utils.isBlank(item))
					return;
				
				var idList = this.models.map(function(item) {return _getModelId(item); });
				var index = idList.indexOf(_getModelId(item));
				if(index < 0) {
					this.models.push(item);
					this.checkedAll = false;
				} else {
					this.models[index] = item;
				}
			},
			
			remove : function(item) {
				var idList = this.models.map(function(item) {return _getModelId(item); });
				var index = idList.indexOf(_getModelId(item));
				
				if(index >= 0) {
					this.models.splice(index, 1);
					this.setSelectedItems();
				}
			},
			
			removeSelectedItems : function() {
				var items = this.getSelectedItems();
				for(var i=0 ; i < items.length ; i++) {
					this.remove(items[i]);
				}
			},
			
			size : function() {
				if(Utils.isBlank(this.models))
					return 0;
				
				return this.models.length;
			},
			
			getSelectedSize : function() {
				return this.getSelectedIdList().length;
			},
			
			hasSelectedItems : function() {
				return (this.getSelectedIdList().length > 0);
			},
			
			getSelectedIdList : function() {
				if(Utils.isBlank(this.selectedIdList))
					this.selectedIdList = [];
				
				return this.selectedIdList;
			},
			
			getSelectedItems : function() {
				var selectedIdList = this.selectedIdList;
				return this.models.filter(function(item) {
					var itemId = _getModelId(item);
					return (selectedIdList.indexOf(itemId) >= 0);
				}); 
			},
			
			setSelectedItems : function(selectedItems) {
				if(!angular.isArray(selectedItems)) {
					selectedItems = this.selectedIdList;
				}
				
				if(!angular.isArray(selectedItems)) {
					selectedItems = [];
				}
				
				var selectedIdList = selectedItems.map(function(item) {return _getModelId(item); });
				
				var allList = this.getItems();
				selectedIdList = selectedItems.filter(function(id) {
					return (allList.findIndex(function(item) { return (_getModelId(item) === id); }) >= 0);
				});
				
				this.selectedIdList = selectedIdList;
				
				if(selectedIdList.length == 0) {
					this.checkedAll = false;
				} else if (this.size() > selectedIdList.length) {
					this.checkedAll = false;
				} else {
					this.checkedAll = true;
				}
			},
			
			selectAll : function(checked) {	// 모든 목록의 아이템의 선택 상태가 변경
				if(Utils.isNull(checked)) 
					checked = this.checkedAll; 
				
				var selectedIdList = (checked)? this.models.map(function(item) {return _getModelId(item); }) : [];
				this.setSelectedItems(selectedIdList);
			},
			
			select : function(item, onlyOne) {
				var handler = this;
				$timeout(function() {
					var selectedIdList = handler.getSelectedIdList();
					if(onlyOne) {
						var itemId = _getModelId(item);
						selectedIdList = [ itemId ];
					}
					
					handler.setSelectedItems(selectedIdList);
				}, 100);
			},
		}
	};
	
	return {
		create : function($timeout) {
			return __construct_handler__($timeout); 
		}
	}
})();


const PageHandler = (function() {
	
	function __construct_handler__() {
		return {
			
			_total : 0, 				// 전체 목록의 개수
			_firstRowIndex : 0, 		// 현재 페이지의 첫 번째 줄의 전체 목록에서의 인덱스 번호
			_numberOfPageLink : 10,		// 페이지 번호를 출력 할 개수
			_pageChangeLinsteners : [],	// 페이지 변경에 대한 이벤트 리스너 목록
			
			numberOfRows : 10, 		// 한 페이지에서 출력할 목록의 개수
			
			initialize : function(props) {
				if(!angular.isObject(props))
					return;
				
				try {
					this._total = props._total;
					this._firstRowIndex = props._firstRowIndex;
					this._numberOfPageLink = props._numberOfPageLink;
					this.numberOfRows = props.rowSize;
				} catch(err) {
					console.error(err);
				}
			},
			
			getTotal : function() {
				return parseInt(this._total);
			}, 
			
			setTotal : function(total, fired) {
				total = parseInt(total);
				if(total != this.getTotal()) {
					var page = this.getCurrentPage();
					this._total = total;
					this.setCurrentPage(page, fired);
				}
			},
			
			getNumberOfRows : function() {
				return parseInt(this.numberOfRows);
			},
			
			setNumberOfRows : function(num, fired) {
				num = parseInt(num);
				if(num != this.getNumberOfRows()) {
					var page = this.getCurrentPage();
					this.numberOfRows = num;
					this.setCurrentPage(page, fired);
				}
			},
			
			getNumberOfPageLink : function() {
				return parseInt(this._numberOfPageLink);
			},
			
			setNumberOfPageLink : function(num, fired) {
				num = parseInt(num);
				if(num != this.getNumberOfPageLink()) {
					var page = this.getCurrentPage();
					this._numberOfPageLink = parseInt(num);
					this.setCurrentPage(page, fired);
				}
			},
			
			setCurrentPage : function(pageNum, fired) {
				pageNum = Math.min(this.getPageSize(), pageNum);	 
				var firstRowIndex = (pageNum-1) * this.numberOfRows;
				this._firstRowIndex = (firstRowIndex < 0) ? 0 : firstRowIndex;
				
				if(fired) {
					this._firePageChangeEvent(0, pageNum);
				}
			},
			
			getCurrentPage : function() {
				return Math.floor(this._firstRowIndex/this.numberOfRows) + 1;
			},
			
			getPageFirstRow : function() {
				var rowNum = ((this.getCurrentPage()-1) * this.numberOfRows) + 1;
				if(this.getTotal() < rowNum)
					return this.getTotal();
				
				return Math.max(rowNum, 0);
			}, 
			
			getPageLastRow : function() {
				var rowNum = (this.getCurrentPage() * this.numberOfRows);
				return Math.min(rowNum, this.getTotal());
			},
			
			isScrollFirstDisabled : function() {
				return (this._firstRowIndex == 0);
			}, 
			
			isScrollPrevDisabled: function() {
				return (this._firstRowIndex == 0);
			},
			
			isPrevDisabled: function() {
				return (this._firstRowIndex == 0 );
			},
			
			isNextDisabled: function() {
				return (this._firstRowIndex >= (this._total - this.numberOfRows));
			}, 
			
			isScrollNextDisabled: function() {
				return (this._firstRowIndex >= (this._total - this.numberOfRows));
			}, 
			
			isScrollLastDisabled: function() {
				return (this._firstRowIndex >= (this._total - this.numberOfRows));
			},
			
			getPageSize : function() {
				return Math.ceil(this._total / this.numberOfRows);
			},
			
			getFirstPageIndex: function() {
				var numberOfPages = this.getPageSize();
				if(numberOfPages <= this._numberOfPageLink) 
					return 0;
				
				var firstPageIndex = Math.round(this._firstRowIndex/this.numberOfRows) - Math.floor(this._numberOfPageLink / 2);
				
				if(firstPageIndex < 0) {
					firstPageIndex = 0;
				} else if ((numberOfPages - firstPageIndex) < this._numberOfPageLink) {
					firstPageIndex = numberOfPages - this._numberOfPageLink;
				}
				
				return firstPageIndex;
			},
			
			getPages : function() {
				var pages = [];
				
				var firstPageIndex = this.getFirstPageIndex();
				var lastPageIndex = Math.min((firstPageIndex + this._numberOfPageLink), this.getPageSize());
				
				for(var i=firstPageIndex ; i < lastPageIndex ; i++) {
					pages.push(i+1);
				}
				
				return pages;
			},
			
			/**
			 * 가장 처음 페이지로 이동
			 */
			scrollFirst : function() {
				var prev = this.getCurrentPage(); 
				this._firstRowIndex = 0;
				var next = this.getCurrentPage();
				
				if(prev > next) {
					this._firePageChangeEvent(prev, next);
				}
			},
			
			/**
			 * 바로 이전 페이지로 이동
			 */
			prev: function() {
				var prev = this.getCurrentPage();
				
				this._firstRowIndex -= this.numberOfRows;
				if(this._firstRowIndex < 0) 
					this._firstRowIndex = 0;
				
				var next = this.getCurrentPage();
				if(prev > next) {
					this._firePageChangeEvent(prev, next);
				}
			},
			
			/**
			 * 바로 다은 페이지로 이동
			 */
			next: function() {
				var prev = this.getCurrentPage();
				
				this._firstRowIndex = this._firstRowIndex + this.numberOfRows;
				if(this._firstRowIndex >= this.getTotal()) {
					this._firstRowIndex = (this.getPageSize()-1) * this.numberOfRows;
					
					if(this._firstRowIndex < 0)
						this._firstRowIndex = 0;
				}
				
				var next = this.getCurrentPage();
				if(prev < next) {
					this._firePageChangeEvent(prev, next);
				}
			},
			
			/**
			 * 가장 마지막 페이지로 이동
			 */
			scrollLast: function() {
				var prev = this.getCurrentPage();
				
				this._firstRowIndex = (this.getPageSize()-1) * this.numberOfRows;
				if(this._firstRowIndex < 0)
					this._firstRowIndex = 0;
				
				var next = this.getCurrentPage();
				if(prev < next) {
					this._firePageChangeEvent(prev, next);
				}
			}, 
			
			setPageChangeListener : function(listener) {
				if(angular.isFunction(listener)) {
					this._pageChangeLinsteners = [listener];
				}
			},
			
			addPageChangeListener : function(listener) {
				if(angular.isFunction(listener)) {
					if(this._pageChangeLinsteners.indexOf(listener) < 0) {
						this._pageChangeLinsteners.push(listener);
					}
				}
			}, 
			
			removePageChangeListener : function(listener) {
				var index = this._pageChangeLinsteners.indexOf(listener);
				if(index >= 0) {
					this._pageChangeLinsteners.splice(index, 1);
				}
			},
			
			_firePageChangeEvent : function(prev, next) {
				if(this._pageChangeLinsteners.length > 0)
					waitingDialog.show("Loading ...");
				
				for(var i=0; i < this._pageChangeLinsteners.length ; i++) {
					this._pageChangeLinsteners[i](this, prev, next);
				}
			}, 
		}
	};
	
	return {
		create : function() {
			return __construct_handler__(); 
		},
	}
})();


const RawDataValidator = (function() {
	
	function __construct_validator__(notifier) {
		
		return {
			validate : function(model) {
				if(!Utils.isRawData(model)) {
					notifier.notice("Is not raw data");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.exprType) || Utils.isBlank(model.rawData.exprType['code'])) {
					notifier.notice("Invalid experiment type");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.domain) || Utils.isBlank(model.rawData.domain['code'])) {
					notifier.notice("Invalid taxonomy");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.sampleName)) {
					notifier.notice("Invalid sample name");
					return false;
				}
				
				if(Utils.isBlank(model.strain)) {
					notifier.notice("Invalid strain");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.organismType)) {
					notifier.notice("Invalid organism type");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.sampleType)) {
					notifier.notice("Invalid sample type");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.geographLocation)) {
					notifier.notice("Invalid geographic location");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.sampleSource) || Utils.isBlank(model.rawData.sampleSource['code'])) {
					notifier.notice("Invalid sample source");
					return false;
				}
				
				if(Utils.isBlank(model.taxonomy) || Utils.isBlank(model.taxonomy.taxonId)) {
					notifier.notice("Invalid species name & taxonomy ID");
					return false;
				}
								
				if(Utils.isBlank(model.rawData.platform) || Utils.isBlank(model.rawData.platform['code'])) {
					notifier.notice("Invalid sequencing platform");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.construction)) {
					notifier.notice("Invalid ngs library contruction");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.selection)) {
					notifier.notice("Invalid ngs library selection");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.sequencer)) {
					notifier.notice("Invalid instrument model");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.strategy)) {
					notifier.notice("Invalid ngs library strategy");
					return false;
				}
				
				if(Utils.isBlank(model.rawData.readType) || Utils.isBlank(model.rawData.readType['code'])) {
					notifier.notice("Invalid sequencing read");
					return false;
				}
				
				return true;
			}, 
		}	
	};
	
	return {
		create : function(notifier) {
			return __construct_validator__(notifier);
		}
	}
})();

const ProcessedValidator = (function() {
	
	function __construct_validator__(notifier) {
		
		return {
			validate : function(model) {
				if(!Utils.isProcessedData(model)) {
					notifier.notice("Is not processed data");
					return false;
				}
				
				if(Utils.isBlank(model.processedData.submitter)) {
					notifier.notice("Invalid submitter");
					return false;
				}
				
				if(Utils.isBlank(model.processedData.submitOrgan)) {
					notifier.notice("Invalid submitting organization");
					return false;
				}
				
				if(Utils.isBlank(model.processedData.project)) {
					notifier.notice("Invalid project");
					return false;
				}
				
				if(Utils.isBlank(model.processedData.projectType)) {
					notifier.notice("Invalid project type");
					return false;
				}
				
				if(Utils.isBlank(model.processedData.assemblyLevel) || Utils.isBlank(model.processedData.assemblyLevel['code'])) {
					notifier.notice("Invalid assembly level");
					return false;
				}
				
				if(Utils.isBlank(model.processedData.genomeType) || Utils.isBlank(model.processedData.genomeType['code'])) {
					notifier.notice("Invalid genome type");
					return false;
				}
				
				if(Utils.isBlank(model.taxonomy) || Utils.isBlank(model.taxonomy.taxonId)) {
					notifier.notice("Invalid taxonomy");
					return false;
				}
				
				if(Utils.isBlank(model.strain)) {
					notifier.notice("Invalid strain");
					return false;
				}
				
				/* Raw data가 없는 경우가 발생해 연계 데이터가 없어도 등록할 수 있도록 임시로 처리 함 */
//				if(Utils.isBlank(model.linkedList)) {
//					notifier.notice("Linked raw data is empty");
//					return false;
//				}
				
				return true;
			}, 
		}	
	};
	
	return {
		create : function(notifier) {
			return __construct_validator__(notifier);
		}
	}
})(); 


const SearchHandler = (function() {
	
	function __construct_handler__($timeout) {
 
		return {
			keyword : '',
			
			match : '',
			
			ngsDataRegist : new NgsDataRegist(),
			
			fields : {
				openYn : '',
				registStatus : '',
				excludeStatus : '',	// 제외시킬 상태정보 
				registUserId : '',
			},
			
			user : {
				userId : '',
				name : '',
				institute : ''
			},
			
			codeGroup : {
				id:''
				,name:''
				,useYn:''
			},
			
			code : {
				id:''
				,codeGroup: {id:''
							,name:''
							,useYn:''}
				,name:''
				,useYn:''
			},
			
			_searchListener : [],
			
			initialize : function(props) {
				if(Utils.isBlank(props) || !angular.isObject(props))
					return;
				
				try {
					this.query = props.query;
					this.fields = props.fields;
					this.user = props.user;
					this.code = props.group;
				} catch (e) {
					console.error(e);
				}
			},
			
			getRequestParams : function() {
				var params = {};
				params['query'] = this.query || '';
				
				for(name in this.fields) {
					if(Utils.isNotBlank(this.fields[name])) {
						params[name] = this.fields[name];
					}
				}
				
				return params;
			},
			
			setSearchListener : function(listener) {
				if(angular.isFunction(listener)) {
					this._searchListener = [];
					this._searchListener.push(listener);
				}
			},
			
			addSearchListener : function(listener) {
				if(angular.isFunction(listener)) {
					if(this._searchListener.indexOf(listener) < 0)
						this._searchListener.push(listener);
				}
			},
			
			removeSearchListener : function(listener) {
				if(Utils.isBlank(listener)) {
					this._searchListener = [];
				} else {
					var index = this._searchListener.indexOf(listener);
					this._searchListener.splice(index, 1);
				}
			},
			
			search : function() {
				if(this._searchListener.length > 0) 
//					waitingDialog.show("Loading ...");
				
				for(var i=0; i < this._searchListener.length ; i++) {
					this._searchListener[i](this);
				}
			},
		}
	};
	
	return {
		create : function($timeout) {
			return __construct_handler__($timeout); 
		},
	}
})();

const SessionHandler = (function() {
	
	var _GLOBAL_PROPERTIES = [ 'USER' ];
	
	var _PROPERTIES = [ 'pageHandler', 'columnHandler' ];
	
	function __construct_handler__($window) {
		var type = PAGE_DATA.RAWDATA;
		
		var handler = {
			
			setModelType : function(modelType) {
				type = modelType;
			},
			
			isUser : function() {
				if(Utils.isBlank(this.getLoginUser()))
					return false;
				
				return this.getLoginUser().authority == 'user';
			},
				
			isAdmin : function() {
				if(Utils.isBlank(this.getLoginUser()))
					return false;
				
				return this.getLoginUser().authority == 'admin';
			},
			
			isSystem : function(){
				if(Utils.isBlank(this.getLoginUser()))
					return false;
				
				return this.getLoginUser().authority == 'sys';
			},
				
			getLoginUser : function() {
				return this.getProperty("USER"); 
			},
			
			getPropertyName : function(property) {
				if(Utils.isBlank(property))
					throw "Session property can not be null";
				
//				if(Utils.isBlank(type) || Utils.isBlank(scope))
//					return property.toUpperCase();
//				
//				if(_GLOBAL_PROPERTIES.indexOf(property) < 0)
//					return (type + "." + scope + "." + property).toUpperCase();
				
				return property.toUpperCase();
			},
			
			getProperty : function(name) {
				var propName = handler.getPropertyName(name);
				var propValue = $window.sessionStorage.getItem(propName); 
				try {
					return angular.fromJson(propValue);
				} catch(e) {
					return propValue;
				}
			},
			
			setProperty : function(name, value) {
				name = handler.getPropertyName(name);
				if(Utils.isBlank(value)) {
					$window.sessionStorage.removeItem(name);
				} else {
					try {
						$window.sessionStorage.setItem(name, angular.toJson(value));
					} catch(e) {
						$window.sessionStorage.setItem(name, value);
					}
				}
			},
			
			hasProperty : function(name) {
				return Utils.isNotBlank(this.getProperty(name));
			},
			
			removeProperty : function(name) {
				name = handler.getPropertyName(name);
				$window.sessionStorage.removeItem(name);
			},
		};
		
		return handler;
	};
	
	return {
		create : function($window) {
			if(Utils.isNull($window)) 
				throw "$window can not be null.";
			
			return __construct_handler__($window);
		},
	}
})();

const UPLOAD_STATE = {
	READY : 'ready',
	PENDING : 'pending', 
	PROCESSING : 'processing', 
	COMPLETE : 'complete', 
	ABORT : 'abort',
	FAIL : 'fail'
};

const UploadHandler = (function() {
	
	function __construct_handler__($window, $timeout, Upload) {
		var handler = {

			_uploadListeners : [], 
			
			/**
			 * 업로드 폼에서 사용되는 파일 목록
			 */
			files : [],
			
			pendingList : [],
			
			requestUrl : Utils.getContextPath() + "/achieves/",
			
			isReady : function(dataFile) {
				if(Utils.isNull(dataFile))
					return false;
				
				return dataFile.status == UPLOAD_STATE.READY;
			},
			
			isPending : function(dataFile) {
				if(Utils.isNull(dataFile))
					return false;
				
				return dataFile.status == UPLOAD_STATE.PENDING;
			},
			
			isProcessing : function(dataFile) {
				if(Utils.isNull(dataFile))
					return false;
				
				return dataFile.status == UPLOAD_STATE.PROCESSING;
			},
			
			isComplete : function(dataFile) {
				if(Utils.isNull(dataFile))
					return false;
				
				return dataFile.status == UPLOAD_STATE.COMPLETE;
			},
			
			isFail : function(dataFile) {
				if(Utils.isNull(dataFile))
					return false;
				
				return dataFile.status == UPLOAD_STATE.FAIL;
			},
			
			isRemovable : function(dataFile) {
				if(Utils.isNull(dataFile))
					return false;
				
				return (dataFile.status == UPLOAD_STATE.COMPLETE || dataFile.status == UPLOAD_STATE.FAIL);
			},
			
			addUploadListener : function(listener) {
				if(angular.isFunction(listener)) {
					handler._uploadListeners.push(listener);
				}
			},
			
			/**
			 * 업로드 요청 URL 설정
			 * 
			 * ex) /regist-data/{registId}/achieves
			 */
			setRequestUrl : function(url) {
				handler.requestUrl = url;
			},
			
			getRequestUrl : function() {
				if(Utils.isBlank(handler.requestUrl))
					return Utils.getContextPath() + "/files/";
				
				return handler.requestUrl;
			},
			
			/**
			 * 선택한 파일(handler.files) 목록을 확인한 후 파일 업로드 진행
			 * handler.files 의 객체 구조(json 구조로 변환되지 않음)
			 * 
			 * files = {
			 *   name : '선택한 파일 이름', 
			 *   size : '선택한 파일 크기',
			 *   type : '선택한 파일 유형',							// ex) application/x-zip-comppressed
			 *   lastModified : '선택한 파일의 최종 수정 타임스탬프', 	// ex) 1472427208000
			 *   lastModifiedDate : '선택한 파일의 최종 수정일', 		// ex) Mon Aug 29 2016 08:33:28 GMT+0900
			 * }
			 */
			upload : function() {
				if(Utils.isNull(handler.files))
					return;
				
				// 다중 업로드가 아닌 경우 배열로 변경
				if(!angular.isArray(handler.files)) {
					var file = handler.files;
					handler.files = [];
					handler.files.push(file);
				}
				
				var files = handler.files.slice();
				for(var i=0; i < files.length ; i++) {
					var pendingFile = new NgsDataFile();
					pendingFile.file = files[i];
					pendingFile.fileName = files[i].name;
					pendingFile.fileSize = files[i].size;
					pendingFile.status = UPLOAD_STATE.READY;
					
					handler.pendingList.push(pendingFile);
					handler.doUploadFile(pendingFile);
					
					for(var idx in handler._uploadListeners) {
						handler._uploadListeners[idx](UPLOAD_STATE.PENDING, pendingFile);
					}
				}
			},
			
			/**
			 * 파일업로드 실행
			 * 
			 * @parameter file : 실제 올려지는 파일 객체  
			 * @parameter dataFile : 서버에 저장되는 파일정보 객체				 
			 */
			doUploadFile : function(pendingFile) {
				pendingFile.status = UPLOAD_STATE.PENDING;
				pendingFile.file.progress = 0;
				
				pendingFile.file.upload = Upload.upload({
					url : handler.getRequestUrl(),
					headers : {
						"optional-header" : "header-value",
						"Content-Type" : pendingFile.file.type
					}, 
					data : {
						file : pendingFile.file
					}
				});
				
				pendingFile.file.upload.then(function(res) {
					$timeout(function() {
						pendingFile.status = UPLOAD_STATE.COMPLETE;
						handler._removePendingFile(pendingFile);
						
						for(var idx in handler._uploadListeners) {
							handler._uploadListeners[idx](UPLOAD_STATE.COMPLETE, pendingFile, res.data[0]);
						}
					});
				}, function(err) {
					if(err.status > 0) {
						pendingFile.file.errorMsg = '[' + err.status + '] ' + err.data;
						pendingFile.status = UPLOAD_STATE.FAIL;
					}
					
					for(var idx in handler._uploadListeners) {
						handler._uploadListeners[idx](UPLOAD_STATE.FAIL, pendingFile);
					}
				}, function(evt) {
					pendingFile.file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
				});
			},
			
			/**
			 * 업로드 중인 파일의 업로드 중지 후 업로딩 목록에서 파일 삭제
			 */
			cancel : function(dataFile) {
				if(dataFile.status == UPLOAD_STATE.PENDING) {
					dataFile.status = UPLOAD_STATE.ABORT;
					dataFile.file.upload.abort();
				}
				
				$timeout(function() {
					handler._removePendingFile(dataFile)
				} , 100);
			},
			
			/**
			 * 업로딩 중인 파일 목록에서 삭제
			 * handler.files 의 객체도 'delete handler.files.splice(index, 1);' 로 지울 것인지 테스트 필요 
			 */
			_removePendingFile : function(dataFile) {
				var index = handler.files.indexOf(dataFile.file);
				if(index >= 0) {
					handler.files.splice(index, 1);
				}
				
				index = handler.pendingList.indexOf(dataFile);
				if(index >= 0) {
					handler.pendingList.splice(index, 1);
				}
			},
			
			/**
			 * 업로드 완료된 파일 다운로드 요청 함수
			 */
			download : function(uploadedFile) {
				if(Utils.isBlank(uploadedFile) || uploadedFile.id < 1) 
					return;
				
				var downloadUrl = Utils.getContextPath() + "/achieves/" + uploadedFile.id + "/file";
				$window.location.assign(downloadUrl);
			},
			
			/**
			 * 핸들러 초기화 (업로딩 중인 파일은 abort 시킨 후 삭제, 업로드 완료된 파일은 서버에 파일 삭제 요청 후 목록에서도 삭제)
			 * sync 가 true인 경우는 서버도 함께 삭제. 
			 */
			clear : function(sync) {
				var pendingList = handler.pendingList.slice();
				for(var idx in pendingList) {
					handler.cancel(pendingList[idx]);
				}
			},
		};
		
		return handler;
	};
	
	return {
		create : function($window, $timeout, Upload, uploadFactory) {
			if(Utils.isNull($window)) 
				throw "$window can not be null.";
			
			if(Utils.isNull($timeout)) 
				throw "$timeout can not be null.";
			
			if(Utils.isNull(Upload)) 
				throw "Upload can not be null.";
			
			return __construct_handler__($window, $timeout, Upload); 
		},
	}
})();

const ModelForm = (function() {
	
	function __construct_form__($timeout) {
		var self = {
				
			model : {},
			
			// 원본 모델 객체
			_source : {},

			errors : {},
			
			errorVisible : false,
			
			_validator : null,
			
			_submitCallback : null,
			
			_updateCallback : null,
			
			_confirmCallback : null,
			
			_removeCallback : null,
			
			_closeCallback : null,
			

			_clearErrors : function() {
				self.errors = {};
				self.errorVisible = false;
			},
			
			setModel : function(model) {
				if(Utils.isNull(model))
					throw "Clinic target model can not be null.";
				
				self.model = angular.copy(model);
				self._source = model;
			},
			
			getModel : function() {
				if(Utils.isNull(self.model)) {
					if(Utils.isNull(self._source)) {
						self.model = {};
					} else {
						self.model = angular.copy(self._source);
					}
				}
				
				return self.model;
			},

			isMessageVisible : function(el) {
				if(!self.errorVisible)
					return false;
				
				if(Utils.isBlank(self.errors[el]))
					return false;
				
				return true;
			},
			
			setErrors : function(errors) {
				if(Utils.isBlank(errors)) {
					self.errors = {};
					self.errorVisible = false;
				} else {
					self.errors = errors;
					self.errorVisible = true;
				}
			},
			
			setValidator : function(validator) {
				if(angular.isFunction(validator)) {
					self._validator = validator;
				}
			},
			
			setSubmitCallback : function(callback) {
				if(angular.isFunction(callback)) {
					self._submitCallback = callback;
				}
			},
			
			setUpdateCallback : function(callback) {
				if(angular.isFunction(callback)) {
					self._updateCallback = callback;
				}
			}, 
			
			setRemoveCallback : function(callback) {
				if(angular.isFunction(callback)) {
					self._removeCallback = callback;
				}
			},
			
			setCloseCallback : function(callback) {
				if(angular.isFunction(callback)) {
					self._closeCallback = callback;
				}
			},

			reset : function() {
				if(Utils.isBlank(self._source)) {
					self.model = ClinicTarget();
				} else {
					self.model = angular.copy(self._source);
				}
				
				self.setErrors(null);
			},
			
			submit : function() {
				if(!Utils.isNull(self._validator)) {
					var errors = self._validator(self.model);
					self.setErrors(errors);
					
					if(Utils.isNotBlank(errors)) {
						// TODO 어떤 처리를 하는 게 좋을까?
						console.error(errors);
						return;
					}
				}
			
				if (Utils.isNotNull(self._submitCallback)) {
					waitingDialog.show("Creating ...");
					self._submitCallback(self.model);
				}
			},
			
			update : function() {
				if(Utils.isNotNull(self._validator)) {
					var errors = self._validator(self.model);

					if(Utils.isNotBlank(errors)) {
						return;
					}
				}
			
				if(Utils.isNotNull(self._updateCallback)) {
					waitingDialog.show("Updating ...");
					self._updateCallback(self.model);
				}
			},
			
			remove : function() {
				if(Utils.isNotNull(self._removeCallback)) {
					waitingDialog.show("Deleting ...");
					self._removeCallback(self.model);
				}
				
			},
			
			confirm : function(){
				if(Utils.isNotNull(self._updateCallback)) {
					model = self.model;
					model.registStatus = model.approvalStatus;
					waitingDialog.show("Updating ...");
					self._updateCallback(model);
				}
			},
			
			close : function() {
				if(Utils.isNotNull(self._closeCallback))
					self._closeCallback(self.model);
			},
			
			reset : function() {
				self.model = {};
			}
		};
		
		return self;
	};
	
	return {
		create : function($timeout) {
			return __construct_form__($timeout); 
		}
	}
})();


/**
 * 비동기 방식 자동완성 목록 호출 객체
 */
const ModelTypeAhead = (function() {
	return {
		create : function($http) {
			var self = {
				_url : '',
				
				params : {
					query : '',
					columns : '',
					registUserId : '',
					openYn : '',
					page : 1,
					rowSize : 15
				},
				
				setUrl : function(url) {
					self._url = url;
				},
				
				getUrl : function() {
					return self._url;
				},
				
				setQueryFields : function(fields) {
					if(Utils.isArray(fields)) {
						self.params.columns = fields.join('+').replace(/\s*/g, '');
					} else {
						self.params.columns = String(fields).replace(/\s*/g, '');
					}
				},
				
				getQueryFields : function() {
					return self.params.columns.split('+');
				},
				
				setRegistUserId : function(registUserId) {
					if(Utils.isBlank(registUserId)) {
						self.params.registUserId = '';
					} else {
						self.params.registUserId = String(registUserId).trim();
					}
				},
				
				getRegistUserId : function() {
					return self.params.registUserid;
				}, 
				
				setOpenYn : function(openYn) {
					if(Utils.isBlank(openYn)) {
						self.params.openYn = '';
					} else {
						self.params.openYn = String(openYn).trim().toUpperCase();
					}
				},
				
				getOpenYn : function() {
					return self.params.openYn;
				},
				
				search : function(query) {
					if(Utils.isBlank(this._url)) {
						console.error('URL can not be null.');
						return;
					} 
						
					if(Utils.isNull(query))
						query = '';
					
					self.params.query = query.trim();
					return $http.get(self._url, {
						params: self.params
					}).then(function(response) {
						return response.data.list;
					});
				},
			};
			
			return self;
		}
	};
})();

const Display = (function() {
	
	return {
		create : function($route, model) {
			if(Utils.isBlank(model))
				model = {registStatus: null, approvalStatus: null };
			
			return {
				
				tabIndex : 0,
				
				/**
				 * 현재 페이지 상태 
				 */
				page : 'detail',
				
				section : '',
				
				model : model,
				
				/**
				 * 아코디온 섹션의 펼침 상태
				 * 펼쳐진 경우는 true, 닫힌 경우는 false
				 */
				accordions : new Array(5).fill(true),
				
				/**
				 * 펼치기/접기 버튼 활성화 변수 
				 */
				expandEnabled : false,

				setPage : function(page) {
					this.page = page;
				},
				
				setSection : function(section){
					this.section = section;
				},
				
				setModel : function(model, type) {
					this.model = model;
				},
				
				isIntegratedViewsPage : function(){
					return (this.section == PAGE_SECTION.PUBIC);
				},
				
				isVerifyPage : function(){
					return (this.section == PAGE_SECTION.REGIST);
				},
				
				isReviewPage : function(){
					return (this.section == PAGE_SECTION.REVIEW);
				},
				
				expandAll : function() {
					this.accordions.fill(true);
					this.expandEnabled = false;
				},
				
				collapseAll : function() {
					this.accordions.fill(false);
					this.expandEnabled = true;
				},
				
				isExpandBtnEnabled : function() {
					var expandedCount = this.accordions.filter(function(el) { return el; }).length;
					var collapsedCount = this.accordions.filter(function(el) { return el; }).length; 
					if( collapsedCount == 0)
						this.expandEnabled = false;
					
					if(expandedCount == 0) 
						this.expandEnabled = true;
						
					return this.expandEnabled;
				},
//				
//				isConfirmVisible : function() {
//					if(scope == DATA_SCOPE.PUBLIC)
//						return false;
//					
//					if(this.isConfirmed() || this.isConfirmable())
//						return true;
//					
//					return false;
//				},
//				
//				/**
//				 * 승인 또는 반려 된 경우 true 반환
//				 */
//				isConfirmed : function() {
//					return (this.model.registStatus == REGIST_STATUS.CONFIRM 
//							|| this.model.registStatus == REGIST_STATUS.REJECT);
//				},
//				
//				isEditable : function() {
//					if(scope == DATA_SCOPE.USER || scope == DATA_SCOPE.SYSTEM)
//						return (this.model.registStatus == REGIST_STATUS.READY);
//					
//					return false;
//				},
//				
//				isConfirmable : function() {
//					if(scope == DATA_SCOPE.CONFIRM && this.model.registStatus == REGIST_STATUS.WAIT)
//						return true;
//					
//					return false;
//				},
//				
//				isRejectable : function(approvalStatus) {
//					if(this.isConfirmable()) {
//						return (approvalStatus == APPROVAL_STATUS.REJECT); 
//					}
//					return false;
//				},
			};
		}
	}
})();

/**
 * 상태정보 일괄변경 명령 실행 함수 
 */
const StatusChangeCommand = (function() {
	
	function __construct_command__(factory, notifier) {
		return {
			
			changeListener : function(resultCode, response) {},
			
			execute : function(modelList, status) {
				var requestList = [];
				
				for(var idx in modelList) {
					var model = angular.copy(modelList[idx]);
					
					if(REGIST_STATUS.isNextStatusAvailable(model.registStatus, status)) {
						model.registStatus = status;
						requestList.push(model);
					} else {
						notifier.notice(model.registNo + "는 등록 상태를 변경할 수 없습니다.");
					}
				}
				
				if(requestList.length == 0)
					return false;
				
				var changeListener = this.changeListener;
				waitingDialog.show("Change status ...");
				factory.changeRegistStatus(requestList).then(
					function(res) {
						waitingDialog.hide();
						var changedList = res.data;
						if(changedList.length > 0) {
							var msg = changedList[0].registNo;
							if(changedList.length > 1) {
								msg += ' 외 ' + (changedList.length-1) + "개의 "; 
							}
							msg += ' 상태가 변경되었습니다.';
							notifier.success(msg);
							
							changeListener(200, changedList);
						} else {
							notifier.info("등록상태 변경 실패!");
							changeListener(400, "등록상태 변경 실패");
						}
					}, 
					function(err) {
						waitingDialog.hide();
						notifier.error(err.data);
						this.changeListener(err.status, err.data);
					}
				);
			}
		}	
	};
	
	return {
		create : function(factory, notifier) {
			return __construct_command__(factory, notifier);
		}
	}
})();

/**
 * 공개정보 일괄변경 명령 실행 함수  
 */
const OpenYnChangeCommand = (function() {
	
	function __construct_command__(factory, notifier) {
		return {
			
			changeListener : function(resultCode, response) {},
			
			execute : function(modelList, openYn) {
				var requestList = [];
				
				for(var idx in modelList) {
					var model = angular.copy(modelList[idx]);
					if(model.openYn == openYn) {
						notifier.notice(model.registNo + "는 이미 공개상태 입니다.");
						continue;
					}
					
					if(!REGIST_STATUS.isApproved(sample.registStatus)) {
						notifier.notice(model.registNo + "는 미승인 상태입니다.");
						continue;
					}
					
					model.openYn = openYn;
					requestList.push(model);
				}
				
				if(requestList.length == 0)
					return;
				
				var changeListener = this.changeListener;
				waitingDialog.show("Change open date ...");
				factory.changeOpenYnStatus(requestList).then(
					function(res) {
						waitingDialog.hide();
						var changedList = res.data;
						if(changedList.length > 0) {
							var msg = changedList[0].registNo;
							if(changedList.length > 1) {
								msg += ' 외 ' + (changedList.length-1) + "개의 "; 
							}
							msg += ' 공개 상태가 변경되었습니다.';
							notifier.success(msg);
							
							changeListener(200, changedList);
						} else {
							notifier.info("등록상태 변경 실패!");
							changeListener(400, "등록상태 변경 실패");
						}
					}, 
					function(err) {
						waitingDialog.hide();
						notifier.error(err.data);
						this.changeListener(err.status, err.data);
					}
				);
			}
		}	
	};
	
	return {
		create : function(factory, notifier) {
			return __construct_command__(factory, notifier);
		}
	}
})();

