# SeleDiff
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](LICENSE)

## Introduction
- `SeleDiff` is implemented with a probabilistic method for testing and estimating selection coefficient differences between populations<sup>1</sup>.
- If you have any problem, please feel free to contact huangxin@picb.ac.cn.
- If you use `SeleDiff`, please cite
- For more details, please see the manual.

## Installation
To install `SeleDiff`, you first clone the `SeleDiff` repository from GitHub.

    > git clone https://github.com/xin-huang/SeleDiff

Then you can enter the `SeleDiff` directory and use `gradlew` to install `SeleDiff`.

    > cd ./SeleDiff
    > ./gradlew build
    > ./gradlew install
    
The runnable `SeleDiff` is in `./build/install/SeleDiff/bin/`. You can add this directory into your system environment variable `PATH` by

    > export PATH="/path/to/SeleDiff/build/install/SeleDiff/bin/":$PATH
    
You can get help information by typing

    > SeleDiff
    
There are two sub-commands in `SeleDiff`. The first sub-command `var` is used for estimating variances of population demography parameter $\Omega$^1^, which are required for the second sub-command `scan`.

## An Example

Here is an example to show how `SeleDiff` estimates and tests selection differences between populations. 4 populations (YRI, CEU, CHB, CHD) from [HapMap3 (release3)](http://hapmap.ncbi.nlm.nih.gov/) were extracted. CHB and CHD were merged into one population called CHS. Correlated individuals and SNPs which major allele frequencies are less than 0.05 were removed by [PLINK 1.7](http://pngu.mgh.harvard.edu/~purcell/plink/download.shtml)(`--geno 0.01 --maf 0.05`). SNPs in strong linkage disequilibrium were removed, applying a window of 50 SNPs advanced by 5 SNPs and $r$^2^ threshold of 0.01 (`--indep-pairwise 50 5 0.01`) in PLINK. All the genetic data are stored in EIGENSTRAT format.

The SNP rs12913832 in gene *HERC2* is associated with blue/non-blue eyes^2^. The SNP rs1800407 in gene *OCA2* is also associated with blue/non-blue eyes^2^.

The counts of alleles in our example data were summarized in below.

| SNP ID | Population | Ancestral Allele Count | Derived Allele Count |
| ------ | --- | --- | --- |
| rs12913832 | YRI | 294 | 0   |
| rs12913832 | CEU | 47  | 177 |
| rs12913832 | CHS | 491 | 1   |
| rs1800407  | YRI | 290 | 0   |
| rs1800407  | CEU | 207 | 17  |
| rs1800407  | CHS | 486 | 4   |

We assume the divergence time of YRI-CEU and YRI-CHS are both 5000 generations, while the divergence time of CEU-CHS is 3000 generations. This information is stored in `examples/example.time`.

First, we estimate variances of population demography parameter using sub-command `var`.


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

From the result, we can see the selection coefficient of rs12913832 in CEU is significantly higher than that in YRI or CHS, which indicates rs12913832 is under positive selection in CEU. While the selection coefficient of rs1800407 in CEU is marginal significantly higher than that in YRI or CHS.

## Dependencies
- [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Commons Math 3.6](https://commons.apache.org/proper/commons-math/index.html)
- [JCommander 1.72](http://mvnrepository.com/artifact/com.beust/jcommander/1.72)
- [t-digest 3.1](https://github.com/tdunning/t-digest)

## References
1. [He et al, *Genome Research*, 2015](http://genome.cshlp.org/content/early/2015/10/13/gr.192336.115.abstract)
