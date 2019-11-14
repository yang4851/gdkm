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

bp_seq_length.pl - lists the number of bases and number of sequences in specified sequence database files

=head1 SYNOPSIS

bp_seq_length.pl *.fa

=head1 DESCRIPTION

bp_seq_length.pl will report the total number of residues and total number of individual sequences contained within a specified sequence database file.

=head1 OPTIONS

 -f/--format          - Specify the database format ('fasta' is default).
                        This script uses SeqIO and as such formats are 
                        limited to those which SeqIO system supports.

=head1 FEEDBACK

=head2 Mailing Lists

User feedback is an integral part of the evolution of this and other
Bioperl modules. Send your comments and suggestions preferably to
the Bioperl mailing list.  Your participation is much appreciated.

  bioperl-l@bioperl.org                  - General discussion
  http://bioperl.org/wiki/Mailing_lists  - About the mailing lists

=head2 Reporting Bugs

Report bugs to the Bioperl bug tracking system to help us keep track
of the bugs and their resolution. Bug reports can be submitted via the
web:

  https://github.com/bioperl/bioperl-live/issues

=head1 AUTHOR - Jason Stajich

Jason Stajich E<lt>jason@bioperl.orgE<gt>

=cut

use strict;
use warnings;
use Bio::SeqIO;
use Getopt::Long;

my $format = 'fasta';
GetOptions('f|format:s' => \$format);

exec('perldoc',$0) unless @ARGV;

foreach my $f ( @ARGV ) {
    my $in = new Bio::SeqIO(-file => $f,
			    -format => $format);
    my $len = 0;
    my $count = 0;
    while( my $seq = $in->next_seq ) {
	$len += $seq->length();
	$count++;
    }
    
    printf "%-10s %d bp $count sequences\n",$f,$len;
}

__END__
:endofperl
