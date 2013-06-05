#usage svnlog-gen.sh [repo-path] [output-file] [limit]
svn log -v -l $3 --xml $1 > tmp.out;
xsltproc meta/svnlog.xslt tmp.out > $2;
rm tmp.out
