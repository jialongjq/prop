DOMAIN = FONT/domain
PRESENTATION = FONT/presentation
DRIVERS_DOMAIN = FONT/domain/drivers
DOMAIN_ALGORISMES = FONT/domain/algorismes
DATA = FONT/data
DATA_DRIVERS = FONT/data/drivers
UNITTEST = FONT/unittests
UTILS = FONT/utils

default: class

class:
	javac -cp "LIB/*" -d BIN $(DOMAIN)/*.java $(DRIVERS_DOMAIN)/*.java $(DOMAIN_ALGORISMES)/*.java $(DATA)/*.java $(DATA_DRIVERS)/*.java $(UNITTEST)/*.java $(UTILS)/*.java $(PRESENTATION)/*.java

jarDriver:
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Class-Path: LIB/junit-platform-console-standalone-1.8.1.jar LIB/json-20210307.jar" >> manifest.txt
	@echo "Main-Class: $(FQN)" >> manifest.txt
	@echo "" >> manifest.txt
	jar cmf manifest.txt $(N).jar -C BIN/ .
	rm manifest.txt

main:
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Class-Path: LIB/junit-platform-console-standalone-1.8.1.jar LIB/json-20210307.jar" >> manifest.txt
	@echo "Main-Class: presentation.Main" >> manifest.txt
	@echo "" >> manifest.txt
	jar cmf manifest.txt Main.jar -C BIN/ .
	rm manifest.txt

run:
	java -jar Main.jar

clean:
	rm -rf BIN/*
	rm *.jar