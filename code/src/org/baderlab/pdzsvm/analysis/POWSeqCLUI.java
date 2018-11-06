package org.baderlab.pdzsvm.analysis;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import libsvm.*;

/**
 * Created by IntelliJ IDEA.
 * User: shirleyhui
 * Date: Aug 8, 2010
 * Time: 10:22:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class POWSeqCLUI {
    public POWSeqCLUI()
    {
    }
    public static void main(String[] args)
    {
        System.out.println("POWSeqCLUI (POW Seq Command Line User Interface)");
        System.out.println("April 10, 2013");
        System.out.println("Authors: Shirley Hui, Gary D. Bader");
        System.out.println("University of Toronto, Dept. Molecular Genetics");
        System.out.println();
        System.out.println("Please cite:");
        System.out.println("Hui S, Bader GD, BMC Bioinformatics 2010, 11:507");
        System.out.println("Hui S, Xing X, Bader GD, BMC Bioinformatics 2013, 14:27");
        System.out.println();
        System.out.println("Software is distribued AS IS under the GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007");
        System.out.println();
        if (args.length != 3)
            System.out.println("Usage: POWSeqCLUI <organism: H,M,W,F> <domain name file> <peptide file>\nRequires exactly three arguments.");
        else
        {
            String organism = args[0];
            System.out.println("Organism: " + organism);

            if (!organism.equals("H") && !organism.equals("M") && !organism.equals("W") && !organism.equals("F"))
            {
                System.out.println("\tError: Organism "+ organism +" not supported...");
            }
            else
            {
                System.out.println("Domain Names:");
                String domainNameFile = args[1];
                HashMap domainNameSeqMap = getDomainNameSeqMap(domainNameFile, organism);

                if (!domainNameSeqMap.isEmpty())
                {
                    List domainNameList = new ArrayList(domainNameSeqMap.keySet());
                    Collections.sort(domainNameList);
                    System.out.println("\t"+domainNameList.size() + " domain(s) read...");

                    System.out.println("Peptides:");
                    String peptideFileName = args[2];
                    List peptideSeqList = getPeptideSeqList(peptideFileName);
                    if (!peptideSeqList.isEmpty())
                    {
                        System.out.println("\t"+peptideSeqList.size() + " peptide(s) read...");


                        POWSeqCLUI t = new POWSeqCLUI();
                        t.clui(domainNameSeqMap, organism, peptideSeqList);

                    }
                }
            }
        }
    }

    private static HashMap getDomainNameSeqMap(String fileName, String organism)
    {
        HashMap domainNameSeqMap = new HashMap();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String domainName = "";
            while((domainName=br.readLine())!=null)
            {
                String domain16Seq = getBindingSiteSeq(domainName, organism);
                if (domain16Seq == null)
                {
                    System.out.println("\tError: Domain "+ domainName +" not supported, skipping...");
                }
                else
                {
                    domainNameSeqMap.put(domainName, domain16Seq);
                }
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println("\tError: " + e);
        }
        return domainNameSeqMap;
    }
    private static boolean isPeptideValid(String peptideSeq)
    {
        String alphabet = "ACDEFGHIKLMNPQRSTVWYX";
        for (int ii =0;ii < peptideSeq.length();ii++)
        {
            String res = String.valueOf(peptideSeq.charAt(ii));
            int value = alphabet.indexOf(res);
            if (value <0)
            {
                return false;
            }
        }
        return true;
    }
    private static List getPeptideSeqList(String fileName)
    {
        List peptideSeqList = new ArrayList();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String peptideSeq = "";
            while((peptideSeq=br.readLine())!=null)
            {
                if (peptideSeq.length()<5)
                {
                    System.out.println("\tError: Peptide "+ peptideSeq +" too short, skipping...");
                }
                else
                {
                    if (!isPeptideValid(peptideSeq))
                    {
                        System.out.println("\tError: Invalid residue in peptide " + peptideSeq +", skipping...");
                    }
                    else
                    {
                        if (peptideSeq.length() > 5)
                        {
                            System.out.println("\tWarning: Peptide "+peptideSeq + " too long, truncating...");
                            peptideSeq = peptideSeq.substring(peptideSeq.length()-5,peptideSeq.length());
                        }

                        if (!peptideSeqList.contains(peptideSeq))
                            peptideSeqList.add(peptideSeq);
                        else
                            System.out.println("\tWarning: Peptide "+peptideSeq + " already in list, skipping...");
                    }

                }
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println("\tError: " + e);
        }
        return peptideSeqList;
    }
    private static String getBindingSiteSeq(String domainName, String organism)
    {
        try
        {   String filename = "";
            if (organism.equals("H"))
            {
                filename = "features/HumanPDZDomainSequenceFeatures.txt";
            }
            else if (organism.equals("M"))
            {
                filename = "features/MousePDZDomainSequenceFeatures.txt";
            }
            else if (organism.equals("W"))
            {
                filename = "features/WormPDZDomainSequenceFeatures.txt";
            }
            else if (organism.equals("F"))
            {
                filename = "features/FlyPDZDomainSequenceFeatures.txt";
            }
            else
            {
                System.out.println("\tError: Organism "+ organism +" not supported...");
            }
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line = "";
            boolean found = false;
            String bindingSite = "";
            while((line=br.readLine())!=null)
            {

                String[] splitLine = line.split("\t");
                String domainNameA = splitLine[0];
                bindingSite = splitLine[1];
                int ix = domainNameA.indexOf("*");
                if (ix > -1)
                {
                    domainNameA = domainNameA.substring(0, domainNameA.length()-1);
                }
                if (domainName.equals(domainNameA))
                {
                    found = true;
                    break;
                }
            }
            br.close();

            if (!found)
                return null;
            else
                return bindingSite;

        }
        catch(Exception e)
        {
            System.out.println("\tError: " + e);
        }
        return null;

    }
    private void clui(HashMap domainNameSeqMap, String organism, List peptideSeqList)
    {

        System.out.println("Results: ");
        System.out.println("DomainName\tBindingSiteSequence\tPeptideSequence\tDecisionScore");

        List domainNameList = new ArrayList(domainNameSeqMap.keySet());
        Collections.sort(domainNameList);
        for (int j = 0; j < domainNameList.size();j++)
        {
            String domainName = (String)domainNameList.get(j);
            String domain16Seq = (String)domainNameSeqMap.get(domainName);
            for (int i = 0; i< peptideSeqList.size();i++)
            {
                String peptideSeq = (String)peptideSeqList.get(i);
                svm_node[] x = encode(domain16Seq, peptideSeq);
                double[] dec_values = new double[1];
                svm_model svmModel = null;
                try
                {
                    svmModel= svm.svm_load_model("models/PDZSVM_SEQ_May2010.model");
                }
                catch(Exception e)
                {
                    System.out.println("\tError: " + e);
                }
                svm.svm_predict_values(svmModel, x,  dec_values);
                System.out.println(domainName +"\t" + domain16Seq + "\t" + peptideSeq + "\t" + dec_values[0]);
            }
        }
    }

    public svm_node[] encode(String domain16Seq, String peptideSeq)
    {
        int[][] contactPositions = new int[38][2];
        contactPositions[0] = new int[]{0,4};
        contactPositions[1] = new int[]{1,4};
        contactPositions[2] = new int[]{2,3};
        contactPositions[3] = new int[]{2,4};
        contactPositions[4] = new int[]{3,2};
        contactPositions[5] = new int[]{3,3};
        contactPositions[6] = new int[]{3,4};
        contactPositions[7] = new int[]{4,1};
        contactPositions[8] = new int[]{4,2};
        contactPositions[9] = new int[]{4,3};
        contactPositions[10] = new int[]{4,4};
        contactPositions[11] = new int[]{5,0};
        contactPositions[12] = new int[]{5,1};
        contactPositions[13] = new int[]{5,2};
        contactPositions[14] = new int[]{5,3};
        contactPositions[15] = new int[]{5,4};
        contactPositions[16] = new int[]{6,0};
        contactPositions[17] = new int[]{6,1};
        contactPositions[18] = new int[]{6,2};
        contactPositions[19] = new int[]{7,0};
        contactPositions[20] = new int[]{7,1};
        contactPositions[21] = new int[]{8,1};
        contactPositions[22] = new int[]{9,1};
        contactPositions[23] = new int[]{9,3};
        contactPositions[24] = new int[]{10,2};
        contactPositions[25] = new int[]{10,3};
        contactPositions[26] = new int[]{10,4};
        contactPositions[27] = new int[]{11,3};
        contactPositions[28] = new int[]{11,4};
        contactPositions[29] = new int[]{12,0};
        contactPositions[30] = new int[]{12,1};
        contactPositions[31] = new int[]{12,2};
        contactPositions[32] = new int[]{13,0};
        contactPositions[33] = new int[]{14,0};
        contactPositions[34] = new int[]{14,2};
        contactPositions[35] = new int[]{14,3};
        contactPositions[36] = new int[]{14,4};
        contactPositions[37] = new int[]{15,4};

        int ix = 0;
        svm_node[] x = new svm_node[contactPositions.length];

        Vector<Double> vy = new Vector<Double>();
        Vector<svm_node[]> vx = new Vector<svm_node[]>();
        // GO through all positions and encode each residue
        for (int j=0; j < contactPositions.length;j++)
        {
            int[] posPair = contactPositions[j];
            int domainPos = posPair[0];
            int peptidePos = posPair[1];

            String domainRes = String.valueOf(domain16Seq.charAt(domainPos));
            String peptideRes = String.valueOf(peptideSeq.charAt(peptidePos));

            final String alphabet = "ACDEFGHIKLMNPQRSTVWY-";
            if (peptideRes.equals("X"))
            {
                peptideRes = domainRes;
            }
            if (domainRes.equals("X"))
            {
                domainRes = peptideRes;
            }
            int domainIx = alphabet.indexOf(domainRes);
            int peptideIx = alphabet.indexOf(peptideRes);
            // only mark first 20 AA, gaps and X's don't count
            int ixx = 1;
            x[ix] = new svm_node();
            int finalIx= 0;
            for (int ii = 0; ii < 21;ii++)
            {
                for (int jj = 0; jj < 21;jj++)
                {
                    if (ii==domainIx && jj == peptideIx)
                    {
                        int shift = j*400;
                        finalIx = shift + ixx;
                        break;
                    }
                    else
                    {


                        ixx = ixx + 1;
                    }
                }
            } // 400 values

            x[ix].value =1.0;
            x[ix].index =finalIx;

            ix = ix+1;

        } // 38 position pairs

        vx.addElement(x);
        vx.addElement(x);
        vy.addElement(1.0);
        svm_problem prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for(int i=0;i<prob.l;i++)
            prob.x[i] = vx.elementAt(i);
        prob.y = new double[prob.l];
        for(int i=0;i<prob.l;i++)
            prob.y[i] = vy.elementAt(i);
        return x;
    }

}
