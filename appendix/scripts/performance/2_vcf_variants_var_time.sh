var=(10000 20000 50000 100000 200000 500000 1000000 2000000 5000000 10000000 100000000)

for ((i=0;i<11;i++))
do
        for ((j=0;j<3;j++))
        do
		sleep 10
                echo "time: $j"
                /usr/bin/time --verbose ../SeleDiff/build/install/SeleDiff/bin/SeleDiff compute-var --vcf ./test_pop_2_ind_2000_snp_${var[$i]}.vcf --ind test_pop_2_ind_2000.ind --output test_pop_2_ind_2000_snp_${var[$i]}.var
                echo
        done
done
