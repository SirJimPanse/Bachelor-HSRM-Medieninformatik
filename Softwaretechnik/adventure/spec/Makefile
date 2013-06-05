TEXDOC=spec
GLODOC=meta/glossary

.PHONY: all pdf clean log

all:	
	@meta/cleanup.sh yes -r
	@echo "generating logfile"
	@meta/svnlog-gen.sh \
	  https://scm.mi.hs-rm.de/svn/2012swt/2012swt03/adventure \
	   meta/svnlog.tex \
	   5
	@echo "starting pdf"
	- @pdflatex $(TEXDOC)
	@echo "1 of 5...............BIBTEX"
	- @bibtex -terse $(TEXDOC)
	@echo "2 of 5.............GLOSSARY"
	- @makeglossaries $(TEXDOC)
	@echo "3 of 5....TABLE OF CONTENTS"
	- @pdflatex $(TEXDOC)
	@echo "4 of 5...........MAKING PDF"
	- @pdflatex $(TEXDOC)
	@echo "5 of 5..........CLEANING UP"
	@meta/cleanup.sh yes -r
	@echo "finished"

log:
	@echo "generating logfile"
	@meta/svnlog-gen.sh . meta/svnlog.tex 5

clean:
	@echo "................CLEANING UP"
	@meta/cleanup.sh yes -r

pdf:	all