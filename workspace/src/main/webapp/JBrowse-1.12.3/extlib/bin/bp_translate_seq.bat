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
use strict;
use warnings;

=head1 NAME

bp_translate_seq - translates a sequence

=head1 SYNOPSIS

bp_translate_seq E<lt> cdna_cds.fa E<gt> protein.fa

=head1 DESCRIPTION 

The script will translate one fasta file (on stdin) to protein on stdout

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

  Ewan Birney E<lt>birney@ebi.ac.ukE<gt>

=cut

use Bio::SeqIO;
use Getopt::Long;

my ($format,$outfile) = 'fasta';

GetOptions(
    'f|format:s'  => \$format,
    'o|out|outfile:s' => \$outfile,
    );

my $oformat = 'fasta';

# this implicity uses the <> file stream
my $seqin = Bio::SeqIO->new( -format => $format, -fh => \*ARGV); 
my $seqout;
if( $outfile ) {
    $seqout = Bio::SeqIO->new( -format => $oformat, -file => ">$outfile" );
} else {
# defaults to writing to STDOUT
    $seqout = Bio::SeqIO->new( -format => $oformat );
}
while( (my $seq = $seqin->next_seq()) ) {
	my $pseq = $seq->translate();
	$seqout->write_seq($pseq);
}

__END__

__END__
:endofperl
