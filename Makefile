GRADLE=`./get_gradle.sh`

all: report clean build

build:
	@$(GRADLE) build

clean:
	@$(GRADLE) clean

report:
	@echo "Using: $(GRADLE)"

start: all
	java -jar duckling.jar
