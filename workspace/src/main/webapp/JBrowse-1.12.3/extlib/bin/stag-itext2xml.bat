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

use Data::Stag qw(:all);
use Data::Stag::ITextParser;
use Data::Stag::XMLWriter;
use Data::Stag::Base;
my $p = Data::Stag::ITextParser->new;
my $h = Data::Stag::Base->new;
#my $h = Data::Stag::XMLWriter->new;
$p->handler($h);
foreach my $f (@ARGV) {
    $p->parse($f);
    print $h->tree->xml;
}

__END__

=head1 NAME

stag-itext2xml - converts between stag formats

=head1 DESCRIPTION

Converts from itext to xml format.

=head1 SEE ALSO

L<Data::Stag>

=cut

__END__
:endofperl
