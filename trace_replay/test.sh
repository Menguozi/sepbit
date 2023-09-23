rm -rf tmp_$1_$2/indexmap/*
rm -rf tmp_$1_$2/ondiskfifo/*
rm -rf tmp_$1_$2/segments/*

mkdir tmp_$1_$2
mkdir tmp_$1_$2/indexmap
mkdir tmp_$1_$2/ondiskfifo
mkdir tmp_$1_$2/segments

mvn exec:java -Dexec.mainClass="gcsimulator.Simulator" -Dexec.args="--path ./etc/synthetic_groups/all \
    --outputPrefix ./tmp_$1_$2/ --propertyPath ./etc/synthetic_property.txt \
    --selectionAlgorithm $1 --setSeparateMethod $2 --setNumOpenSegments 6" | tee ./tmp_$1_$2/output.txt
