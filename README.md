<head>
...
    <script type="text/javascript"
            src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
    </script>
...
</head>

# SelectionDiff

## Introduction

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
Here is an example MathJax inline rendering \\( 1/x^{2} \\), and here is a block rendering: 
\\[ \frac{1}{n^{2}} \\]

## Dependencies
- [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Commons Math 3.5](https://commons.apache.org/proper/commons-math/index.html)
- [JCommander 1.48](http://mvnrepository.com/artifact/com.beust/jcommander/1.48)
- [Algs4](http://algs4.cs.princeton.edu/home/)

## References
- [He *et al*, Genome Research, 2015](http://genome.cshlp.org/content/early/2015/10/13/gr.192336.115.abstract)

## License
- Copyright 2015, Xin Huang.
- `SelectionDiff.jar` is free software: You can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
- `SelectionDiff.jar` is distributed in the hope that it will be useful, but **WITHOUT ANY WARRANTY**; without even the implied warranty of **MERCHANTABILITY** or **FITNESS FOR A PARTICULAR PURPOSE**. See the GNU General Public License for more details.