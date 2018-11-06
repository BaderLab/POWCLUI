README
======
POWSeqCLUI (POW Seq Command Line User Interface)
POWStructCLUI (POW Struct Command Line User Interface)

April 10, 2013
Authors: Shirley Hui, Gary D. Bader
University of Toronto, Dept. Molecular Genetics

Please cite:
Hui S, Bader GD, BMC Bioinformatics 2010, 11:507
Hui S, Xing X, Bader GD, BMC Bioinformatics 2013, 14:27

Software is distribued AS IS under the GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007

Website version: http://webservice.baderlab.org/domains/POW/

REQUIREMENTS
============
This software is a java application and is confirmed to run under Java version 1.5.0_30.

HOW TO RUN
==========

1) Unzip POWCLUI.zip in a directory
2) At the command line, run one of the following commands in the directory above.

POWSeqCLUI:
java -cp .:lib/libsvm-2.89-pdzsvm.jar org.baderlab.pdzsvm.analysis.POWSeqCLUI <organism> <domain name file> <peptide file>

POWStructCLUI:
java -cp .:lib/libsvm-2.89-pdzsvm.jar org.baderlab.pdzsvm.analysis.POWStructCLUI <organism> <domain name file> <peptide file>

Parameters:
<organism>: H = Human, M = Mouse, W = Worm, F = Fly
<domain name file>: Path to a file containing the PDZ domain names (one per line)
<peptide file>: Path to a file containing the peptides of length five (one per line)

Note: POWCLUI takes exactly three parameters.

OUTPUT
======
Domain names that are repeated or not supported are skipped.
Peptides that are too short (< 5), contain invalid residues or are repeated are skipped.
Peptides that are too long (> 5) are truncated and skipped if repeated.
The programs will output information for all predictions (positive and negative).

Information reported include:
* Domain Name
* Binding Site Sequence (POWSeqCLUI only)
* Peptide Sequence
* Decision Value

EXAMPLES
========
1) POWSeqCLUI Output:

>java -cp .:lib/libsvm-2.89-pdzsvm.jar org.baderlab.pdzsvm.analysis.POWSeqCLUI H examples/PDZDomains.txt examples/Peptides.txt
POWSeqCLUI (POW Seq Command Line User Interface)
April 10, 2013
Authors: Shirley Hui, Gary D. Bader
University of Toronto, Dept. Molecular Genetics

Please cite:
Hui S, Bader GD, BMC Bioinformatics 2010, 11:507
Hui S, Xing X, Bader GD, BMC Bioinformatics 2013, 14:27

Software is distribued AS IS under the GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007

Organism: H
Domain Names:
	Error: Domain BLAH not supported, skipping...
	2 domain(s) read...
Peptides:
	Warning: Peptide EDTWV already in list, skipping...
	Error: Peptide S too short, skipping...
	Warning: Peptide SSEDTWV too long, truncating...
	Warning: Peptide EDTWV already in list, skipping...
	Error: Invalid residue in peptide 12345, skipping...
	3 peptide(s) read...
Results: 
DomainName	BindingSiteSequence	PeptideSequence	DecisionScore
DLG1-1	GLGFSIAGTKIGHSVA	FLGWF	-1.8040996291275562
DLG1-1	GLGFSIAGTKIGHSVA	EDTWV	-0.19450785721099295
DLG1-1	GLGFSIAGTKIGHSVA	SSSSS	-1.3645443983035186
MPDZ-1	SLGFSVVGQEQSHQIA	FLGWF	0.6199364236346863
MPDZ-1	SLGFSVVGQEQSHQIA	EDTWV	0.5265758575783792
MPDZ-1	SLGFSVVGQEQSHQIA	SSSSS	-1.3837781314980506

1) POWStructCLUI Output:

>java -cp .:lib/libsvm-2.89-pdzsvm.jar org.baderlab.pdzsvm.analysis.POWStructCLUI H examples/PDZDomains.txt examples/Peptides.txt 
POWStructCLUI (POW Struct Command Line User Interface)
April 10, 2013
Authors: Shirley Hui, Gary D. Bader
University of Toronto, Dept. Molecular Genetics

Please cite:
Hui S, Bader GD, BMC Bioinformatics 2010, 11:507
Hui S, Xing X, Bader GD, BMC Bioinformatics 2013, 14:27

Software is distribued AS IS under the GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007

Organism: H
Domain Names:
	Error: Domain BLAH not supported, skipping...
	2 domain(s) read...
Peptides:
	Warning: Peptide EDTWV already in list, skipping...
	Error: Peptide S too short, skipping...
	Warning: Peptide SSEDTWV too long, truncating...
	Warning: Peptide EDTWV already in list, skipping...
	Error: Invalid residue in peptide 12345, skipping...
	3 peptide(s) read...
Results: 
DomainName	PeptideSequence	DecisionScore
MPDZ-1	FLGWF	1.3722791766167184
MPDZ-1	SSSSS	-2.298175924389645
MPDZ-1	EDTWV	0.6017692863722264
DLG1-1	FLGWF	-1.214349274364786
DLG1-1	SSSSS	-2.5483924802411475
DLG1-1	EDTWV	0.9241258336582265
