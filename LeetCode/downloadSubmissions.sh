#!/bin/sh
#!/usr/bin/bash
filename="$1"
while read -r line
do
    name="$line"
    echo "Name read from file - $name"
    prob=`echo $name | cut -d "*" -f1`
    link=`echo $name | cut -d "*" -f2`
    echo $prob
    echo $link
    class=`echo $prob | sed 's/ //g'`
    echo $class
    echo -en "$(curl --cookie "express.sid=s%3AHthn0HNrSz0ZE3eNKMy-7KZYznWuoeCv.UNlh6YWWvs3Wt%2BxvCi2pziA1tq1NgbJ2drMgn%2BwryZ4; LEETCODE_SESSION=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFuc2h1MiIsInVzZXJfc2x1ZyI6ImFuc2h1MiIsIl9hdXRoX3VzZXJfaWQiOiIxMTMxODUiLCJ0aW1lc3RhbXAiOiIyMDE3LTAyLTE5IDA2OjI1OjIzLjg5MjA5MyswMDowMCIsIl9hdXRoX3VzZXJfYmFja2VuZCI6ImFsbGF1dGguYWNjb3VudC5hdXRoX2JhY2tlbmRzLkF1dGhlbnRpY2F0aW9uQmFja2VuZCIsImlkIjoxMTMxODUsIl9hdXRoX3VzZXJfaGFzaCI6ImU5MGYyN2E4MTJlMjkwNDQ4N2EzMjRiYmJkMmExNDIyZWIzZTBkNWUiLCJlbWFpbCI6ImEucmFuamFuLmNvb2xAZ21haWwuY29tIn0.6x5uWH6nnA45sJI_C9vc-7sa-mJevdjlDuFA_2zSdYo; csrftoken=sKOn3fGFGkBqiUN0gXoeAcqdqJjjMNzPQUms3Pu208fEZgSB0kvayEci98A3MLIT; _gat=1; _ga=GA1.2.200371905.1486253242; __atuvc=7%7C6%2C18%7C7%2C10%7C8; __atuvs=58aa3b78902ddc32000" $link | grep submissionCode | sed -e 's/..$//' -e 's/  submissionCode: //g' -e 's/^.//' -e 's/Solution/'"$class"'/')" > $prob.java
done < "$filename"
	#prob="`echo $f | cut -d "*" -f1`"
	#link=`echo $f | cut -d "*" -f2`
	#echo $prob
	#echo $link
#done
