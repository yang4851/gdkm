var USER = [ {
	id : 'administrator',
	name : '관리자',
	age : 64,
	height : 185,
	weight : 77,
	permissionGroup : 'Accept worker / System Manager',
	email : 'ismun@wikim.re.kr',
	affiliation : 'World Institute of Kimchi',
	department : 'Microbiology and Functionality RG',
	phone : '010-0070-0070',
	contact : '+82-62-123-4567',
	useYn : 'Y',
	auth : 'admin'
},
{
	id : 'administrator1',
	name : '관리자1',
	age : 641,
	height : 1851,
	weight : 771,
	permissionGroup : 'Accept worker / System Manager',
	email : 'ismun1@wikim.re.kr',
	affiliation : 'World Institute of Kimchi',
	department : 'Microbiology and Functionality RG',
	phone : '010-0070-0220',
	contact : '+82-62-123-4557',
	useYn : 'Y',
	auth : 'admin'
},
{
	id : 'administrator2',
	name : '관리자2',
	age : 642,
	height : 1852,
	weight : 772,
	permissionGroup : 'Accept worker / System Manager',
	email : 'ismun2@wikim.re.kr',
	affiliation : 'World Institute of Kimchi',
	department : 'Microbiology and Functionality RG',
	phone : '010-0070-3370',
	contact : '+82-62-123-4447',
	useYn : 'Y',
	auth : 'admin'
}
]


var FILE_LIST = [ {
	id : 1,
	name : 'TN1R0255_1.fq',
	rawDataId : 'GDKM-N-BA-000001',
	rawDataFileId : 'GDKM-N-BA-000001-01',
	format : 'fastq',
	size : '1,000MB',
	md5 : '287e6bc2e564da741f5bd75224clxa',
	seqReadCnt : 2000,
	seqTotSize : 100000000,
	seqAvgLenngth : 10000,
	GcContents : 50,
	AseqCnt : 25001,
	TseqCnt : 25002,
	GseqCnt : 25003,
	CseqCnt : 25004,
	NseqCnt : 25005
}, {
	id : 2,
	name : 'TN1R0255_4.bas',
	rawDataId : 'GDKM-N-BA-000001',
	rawDataFileId : 'GDKM-N-BA-000001-02',
	format : 'bas.h5',
	size : '1,000MB',
	md5 : '287e6bc2e564da741f5bd75224clxa',
	seqReadCnt : 2000,
	seqTotSize : 100000000,
	seqAvgLenngth : 10000,
	GcContents : 50,
	AseqCnt : 25001,
	TseqCnt : 25002,
	GseqCnt : 25003,
	CseqCnt : 25004,
	NseqCnt : 25005
}, {
	id : 3,
	name : 'TN1R0255_1.tnt',
	rawDataId : 'GDKM-BA-A-000002',
	rawDataFileId : 'GDKM-BA-A-000002-01',
	format : 'bax.h5',
	size : '1,000MB',
	md5 : '287e6bc2e564da741f5bd75224clxa',
	seqReadCnt : 2000,
	seqTotSize : 100000000,
	seqAvgLenngth : 10000,
	GcContents : 50,
	AseqCnt : 25001,
	TseqCnt : 25002,
	GseqCnt : 25003,
	CseqCnt : 25004,
	NseqCnt : 25005
}, {
	id : 4,
	name : 'TN1R0255_1.bax.h5',
	annotationDataId : 'GDKM-N-BA-000001',
	annotationDataFileId : 'GDKM-N-BA-000001-01',
	format : 'bax.h5',
	size : '1,000MB',
	md5 : '287e6bc2e564da741f5bd75224clxa',
	seqReadCnt : 2000,
	seqTotSize : 100000000,
	seqAvgLenngth : 10000,
	GcContents : 50,
	AseqCnt : 25001,
	TseqCnt : 25002,
	GseqCnt : 25003,
	CseqCnt : 25004,
	NseqCnt : 25005,
	locus : 'Se3_3_1',
	organism : 'Genus species strain strain',
	strain : 'Se3_3_1',
	registDate : '2017.07.12',
	registStatus : 'WAIT'
}, {
	id : 6,
	name : 'TN1R0255_1.gbk',
	annotationDataId : 'GDKM-N-BA-000005',
	annotationDataFileId : 'GDKM-N-BA-000005-02',
	format : 'gbk',
	size : '3,000MB',
	md5 : '287e6bc2e564da741f5bd75224clxa',
	seqReadCnt : 2000,
	seqTotSize : 100000000,
	seqAvgLenngth : 10000,
	GcContents : 50,
	AseqCnt : 25001,
	TseqCnt : 25002,
	GseqCnt : 25003,
	CseqCnt : 25004,
	NseqCnt : 25005,
	locus : 'Se3_3_1',
	organism : 'Genus species strain strain',
	strain : 'Se3_3_1',
	registDate : '2017.07.12',
	registStatus : 'WAIT'
}, {
	id : 7,
	name : 'TN1R0255_1.fasta',
	annotationDataId : 'GDKM-N-BA-000002',
	annotationDataFileId : 'GDKM-N-BA-000002-02',
	format : 'fasta',
	size : '3,000MB',
	md5 : '287e6bc2e564da741f5bd75224clxa',
	seqReadCnt : 2000,
	seqTotSize : 100000000,
	seqAvgLenngth : 10000,
	GcContents : 50,
	AseqCnt : 25001,
	TseqCnt : 25002,
	GseqCnt : 25003,
	CseqCnt : 25004,
	NseqCnt : 25005,
	locus : 'Se3_3_1',
	organism : 'Genus species strain strain',
	strain : 'Se3_3_1',
	registDate : '2017.07.12',
	registStatus : 'WAIT'
}, {
	id : 5,
	name : 'TN1R0255_1.gff',
	annotationDataId : 'GDKM-N-BA-000002',
	annotationDataFileId : 'GDKM-N-BA-000002-01',
	format : 'gff',
	size : '1,000MB',
	md5 : '287e6bc2e564da741f5bd75224clxa',
	seqReadCnt : 20040,
	seqTotSize : 1000300000,
	seqAvgLenngth : 120000,
	GcContents : 50,
	AseqCnt : 125001,
	TseqCnt : 225002,
	GseqCnt : 325003,
	CseqCnt : 425004,
	NseqCnt : 525005,
	locus : 'Se3_3_1',
	organism : 'Genus species strain strain',
	strain : 'Se3_3_1',
	registDate : '2017.07.12',
	registStatus : 'WAIT'
} ]

var ANNOTATION_DATA = [ {
	id : 1,
	annotationDataId : 'GDKM-BA-A-00001',
	registDate : '2018-01-02',
	registStatus : 'CONFIRM',
	openStatus : 'Y',
	file : FILE_LIST[5],
	files : [FILE_LIST[2], FILE_LIST[5]],
	submitterInfo : {
		submitter : 'Seeong Woon Roh',
		submitterOrganization : 'World Institute of Kimchi',
		project : 'Genome sequences of Natrinema thermophila CBA1119T',
		projectType : 'whole genome shotgun sequencing',
		comment : 'XXX'
	},
	sequencing : {
		assemblyMethods : 'PacBio SMRT Anlysis 2.3.1',
		assemblyLevel : 'draft',
		genomeType : 'circular',
		genomeCoverage : '278.85',
		comment : 'XXX'
	},
	reference : {
		title : 'Genome sequences of Natrinema thermophila CBA1119T',
		publishingStatus : 'unpublished',
		comment : 'XXX'
	},
	linkedRawData : [ {
		id : 'GDKM-BA-N-000001',
		exprType : 'Genome1',
		seqPlatform : 'illumina-hiseq1'
	}, {
		id : 'GDKM-BA-N-000002',
		exprType : 'Genome2',
		seqPlatform : 'illumina-hiseq2'
	}, {
		id : 'GDKM-BA-N-000003',
		exprType : 'Genome3',
		seqPlatform : 'illumina-hiseq3'
	} ]
}, {
	id : 2,
	annotationDataId : 'GDKM-BA-A-00002',
	registDate : '2018-03-04',
	registStatus : 'WAIT',
	openStatus : 'N',
	file : FILE_LIST[4],
	files : [FILE_LIST[2], FILE_LIST[5], FILE_LIST[1]],
	submitterInfo : {
		submitter : 'Woon RohSeeong',
		submitterOrganization : 'World Institute of song-pyon',
		project : 'genome project',
		projectType : 'whole genome shotgun sequencing',
		comment : 'XXXXX'
	},
	sequencing : {
		assemblyMethods : 'PacBio SMRT Anlysis 2.3.5',
		assemblyLevel : 'draft',
		genomeType : 'circular',
		genomeCoverage : '278.85',
		comment : 'XXXXX'
	},
	reference : {
		title : 'Genome sequences of Natrinema thermophila CBA1119T',
		publishingStatus : 'unpublished',
		comment : 'XXX'
	},
	linkedRawData : [ {
		id : 'GDKM-BA-N-000001',
		exprType : 'Genome1',
		seqPlatform : 'illumina-hiseq1'
	}, {
		id : 'GDKM-BA-N-000002',
		exprType : 'Genome2',
		seqPlatform : 'illumina-hiseq2'
	} ]
},{
	id : 3,
	annotationDataId : 'GDKM-BA-A-00003',
	registDate : '2018-03-04',
	registStatus : 'WAIT',
	openStatus : 'N',
	file : FILE_LIST[6],
	files : [FILE_LIST[2], FILE_LIST[5], FILE_LIST[1]],
	submitterInfo : {
		submitter : 'Woon RohSeeong',
		submitterOrganization : 'World Institute of song-pyon',
		project : 'genome project',
		projectType : 'whole genome shotgun sequencing',
		comment : 'XXXXX'
	},
	sequencing : {
		assemblyMethods : 'PacBio SMRT Anlysis 2.3.5',
		assemblyLevel : 'draft',
		genomeType : 'circular',
		genomeCoverage : '278.85',
		comment : 'XXXXX'
	},
	reference : {
		title : 'Genome sequences of Natrinema thermophila CBA1119T',
		publishingStatus : 'unpublished',
		comment : 'XXX'
	},
	linkedRawData : [ {
		id : 'GDKM-BA-N-000001',
		exprType : 'Genome1',
		seqPlatform : 'illumina-hiseq1'
	}, {
		id : 'GDKM-BA-N-000002',
		exprType : 'Genome2',
		seqPlatform : 'illumina-hiseq2'
	} ]
}

];

var RAW_DATA = [
		{
			id : 1,
			rawDataId : 'GDKM-N-BA-000001',
			openStatus : 'Y',
			file : FILE_LIST[0],
			files : FILE_LIST,
			exprType : 'Genome',
			taxonomy : 'Archaea',
			cultureCondition: ' Kimchi microbes strain - UCC118',
			treatment : 'Control of kimchi additive reaction',
			replicate : 'Experimental replicate',
			sample : {
				name : 'Lactobacillus salivarius UCC118(1)',
				strain : 'UCC118',
				organismType : 'prokaryote',
				bioasampleType : 'solar salt',
				geographicLocation : 'South Korea',
				source : 'Kimchi',
				comment : 'XXX'
			},
			ncbiTaxonomy : {
				speciesName : 'Lactobacillus salivarius UCC118',
				taxonomyId : '235346',
				domain : 'Bacteria',
				kingdom : 'Bactria',
				phylum : 'Firmicutes',
				class : 'Bacilli',
				order : 'Lactobacillales',
				family : 'genus',
				genus : 'Lactobacilus',
				species : 'sailivarius'
			},
			registDate : '2017-07-22',
			registStatus : 'WAIT',
			library : {
				sequencingPlatform : 'illumina-hiseq',
				construction : 'TruSeq',
				selection : 'RANDOM',
				instrumentModel : 'illumina Hiseq',
				strategy : 'WGS',
				readLength : 150,
				insertSize : 250,
				read : 'Paired end',
				adapterSequence : '1`-GTTCAGAGTTCACAGTCCGACGATC  / 3`-TGGAATTCTCGGGTGCCAAGG',
				qualityScoreVersion : 'illumina 1.5+Phred+64',
				baseCallingProgram : 'Phired'
			}
		},
		{
			id : 2,
			rawDataId : 'GDKM-N-BA-000002',
			openStatus : 'N',
			file : FILE_LIST[1],
			files : FILE_LIST,
			exprType : 'Genome',
			taxonomy : 'Eukayota',
			cultureCondition : ' Kimchi microbes strain - UCC118',
			treatment : 'Control of kimchi additive reaction',
			replicate : 'Experimental replicate',
			sample : {
				name : 'Lactobacillus salivarius UCC118(2)',
				strain : 'UCC118',
				organismType : 'prokaryote',
				bioasampleType : 'solar salt',
				geographicLocation : 'South Korea',
				source : 'Salt',
				comment : 'XXX'
			},
			ncbiTaxonomy : {
				speciesName : 'Lactobacillus salivarius UCC118',
				taxonomyId : '235346',
				domain : 'Bacteria',
				kingdom : 'Bactria',
				phylum : 'Firmicutes',
				class : 'Bacilli',
				order : 'Lactobacillales',
				family : 'genus',
				genus : 'Lactobacilus',
				species : 'sailivarius'
			},
			registDate : '2017-07-22',
			registStatus : 'CONFIRM',
			library : {
				sequencingPlatform : 'illumina-hiseq',
				construction : 'TruSeq',
				selection : 'RANDOM',
				instrumentModel : 'illumina Hiseq',
				strategy : 'WGS',
				readLength : 150,
				insertSize : 250,
				read : 'Paired end',
				adapterSequence : '2`-GTTCAGAGTTCACAGTCCGACGATC  / 3`-TGGAATTCTCGGGTGCCAAGG',
				qualityScoreVersion : 'illumina 1.5+Phred+64',
				baseCallingProgram : 'Phired'
			}
		},
		{
			id : 3,
			rawDataId : 'GDKM-N-BA-000002',
			openStatus : 'Y',
			file : FILE_LIST[2],
			files : FILE_LIST,
			exprType : 'Genome',
			taxonomy : 'Viruses',
			cultureCondition : ' Kimchi microbes strain - UCC118',
			treatment : 'Control of kimchi additive reaction',
			replicate : 'Experimental replicate',
			sample : {
				name : 'Lactobacillus salivarius UCC118(3)',
				strain : 'UCC118',
				organismType : 'prokaryote',
				bioasampleType : 'solar salt',
				geographicLocation : 'South Korea',
				source : 'Kimchi',
				comment : 'XXX'
			},
			ncbiTaxonomy : {
				speciesName : 'Lactobacillus salivarius UCC118',
				taxonomyId : '235346',
				domain : 'Bacteria',
				kingdom : 'Bactria',
				phylum : 'Firmicutes',
				class : 'Bacilli',
				order : 'Lactobacillales',
				family : 'genus',
				genus : 'Lactobacilus',
				species : 'sailivarius'
			},
			registDate : '2017-07-22',
			registStatus : 'WAIT',
			library : {
				sequencingPlatform : 'illumina-hiseq',
				construction : 'TruSeq',
				selection : 'RANDOM',
				instrumentModel : 'illumina Hiseq',
				strategy : 'WGS',
				readLength : 150,
				insertSize : 250,
				read : 'Paired end',
				adapterSequence : '3`-GTTCAGAGTTCACAGTCCGACGATC  / 3`-TGGAATTCTCGGGTGCCAAGG',
				qualityScoreVersion : 'illumina 1.5+Phred+64',
				baseCallingProgram : 'Phired'
			}
		} ];