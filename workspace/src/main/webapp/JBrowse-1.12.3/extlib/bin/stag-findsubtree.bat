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
#!/usr/local/bin/perl -w
#line 15


use strict;

use Data::Stag qw(:all);
use Getopt::Long;


use strict;

use Carp;
use Data::Stag qw(:all);
use Getopt::Long;

my $parser = "";
my $handler = "";
my $mapf;
my $tosql;
my $toxml;
my $toperl;
my $debug;
my $help;
my @interpose = ();
my @regexps = ();
my @delete = ();
my @add = ();
my $lc;
my $merge;
GetOptions(
           "help|h"=>\$help,
           "parser|format|p=s" => \$parser,
           "handler|writer|w=s" => \$handler,
           "interpose|i=s@" => \@interpose,
           "add|a=s@" => \@add,
           "delete|d=s@" => \@delete,
	   "regexp|re|r=s@"=> \@regexps,
           "xml"=>\$toxml,
           "perl"=>\$toperl,
           "lc"=>\$lc,
           "debug"=>\$debug,
	   "merge=s"=>\$merge,
          );
if ($help) {
    system("perldoc $0");
    exit 0;
}


my $e = shift @ARGV;
my @files = @ARGV;

foreach my $fn (@files) {
    my $tree = Data::Stag->parse($fn);
    my @subtrees = $tree->find($e);
    print $_->xml foreach  (@subtrees);
}

__END__

=head1 NAME 

stag-findsubtree - finds nodes in a stag file

=head1 SYNOPSIS

  stag-findsubtree 'person/name' file.xml

=head1 DESCRIPTION

parses in an input file and writes out subnodes

=head1 USAGE

  stag-findsubtree [-p PARSER] [-w WRITER] NODE FILE

=head1 ARGUMENTS

=over

=item -p|parser FORMAT

FORMAT is one of xml, sxpr or itext, or the name of a perl module

xml assumed as default

=item -w|writer FORMAT

FORMAT is one of xml, sxpr or itext, or the name of a perl module

=item NODE

the name of the node/element 

=back

=head1 LIMITATIONS

not event based

=head1 SEE ALSO

L<Data::Stag>

=cut


__END__
:endofperl
