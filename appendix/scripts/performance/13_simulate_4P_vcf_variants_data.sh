var=(10000 20000 50000 100000 200000 500000 1000000 2000000 5000000)

for ((i=0;i<9;i++))
do
        java -jar ../../programs/VCFGenerator.jar 2 2000 ${var[$i]} test_4P_pop_2_ind_2000_snp_${var[$i]}
	sed 's/0\t1/A\tT/' test_4P_pop_2_ind_2000_snp_${var[$i]}.vcf > tmp
	mv tmp test_4P_pop_2_ind_2000_snp_${var[$i]}.vcf
done
for ((i=0;i<1000;i++)) do echo "ind$i pop0"; done > test_4P_pop_2_ind_2000.ind
for ((i=1000;i<2000;i++)) do echo "ind$i pop1"; done >> test_4P_pop_2_ind_2000.ind
