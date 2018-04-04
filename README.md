# SeleDiff

[![license](https://img.shields.io/badge/license-Apache%202.0-red.svg)](LICENSE)
[![language](http://img.shields.io/badge/language-java-orange.svg)](https://www.java.com/)
[![codecov](https://img.shields.io/codecov/c/github/xin-huang/SeleDiff/master.svg)](https://codecov.io/gh/xin-huang/SeleDiff)
[![build Status](https://travis-ci.org/xin-huang/SeleDiff.svg?branch=master)](https://travis-ci.org/xin-huang/SeleDiff)
[![manual](https://img.shields.io/badge/manual-v1.0-00838F.svg)](https://github.com/xin-huang/SeleDiff/blob/master/docs/SeleDiff_Manual_v1.0.pdf)
[![release](https://img.shields.io/github/release/xin-huang/SeleDiff.svg)](https://github.com/xin-huang/SeleDiff/releases)
![doi](https://img.shields.io/badge/doi-in%20submission-BA55D3.svg)



## Introduction
- `SeleDiff` implements a probabilistic method for estimating and testing selection (coefficient) differences between populations<sup>1</sup>.
- If you have any problem, please feel free to contact xin.huang07@gmail.com, or open an [issue](https://github.com/xin-huang/SeleDiff/issues) in this repository.
- If you use `SeleDiff`, please cite 

        Huang X, Jin L, He Y. 2018. SeleDiff: A fast and scalable tool for estimating and testing 
        selection differences between populations. *In submission*.
- For more details, please see the manual in `./docs`.

## Installation
To install `SeleDiff`, you should first install [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 
or [OpenJDK8](http://openjdk.java.net/install/).

### Linux/Mac

In Linux/Mac, you can open the terminal and clone `SeleDiff` using `git`:

    > git clone https://github.com/xin-huang/SeleDiff

Then you can enter the `SeleDiff` directory and use `gradlew` to install `SeleDiff`:

    > cd ./SeleDiff
    > ./gradlew build
    > ./gradlew install
    
The runnable `SeleDiff` is in `./build/install/SeleDiff/bin/`. You can add this directory into your `PATH` environment variable by:

    > export PATH="/path/to/SeleDiff/build/install/SeleDiff/bin/":$PATH
    
You can get help information by typing:

    > SeleDiff
    
You can use `gradlew` to remove `SeleDiff`:

    > ./gradlew clean
    
### Windows

In Windows, you can download the [latest release](https://github.com/xin-huang/SeleDiff/releases). Please make sure your environment variable `JAVA_HOME` correctly point to you JDK directory. After download and uncompression, you can open `cmd` and enter the directory of `SeleDiff` in `cmd`. Please use `gradlew.bat` to build and install `SeleDiff`.

    > cd /path/to/SeleDiff
    > gradlew.bat build
    > gradlew.bat install
        
 And run `SeleDiff.bat` in `./build/install/SeleDiff/bin/`:
 
    > cd /build/install/SeleDiff/bin/
    > SeleDiff.bat
        
You can use `gradlew.bat` to remove `SeleDiff`:

    > cd /path/to/SeleDiff
    > gradlew.bat clean
    
## Commands

`SeleDiff` contains two sub-commands:

- `compute-var` for estimating variances of Ω<sup>1</sup>, which is required for the `compute-diff` command;
- `compute-diff` for estimating selection differences among loci.

## Input Files

`SeleDiff` assumes bi-allelic genetic data and will not perform any checks on this assumption. All input files can be compressed by `gzip`.

### EIGENSTRAT

`SeleDiff` accepts [EIGENSTRAT](https://reich.hms.harvard.edu/software/InputFileFormats) format of genetic data as inputs. [EIGENSOFT](https://github.com/DReichLab/EIG) provides several functions to convert other formats to EIGENSTRAT format.

### VCF

`SeleDiff` also accepts [VCF](https://samtools.github.io/hts-specs/VCFv4.2.pdf) format of genetic data as inputs, and assumes genotypes of each individual are encoded with 0 and 1. Because VCF format contains no population information of each individual, users should provide an additional file following EIGENSTRAT IND format.

### Var File

The Var file is the output file from the first sub-command `compute-var`, which stores variances of pairwise Ω. When using sub-command `compute-diff` to estimate selection differences, `SeleDiff` uses `--var` option to accept a a *SPACE* delimited file without header that specifies variances of Ω between populations.

        YRI CEU 1.547660
        YRI CHS 1.639591
        CEU CHS 0.989241

The first two columns are the population IDs, and the third column is the variances of Ω between populations.

### Divergence Time File

When using sub-command `compute-diff` to estimate selection differences, `SeleDiff` uses `--time` option to accept a *SPACE* delimited file without header that specifies divergence times between two populations.
    
        YRI CEU 5000
        YRI CHS 5000
        CEU CHS 3000
            
The first two columns are the population IDs, and the third column is the divergence times of the two populations.

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
| 8 | Lower bound of 95% CI | Lower bound of 95% confidence interval of the selection difference |
| 9 | Upper bound of 95% CI | Upper bound of 95% confidence interval of the selection difference |
| 10 | Delta | The delta statistic for selection difference |
| 11 | *p*-value | The *p*-value of the delta statistic |

## An Example

Here is an example to show how `SeleDiff` estimates and tests selection differences between populations. Four populations (YRI, CEU, CHB, CHD) from [HapMap3 (release3)](http://hapmap.ncbi.nlm.nih.gov/) were extracted. CHB and CHD were merged into one population called CHS. [PLINK 1.7](http://pngu.mgh.harvard.edu/~purcell/plink/download.shtml) were used to remove correlated individuals and SNPs with minor allele frequences less than 0.05 and strong linkage disequilibrium. These genome-wide data are stored in `./examples/data/example.geno` and used for estimating variances of Ω.

Two alternative alleles (rs1800407 and rs12913832) associated with blue eyes were identified in genes *HERC2* and *OCA2*<sup>2</sup>. These candidate data are stored in `./examples/data/example.candidates.geno` and used for estimating selection differences of these SNPs between populations.

The counts of alleles in our example data were summarized in below.

| SNP ID | Population | Reference Allele Count | Alternative Allele Count |
| ------ | --- | --- | --- |
| rs1800407  | YRI | 290 | 0   |
| rs1800407  | CEU | 207 | 17  |
| rs1800407  | CHS | 486 | 4   |
| rs12913832 | YRI | 294 | 0   |
| rs12913832 | CEU | 47  | 177 |
| rs12913832 | CHS | 491 | 1   |

We assume the divergence time of YRI-CEU and YRI-CHS are both 5000 generations, while the divergence time of CEU-CHS is 3000 generations. This information is stored in `./examples/data/example.time`.

First, we estimate variances of Ω using sub-command `compute-var`:


    > SeleDiff compute-var --geno ./examples/data/example.geno \
                           --ind ./examples/data/example.ind \
                           --snp ./examples/data/example.snp \
                           --output ./examples/results/example.geno.var


To estimate selection differences of candidates, we use the sub-command `compute-diff`:


    > SeleDiff compute-diff --geno ./examples/data/example.candidates.geno \
                            --ind ./examples/data/example.candidates.ind \
                            --snp ./examples/data/example.candidates.snp \
                            --var ./examples/results/example.geno.var \
                            --time ./examples/data/example.time \
                            --output ./examples/results/example.candidates.geno.results
        
The result is stored in `./examples/example.candidates.geno.results`. The main result is in below.

| SNP ID | Population1 | Population2 | Selection difference | Std | delta | *p*-value |
| ------ | ------------ | ------------ | -------------- | --------- | --------- | -------- |
| rs1800407  | YRI  | CEU | -0.000773 | 0.000380 | 4.129 | 0.042154 |
| rs1800407  | YRI  | CHS | -0.000336 | 0.000393 | 0.731 | 0.392559 |
| rs1800407  | CEU  | CHS | 0.000728  | 0.000377 | 3.730 | 0.053443 |
| rs12913832 | YRI  | CEU | -0.001541 | 0.000378 | 16.583 | 0.000047 |
| rs12913832 | YRI  | CHS | -0.000117 | 0.000415 | 0.080  | 0.777297 |
| rs12913832 | CEU  | CHS | 0.002372  | 0.000433 | 30.062 | 0.000000 |

From the result, we can see the selection coefficient of rs12913832 in CEU is significantly larger than that in YRI or CHS, which indicates rs12913832 is under directional selection in CEU. While the selection coefficient of rs1800407 in CEU is marginal significantly larger than that in YRI or CHS.

## Dependencies
- [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Commons Math 3.6](https://commons.apache.org/proper/commons-math/index.html)
- [JCommander 1.72](http://mvnrepository.com/artifact/com.beust/jcommander/1.72)
- [t-digest 3.1](https://github.com/tdunning/t-digest)

## References
1. [He et al., *Genome Res*, 2015.](http://genome.cshlp.org/content/early/2015/10/13/gr.192336.115.abstract)
2. [Sturm et al., *Am J Hum Genet*, 2008.](https://linkinghub.elsevier.com/retrieve/pii/S0002-9297(07)00040-7)
