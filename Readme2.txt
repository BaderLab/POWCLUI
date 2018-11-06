README
======
CTerminome 

Generates a file that contains all unique n terminal residues of all protein coding sequences in a given proteome file.

March 10, 2015
Author: Shirley Hui
University of Toronto\

Software is distribued AS IS under the GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007

REQUIREMENTS
============
This software is a java application and is confirmed to run under Java version 1.6.0_65.

HOW TO RUN
==========

java -cp org.baderlab.pdzsvm.analysis.CTerminome <proteome fasta filename> <out filename> <num. C terminal residues>

Parameters:
<proteome fasta filename>: Path to the fasta file that contains the protein sequences of the proteome of interest as downloaded from Ensembl FTP: http://useast.ensembl.org/info/data/ftp/index.html?redirect=no
<out filename>: Path to output file
<num. C terminal residues>: Number of C terminial residues

