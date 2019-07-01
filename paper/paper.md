---
title: 'SeleDiff: A fast and scalable tool for estimating and testing selection differences between populations'
tags:
    - Java
    - Population genetics
    - Natural selection
    - Selective pressures
authors:
    - name: Xin Huang
      orcid: 0000-0002-9918-9602
      affiliation: "1, 2"
    - name: Li Jin
      affiliation: "1, 3"
    - name: Yungang He
      orcid: 0000-0002-2931-2871
      affiliation: "4"
affiliations:
    - name:  Chinese Academy of Sciences Key Laboratory of Computational Biology, Chinese Academy of Sciences-Max Planck Society Partner Institute for Computational Biology, Shanghai Institutes for Biological Sciences, Shanghai, 200031, China
      index: 1
    - name: Chinese Academy of Sciences, University of Chinese Academy of Sciences, Beijing, 100049, China
      index: 2
    - name: State Key Laboratory of Genetic Engineering and Ministry of Education Key Laboratory of Contemporary Anthropology, Collaborative Innovation Center for Genetics and Development, School of Life Sciences, Fudan University, Shanghai, 200433, China
      index: 3
    - name: Institutes of Biomedical Sciences, Shanghai Medical College, Fudan University, Shanghai, 200032, China
      index: 4
date: 29 June 2019
bibliography: paper.bib
---

# Summary

Detecting and quantifying selection is a classical task in population genetics. Over the last two decades, many studies detected selection signals in genomes. Few studies, however, quantified differences in selective pressures  between populations due to the lack of efficient tools. Here we implemented an open-source software, SeleDiff, with an established probabilistic method to estimate and test differences in selective pressures between populations. Extensive simulation revealed that SeleDiff is robust in various demographic models, as well as fast and scalable for analyzing large-scale genomic datasets. Thus, SeleDiff is helpful for analyzing selection as genomic datasets grow, and is available at https://github.com/xin-huang/SeleDiff.

# Introduction

Analyzing natural selection is critically important in population genetics [@Haldane:1924]. In the past 20 years, researchers have learned extensively about selection signals in genomic data [@Vitti:2013], but a deeper understanding of selection strength has remained elusive [@Thurman:2016].This is particularly due to difficulties in estimating selective pressures using empirical data. In addition, as the amount of genomic data has increased dramatically, researchers require more efficient software for analyzing large-scale genomic datasets. To meet these computational demands, we introduced and evaluated SeleDiff, a fast and scalable tool for quantifying differences in selective pressures between populations.

# Results

SeleDiff implements a probabilistic method from our previous study [@He:2015]. In this approach, we introduced logarithm odds ratios of allele frequencies to measure differences in selective pressures. For a bi-allelic locus in the population $i$, let $p_i\left(t\right)$ and $q_i\left(t\right)$ denote the derived and ancestral allele frequencies at time $t$. We define the absolute fitness of the derived and ancestral alleles as $w_D$ and $w_A$. The relative fitness becomes

$$e^s = \frac{w_D}{w_A},$$

where $s$ is the (genic) selection coefficient. The selection (coefficient) difference between populations $i$ and $j$ is

$$d_{ij} = s_i - s_j = \frac{1}{t}\left[\ln\frac{p_i\left(t\right)/q_i\left(t\right)}{p_j\left(t\right)/q_j\left(t\right)} + \Omega\right] = \frac{1}{t}\left(\ln\text{OR}+\Omega\right),$$

where $\text{OR}$ stands for odds ratio; $\Omega$ approximately follows a normal distribution with a mean of zero and reflects the uncertainty of allele frequencies caused by factors other than selection; $t$ is the divergence time from populations $i$ and $j$ to their most recent common ancestor. Thus, the expectation and variance of $d_{ij}$ are

$$\begin{aligned}
\text{E}\left(d_{ij}\right) &= \text{E}\left(s_i - s_j\right) = \frac{1}{t}\ln\text{OR} \\
\text{var}\left(d_{ij}\right) &= \frac{1}{t^2}\left[\text{var}\left(\text{OR}\right)+\text{var}\left(\Omega\right)\right]
\end{aligned}.$$

Given a dataset with $n$ loci, we can estimate $\text{var}\left(\Omega\right)$ as

$$\text{median}\left\{\frac{\ln^2\text{OR}_k}{0.455}-\text{var}\left(\ln\text{OR}_k\right)\right\}, 1\leq k \leq n,$$

where $\text{var}\left(\ln\text{OR}\right)\approx1/\left[N_i\hat{p}_i\left(t\right)\right]+1/\left[N_i\hat{q}_i\left(t\right)\right]+1/[N_j\hat{p}_j\left(t\right)]+1/[N_j\hat{q}_j\left(t\right)]$. Here, $N_i$ and $N_j$ are the sample sizes of populations $i$ and $j$. We add 0.5 to allele counts less than 5 for continuity correction. To test the selection differences in a locus, we proposed a statistic:

$$\delta = \frac{\left[\text{E}\left(d_{ij}\right)\right]^2}{\text{var}\left(d_{ij}\right)},$$

where $\delta$ follows a central $\chi^2$-distribution with one degree of freedom in the absence of selection differences.

![The demographic models in simulation.](https://raw.githubusercontent.com/xin-huang/SeleDiff/master/figures/Fig1.png)

We evaluated SeleDiff in different demographic models (Figure 1) simulated by SLiM 2 [@Haller:2017]. In Models 1–70, we assume larger selection coefficients in Population 1 than in Population 2 (Figure 1A–E).  Without migration, SeleDiff accurately estimates selection differences ranging from 0 to 0.002/generation in scenarios with different population sizes (Figure 2A, Models 1–9). The estimated differences (Figure 2A, Models 10–17) are slightly smaller in scenarios with low initial frequencies ($\leq$ 0.02) of the selective allele or long divergence times ($\geq$ 5000 generations), because alleles with low initial frequencies are easily lost regardless of their selection coefficients, and alleles with small selection coefficients can reach high frequencies with long enough time.  SeleDiff is affected little by time-varied population sizes (Figure 2A, Models 18–37), except for extremely severe bottlenecks in populations under less selective pressures (Figure 2A, Model 23). In Models 38–46 (Figure 2A), populations diverge into subpopulations, and selection stops in one of these subpopulations. If we ignore their structures, then the estimated differences diminish because SeleDiff treats all the individuals in a group homogenously.  Therefore, it is important to select samples carefully and interpret results cautiously. In models with moderate migration rates (0.00001–0.0001/generation), the estimated differences are only slightly smaller than the given values, whereas strong migration reduces differences between populations (Figure 2B, Models 47–70), a well-known phenomenon in population genetics [@Crow:2009]. SeleDiff also works well in complex models (Figure 2B, Model 1a–6d) involving multiple demographic events from human evolution [@Gravel:2011]. Thus, SeleDiff is robust in various demographic models, and indicates the lower bounds of differences in selective pressures when migration or substructure exists.

![Accuracy and speed of SeleDiff.](https://raw.githubusercontent.com/xin-huang/SeleDiff/master/figures/Fig2.png)

Finally, we compared the performance of SeleDiff with other cross-population methods in two recent programs—4P and selscan—for genome-wide selection scans [@Benazzo:2014; @Szpeich:2014]. All the programs were executed with a single thread. SeleDiff can analyze a dataset containing $10^8$ base pairs of variants in less than 1 hour (Figure 2C) with less than 4 gigabytes of random-access memory (Figure 3), and is much faster than the other two programs (Figure 2D). To enhance the scalability of SeleDiff, we integrated it with a newly developed online algorithm— t-digest [@Dunning:2014]. T-digest allows SeleDiff to estimate $\text{var}\left(\Omega\right)$ from genome-wide data with only a small amount of memory (Figure 3). In summary, SeleDiff can help researchers detect and quantify natural selection from massive genomes in this era of big data.

![Performance of SeleDiff.](https://raw.githubusercontent.com/xin-huang/SeleDiff/master/figures/Fig3.png)

# Acknowledgements

This work was supported by grants from National Natural Science Foundation of China (91331109 and 91731310 to Y.H.; 31271338 and 3133038 to L.J.). L.J. was also supported by the Shanghai Leading Academic Discipline Project (B111) and the Center for Evolutionary Biology at Fudan University. The authors declare no conflict of interest.

# References