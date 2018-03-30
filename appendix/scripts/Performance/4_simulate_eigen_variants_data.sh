var=(10000 20000 50000 100000 200000 500000 1000000 2000000 5000000 10000000 100000000)

for ((i=0;i<10;i++))
do
        java -jar ../SeleDiff/appendix/EigenStratSimulator.jar 2 2000 ${var[$i]} test_pop_2_ind_2000_snp_${var[$i]}
done
