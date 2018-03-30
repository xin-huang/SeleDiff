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

We used five programs for simulation except `SeleDiff`. These programs are in `./programs`. `EigenStratSimulator.jar` and `VCFGenerator.jar` generate random genetic data in EIGENSTRAT and VCF formats, respectively. Three other programs--`SLiM 2`, `4P`, and `selscan`--are written by other researchers<sup>1-3</sup>, and can be obtained through `0_download_programs.sh`. All the programs were executed with a single thread.

### Simulation for Demographic Models

We used [SLiM 2](https://messerlab.org/slim/) to simulate different demographic models. In total, we considered six scenarios and tuned them with different parameters. All the SLiM 2 scripts are in `./scripts/SLiM2`. You can simulate any model by

	> slim model.txt

Here, we briefly describe our demographic models. Except the Complex Models, we assume two populations, and the selection coefficient is larger in Population1 than in Population2. For the Complex Models, we follow the recipe in [SLiM 2 Manual](https://messerlab.org/slim/), and simulate recent selection started from the divergence of Eurasians and Africans, and continued in both Europeans and East Asians.

- Basic Models: Two populations without migration, and constant population sizes through time. We vary:
	- Magnitudes of selection differences: 0-0.002/generation;
	- Population sizes: 5000-100,000 diploid individuals/population;
	- Initial frequencies of the selective allele: 0.01-0.2;
	- Divergenc times: 1000-5000 generations.
- Bottleneck Models: Two populations without migration, and one population suddenly reduces its population size at the 1000th generation.
	- The initial frequency of the selective allele: 0.1;
	- The selection difference: 0.001/generation;
	- The divergence time: 2000 generations;
	- Bottleneck strength: 0.1-0.9. 
- Expansion Models: Two populations without migration, and one population grows exponentially at the 1000th generation.
	- The initial frequency of the selective allele: 0.1;
	- The selection difference: 0.001/generation;
	- The divergence time: 2000 generations;
	- Expansion strength: 0.1-0.9.
- Migration Models:
	- The initial frequency of the selective allele: 0.1;
	- The divergence time: 2000 generations;
	- Constant population size: 10000 diploid individuals/population.
	- Selection differences: 0-0.002/generation;
	- Migration rates: 0.00001-0.002/generation.
- Substructure Models: Two populations without migration, and at least one population divides into two subpopulations at the 1000th generation.
	- The initial frequency of the selective allele: 0.1;
	- The divergence time: 2000 generations;
	- Selection differences: 0-0.002/generation.
- Complex Models: Three populations (Africans, Europeans, and East Asians) involving multiple demographic events.
	- The initial frequency of the selective allele: 0.1;
	- Selection differences: 0-0.002/generation between Africans and Eurasians.

### Simulation for Performance analysis

To analyse the performance of `SeleDiff`, we first used `EigenStratSimulator.jar` and `VCFGenerator.jar` to simulate two populations (1000 diploid individuals/population), 
and varied sizes of variants from 10,000 to 100,000,000 bp. Then we fixed the size of variants to 1,000,000 bp, and varied the numbers of individuals in each population from 1,000 to 100,000.

We compared `SeleDiff` with other cross-population methods in two programs: `DIST` in [4P](https://github.com/anbena/4p) and `XP-EHH` in [selscan](https://github.com/szpiech/selscan). 
`DIST` in `4P` contains five statistics: Nei's Gst (1973), Nei's Gst (1983), Hedrick's G'st (2005), Jost's D, and Weir & Cockerham's Fst (1984). 
In these comparisons, we used `VCFGenerator.jar` to simulate two populations (1000 diploid individuals/population), and varied sizes of variants from 10,000 to 5,000,000 bp.

The scripts for performance analysis are in `./scripts/performance`

## References

1. [Haller & Messer, *Mol Biol Evol*, 2017.](https://academic.oup.com/mbe/article/34/1/230/2670194)
2. [Benazzo, Panziera & Bertorelle, *Ecol Evol*, 2014.](https://onlinelibrary.wiley.com/doi/abs/10.1002/ece3.1261)
3. [Szpeich & Hernandez, *Mol Biol Evol*, 2014.](https://academic.oup.com/mbe/article/31/10/2824/1012603)
