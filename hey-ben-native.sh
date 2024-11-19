hey -n 100000 -c 100 -m GET http://localhost:8080/rules/v1/ > ./ben-logs/ben_c100_n100000_native_512m.txt
hey -n 100000 -c 200 -m GET http://localhost:8080/rules/v1/ > ./ben-logs/ben_c200_n100000_native_512m.txt
hey -n 100000 -c 400 -m GET http://localhost:8080/rules/v1/ > ./ben-logs/ben_c400_n100000_native_512m.txt
hey -n 100000 -c 800 -m GET http://localhost:8080/rules/v1/ > ./ben-logs/ben_c800_n100000_native_512m.txt