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
# -*-Perl-*- mode (for emacs)

=head1 NAME

bp_seqret - bioperl implementation of sequence fetch from local db (like EMBOSS seqret)

=head1 USAGE

bp_seqret [-f/--format outputformat] [-o/--out/--outfile outfile] [-d/--db dbname] [-i/--id/-s/--seqname seqname1]

Example usage:

   bp_seqret -f fasta -db db.fa -i seq1 -i seq2 > output.fas
   bp_seqret db.fa:seq1 output.fas
   bp_seqret db.fa:seq1 -o output.fas
   bp_seqret -db db.fa -o output.fas seq1 seq2 seq3
   bp_seqret -db db.fa seq1 seq2 seq3 output.fas
   bp_seqret -db db.fa seq1 seq2 seq3 - > output.fas  

The DB is expected to be a Fasta formatted sequence file with multiple
sequences.

Output format is Fasta by default.

If no output filename is provided then output is written to STDOUT.
Providing '-' as the output filename will accomplish the same thing.


=head1 AUTHOR

Jason Stajich jason_AT_bioperl-dot-org

=cut

use strict;
use warnings;
use Bio::DB::Fasta;
use Bio::SeqIO;
use Getopt::Long;

my $dbname;
my @names;
my $format = 'fasta';
my $outfile;
my ($start,$end);
GetOptions(
	   'f|format:s'   => \$format,
	   'o|out|outfile:s' => \$outfile,
	   's|sbegin|begin|start:s'  => \$start,
	   'e|send|end|stop:s'       => \$end,
	   'd|db|dbname:s'   => \$dbname,
	   'i|id|seqname:s' => \@names);


if( ! $dbname ) {
    die "need a dbname\n" unless @ARGV;
    $dbname = shift @ARGV;	
    if( $dbname =~ s/^([^:]+):// ) {
	push @names, $dbname;
	$dbname = $1;
    }				
}

my $db = Bio::DB::Fasta->new($dbname, -glob => "*.{fa,fas,fsa,fasta,pep,aa,seq,cds,peps}");
if( ! $outfile ) {
    $outfile = pop @ARGV;
}
my $out;
if( $outfile ) {
    $out = Bio::SeqIO->new(-format => $format,
			   -file   => ">$outfile");
} else {
    $out = Bio::SeqIO->new(-format => $format);
}
for my $nm ( @names ) {   
    my $seq;
    if( $start || $end ) {
	$seq = $db->seq($nm, $start => $end);
    } else { 
	$seq = $db->seq($nm);
    }
    if( $seq ) { 
	my ($id,$desc) = split(/\s+/,$db->header($nm),2);
	if( $start && $end ) { 
	    $id = sprintf("%s_%d-%d",$id,$start || 0,$end || 0);
	}
	
	$out->write_seq(Bio::PrimarySeq->new(-display_id => $id,
					     -description => $desc,
					     -seq => $seq));
    } else {
	warn("$nm not found\n");
    }
}


__END__
:endofperl
