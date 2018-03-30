ind=(2000 4000 10000 20000 40000 100000 200000)
sigind=(1000 2000 5000 10000 20000 50000 100000)

for ((i=0;i<7;i++))
do
	java -jar ../../programs/EigenStratSimulator.jar 2 ${ind[$i]} 1000000 test_pop_2_ind_${ind[$i]}_snp_1000000
	for ((j=0;j<${sigind[$i]};j++)) do echo "ind$j U pop0"; done > test_pop_2_ind_${ind[$i]}_snp_1000000.ind
	for ((j=${sigind[$i]};j<${ind[$i]};j++)) do echo "ind$j U pop1"; done >> test_pop_2_ind_${ind[$i]}_snp_1000000.ind
done
