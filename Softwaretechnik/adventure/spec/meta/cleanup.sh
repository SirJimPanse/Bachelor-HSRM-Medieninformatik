#!/bin/bash
SELF=$(readlink -f $0)

usage()
{
	echo "Dieses Shell-Skript löscht alle Dateien mit den Endungen"
	echo ".acn .acr .alg .aux .bbl .blg .dvi .glg"
	echo ".glo .gls .ist .log .out .ps .toc"
	echo "in dem aktuellen Verzeichnis (mit -r auch rekursiv in jedem Unterverzeichnis)."
	echo
	echo "Ebefalls gelöscht werden auch Dateien die auf .backup und ~ (Tilde) enden."
	echo "Um Unglück zu Vermeiden, erwartet das Skript als ersten Parameter 'yes'."
	echo 
}

if [ $# -lt 1 ]; then
	usage
	exit 0
fi

if [ $1 != "yes" ]; then
	usage
	exit 0
fi

RECURSIVE=0
if [ $# -gt 1 ]; then
	if [ $2 == "-r" ]; then
		RECURSIVE=1
	fi
fi

rm -f *.acn
rm -f *.acr
rm -f *.alg
rm -f *.aux
rm -f *.bbl
rm -f *.blg
rm -f *.dvi
rm -f *.glg
rm -f *.glo
rm -f *.gls
rm -f *.ist
rm -f *.log
rm -f *.out
rm -f *.ps
rm -f *.toc
rm -f *.backup
rm -f *~

if [ $RECURSIVE -eq 1 ]; then
	for name in *; do
		if [ -d "$name" ]; then
			cd "$name"
			"$SELF" yes -r
			cd ..
		fi
	done
fi
