package etc;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.features.Qualifier;
import org.biojava.nbio.core.sequence.features.TextFeature;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.io.GenbankReader;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderFormat;
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderParser;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

public class GenbankToFasta {

	public static void main(String[] args) throws Exception {
		
		File genbankFile = new File("samples/Heterosigma_akashiwo_example.gbk");
		File fastaFile = new File("samples/Heterosigma_akashiwo_example.fasta");
		File gffFile = new File("samples/Heterosigma_akashiwo_example.gff");
		
		Map<String, DNASequence> sequences = FastaReaderHelper.readFastaDNASequence(fastaFile);
		
		Iterator<String> names = sequences.keySet().iterator();
		while(names.hasNext()) {
			String name = names.next();
			DNASequence seq = sequences.get(name);
			
			System.out.println("> " + name);
			System.out.println(seq.getSequenceAsString());
		}
		
		
		System.out.println(" ===================== ");
		sequences = GenbankReaderHelper.readGenbankDNASequence(genbankFile);
		names = sequences.keySet().iterator();
		
		while(names.hasNext()) {
			String name = names.next();
			DNASequence sequence = sequences.get(name);
			
			System.out.println("> " + name);
			System.out.println(sequence.getLength());
			System.out.println(sequence.getSequenceAsString().substring(0, 1000));
			
			List<FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound>> features = sequence.getFeatures();
			for(FeatureInterface feature : features) {
				
//				44
				
				Iterator<String> keys = feature.getQualifiers().keySet().iterator();
				while(keys.hasNext()) {
					String key = keys.next();
					System.out.println(key + " = " + feature.getQualifiers().get(key).getClass().getName());
					
					System.out.println();
				}
			}
		}		
	}
}
