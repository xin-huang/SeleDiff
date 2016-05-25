# SeleDiff

## Introduction
- `SeleDiff.jar` is implemented with a probabilistic method for testing and estimating selection differences between populations<sup>1</sup>.
- If you have any problem, please feel free to contact huangxin@picb.ac.cn.
- For more details, please see the manual.

## Usages
- To use `SeleDiff.jar`, you should install [Java SE Runtime Environment 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) first.
- Once you have Java SE Runtime Environment 8, then you can run `SeleDiff.jar` without any parameter in the command line to look at help information.

		java -jar SeleDiff.jar

    The help information is as follow.

		Usage: SeleDiff [options]
         Options:
          --admixed-population
            A file specifies admixed population.
          --all-gen
            A Oxford GEN file contains all the sample SNPs' information and genotype
            data.
          --all-gen-threshold
            A threshold specifes the confidence of genotype in all the sample data,
            if Oxford GEN/SAMPLE format is used.
            Default: 0.9
          --all-geno
            A EIGENSTRAT GENO file contains all the sample genotype data.
          --all-haps
            A HAPS file contains all the sample SNPs' information and genotype data.
          --all-ind
            A EIGENSTRAT IND file contains all the sample individuals' information.
          --all-sample
            A Oxford SAMPLE file contains all the sample individuals' information.
          --all-snp
            A EIGENSTRAT SNP file contains all the sample SNPs' information.
        * --ancestral-allele
            A file specifies ancestral alleles.
          --candidate-gen
            A Oxford GEN file contains the candidate SNPs' information and genotype
            data.
          --candidate-gen-threshold
            A threshold specifies the confidence of genotypes in the candidate data,
            if Oxford GEN/SAMPLE format is used.
            Default: 0.9
          --candidate-geno
            A EIGENSTRAT GENO file contains the candidate genotype data.
          --candidate-haps
            A HAPS file contains the candidate SNPs' information and genotype data.
          --candidate-ind
            A EIGENSTRAT IND file contains the candidate individuals' information.
          --candidate-sample
            A Oxford SAMPLE file contains the candidate individuals' information.
          --candidate-snp
            A EIGENSTRAT SNP file contains the candidate SNPs' information.
        * --divergence-time
            A file specifies divergence time.
          --haplotype
            A file specifies haplotypes.
          --help
            Show SeleDiff's usage.
            Default: false
          --log
            Redirect log into a file.
        * --output
            The output file.
        
    \* indicates required options.

- Input files
    - `SeleDiff` accepts 3 kinds of file formats of genetic data as inputs. They are [EIGENSTRAT](http://genepath.med.harvard.edu/~reich/InputFileFormats.htm) format, [Oxford GEN/SAMPLE](http://www.stats.ox.ac.uk/~marchini/software/gwas/file_format.html)
format and [HAPS/SAMPLE](https://mathgen.stats.ox.ac.uk/genetics_software/shapeit/shapeit.html#formats) format.
    - `SeleDiff` uses `--ancestral-allele` option (required) to accept a *TAB* delimited file that specifies the ancestral allele of each SNP in the data. The content of the file looks like:
    
            rsID    Ancestral Allele
            rs001   A
            rs002   G
            ...
            
        The first line is a header which will be skipped by `SeleDiff`.
            
    - `SeleDiff` uses `--divergence-time` option (required) to accept a *TAB* delimited file that specifies divergence time between two populations.
    
            Population1 Population2 Time(generations)
            EastAfrica  WesAfrica   2000
            EastAfrica  EastSouthasia(EastAsia) 3600
            ...
            
        The first line is a header which will be skipped by `SeleDiff`. Here, EastSouthasia is a admixed population. We estimate and use its missing parental population in East Asia instead.
            
    - `SeleDiff` uses `--haplotype` option to accept a *TAB* delimited file that specifies haplotypes in the data. The content of the file looks like:
    
            rs001   rs002
            rs001   rs003   rs004
            
        Each row represents a haplotype.
        
- Output file
    - The output file from `SeleDiff` is *TAB* delimited. The first row is a header that describes the meaning of
each column.

        | Column | Column Name | Description |
        | ------ | ----------- | ----------- |
        | 1 | SNP ID/Haplotype ID | The name of a SNP/haplotype |
        | 2 | Ancestral Allele | The ancestral allele of a SNP/haplotype |
        | 3 | Derived Allele | The derived allele of a SNP/haplotype |
        | 4 | Population1 | The first population's ID |
        | 5 | Ancestral Allele Count | The count of the ancestral allele in the first population |
        | 6 | Derived Allele Count | The count of the derived allele in the first population |
        | 7 | Population2 | The second population's ID |
        | 8 | Ancestral Allele Count | The count of the ancestral allele in the second population |
        | 9 | Derived Allele Count | The count of the derived allele in the second population | 
        | 10 | Selection Difference (Population1 - Population2) | The selection difference between the first and second populations |
        | 11 | Std(Selection Difference) | The standard deviation of the selection difference |
        | 12 | Divergence Time | The divergence time between the first and second populations |
        | 13 | log(Odds Ratio) | The logarithm of Odds Ratio |
        | 14 | Var(log(Odds Ratio)) | The variance of the logarithm of Odds Ratio |
        | 15 | Population Variance | The drift strength $\hat{\text{Var}}(\Omega)$ between the first and second populations without dividing the square of divergence time |
        | 16 | Delta | The $\delta$ statistic for selection difference |
        | 17 | p-value | The p-value of the $\delta$ statistic |
        
    Note: For a admixed population, the allele counts of its missing parental populations are estimated by their estimated allele frequecies multiply by 1000 (See Introduction section in the manual for estimating allele frequencies in missing parental populations).


## Examples
Here is an example to show how `SeleDiff.jar` tests and estimates selection differences between populations. Four populations (YRI, CEU, CHB, CHD) from [HapMap3 (release3)](http://hapmap.ncbi.nlm.nih.gov/) were extracted. CHB and CHD were merged into one population called CHS. Correlated individuals and SNPs which major allele frequencies are less than 0.05 were removed.

### Estimate Selection Differences in SNPs

The SNP rs12913832 in gene *HERC2* was associated with blue/non-blue eyes. It has two alleles. One is the ancestral allele A and the other is the derived allele G. The counts of alleles in our example data were summarized in below.

| Population | Allele A counts | Allele G counts |
| --- | --- | --- |
| YRI | 294 | 0   |
| CEU | 47  | 177 |
| CHS | 491 | 1   |

### Estimate Selection Differences in Haplotypes

Using `SeleDiff.jar`, we can obtain the result as follow.

| SNP Id | Population 1 | Population 2 | log(Odds Ratio) | Var(log(Odds Ratio)) | Var(Omega) | delta | p-value |
| ------ | ------------ | ------------ | --------------- | -------------------- | ---------- | ----- | ------- |
| rs12913832 | CHS      | YRI          | -0.585748       | 2.672105         | 3.852386   | 0.052587 | 0.818622 |
| rs12913832 | CHS      | CEU          | 7.116981        | 0.69563          | 1.875915   | 19.696884 | 9.0E-6  |
| rs12913832 | YRI      | CEU          | 7.072729        | 2.030328         | 2.935674   | 11.947648 | 5.47E-4 |

## Dependencies
- [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Commons Math 3.5](https://commons.apache.org/proper/commons-math/index.html)
- [JCommander 1.48](http://mvnrepository.com/artifact/com.beust/jcommander/1.48)


## References
1. [He *et al*, Genome Research, 2015](http://genome.cshlp.org/content/early/2015/10/13/gr.192336.115.abstract)
