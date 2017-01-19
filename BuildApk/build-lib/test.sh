
output_apk_path=
gradlew_path="./gradlew"
echo "start build..."

for line in `cat path`
    do
        echo $line
        if [[ $line =~ "#" ]];then
            echo "has not apk path"
        else
            output_apk_path=${line}
        fi
    done

line=sourceChannel
cd ..
if [ ! -n "$output_apk_path" ]; then
    echo "IS NULL"
    ${gradlew_path} assemble${line}Release
else
    echo "NOT NULL"
    ${gradlew_path} assemble${line}Release -Poutput_dir=${output_apk_path}
fi