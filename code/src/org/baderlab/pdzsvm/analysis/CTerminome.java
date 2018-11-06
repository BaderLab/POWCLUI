package org.baderlab.pdzsvm.analysis;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: shirleyhui
 * Date: Mar 10, 2015
 * Time: 3:04:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CTerminome {
    public CTerminome()
    {

    }
    public void makeFlatFormat(String fastaFileName, String outFileName, int numCTerm)
    {
        List uniqList = new ArrayList();
        
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFileName)));
            BufferedReader br = new BufferedReader(new FileReader(new File(fastaFileName)));
            String line = "";
            boolean proteinCoding = false;
            String seq = "";
            while((line=br.readLine())!=null)
            {
                if (line.startsWith(">"))
                {
                    if (proteinCoding)
                    {
                        if (seq.length() >= numCTerm)
                        {
                            seq = seq.substring(seq.length()-numCTerm, seq.length());
                            if (!uniqList.contains(seq))
                            {
                                uniqList.add(seq);
                                //System.out.println(seq);
                                bw.write(seq + "\n");
                            }
                        }
                        seq = "";
                        proteinCoding = false;

                    }
                    String[] splitLine = line.split(" ");
                    String proteinCodingToken = splitLine[5];
                    String[] splitCodingToken = proteinCodingToken.split(":");

                    if (splitCodingToken[1].equals("protein_coding"))
                    {
                        proteinCoding = true;
                    }
                }
                else
                {
                    if (proteinCoding)
                        seq = seq + line;
                }
            }
            br.close();
            bw.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+ e);
            e.printStackTrace();
        }
        System.out.println("Number unique protein coding C termini of length " + numCTerm + ": " + uniqList.size());
    }
    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("Usage: CTerminome <proteome fasta filename> <out filename> <num. C terminal residues>\nRequires exactly three arguments.");
            return;
        }

        String fastaFileName = args[0];
        String outFileName = args[1];
        int numCTerm = Integer.parseInt(args[2]);

        System.out.println("Proteome fasta file name: " + fastaFileName);
        System.out.println("Output file name: " + outFileName);
        System.out.println("Number C terminal residues: " + numCTerm);
        //String fastaFileName = "/Users/shirleyhui/Data/SVMProject/Data/Proteomes/Homo_sapiens.GRCh38.pep.all.fa";
        CTerminome c = new CTerminome();

        c.makeFlatFormat(fastaFileName, outFileName, numCTerm);
    }
}
