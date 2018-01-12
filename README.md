# SeleDiff
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](LICENSE)

## Introduction
- `SeleDiff` is implemented with a probabilistic method for testing and estimating selection coefficient differences between populations<sup>1</sup>.
- If you have any problem, please feel free to contact huangxin@picb.ac.cn.
- If you use `SeleDiff`, please cite
- For more details, please see the manual.

## Installation
<<<<<<< HEAD
`git clone https://github.com/xin-huang/SeleDiff`
`./gradlew build`
`./gradlew install`
=======
>>>>>>> origin/master

## Usages
- To use `SeleDiff`, you should install [Java SE Runtime Environment 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) first.
- Once you have Java SE Runtime Environment 8, then you can run `SeleDiff` without any parameter or with `--help` option in the command line to look at help information.

		java -jar SeleDiff.jar

    The help information is as follow.

    `*` indicates required options.

- Input files
    - `SeleDiff` assumes no missing data in the input files.
    - `SeleDiff` uses `--ancestral-allele` option (required) to accept a *TAB* delimited file that specifies the ancestral allele of each SNP in the data. The content of the file looks like:
    
            rs001   A
            rs002   G
            ...
            
        The first line is a header which will be skipped by `SeleDiff`.
            
    - `SeleDiff` uses `--divergence-time` option (required) to accept a *TAB* delimited file that specifies divergence time between two populations. The content of the file looks like:
    
            EastAfricans  WesAfricans   2000
            EastAsians	Europeans   3600
            ...
        
- Output file
    - The output file from `SeleDiff` is *TAB* delimited. The first row is a header that describes the meaning of
each column.

        | Column | Column Name | Description |
        | ------ | ----------- | ----------- |
        | 1 | SNP ID/Haplotype ID | The name of a SNP/haplotype |
        | 2 | Ancestral Allele | The ancestral allele of a SNP/haplotype |
        | 3 | Derived Allele | The derived allele of a SNP/haplotype |
        | 4 | Population1 | The first population's ID |
        | 5 | Population2 | The second population's ID |
        | 6 | Selection Coefficient Difference (Population1 - Population2) | The selection coefficient difference between the first and second populations |
        | 7 | Std | The standard deviation of the selection coefficient difference |
        | 8 | 95% Confidence Interval | 95% confidence interval of the selection coefficient difference |
        | 9 | Divergence Time | The divergence time between the first and second populations |
        | 10 | log(Odds Ratio) | The logarithm of Odds Ratio |
        | 11 | Var(log(Odds Ratio)) | The variance of the logarithm of Odds Ratio |
        | 12 | Population Variance | The drift strength $\hat{\text{Var}}(\Omega)$ between the first and second populations without dividing the square of divergence time |
        | 13 | Delta | The $\delta$ statistic for selection difference |
        | 14 | p-value | The p-value of the $\delta$ statistic |
        

## Examples
Here is an example to show how `SeleDiff` tests and estimates selection coefficient differences between populations. 5 populations (YRI, CEU, CHB, CHD, ASW) from [HapMap3 (release3)](http://hapmap.ncbi.nlm.nih.gov/) were extracted. CHB and CHD were merged into one population called CHS. Correlated individuals and SNPs which major allele frequencies are less than 0.05 were removed by [PLINK 1.07](http://pngu.mgh.harvard.edu/~purcell/plink/download.shtml)(`--geno 0.01 --maf 0.05`). SNPs in strong linkage disequilibrium were removed, applying a window of 50 SNPs advanced by 5 SNPs and r<sup>2</sup> threshold of 0.01 (`--indep-pairwise 50 5 0.01`) in PLINK. All the genetic data are stored in EIGENSTRAT format.

## Performance

### Estimate Selection Coefficient Differences in SNPs

The SNP rs12913832 in gene *HERC2* is associated with blue/non-blue eyes. Its ancestral allele is A and its derived allele is G. The SNP rs1800407 in gene *OCA2* is also associated with blue/non-blue eyes. Its ancestral allele is C and its derived allele is T. The ancestral allele information is stored in `examples/ancestral_alleles.tsv`.

The counts of alleles in our example data were summarized in below.

| SNP ID | Population | Ancestral Allele Count | Derived Allele Count |
| ------ | --- | --- | --- |
| rs12913832 | YRI | 294 | 0   |
| rs12913832 | CEU | 47  | 177 |
| rs12913832 | CHS | 491 | 1   |
| rs1800407  | YRI | 290 | 0   |
| rs1800407  | CEU | 207 | 17  |
| rs1800407  | CHS | 486 | 4   |

We assume the divergence time of YRI-CEU and YRI-CHS are both 3600 generations, while the divergence time of CEU-CHS is 2000 generations. This information is stored in `examples/divergence_times.tsv`.

To estimate selection coefficient differences, in the command line, we type

        java -jar SeleDiff.jar --all-geno example.geno --all-ind example.ind --all-snp example.snp --candidate-geno example.candidate.geno --candidate-ind example.candidate.ind --candidate-snp example.candidate.snp --ancestral-allele ancestral_alleles.tsv --divergence-time divergence_times.tsv --output example.result.tsv
        
The result is stored in `examples/example.result.tsv`. The main result is in below.

| SNP ID | Population1 | Population2 | Selection Coefficient Difference (Population1 - Population2) | Std | 95% Confidence Interval | delta | p-value |
| ------ | ------------ | ------------ | -------------- | --------- | ------------------- | --------- | -------- |
| rs12913832 | YRI      | CEU          | -0.00214       | 3.96E-4   | [-0.002915,-0.001364] | 16.57374  | 4.7E-5   |
| rs12913832 | YRI      | CHS          | -1.63E-4       | 4.54E-4   | [-0.001053,7.27E-4] | 0.079586  | 0.777859 |
| rs12913832 | CEU      | CHS          | 0.003558       | 4.17E-4   | [0.002741,0.004376] | 30.083597 | 0.0      |
| rs1800407  | YRI      | CEU          | -0.001073      | 3.99E-4   | [-0.001856,-2.9E-4] | 4.127239  | 0.042198 |
| rs1800407  | YRI      | CHS          | -4.67E-4       | 4.15E-4   | [-0.00128,3.46E-4] | 0.730705  | 0.392655 |
| rs1800407  | CEU      | CHS          | 0.001091       | 2.68E-4   | [5.65E-4,0.001617] | 3.733448  | 0.053333 |

From the result, we can see the selection coefficient coefficient of rs12913832 in CEU is significantly higher than that in YRI or CHS, which indicates rs12913832 is under positive selection in CEU. While the selection coefficient of rs1800407 in CEU is marginal significantly higher than that in YRI or CHS.

When estimating selection coefficient differences in admixed populations, we have to correct for its admixed proportions from parental populations. In our example, ASW is an admixed population. We assume its parental populations are YRI and CEU. This information is stored in `examples/admixed_populations.tsv`.

To estimate selection coefficient differences, we have to use `--admixed-population` to specify the information of admixed populations. In the command line, we type

        java -jar SeleDiff.jar --all-geno example.geno --all-ind example.ind --all-snp example.snp --candidate-geno example.candidate.geno --candidate-ind example.candidate.ind --candidate-snp example.candidate.snp --ancestral-allele ancestral_alleles.tsv --divergence-time admixed_divergence_times.tsv --admixed-population admixed_populations.tsv --output example.admixed.result.tsv
        
From the log information generated by `SeleDiff`, we can see the admixed proportion of ASW from YRI is approximately equal to  0.8. The result is stored in `examples/example.admixed.result.tsv`.

### Estimate Selection Coefficient Differences in Haplotypes

It was reported that the derived allele of rs1800407 increased the penetrance of the blue eye phenotype associated with the derived allele of rs12913832. We used [IMPUTE2](http://mathgen.stats.ox.ac.uk/impute/impute_v2.html) to phase chromosome 15 (`-phase -int 24e6 27e6 -Ne 10000 -m genetic_map_chr15_combined_b36.txt`) in our example data and stored the phased data in `examples/example.candidate.chr15.phased.haps` and `examples/example.candidate.chr15.phased.sample`. The genetic map was downloaded from [IMPUTE2](https://mathgen.stats.ox.ac.uk/impute/data_download_hapmap3_r2.html). We specified the haplotype by the haplotype list file, which is stored in `examples/haplotype.list`.

To estimate selection coefficient differences, we have to store candidate data in HAPS/SAMPLE format and use `--haplotype` to specify the information of haplotypes. In the command line, we type

        java -jar SeleDiff.jar --all-geno example.geno --all-ind example.ind --all-snp example.snp --candidate-haps example.candidate.chr15.phased.haps --candidate-sample example.candidate.chr15.phased.sample --ancestral-allele ancestral_alleles.tsv --divergence-time divergence_times.tsv --haplotype haplotype.list --output example.hap.result.tsv
        
The result is stored in `examples/example.hap.result.tsv`. As the result indicates, the derived haplotypes have signature of positive selection in CEU population.

## Dependencies
- [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Commons Math 3.6](https://commons.apache.org/proper/commons-math/index.html)
- [JCommander 1.72](http://mvnrepository.com/artifact/com.beust/jcommander/1.72)
- [t-digest 3.1](https://github.com/tdunning/t-digest)

## References
1. [He et al, *Genome Research*, 2015](http://genome.cshlp.org/content/early/2015/10/13/gr.192336.115.abstract)
