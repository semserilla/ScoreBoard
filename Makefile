#
# generic java makefile template
#
# Roland Poppenreiter <rpoppen@xdevel.net>
#

# extension of the source
sourceextension := java

# source directory
sourcedir := ./src

# executeable binary
exec := ScoreBoard.jar

# entry point (main)
entry := $(shell egrep -lr 'void main' --include=*.$(sourceextension) $(sourcedir) | tail -n1 | sed -e 's|.$(sourceextension)||g; s|$(sourcedir)/||g; s|/|.|g')

# source files
sources := $(shell find $(sourcedir) -name *.$(sourceextension))

# destination files
objs := $(patsubst %.$(sourceextension), %*.class, $(sources))

# commands
CC := javac
CFLAGS := 
INCLUDES :=
LIBS := 

# default target
default: all

.PHONY: all default clean

all: binary 

binary: 
	$(CC) $(CFLAGS) $(sources) 
	jar cvfe $(exec) $(entry) -C $(sourcedir) .

run: 
	java -jar $(exec)

clean:
	rm -fr $(exec) $(objs)

