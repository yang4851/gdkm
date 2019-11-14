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
#!/usr/bin/env perl
#line 15
use strict;
use warnings;

use Getopt::Std;
use Pod::Usage;
use Bio::GFF3::Transform::FromFasta 'gff3_from_fasta';

my %opt;
getopts('t:fs:', \%opt ) or pod2usage();

gff3_from_fasta(
    in   => \@ARGV,
    out  => \*STDOUT,
    type => $opt{t},
    fasta_section => $opt{f},
    source => $opt{s},
  );

# PODNAME: gff3_from_fasta
# ABSTRACT: make GFF3 from fasta sequences

__END__

=pod

=encoding utf-8

=head1 NAME

gff3_from_fasta - make GFF3 from fasta sequences

=head1 DESCRIPTION

Thin wrapper for gff3_from_fasta function in
L<Bio::GFF3::Transform::FromFasta>.

=head1 USAGE

  gff3_from_fasta -t SO_type -s source_name  file1.fasta  file2.fasta ... > my.gff3

=head1 AUTHOR

Robert Buels <rmb32@cornell.edu>

=head1 COPYRIGHT AND LICENSE

This software is copyright (c) 2012 by Robert Buels.

This is free software; you can redistribute it and/or modify it under
the same terms as the Perl 5 programming language system itself.

=cut

__END__
:endofperl
