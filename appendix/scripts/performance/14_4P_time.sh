var=(10000 20000 50000 100000 200000 500000 1000000 2000000 5000000)
chmod a+x ../../programs/4p/bin/4P_64_linux

for ((i=0;i<9;i++))
do
	for ((j=0;j<3;j++))
	do
		sleep 10
		echo "time: $j"
		time ../../programs/4p/bin/4P_64_linux -f test_4P_pop_2_ind_2000_snp_${var[$i]}.vcf -i 2 -n 2000 -s ${var[$i]} -p test_4P_pop_2_ind_2000.ind -t 1
		echo
	done
done
