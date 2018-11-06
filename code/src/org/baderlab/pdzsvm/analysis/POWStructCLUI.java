package org.baderlab.pdzsvm.analysis;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import libsvm.svm_problem;
import libsvm.svm_node;
import libsvm.svm;
import libsvm.svm_model;

/**
 * Created by IntelliJ IDEA.
 * User: shirleyhui
 * Date: Apr 10, 2013
 * Time: 1:11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class POWStructCLUI
{
    public POWStructCLUI()
    {

    }

    public static void main(String[] args)
    {
        System.out.println("POWStructCLUI (POW Struct Command Line User Interface)");
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
            System.out.println("Usage: POWStructCLUI <organism: H,M,W,F> <domain name file> <peptide file>\nRequires exactly three arguments.");
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
                HashMap domainFeatMap = getDomainNameFeaturesMap(domainNameFile, organism);

                if (!domainFeatMap.isEmpty())
                {
                    List domainNameList = new ArrayList(domainFeatMap.keySet());
                    Collections.sort(domainNameList);
                    System.out.println("\t"+domainNameList.size() + " domain(s) read...");

                    System.out.println("Peptides:");
                    String peptideFileName = args[2];
                    HashMap peptideFeatMap =  getPeptideSeqFeaturesMap(peptideFileName);
                    if (!peptideFeatMap.isEmpty())
                    {
                        List peptideSeqList = new ArrayList(peptideFeatMap.keySet());
                        System.out.println("\t"+peptideSeqList.size() + " peptide(s) read...");

                        POWStructCLUI t = new POWStructCLUI();
                        t.clui(domainFeatMap, peptideFeatMap);

                    }
                }
            }
        }
    }

    private static List getPeptideFeatures(String peptideSeq)
    {

        List peptideFeatures = new ArrayList();
        String alphabet = "ACDEFGHIKLMNPQRSTVWYX";
        for (int ii =0;ii < peptideSeq.length();ii++)
        {
            String res = String.valueOf(peptideSeq.charAt(ii));
            List encodeResFeatures = new ArrayList();
            for (int i = 0; i < 20;i++)
            {
                encodeResFeatures.add(0.0);
            }

            int value = alphabet.indexOf(res);
            if (value <0)
            {
                System.out.println("\tError: Invalid residue in peptide " + peptideSeq +", skipping...");
                return new ArrayList();
            }
            else
            {
                encodeResFeatures.set(value,1.0);
                peptideFeatures.addAll(encodeResFeatures);
            }
        }
        return peptideFeatures;
    }
    private static HashMap getPeptideSeqFeaturesMap(String peptideSeqFileName)
    {
        HashMap peptideSeqFeatureMap = new HashMap();
        List peptideSeqList = new ArrayList();
        try
        {

            BufferedReader br = new BufferedReader(new FileReader(new File(peptideSeqFileName)));
            String peptideSeq = "";

            while((peptideSeq=br.readLine())!=null)
            {
                if (peptideSeq.length()<5)
                {
                    System.out.println("\tError: Peptide "+ peptideSeq +" too short, skipping...");
                }
                else
                {
                    if (peptideSeq.length() > 5)
                    {
                        System.out.println("\tWarning: Peptide "+peptideSeq + " too long, truncating...");
                        peptideSeq = peptideSeq.substring(peptideSeq.length()-5,peptideSeq.length());
                    }
                    if (peptideSeqFeatureMap.get(peptideSeq) == null)
                    {
                        List peptideFeatures = getPeptideFeatures(peptideSeq);
                        if (!peptideFeatures.isEmpty())
                        {
                            peptideSeqFeatureMap.put(peptideSeq, peptideFeatures);
                        }
                    }
                    else
                    {
                        System.out.println("\tWarning: Peptide "+peptideSeq + " already in list, skipping...");
                    }
                }
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println("\tError: "+ e);
        }
        return peptideSeqFeatureMap;
    }
    private static HashMap getDomainNameFeaturesMap(String domainNamefileName, String organism)
    {
        HashMap domainNameFeatMap = new HashMap();
        HashMap allDomainNameFeatMap = new HashMap();
        try
        {
            String fileName = "";
            if (organism.equals("H"))
            {
                fileName = "features/HumanPDZDomainStructureFeatures.txt";
            }
            else if (organism.equals("M"))
            {
                fileName = "features/MousePDZDomainStructureFeatures.txt";
            }
            else if (organism.equals("W"))
            {
                fileName = "features/WormPDZDomainStructureFeatures.txt";
            }
            else if (organism.equals("F"))
            {
                fileName = "features/FlyPDZDomainStructureFeatures.txt";
            }
            else
            {
                System.out.println("\tError: Organism "+ organism +" not supported...");
                return new HashMap();
            }

            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String line = "";

            while((line=br.readLine())!=null)
            {
                String[] splitLine = line.split("\t");
                String domainName = splitLine[0];
                List domainFeatures = new ArrayList();
                for (int i = 1; i< splitLine.length;i++)
                {
                    double value = Double.parseDouble(splitLine[i]);
                    domainFeatures.add(value);
                }
                allDomainNameFeatMap.put(domainName, domainFeatures);
                //System.out.println(domainName +"\t" + domainFeatures.size());

            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println("\tError: " + e);
        }

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(new File(domainNamefileName)));
            String domainName = "";

            while((domainName=br.readLine())!=null)
            {
                List domainFeatures = (List)allDomainNameFeatMap.get(domainName);
                if (domainFeatures == null)
                {
                    System.out.println("\tError: Domain "+ domainName +" not supported, skipping...");
                }
                else
                {
                    domainNameFeatMap.put(domainName, domainFeatures);
                }
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println("\tError: " + e);
        }

        return domainNameFeatMap;
    }


    private void clui(HashMap domainFeatMap, HashMap peptideFeatMap)
    {
        System.out.println("Results: ");
        System.out.println("DomainName\tPeptideSequence\tDecisionScore");

        List domainNameList = new ArrayList(domainFeatMap.keySet());
        List peptideSeqList = new ArrayList(peptideFeatMap.keySet());
        svm_problem prob = new svm_problem();

        Vector<Double> vy = new Vector<Double>();
        Vector<svm_node[]> vx = new Vector<svm_node[]>();
        int max_index = 0;
        for (int i = 0;i < domainNameList.size();i++)
        {
            String domainName = (String)domainNameList.get(i);
            List dFeatList = (List)domainFeatMap.get(domainName);

            for (int jj = 0 ; jj < peptideSeqList.size();jj++)
            {
                String peptideSeq = (String)peptideSeqList.get(jj);
                List pFeatList = (List)peptideFeatMap.get(peptideSeq);

                vy.addElement(0.0);     // Arbitrarily set class value

                int m = 240+100;
                svm_node[] x = new svm_node[m];
                int j = 0;

                for (int ii=0; ii < dFeatList.size();ii++)
                {
                    x[j] = new svm_node();
                    x[j].index = (j+1);
                    double theValue = (Double)dFeatList.get(ii);
                    x[j].value = theValue;
                    j = j+1;
                }
                for (int ii=0; ii < pFeatList.size();ii++)
                {
                    x[j] = new svm_node();
                    x[j].index = (j+1);
                    double theValue = (Double)pFeatList.get(ii);

                    x[j].value = theValue;
                    j = j+1;
                }
                if(m>0) max_index = Math.max(max_index, x[m-1].index);
                vx.addElement(x);
            }
        }

        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for(int i=0;i<prob.l;i++)
            prob.x[i] = vx.elementAt(i);
        prob.y = new double[prob.l];
        for(int i=0;i<prob.l;i++)
            prob.y[i] = vy.elementAt(i);


        prob.names = new String[prob.l];
        prob.peptideSeq = new String[prob.l];
        int ix = 0;
        for (int i = 0;i < domainNameList.size();i++)
        {
            String domainName = (String)domainNameList.get(i);

            for (int jj = 0 ; jj < peptideSeqList.size();jj++)
            {
                String peptideSeq = (String)peptideSeqList.get(jj);
                prob.names[ix] = domainName;
                prob.peptideSeq[ix]= peptideSeq;
                ix = ix+1;
            }
        }
        try
        {
            svm_model svmModel = svm.svm_load_model("models/PDZSVM_STRUCT_Sep62011.model");
            for (int i = 0; i < prob.l;i++)
            {
                svm_node[] node = prob.x[i];
                double[] ret = svm.svm_predict_x(svmModel,node);
                double decValue = ret[0];
                System.out.println(prob.names[i] +"\t" + prob.peptideSeq[i] + "\t" + decValue);

            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " +e);
        }

    }
}
