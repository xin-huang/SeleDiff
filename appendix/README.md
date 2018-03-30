## Introduction

In this appendix, we would like to share the used programs/scripts in order to make our study more reproducible. We performed all the simulations in [Digital Ocean](https://cloud.digitalocean.com/) Optimized Droplets. The information of these droplets is as follows:

- CPU: Intel(R) Xeon(R) Platinum 8168 Processor
- Random-access memory: 64 GB
- Operating system: Ubuntu 16.04.4 x64
- Java version: 1.8.0_161
- Java SE Runtime Environment (JRE): build 1.8.0_161-b12
- Java Virtual Machine (JVM): build 25.161-b12

## Simulation

### Programs

We used five programs for simulation. These programs are in `./programs`. `EigenStratSimulator.jar` and `VCFGenerator.jar` generate random genetic data in EIGENSTRAT and VCF formats, respectively. 
Three other programs--SLiM 2^1^, 4P^2^, and selscan^3^--are written by other researchers, and can be obtained through `0_download_programs.sh`.

### Simulation for Demographic Models

We used [SLiM 2](https://messerlab.org/slim/) to simulate different demographic models. In total, we considered six scenarios and tuned them with different parameters. All the SLiM 2 scripts are in `./scripts/SLiM2`. You can simulate any model by

	> slim model.txt

### Simulation for Performance analysis

We compared `SeleDiff` with other cross-population methods in two programs: DIST in [4P](https://github.com/anbena/4p) and XP-EHH in [selscan](https://github.com/szpiech/selscan). 
DIST in 4P contains five statistics: Nei's Gst (1973), Nei's Gst (1983), Hedrick's G'st (2005), Jost's D, and Weir & Cockerham's Fst (1984).

## References

1. [Haller & Messer, Mol Biol Evol, 2017.](https://academic.oup.com/mbe/article/34/1/230/2670194)
2. [Benazzo, Panziera & Bertorelle, Ecol Evol, 2014.](https://onlinelibrary.wiley.com/doi/abs/10.1002/ece3.1261)
3. [Szpeich & Hernandez, Mol Biol Evol, 2014.](https://academic.oup.com/mbe/article/31/10/2824/1012603)