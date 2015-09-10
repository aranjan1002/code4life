#!/usr/local/bin/perl
# Diff Tester
# Ken Watford - 6/24/07
# Use/modify at own risk.

# Defaults 
#$prog1 = "rpal -ast -noout FILE"; # program 1 (FILE will be replaced)
#$prog2 = "jarpal -ast -noout FILE"; # program 2
#$test = "${ENV{HOME}}/rpal/tests/"; # test files read from here
$result = "./diffresult/"; # result files go here
$verb = 1; # 0 = silent, 1 = default, 2 = noisy
$keep = 0; # 0 = delete successful results, 1 = keep all

# Read command line arguments
while (@ARGV) {
    $_ = shift(@ARGV);
    if ($_ eq "-1") {$prog1 = shift(@ARGV)}
    elsif ($_ eq "-2") {$prog2 = shift(@ARGV)}
    elsif ($_ eq "-t") {$test = shift(@ARGV)}
    elsif ($_ eq "-r") {$result = shift(@ARGV)}
    elsif ($_ eq "-v") {$verb = shift(@ARGV)}
    elsif ($_ eq "-keep") {$keep = 1}
    else { help(); }
} # while

# Check / make adjustments to inputs if necessary.

if (!$prog1) {die "Program 1 not specified!";}
if (!$prog2) {die "Program 2 not specified!";}
if (!$test) {die "Test subject not specified!";}

if (!(-e $result)) {mkdir $result}
if (!(-d $result)) {die "$result is not a directory"}
if (!(-w $result)) {die "$result is not writable"}
if ($result !~ m|/$|) {$result .= '/'}

if (!(-e $test)) {die "$test : file not found"}
if (-d $test && $test !~ m|/$|) {$result .= '/'}

# If $test is a directory, run on each file.

if (-d $test) {
    opendir TEST, $test;
    my $file;
    my $fail = 0;
    while ($file = readdir(TEST)) {
	if ($file =~ /^\./) { next; } #skip hidden 
	if (-d $test.$file) { next; } #skip directories
	$fail += testfile($test,$file);
    }
    if ($verb > 0) {
	print "$fail tests failed!\n" if $fail;
	print "All tests succeeded.\n" if !$fail;
    }
    exit $fail;
}
else {
    $test =~ m|^(.*?)([^/]+)$|;
    $dir = $1;
    $tfile = $2;
    $fail = testfile($dir,$tfile);
    if ($verb > 0) {
	print "Test failed!\n" if $fail;
	print "Test successful!" if !$fail;
    }
    exit $fail;
}

# ------------------- helper functions ----------------------

# Tests a given file. 
# First argument: Directory containing test file
# Second argument: File to test
# Returns 0 if the file tests ok, 1 otherwise.
sub testfile {
    my $c1 = $prog1;
    my $c2 = $prog2;
    my $dir = shift;
    my $tfile = shift;
    $c1 =~ s/FILE/${dir}${tfile}/;
    $c2 =~ s/FILE/${dir}${tfile}/;
    my $file = $result . $tfile;
    print "Testing $tfile..." if $verb > 0;
    print "\n$c1" if $verb > 1;
    my $o = (system "$c1 > ${file}.1")/256;
    print " -> $o\n$c2" if $verb > 1;
    $o = (system "$c2 > ${file}.2")/256;
    print " -> $o\ncomparing... " if $verb > 1;
    $o = (system "diff -b ${file}.1 ${file}.2 > ${file}.diff")/256;
    if (-z "${file}.diff") {
	print "OK!\n" if $verb > 0;
	unless ($keep) {
	    unlink "${file}.1";
	    unlink "${file}.2";
	    unlink "${file}.diff";
	}
	return 0;
    }
    else {
	print "Doesn't match! See ${file}.diff \n" if $verb > 0;
	return 1;
    }
}


# print out documentation and quit.
sub help {
print <<END;
Unrecognized argument: $_
Usage:
    difftest.pl -1 prog1 -2 prog2 -t test 
                [-r result] [-v verbosity] [-keep]

Example:
    difftest.pl -1 "rpal -noout -ast FILE" 
                -2 "p2 -noout -ast FILE"
		-t ~/rpal/tests
                -r results -v 2
This will run "rpal" and "p2" on each file
in ~/rpal/tests, place the resulting files in
the directory 'results', and produce a lot of output.
Note the placeholder word FILE in the commands. Use
this to specify where the filename should go.

If you do not specify -keep, any result files produced for
a successful test will be deleted, so that you can focus on
looking at the failed tests. 

The value this script returns is the number of failed tests.
END
exit(255);
}
