# SeleDiff
[![License](http://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Language](http://img.shields.io/badge/language-java-brightgreen.svg)](https://www.java.com/)

## Introduction
- `SeleDiff` implements a probabilistic method for estimating and testing selection (coefficient) differences between populations<sup>1</sup>.
- If you have any problem, please feel free to contact xin.huang07@gmail.com, or open an [issue](https://github.com/xin-huang/SeleDiff/issues) in this repository.
- If you use `SeleDiff`, please cite 

        Huang X, Jin L, He Y. 2018. SeleDiff: A fast and scalable tool for estimating and testing 
        selection differences between populations. *In submission*.
- For more details, please see the manual.

## Installation
To install `SeleDiff`, you should first install [Java SE Development Kit 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and clone the `SeleDiff` repository from GitHub.

### Linux

In Linux, you can open the terminal and clone `SeleDiff` using `git`.

    > git clone https://github.com/xin-huang/SeleDiff

Then you can enter the `SeleDiff` directory and use `gradlew` to install `SeleDiff`.

    > cd ./SeleDiff
    > ./gradlew build
    > ./gradlew install
    
The runnable `SeleDiff` is in `./build/install/SeleDiff/bin/`. You can add this directory into your `PATH` environment variable by

    > export PATH="/path/to/SeleDiff/build/install/SeleDiff/bin/":$PATH
    
You can get help information by typing

    > SeleDiff
    
You can use `gradlew` to remove `SeleDiff`.

    > ./gradlew clean
    
### Windows

In Windows, you can download this repository directly using the green button `Clone or download` at the upright corner. Please make sure your environment variable `JAVA_HOME` correctly point to you JDK directory. After download and uncompression, you can open `cmd` and enter the directory of `SeleDiff` in `cmd`. Please use `gradlew.bat` to build and install `SeleDiff`.

    > cd /path/to/SeleDiff
    > gradlew.bat build
    > gradlew.bat install
        
 And run `SeleDiff.bat` in `./build/install/SeleDiff/bin/`
 
    > cd /build/install/SeleDiff/bin/
    > SeleDiff.bat
        
You can use `gradlew.bat` to remove `SeleDiff`.

    > cd /path/to/SeleDiff
    > gradlew.bat clean
    
There are two sub-commands in `SeleDiff`. The first sub-command `compute-var` is used for estimating variances of population demography parameter Omega<sup>1</sup>, which are required for the second sub-command `compute-diff`.

## Input Files

`SeleDiff` assumes biallelic genetic data and will not perform any checks on this assumption. All input files can be compressed by `gzip`.

### EIGENSTRAT

`SeleDiff` accepts [EIGENSTRAT](http://genepath.med.harvard.edu/~reich/InputFileFormats.htm) format of genetic data as inputs.

### VCF

`SeleDiff` also accepts [VCF](https://samtools.github.io/hts-specs/VCFv4.2.pdf) format of genetic data as inputs, and assumes genotypes of each individual are encoded with 0 and 1.

### Var File

The Var file is the output file from the first sub-command `compute-var`, which stores variances of pairwise population demography parameters. When using sub-command `compute-diff` to estimate selection differences, `SeleDiff` uses `--var` option to accept a a *SPACE* delimited file without header that specifies variances of population demography parameter between two populations.

        YRI CEU 1.547660
        YRI CHS 1.639591
        CEU CHS 0.989241

The first two columns are the population IDs, and the third column is the variances of population demography parameter of the two populations.

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

Here is an example to show how `SeleDiff` estimates and tests selection differences between populations. 4 populations (YRI, CEU, CHB, CHD) from [HapMap3 (release3)](http://hapmap.ncbi.nlm.nih.gov/) were extracted. CHB and CHD were merged into one population called CHS. [PLINK 1.7](http://pngu.mgh.harvard.edu/~purcell/plink/download.shtml) were used to remove correlated individuals and SNPs with minor allele frequences less than 0.05 and strong linkage disequilibrium. These genome-wide data are stored in `./examples/example.geno` and used for estimating variances of population demography parameters.

Two alternative alleles (rs1800407 and rs12913832) associated with blue eyes were identified in genes *HERC2* and *OCA2*<sup>2</sup>. These candidate data are stored in `./examples/example.candidates.geno` and used for estimating selection differences of these SNPs between populations.

The counts of alleles in our example data were summarized in below.

| SNP ID | Population | Reference Allele Count | Alternative Allele Count |
| ------ | --- | --- | --- |
| rs1800407  | YRI | 290 | 0   |
| rs1800407  | CEU | 207 | 17  |
| rs1800407  | CHS | 486 | 4   |
| rs12913832 | YRI | 294 | 0   |
| rs12913832 | CEU | 47  | 177 |
| rs12913832 | CHS | 491 | 1   |

We assume the divergence time of YRI-CEU and YRI-CHS are both 5000 generations, while the divergence time of CEU-CHS is 3000 generations. This information is stored in `./examples/example.time`.

First, we estimate variances of population demography parameters using sub-command `compute-var`.


    > SeleDiff compute-var --geno ./examples/example.geno \
                           --ind ./examples/example.ind \
                           --snp ./examples/example.snp \
                           --output ./examples/example.var


To estimate selection differences of candidates, we use the sub-command `compute-diff`.


    > SeleDiff compute-diff --geno ./examples/example.candidates.geno \
                            --ind ./examples/example.candidates.ind \
                            --snp ./examples/example.candidates.snp \
                            --var ./examples/example.var \
                            --time ./examples/example.time \
                            --output ./examples/example.candidates.results
        
The result is stored in `./examples/example.candidates.results`. The main result is in below.

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
1. [He et al., *Genome Res*, 2015](http://genome.cshlp.org/content/early/2015/10/13/gr.192336.115.abstract)
2. [Sturm et al., *Am J Hum Genet*, 2008](https://linkinghub.elsevier.com/retrieve/pii/S0002-9297(07)00040-7)
