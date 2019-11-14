@rem = '--*-Perl-*--
@echo off
if "%OS%" == "Windows_NT" goto WinNT
perl -x -S "%0" %1 %2 %3 %4 %5 %6 %7 %8 %9
goto endofperl
:WinNT
perl -x -S %0 %*
if NOT "%COMSPEC%" == "%SystemRoot%\system32\cmd.exe" goto endofperl
if %errorlevel% == 9009 echo You do not have Perl in your PATH.
if errorlevel 1 goto script_failed_so_exit_with_non_zero_val 2>nul
goto endofperl
@rem ';
#!C:\Strawberry\perl\bin\perl.exe 
#line 15
#
=head1 NAME

bp_make_mrna_protein - Convert an input mRNA/cDNA sequence into protein

=head1 DESCRIPTION

Convert an input mRNA/cDNA sequence into protein using translate()

  -f/--frame           Specifies frame [0,1,2]

One can also specify:

  -t/--terminator      Stop Codon character (defaults to '*')
  -u/--unknown         Unknown Protein character (defaults to 'X')
  -cds/--fullcds       Expected Full CDS (with start and Stop codon)
  -throwOnError        Throw error if no Full CDS (defaults to 0)
  -if/--format         Input format (defaults to FASTA/Pearson)
  -of/--format         Output format (defaults to FASTA/Pearson)
  -o/--output          Output Filename (defaults to STDOUT)
  -i/--input           Input Filename (defaults to STDIN)
  -ct/--codontable     Codon table to use (defaults to '1')

See L<Bio::PrimarySeq> for more information on codon tables
and the translate() method

=head1 AUTHOR - Jason Stajich

  Email jason-at-bioperl-dot-org

=cut

use strict;
use warnings;
use Bio::SeqIO;
use Getopt::Long;

use vars qw($USAGE);

BEGIN {
    $USAGE =
qq{make_mrna_protein.pl < file.fa > file.prots
-f/--frame            Translation Frame (0,1,2) are valid (defaults to '0')
-t/--terminator	    Stop Codon Character ('*' by default)
-u/--unknown          Unknown Protein character (defaults to 'X')
-ct/--codontable      Codon table to use (defaults to '1')
                      (see Bio::PrimarySeq for more information)
-cds/--fullcds        Expected Full CDS (with start and Stop codon)
-throwOnError         Throw an error if no Full CDS (defaults to 0)
-if/--iformat         Input format (defaults to FASTA/Pearson)
-of/--oformat         Output format (defaults to FASTA/Pearson)
-o/--output           Output Filename (defaults to STDOUT)
-i/--input            Input Filename (defaults to STDIN)
};

}
my ($iformat, $oformat, $frame, $termchar, $unknownProt, $codontable, $fullCDS,
    $throw_on_Incomp_CDS, $help) = ('fasta','fasta', 0, undef, undef, 1, 0, 0);
my ($input,$output);

GetOptions('f|frame:s'       => \$frame,
			  't|terminator:s'  => \$termchar,
			  'u|unknown:s'     => \$unknownProt,
			  'ct|codontable:s' => \$codontable,
			  'cds|fullcds'     => \$fullCDS,
			  'throwOnError'    => \$throw_on_Incomp_CDS,
			  'h|help'          => \$help,
			  'i|input:s'       => \$input,
			  'if|iformat:s'    => \$iformat,
			  'of|oformat:s'    => \$oformat,
			  'o|output:s'      => \$output,
			 );

die $USAGE if( $help );

my ($in,$out);
if( $input ) {
	$in = new Bio::SeqIO('-format' => $iformat, '-file' => $input);
} else {
	$in = new Bio::SeqIO('-format' => $iformat, '-fh' => \*STDIN);
}

if( $output ) { 
	$out = new Bio::SeqIO('-format' => $oformat, '-file' => ">$output" );
} else {
	$out = new Bio::SeqIO('-format' => $oformat );
}

while( my $seq = $in->next_seq ) {
    my $protseq = $seq->translate(-terminator => $termchar,
											 -unknown => $unknownProt,
											 -frame => $frame,
											 -codontable_id => $codontable,
											 -complete => $fullCDS,
											 -throw => $throw_on_Incomp_CDS );
    $out->write_seq($protseq);
}

__END__

__END__
:endofperl
