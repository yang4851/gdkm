var SelectObjectHandler = (function(){
	return {
		openYn : [
			 { 'label' : 'ALL', 	'value' : '' }
			,{ 'label' : 'OPEN', 	'value' : 'Y' }
			,{ 'label' : 'CLOSE', 	'value' : 'N' }
		],
		
		date : [
			{ 'label' : 'ALL', 	'value' : '' }
			,{ 'label' : '2018', 	'value' : '2018' }
			,{ 'label' : '2017', 	'value' : '2017' }
		],
		
		registStatus : [
			 { 'label' : 'ALL', 					'value' : '' } 				
			,{ 'label' : 'Registration reception', 	'value' : 'ready' } 		//작성상태(준비)
			,{ 'label' : 'Verifying file', 			'value' : 'validating' } 	//검증중
			,{ 'label' : 'File error', 				'value' : 'error' }			//검증실패
			,{ 'label' : 'Finishing verification', 	'value' : 'validated' }		//검증완료
			,{ 'label' : 'Submitting', 				'value' : '' }              //제출상태(제출)
			,{ 'label' : 'Finishing registration', 	'value' : '' }              //
		],
		
		acceptStatus: [
			 { 'label' : 'ALL', 					'value' : '' } 				
			,{ 'label' : 'Accepting submission', 	'value' : 'accept' } 		//승인
			,{ 'label' : 'Rejecting submission', 	'value' : 'reject' } 		//반려
		],
		
		rawDataVerify : [
			 { 'label' : 'ALL', 			'value' : '' } 				
			,{ 'label' : 'Raw Data ID', 	'value' : 'rawDataId' }              //
			,{ 'label' : 'Source', 			'value' : 'source' }             	 //
			,{ 'label' : 'Sample Name', 	'value' : 'sampelName' }             //
			,{ 'label' : 'Organism Type', 	'value' : 'organismType' }           //
			,{ 'label' : 'Species Name', 	'value' : 'speciesName' }            //
			,{ 'label' : 'Taxonomy ID', 	'value' : 'taxonomyId' }             //
			,{ 'label' : 'Sequencing Platform', 	'value' : 'sequencingPlatform' }              //
		],
		
		rawDataExprType : [
			{ 'label' : 'Choice expriment type', 			'value' : '' } 				
			,{ 'label' : 'Genome', 	'value' : 'RE-01' }              //
			,{ 'label' : 'Metagenome', 	'value' : 'RE-02' }              //
			,{ 'label' : 'Transcriptome', 	'value' : 'RE-03' }              //
			,{ 'label' : 'Metatranscriptome', 	'value' : 'RE-04' }              //
			,{ 'label' : 'Other', 	'value' : 'RE-05' }              //
		],
		
		domain : [
			{ 'label' : 'Choice taxonomy', 			'value' : '' } 				
			,{ 'label' : 'Archaea', 	'value' : 'RT-01' }              //
			,{ 'label' : 'Bacteria', 	'value' : 'RT-02' }              //
			,{ 'label' : 'Eukaryota', 	'value' : 'RT-03' }              //
			,{ 'label' : 'Viruses', 	'value' : 'RT-04' }              //
			,{ 'label' : 'Other', 	'value' : 'RT-05' }              //
			,{ 'label' : 'Unclassified', 	'value' : 'RT-06' }              //
		],
		
		
	}
})();