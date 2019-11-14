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
use lib '.','./blib','../../blib/lib';
use Bio::DB::GFF;
use Getopt::Long;

my $usage = <<USAGE;
Usage: $0 [options] feature_type1 feature_type2...

Dump out a GFF-formatted histogram of the density of the indicated set
of feature types.

 Options:
   --dsn        <dsn>       Data source (default dbi:mysql:test)
   --adaptor    <adaptor>   Schema adaptor (default dbi::mysqlopt)
   --user       <user>      Username for mysql authentication
   --pass       <password>  Password for mysql authentication
   --bin        <bp>        Bin size in base pairs.
   --aggregator <list>      Comma-separated list of aggregators
   --sort                   Sort the resulting list by type and bin
   --merge                  Merge features with same method but different sources
USAGE
;

my ($DSN,$ADAPTOR,$AGG,$USER,$PASSWORD,$BINSIZE,$SORT,$MERGE);
GetOptions ('dsn:s'         => \$DSN,
	    'adaptor:s'     => \$ADAPTOR,
	    'user:s'        => \$USER,
	    'password:s'    => \$PASSWORD,
            'aggregators:s' => \$AGG,
            'bin:i'         => \$BINSIZE,
	    'sort'          => \$SORT,
	    'merge'         => \$MERGE,
	   ) or die $usage;

my @types = @ARGV or die $usage;

# some local defaults
$DSN     ||= 'dbi:mysql:test';
$ADAPTOR ||= 'dbi::mysqlopt';
$BINSIZE ||= 1_000_000;   # 1 megabase bins

my @options;
push @options,(-user=>$USER)     if defined $USER;
push @options,(-pass=>$PASSWORD) if defined $PASSWORD;
push @options,(-aggregator=>[split /\s+/,$AGG]) if defined $AGG;

my $db = Bio::DB::GFF->new(-adaptor=>$ADAPTOR,-dsn => $DSN,@options)
  or die "Can't open database: ",Bio::DB::GFF->error,"\n";

my @features = $db->features(-binsize=>$BINSIZE,-types=>\@types);

if ($MERGE) {
  my %MERGE;
  for my $f (@features) {
    my $name  = $f->name;
    my $class = $name->class;
    $name =~ s/^(.+:.+):.+$/$1/;
    $f->group(Bio::DB::GFF::Featname->new($class,$name));
    my $source = $f->source;
    $source =~ s/:.+$//;
    $f->source($source);
    if (my $already_there = $MERGE{$f->source,$f->abs_ref,$f->abs_start}) {
      $already_there->score($already_there->score + $f->score);
    } else {
      $MERGE{$f->source,$f->abs_ref,$f->abs_start} = $f;
    }
  }
  @features = values %MERGE;
}

# sort features by type, ref and start if requested
if ($SORT) {
  @features = sort {
    $a->type cmp $b->type
      || $a->abs_ref cmp $b->abs_ref
	|| $a->start <=> $b->start
      }
    @features;
}

for my $f (@features) {
  print $f->gff_string,"\n";
}


__END__

=head1 NAME

bp_generate_histogram.pl -- Generate a histogram of Bio::DB::GFF features

=head1 SYNOPSIS

  bp_generate_histogram.pl -d gadfly variation gene:curated

=head1 DESCRIPTION

Use this utility to generate feature density histograms from
Bio::DB::GFF databases.  The result is a GFF data file that is
suitable for loading with load_gff.pl.

=head2 OPTIONS

The following options are recognized:

  Option        Description
  ------        -----------

   --dsn        <dsn>       Data source (default dbi:mysql:test)
   --adaptor    <adaptor>   Schema adaptor (default dbi::mysqlopt)
   --user       <user>      Username for mysql authentication
   --pass       <password>  Password for mysql authentication
   --aggregator <list>      Comma-separated list of aggregators

=head1 BUGS

Please report them.

=head1 SEE ALSO

L<Bio::DB::GFF>

=head1 AUTHOR

Lincoln Stein E<lt>lstein@cshl.orgE<gt>

Copyright (c) 2001 Cold Spring Harbor Laboratory

This library is free software; you can redistribute it and/or modify
it under the same terms as Perl itself.  See DISCLAIMER.txt for
disclaimers of warranty.

=cut


__END__
:endofperl
