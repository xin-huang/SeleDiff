# SelectionDiff

## Introduction
- `SelectionDiff.jar` is implemented with a probabilistic method for testing and estimating selection differences between populations<sup>1</sup>.
- If you have any problem, please feel free to contact huangxin@picb.ac.cn.

## Usage
- To use `SelectionDiff.jar`, you should have [Java SE Runtime Environment 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) first.
- Once you have Java SE Runtime Environment 8, then you can run `SelectionDiff.jar` in the command line.

		java -jar SelectionDiff.jar

	This will show help information to help you run `SelectionDiff.jar`. The help information is as follow.

		SelectionDiff - 1.0.0 2015/11/03 (Xin Huang)

		Usage: java -jar SelectionDiff.jar --geno <.geno file> --ind <.ind file> --snp <.snp file> --output <output file> --mode {o, d, s} [--omega <omega file>]

		Options:
		--geno		The .geno file contains genotype information, required
		--ind		The .ind file contains individual information, required
		--snp		The .snp file contains SNP information, required
		--output	The file stores results, required
		--mode		Select a analysis mode to perform, required
					o: estimate the variance of pairwise population drift Omega only
					d: estimate the delta statistics
					s: estimate the delta statistics with the given Omega
		--omega		The file stores the variance of pairwise population drift Omega
					which can be obtained by performing mode o analysis first
- Input files
- Output files

## Example
Here is an example to show how `SelectionDiff.jar` tests and estimates selection differences between populations. Four populations (YRI, CEU, CHB, CHD) from [HapMap3 (release3)](http://hapmap.ncbi.nlm.nih.gov/) were extracted. CHB and CHD were merged into one population called CHS. Correlated individuals and SNPs which major allele frequencies are less than 0.05 were removed.

For SNP rs12913832 of gene *HERC2*, there are two alleles. One is the ancestral allele A and the other is the derived allele G. The counts of alleles were summarized in below.

| Population | Allele A counts | Allele G counts |
| - | - | - |
| YRI | 294 | 0 |
| CEU | 47 | 177 |
| CHS | 491 | 1 |

## Dependencies
- [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Commons Math 3.5](https://commons.apache.org/proper/commons-math/index.html)
- [JCommander 1.48](http://mvnrepository.com/artifact/com.beust/jcommander/1.48)
- [Algs4](http://algs4.cs.princeton.edu/home/)

## References
1. [He *et al*, Genome Research, 2015](http://genome.cshlp.org/content/early/2015/10/13/gr.192336.115.abstract)

## License
- Copyright 2015, Xin Huang.
- `SelectionDiff.jar` is free software: You can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
- `SelectionDiff.jar` is distributed in the hope that it will be useful, but **WITHOUT ANY WARRANTY**; without even the implied warranty of **MERCHANTABILITY** or **FITNESS FOR A PARTICULAR PURPOSE**. See the GNU General Public License for more details.