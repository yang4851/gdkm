const User = (function() {
	return {
		userId : '',
		password : '',
		name : '',
		email : '',
		institute : '',
		department : '',
		phone : '',
		authority : 'admin',
		updateUser : ''
	}
});

const CodeGroup = (function() {
	return {
		codeGroup : '',
		name : '',
		useYn : 'Y',
		registDate : ''
	}
});

const Code = (function() {
	return {
		code : '',
		group : '',
		name : '',
		useYn : 'Y',
		registDate : new Date()
	}
});

const NgsDataRegist = (function(type) {
	return {
		id : -1,
		registNo : '',
		dataType : type,
		taxonomy : new Taxonomy(),
		strain : '',
		rawData : Utils.isRawData(type)? new NgsRawData() : null,
		processedData : Utils.isProcessedData(type)? new NgsProcessedData() : null,
		openYn : 'N',
		openDate : null,
		approvalStatus : null,
		approvalUser : null,
		approvalDate : null,
		registStatus : 'ready',
		registUser : null,
		registDate : new Date(),
		updateUser : null,
		updateDate : new Date(),
		linkedList : null
	}
});

const NgsRawData = (function() {
	return {
		registId : -1,
		exprType : new Code(),
		domain : new Code(),
		cultureCondition : '',
		treatment : '',
		replicate : {code:'RR-01'},
		sampleName : '',
		organismType : '',
		sampleType : '',
		sampleSource : new Code(),
		geographLocation : '',
		sampleComment : '',
		platform : new Code(),
		construction : '',
		selection : '',
		sequencer : '',
		strategy : '',
		readType : new Code(),
		readLength : 0,
		insertSize : 0,
		adapterSeq : '',
		qualityScoreVersion : '',
		baseCallingProgram : '',
		libraryComment : ''
	}
});

const NgsDataFile = (function() {
	return {
		id : -1,
		registNo : '',
		dataRegist : null,
		fileName : '',
		fileType : FILE_TYPE.OTHER,
		fileSize : 0,
		checksum : '',
		verifiStatus : null,
		verifiError: '',
		registStatus : REGIST_STATUS.READY
	}
});

const NgsProcessedData = (function() {
	return {
		registId : -1,
		submitter : '',
		submitOrgan : '',
		project : '',
		projectType : '',
		submitComment : '',
		assemblyMethod : '',
		assemblyLevel : {code:'PA-00'},
		genomeType : {code:'PG-00'},
		genomeCoverage : '',
		seqComment : '',
		refTitle : '',
		refPubStatus : '',
		refComment : ''
	}
});

const Taxonomy = (function() {
	return {
		taxonId : null,
		ncbiTaxonId : null,
		parentId : null,
		name : '',
		rank : ''
	}
});

const SearchParams = (function() {
	return { 
		dataType: '',
		openYn: '',
		registStatus: '',
		registFrom: null,
		registTo: null,
		fields : '',
		gene : '', 
		product : '',
		species : '',
		strain : '',
		keyword : '',
		match : 'exact',
		page: 1,
		rowSize: 10
	}
});

const Research = (function() {
	return {
		id : -1,
		registNo : '',
		title : '',
		journal : '',
		volume : '', 
		issue : '', 
		pages : '', 
		published : '',
		submitter : '',
		submitOrgan : '',
		fullTextLink : '',
		content : '',
		openYn : 'N',
		openDate : null,
		registStatus : REGIST_STATUS.READY, 
		registUser : null, 
		registDate : null,
		updateUser : null,
		updateDate : null,
		attachmentList : [],
		omicsList : [],
		linkedList : []
	}
});

const ResearchAttachment = (function() {
	return {
		id : -1,
		registNo : '',
		research : null,
		name : '',
		type : '',
		size : 0,
		path : '',
		registUser : null,
		registDate : null
	}
});

const ResearchOmics = (function() {
	return {
		id : -1,
		registNo : '',
		research : null,
		category : null,
		name : '',
		type : '',
		size : 0,
		path : '',
		registUser : null,
		registDate : null
	}
});