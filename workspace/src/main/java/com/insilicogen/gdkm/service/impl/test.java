package com.insilicogen.gdkm.service.impl;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.Strand;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.CompoundSet;

import com.insilicogen.gdkm.model.AchiveFeaturesXref;
import com.insilicogen.gdkm.model.AchiveHeaderXref;
import com.insilicogen.gdkm.model.NgsDataFeatures;
import com.insilicogen.gdkm.model.NgsDataFeaturesHeader;
import com.insilicogen.gdkm.model.NgsDataFeaturesXref;
import com.insilicogen.gdkm.model.NgsFileAnnotation;

public class test {

	
	public static void main(String[] args) {
		
		File realFile = new File("E:/data/gdkm/processed/GDKM-BA-A-0021-002_6._Leuconostoc_gelidum_JB7.gbff");
		
		System.out.println("gbk 파일 파싱 시작 : " + realFile.getAbsolutePath());

		try {
			
			LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence(realFile, true);
			AchiveHeaderXref ahxref = new AchiveHeaderXref();
			NgsDataFeaturesHeader header = new NgsDataFeaturesHeader();

			for (DNASequence sequence : dnaSequences.values()) {
				header.setAccession(sequence.getAccession().getIdentifier());
				header.setOrganism(sequence.getDescription());
				header.setStart(sequence.getBioBegin());
				header.setEnd(sequence.getBioEnd());
//				header.setDate(achive.getRegistDate());
				
				String[] temp = sequence.getOriginalHeader().replaceAll("\\s{2,}", " ").split(" ");
				header.setLocus(temp[0]);

				if ( sequence.getFeatures() != null) {
					Iterator<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> it = sequence.getFeatures().iterator();
					int geneCount = 0;
					int cdsCount = 0;

					while (it.hasNext()) {
						FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound> ft = it.next();
						NgsDataFeatures features = new NgsDataFeatures();
						NgsDataFeaturesXref xref = new NgsDataFeaturesXref();
						AchiveFeaturesXref afxref = new AchiveFeaturesXref();
						features.setType(ft.getType());

						if (ft.getType().equalsIgnoreCase("CDS")) {
							cdsCount++;
						} else if (ft.getType().equalsIgnoreCase("gene")) {
							geneCount++;
							if(!ft.getQualifiers().containsKey("transl_table")) {
								System.out.println("start :  " + Integer.parseInt(ft.getLocations().getStart().toString()) + ", end :  "
										+ Integer.parseInt(ft.getLocations().getEnd().toString()));
								System.out.println(sequence.getSequenceAsString(Integer.parseInt(ft.getLocations().getStart().toString()),
										Integer.parseInt(ft.getLocations().getEnd().toString()), Strand.POSITIVE));
							}
						}

						features.setStart(Integer.parseInt(ft.getLocations().getStart().toString()));
						features.setEnd(Integer.parseInt(ft.getLocations().getEnd().toString()));
						features.setStrand(ft.getLocations().getStrand().toString());

						if (ft.getQualifiers().containsKey("gene")) {
							features.setGene(ft.getQualifiers().get("gene").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("product")) {
							features.setProduct(ft.getQualifiers().get("product").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("translation")) {
							features.setSequence(ft.getQualifiers().get("translation").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("codon_start")) {
							features.setCodonStart(
									Integer.parseInt(ft.getQualifiers().get("codon_start").get(0).getValue()));
						}

						if (ft.getQualifiers().containsKey("EC_number")) {
							features.setEcNumber(ft.getQualifiers().get("EC_number").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("protein_id")) {
							features.setProteinId(ft.getQualifiers().get("protein_id").get(0).getValue());
						}

						if (ft.getQualifiers().containsKey("transl_table")) {
							features.setTranslTable(
									Integer.parseInt(ft.getQualifiers().get("transl_table").get(0).getValue()));
						}

						if (ft.getQualifiers().containsKey("locus_tag")) {
							features.setLocusTag(ft.getQualifiers().get("locus_tag").get(0).getValue());
						}


					}

					NgsFileAnnotation annotation = new NgsFileAnnotation();
//					annotation.setAchive(achive);
					annotation.setCdsCount(cdsCount);
					annotation.setGeneCount(geneCount);
//					annotation.setRegistDate(achive.getRegistDate());
//					annotation.setRegistUser(achive.getRegistUser());

				}
				System.out.println("header : " + header);
			}

			System.out.println("gbk 파일 파싱 완료 : " + realFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
