## Introduction

In this appendix, we would like to share the programs/scripts used in our simulation in order to make our study more reproducible. We performed all the simulations in [Digital Ocean](https://cloud.digitalocean.com/) Optimized Droplets. The information of these droplets is as follows:

- CPU: Intel(R) Xeon(R) Platinum 8168 Processor
- Random-access memory: 64 GB
- Operating system: Ubuntu 16.04.4 x64
- Java version: 1.8.0_161
- Java SE Runtime Environment (JRE): build 1.8.0_161-b12
- Java Virtual Machine (JVM): build 25.161-b12

## Simulation Programs

### Simulation for Demographic Models

We used [SLiM 2](https://messerlab.org/slim/) to simulate different demographic models. In total, we considered six scenarios and tuned them with different parameters. All the SLiM2 scripts are in `./appendix/scripts/SLiM2`. You can simulate any model by

	> slim model.txt

### Simulation for Performance analysis

We compared SeleDiff with other cross-population methods in two programs: [4P](https://github.com/anbena/4p) and [selscan](https://github.com/szpiech/selscan). We 