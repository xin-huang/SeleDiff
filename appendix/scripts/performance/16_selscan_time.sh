var=(10000 20000 50000 100000 200000 500000 1000000 2000000 5000000)

for ((i=0;i<9;i++))
do
	for ((j=0;j<3;j++))
	do
		sleep 10
		echo "time: $j"
		time ../../programs/selscan/bin/linux/selscan --xpehh --vcf test_selscan_pop0_ind_1000_snp_${var[$i]}.vcf --vcf-ref test_selscan_pop1_ind_1000_snp_${var[$i]}.vcf --out test_selscan_snp_${var[$i]} --map test_selscan_pop0_ind_1000_snp_${var[$i]}.map
		echo
	done
done
