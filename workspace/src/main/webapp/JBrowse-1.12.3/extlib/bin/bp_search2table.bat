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

=head1 NAME

bp_search2table - turn SearchIO parseable reports into tab delimited format like NCBI's -m 9 

=head1 SYNOPSIS

  bp_search2table -f fasta -i file.FASTA -o output.table

=head1 DESCRIPTION 

Turn SearchIO reports into a tabular format like NCBI's -m 9 output.

=head1 FEEDBACK

=head2 Mailing Lists

User feedback is an integral part of the evolution of this and other
Bioperl modules. Send your comments and suggestions preferably to
the Bioperl mailing list.  Your participation is much appreciated.

  bioperl-l@bioperl.org                  - General discussion
  http://bioperl.org/wiki/Mailing_lists  - About the mailing lists

=head2 Reporting Bugs

Report bugs to the Bioperl bug tracking system to help us keep track
of the bugs and their resolution. Bug reports can be submitted via
email or the web:

  https://github.com/bioperl/bioperl-live/issues

=head1 AUTHOR

  Jason Stajich jason_at_bioperl-dot-org

=cut

use strict;
use warnings;
use Bio::SearchIO;
use Getopt::Long;

my ($format, $file,$output) = ('blast');

GetOptions(
	   'f|format:s'   => \$format,
	   'i|input:s'    => \$file,
	   'o|output:s'   => \$output);

if( @ARGV ) { 
    $file = shift;
}
    
my $in = Bio::SearchIO->new(-format => $format,
			    -file   => $file);
my $out;
if( $output ) { 
    open $out, '>', $output or die "Could not write file '$output': $!\n";
} else { 
    $out = \*STDOUT;
}

while( my $r = $in->next_result ) {
    while( my $hit = $r->next_hit ) {
	while( my $hsp = $hit->next_hsp ) {
	    my $mismatchcount = $hsp->length('total') - 
		($hsp->num_conserved + $hsp->gaps('total'));
	    print $out join("\t", ( $r->query_name,
				    $hit->name,
				    sprintf("%.2f",$hsp->percent_identity),
				    $hsp->length('total'),
				    $mismatchcount,
				    $hsp->gaps('total'),
				    # flip start/end on rev strand
				    $hsp->query->strand < 0 ?
				    ( $hsp->query->end,
				      $hsp->query->start ) :
				    ( $hsp->query->start,
				      $hsp->query->end ),
				    $hsp->hit->strand < 0 ?
				    ( $hsp->hit->end,
				      $hsp->hit->start ) :
				    ( $hsp->hit->start,
				      $hsp->hit->end ),

				    $hsp->evalue,
				    # chance this to $hsp->sw_score 
				    # if you would rather have that
				    # it will only work for FASTA parsing though!
				    $hsp->bits)),"\n";
	}
    }
}

__END__
:endofperl
