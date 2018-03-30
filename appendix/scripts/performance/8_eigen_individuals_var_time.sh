ind=(2000 4000 10000 20000 40000 100000 200000)

for ((i=0;i<7;i++))
do
        for ((j=0;j<3;j++))
        do
		sleep 10
                echo "time: $j"
                /usr/bin/time --verbose ../../../build/install/SeleDiff/bin/SeleDiff compute-var --geno ./test_pop_2_ind_${ind[$i]}_snp_1000000.geno --ind test_pop_2_ind_${ind[$i]}_snp_1000000.ind --snp ./test_pop_2_ind_${ind[$i]}_snp_1000000.snp --output test_pop_2_ind_${ind[$i]}_snp_1000000.var
                echo
        done
done
