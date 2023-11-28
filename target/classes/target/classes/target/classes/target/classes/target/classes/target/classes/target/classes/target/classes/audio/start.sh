#!/bin/bash

#关闭$1程序
pid=$(ps -ef | grep $1 | grep -v grep | awk '{print $2}')
if [ -n "$pid" ]; then
    kill -9 $pid
fi

jar_name="$1"
new_name="$2"

echo "关闭程序！" + $jar_name

if [ -z "$new_name" ]; then
    nohup java -jar $jar_name > /dev/null 2>&1 &
    echo "new_name为空，启动原程序" + $jar_name
    exit 1
fi

#备份$1
backup_dir="/data/software/chunk"
backup_name="${jar_name}.$(date +%Y%m%d%H%M%S)"
mv $jar_name $backup_dir/$backup_name
echo "备份程序 " + $backup_name 

#启动$2程序
if nohup java -jar $new_name > /dev/null 2>&1 & then
    echo "启动成功！" + $new_name
else
    echo "启动失败！" + $new_name
fi                          



