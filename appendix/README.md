## Introduction

In this appendix, we shared the used programs/scripts in order to make our study more reproducible. We performed all the simulations in [Digital Ocean](https://cloud.digitalocean.com/) Optimized Droplets. The information of these droplets is as follows:

- CPU: Intel(R) Xeon(R) Platinum 8168 Processor
- Random-access memory: 64 GB
- Operating system: Ubuntu 16.04.4 x64
- Java version: 1.8.0_161
- Java SE Runtime Environment (JRE): build 1.8.0_161-b12
- Java Virtual Machine (JVM): build 25.161-b12

## Simulation

### Programs

We provide five programs in `./programs`. `EigenStratSimulator.jar` and `VCFGenerator.jar` generate random genetic data in EIGENSTRAT and VCF formats, respectively. 
Because these two programs produce random numbers of individuals in each population, we provide an additional file describing two populations, each comprising 1000 diploid individuals.
Three other programs--`SLiM 2`, `4P`, and `selscan`--are written by other researchers<sup>1-3</sup>, and can be obtained through `0_download_programs.sh`. 
All the programs were executed with a single thread.

`./programs/TDigestTest.java` compares median estimations from `t-digest` and `quickselect`.
After build and install `SeleDiff`, you can execute `./programs/run_TDigestTest.sh` to see the results.

### Simulation for Demographic Models

We used [SLiM 2](https://messerlab.org/slim/) to simulate different demographic models. In total, we considered six scenarios and tuned them with different parameters. All the SLiM 2 scripts are in `./scripts/SLiM2`. You can simulate any model by:

	> slim model.txt

Here, we briefly describe our demographic models. Except the Complex Models, we assume two populations, and the selection coefficient is larger in Population1 than in Population2. For the Complex Models, we follow the recipe in the [SLiM 2 Manual](https://messerlab.org/slim/), and simulate recent selection started from the divergence of Eurasians and Africans, and continued in both Europeans and East Asians.

- Basic Models: Two populations without migration, and constant population sizes through time. We vary:
	- Magnitudes of selection differences: 0-0.002/generation;
	- Population sizes: 5000-100,000 diploid individuals/population;
	- Initial frequencies of the beneficial allele: 0.01-0.2;
	- Divergence times: 1000-5000 generations.
- Bottleneck Models: Two populations without migration, and one population suddenly reduces its population size at the 1000th generation.
	- The initial frequency of the beneficial allele: 0.1;
	- The initial population size: 10,000 diploid individuals/population;
	- The selection difference: 0.001/generation;
	- The divergence time: 2000 generations;
	- Bottleneck strength: 0.1-0.9. 
- Expansion Models: Two populations without migration, and one population grows exponentially at the 1000th generation.
	- The initial frequency of the beneficial allele: 0.1;
	- The initial population size: 10,000 diploid individuals/population;
	- The selection difference: 0.001/generation;
	- The divergence time: 2000 generations;
	- Expansion strength: 0.0005-0.002.
- Migration Models:
	- The initial frequency of the beneficial allele: 0.1;
	- The divergence time: 2000 generations;
	- Constant population size: 10,000 diploid individuals/population;
	- Selection differences: 0-0.002/generation;
	- Migration rates: 0.00001-0.002/generation.
- Substructure Models: Two populations without migration, and at least one population divides into two subpopulations at the 1000th generation.
	- The initial frequency of the beneficial allele: 0.1;
	- The initial population size: 10,000 diploid individuals/population;
	- The population size of each subpopulation: 5000 diploid individuals/population;
	- The divergence time: 2000 generations;
	- Selection differences before divergence: 0-0.002/generation.
- Complex Models: Three populations (Africans, Europeans, and East Asians) involving multiple demographic events.
	- The initial frequency of the beneficial allele: 0.1;
	- Selection differences: 0-0.002/generation between Africans and Eurasians.
	
The nomenclature of each file in `./SLiM2` is as follow.

- The first two letters specify models:
	- `bm`: The Basic Models;
	- `bn`: The Bottleneck Models;
	- `pe`: The Expansion Models;
	- `ss`: The Substructure Models;
	- `mg`: The Migration Models;
	- `cm`: The Complex Models.
- `if`: The initial frequency of the beneficial allele.
- `t`: The divergence time.
- `m`: The migration rate.
- `d`: The selection difference.
- `f`: The strength of the bottleneck.
- `a`: The strength of the expansion.
- `p1`: The bottleneck/expansion occurs in the population p1.
- `p2`: The bottleneck/expansion occurs in the population p2.

### Simulation for the Performance Analysis

To analyse the performance of `SeleDiff`, we first used `EigenStratSimulator.jar` and `VCFGenerator.jar` to simulate two populations (1000 diploid individuals/population), 
and varied sizes of variants from 10,000 to 100,000,000 bp. Then we fixed the size of variants to 1,000,000 bp, and varied the numbers of individuals in each population from 1000 to 100,000.

We compared `SeleDiff` with other cross-population methods in two programs: `DIST` in [4P](https://github.com/anbena/4p) and `XP-EHH` in [selscan](https://github.com/szpiech/selscan). 
`DIST` in `4P` contains five statistics: Nei's Gst (1973), Nei's Gst (1983), Hedrick's G'st (2005), Jost's D, and Weir & Cockerham's Fst (1984). 
In these comparisons, we used `VCFGenerator.jar` to simulate two populations (1000 diploid individuals/population), and varied sizes of variants from 10,000 to 5,000,000 bp. 
You should prepare about 5TB hard disk space, if you want to replicate all the simulations.

The scripts for performance analysis are in `./scripts/performance`

### Visualization

We provide a Jupyter Notebook `./scripts/visualization/SeleDiff-Figures.ipynb` for visualizing our results. You can execute this notebook in [Anaconda](https://anaconda.org/) with Python 3.

## References

1. [Haller & Messer, *Mol Biol Evol*, 2017.](https://academic.oup.com/mbe/article/34/1/230/2670194)
2. [Benazzo, Panziera & Bertorelle, *Ecol Evol*, 2014.](https://onlinelibrary.wiley.com/doi/abs/10.1002/ece3.1261)
3. [Szpeich & Hernandez, *Mol Biol Evol*, 2014.](https://academic.oup.com/mbe/article/31/10/2824/1012603)
