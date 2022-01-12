total=0

for i in $(find . -type f -name '*.kt')
do
 total=$(($total+$(wc -l < $i)))
done

echo $total
