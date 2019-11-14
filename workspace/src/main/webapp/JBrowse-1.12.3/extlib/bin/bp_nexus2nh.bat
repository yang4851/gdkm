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

bp_nexus2nh - convert nexus format trees (from PAUP* and MrBayes) to new hampshire

=head1 SYNOPSIS

 bp_nexus2nh file.nexus > file.nh

 # OR pipe in through STDIN

 cat file.nexus | bp_nexus2nh > file.nh

 # OR specify an output

 bp_nexus2nh -o file.nh file.nexus

=head1 DESCRIPTION

Convert Nexus Tree files into Newick/New Hampshire format tree files.


=cut

use strict;
use warnings;
use Bio::TreeIO;
use Getopt::Long;

my $outfile;

GetOptions('o|out|outfile:s' => \$outfile);

my $in = Bio::TreeIO->new(-format => 'nexus', -fh => \*ARGV);
my $out;
if( $outfile ) { 
    $out= Bio::TreeIO->new(-format => 'newick',
			   -file   => ">$outfile");
} else { 
    # write to STDOUT
    $out= Bio::TreeIO->new(-format => 'newick');
}

while( my $t = $in->next_tree ) {
    $out->write_tree($t);
}

__END__
:endofperl
