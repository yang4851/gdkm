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
use Data::Stag::XMLParser;
use Data::Stag::ITextWriter;
my $p = Data::Stag::XMLParser->new;
my $h = Data::Stag::ITextWriter->new;
$p->handler($h);
foreach my $xmlfile (@ARGV) {
    $p->parse($xmlfile);
}
__END__

=head1 NAME

stag-xml2itext - converts between stag formats

=head1 DESCRIPTION

Converts from xml to itext format.

=head1 SEE ALSO

L<Data::Stag>

=cut


__END__
:endofperl
