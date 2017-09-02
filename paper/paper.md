---
title: 'SeleDiff'
tags:
    - population genetics/genomics
    - natural selection
authors:
    - name: Xin Huang
      orcid:
      affiliation: 1, 2
    - name: Li Jin
      affiliation: 1, 3
    - name: Yungang He
      orcid:
      affiliation: 1
affiliations:
    - name: Chinese Academy of Sciences Key Laboratory of Computational Biology, Chinese Academy of Sciences-Max Planck Society           Partner Institute for Computational Biology, Shanghai Institutes for Biological Sciences, Shanghai, 200031, China
      index: 1
    - name: Chinese Academy of Sciences, University of Chinese Academy of Sciences, Beijing, 100049, China
      index: 2
    - name: State Key Laboratory of Genetic Engineering and Ministry of Education Key Laboratory of Contemporary Anthropology,             Collaborative Innovation Center for Genetics and Development, School of Life Sciences, Fudan University, Shanghai,             200433, China
      index: 3
date: 2 Sep 2017
bibliography: paper.bib
---
# Summary

Genome-wide scan for natural selection is a classical challenge in population genetics. Currently, there are two popular kinds of approaches for detecting signals of natural selection from genomic data. One is based on extended haplotype homozygosity (EHH); the other is based on genetic diversity θ. The time complexities of these approaches, however, are quadratic with respect to the number of variants or the number of samples. As more and more massive genomic datasets become available, these quadratic time complexities would limit research community to quickly detect signals of natural selection. Moreover, neither of these approaches could quantify the differences of the strength of natural selection between populations. 

Here, we implemented a scalable tool called SeleDiff for testing and estimating selection differences between populations. Using SeleDiff, we can analyze a simulated dataset containing 300,000 variants and 100,000 samples in 15.7 minutes (wall time) with less than four gigabytes of random access memory (OS: Red Hat Enterprise Linux Server release 6.3; CPU: AMD OpteronTM 6174). Running time analysis showed SeleDiff has linear time complexity with respect to both the number of variants and the number of samples. This linear time complexity would make SeleDiff a fast and scalable tool for quantifying signals of natural selection in genome-wide scan.

# References
1. He Y, et al. A probabilistic method for testing and estimating selection differences between populations. Genome Res. 25, 1903–1909 (2015).