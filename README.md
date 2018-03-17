# SeleDiff
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](LICENSE)

## Introduction
- `SeleDiff` is implemented with a probabilistic method for estimating and testing selection (coefficient) differences between populations<sup>1</sup>.
- If you have any problem, please feel free to contact xin.huang07@gmail.com.
- If you use `SeleDiff`, please cite 

        Huang X, Jin L, He Y. 2018. SeleDiff: A fast and scalable tool for estimating and testing 
        selection differences between populations. *In submission*.
- For more details, please see the manual.

## Installation
To install `SeleDiff`, you should first have [Java SE Runtime Environment 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) and clone the `SeleDiff` repository from GitHub.

    > git clone https://github.com/xin-huang/SeleDiff

Then you can enter the `SeleDiff` directory and use `gradlew` to install `SeleDiff`.

    > cd ./SeleDiff
    > ./gradlew build
    > ./gradlew install
    
The runnable `SeleDiff` is in `./build/install/SeleDiff/bin/`. You can add this directory into your system environment variable `PATH` by

    > export PATH="/path/to/SeleDiff/build/install/SeleDiff/bin/":$PATH
    
You can get help information by typing

    > SeleDiff
    
There are two sub-commands in `SeleDiff`. The first sub-command `var` is used for estimating variances of population demography parameter Omega<sup>1</sup>, which are required for the second sub-command `scan`.

## Input Files

### EIGENSTRAT

`SeleDiff` accepts [EIGENSTRAT](http://genepath.med.harvard.edu/~reich/InputFileFormats.htm) format of genetic data as inputs.

### Var File

The Var file is the output file from the first sub-command `var`, which stores variances of pairwise population demography parameters. When using sub-command `scan` to estimate selection differences, `SeleDiff` uses `--var` option to accept a a *SPACE* delimited file without header that specifies variances of population demography parameters between two populations.

        YRI CEU 1.547660
        YRI CHS 1.639591
        CEU CHS 0.989241

The first two columns are the population IDs, and the third column is the variance of population demography parameter of the two populations.

### Divergence Time File

When using sub-command `scan` to estimate selection differences, `SeleDiff` uses `--time` option to accept a *SPACE* delimited file without header that specifies divergence time between two populations.
    
        YRI CEU 5000
        YRI CHS 5000
        CEU CHS 3000
            
The first two columns are the population IDs, and the third column is the divergence time of the two populations.

## Output File

The output file from `SeleDiff` is *TAB* delimited. The first row is a header that describes the meaning of each column.

| Column | Column Name | Description |
| ------ | --------------------- | ----------------------------------- |
| 1 | SNP ID | The name of a SNP |
| 2 | Ref | The reference allele |
| 3 | Alt | The alternative allele |
| 4 | Population1 | The first population ID |
| 5 | Population2 | The second population ID |
| 6 | Selection difference | The selection difference between the first and second populations |
| 7 | Std | The standard deviation of the selection difference |
| 8 | Lower bound of 95% CI | Lower bound of 95% confidence interval of the selection coefficient difference |
| 9 | Upper bound of 95% CI | Upper bound of 95% confidence interval of the selection coefficient difference |
| 10 | Delta | The delta statistic for selection difference |
| 11 | *p*-value | The *p*-value of the delta statistic |

## An Example

Here is an example to show how `SeleDiff` estimates and tests selection differences between populations. 4 populations (YRI, CEU, CHB, CHD) from [HapMap3 (release3)](http://hapmap.ncbi.nlm.nih.gov/) were extracted. CHB and CHD were merged into one population called CHS. [PLINK 1.7](http://pngu.mgh.harvard.edu/~purcell/plink/download.shtml) were used to remove correlated individuals and SNPs with minor allele frequences less than 0.05. SNPs in strong linkage disequilibrium were removed, applying a window of 50 SNPs advanced by 5 SNPs and *r*<sup>2</sup> threshold of 0.01 (`--indep-pairwise 50 5 0.01`) in PLINK. All the genetic data are stored in EIGENSTRAT format.

The SNP rs12913832 in gene *HERC2* is associated with blue/non-blue eyes<sup>2</sup>. The SNP rs1800407 in gene *OCA2* is also associated with blue/non-blue eyes<sup>2</sup>.

The counts of alleles in our example data were summarized in below.

| SNP ID | Population | Ancestral Allele Count | Derived Allele Count |
| ------ | --- | --- | --- |
| rs1800407  | YRI | 290 | 0   |
| rs1800407  | CEU | 207 | 17  |
| rs1800407  | CHS | 486 | 4   |
| rs12913832 | YRI | 294 | 0   |
| rs12913832 | CEU | 47  | 177 |
| rs12913832 | CHS | 491 | 1   |

We assume the divergence time of YRI-CEU and YRI-CHS are both 5000 generations, while the divergence time of CEU-CHS is 3000 generations. This information is stored in `examples/example.time`.

First, we estimate variances of population demography parameters using sub-command `var`.


    > SeleDiff var --geno ./examples/example.geno \
                   --ind ./examples/example.ind \
                   --snp ./examples/example.snp \
                   --output ./examples/example.var


To estimate selection coefficient differences, we use the sub-command `scan`.


    > SeleDiff scan --geno ./examples/example.candidates.geno \
                    --ind ./examples/example.candidates.ind \
                    --snp ./examples/example.candidates.snp \
                    --var ./examples/example.var \
                    --time ./examples/example.time \
                    --output ./examples/example.candidates.results
        
The result is stored in `./examples/example.candidates.results`. The main result is in below.

| SNP ID | Population1 | Population2 | Selection difference | Std | delta | p-value |
| ------ | ------------ | ------------ | -------------- | --------- | --------- | -------- |
| rs1800407  | YRI  | CEU | -0.000773 | 0.000380 | 4.129 | 0.042154 |
| rs1800407  | YRI  | CHS | -0.000336 | 0.000393 | 0.731 | 0.392559 |
| rs1800407  | CEU  | CHS | 0.000728  | 0.000377 | 3.730 | 0.053443 |
| rs12913832 | YRI  | CEU | -0.001541 | 0.000378 | 16.583 | 0.000047 |
| rs12913832 | YRI  | CHS | -0.000117 | 0.000415 | 0.080  | 0.777297 |
| rs12913832 | CEU  | CHS | 0.002372  | 0.000433 | 30.062 | 0.000000 |

From the result, we can see the selection coefficient of rs12913832 in CEU is significantly higher than that in YRI or CHS, which indicates rs12913832 is under directional selection in CEU. While the selection coefficient of rs1800407 in CEU is marginal significantly higher than that in YRI or CHS.

## Dependencies
- [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Commons Math 3.6](https://commons.apache.org/proper/commons-math/index.html)
- [JCommander 1.72](http://mvnrepository.com/artifact/com.beust/jcommander/1.72)
- [t-digest 3.1](https://github.com/tdunning/t-digest)

## References
1. [He et al, *Genome Res*, 2015](http://genome.cshlp.org/content/early/2015/10/13/gr.192336.115.abstract)
2. [Sturm et al, *Am J Hum Genet*, 2008](https://linkinghub.elsevier.com/retrieve/pii/S0002-9297(07)00040-7)
