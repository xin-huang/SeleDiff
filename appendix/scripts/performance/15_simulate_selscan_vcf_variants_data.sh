var=(10000 20000 50000 100000 200000 500000 1000000 2000000 5000000)

for ((i=0;i<9;i++))
do
        java -jar ../../programs/VCFGenerator.jar 1 1000 ${var[$i]} test_selscan_pop0_ind_1000_snp_${var[$i]}
	java -jar ../../programs/VCFGenerator.jar 1 1000 ${var[$i]} test_selscan_pop1_ind_1000_snp_${var[$i]}
	sed '1d' test_selscan_pop0_ind_1000_snp_${var[$i]}.vcf | awk '{print $1" "$3" "$2" "$2}' > test_selscan_pop0_ind_1000_snp_${var[$i]}.map
done
