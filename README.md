# Zendesk Melbourne - Coding Challenge

* [Quckstart](#quickstart)
* [The Longer Way Around](#the-longer-way-around)
* [How it All Works](#how-it-all-works)
* [Some Notes](#some-notes)
* [Assumptions](#assumptions)
* [Limitations](#limitations)

## Quickstart
The easiest way to run this command line tool is by [docker]([https://www.docker.com/](https://www.docker.com/)).

1. Clone the project into your local first.
```
> git clone https://github.com/mvadood/zendesk-coding-challenge.git zendesk
```
2. cd into the cloned directory
```
> cd zendesk
```
3. Build the docker image. This will take a couple of minutes.
```
> docker build -t zendesk .
```
4. Run the cli sharing a directory with it so you can pass on json files to it
```
> run --rm -it -v {host-directory-path}:/app/resources zendesk
```
5. By now, the cli should be launched. Run the `load` command to load the default json files.
```
> shell:> load
```
* In case you want to load your own files, first copy your files into `{host-directory-path}` you entered before and then do `load /app/resources/{users-file-name} /app/resources/{orgs-file-name} /app/resources/{tickets-file-name}`
6. Search!
```
> shell:> search user _id "2"
> shell:> search organization domain_names "kage.com"
> shell:> search ticket due_at "2016-07-31T02:37:50 -10:00"
```
7. Use the `help` command if needed.
```
shell:> help
AVAILABLE COMMANDS

Built-In Commands
        clear: Clear the shell screen.
        exit, quit: Exit the shell.
        help: Display help about available commands.
        history: Display or save the history of previously run commands
        script: Read and execute commands from a file.
        stacktrace: Display the full stacktrace of the last error.

Shell Commands
        fields: Show the fields on which search is supported.
        load: Load up entity files. Pass three values pointing to the users, organization and tickets files.
        search: Search for entities. Do 'search ticket[|user|organization] field value' to search for entities.

```
You can ask for help for a specific command as well.
```
shell:> help search


NAME
	search - Search for entities. Do 'search ticket[|user|organization] field value' to search for entities.

SYNOPSYS
	search [[--e] string]  [[--f] string]  [[--v] string]

OPTIONS
	--e or --entity  string
		Entity to search. Choose 'user','organization' or 'ticket'
		[Optional, default = ticket]

	--f or --field  string
		Field to search. For a full list of fields run the 'fields' command
		[Optional, default = _id]

	--v or --value  string
		Value to search for
		[Optional, default = ]
```
8. Run `quit` or `exit` to exit the shell.
## The longer way around
This cli was written in java ([JDK 14]([https://openjdk.java.net/projects/jdk/14/](https://openjdk.java.net/projects/jdk/14/))). [Maven]([https://maven.apache.org/](https://maven.apache.org/)) was also used to build the project. Therefore, if you intend to run the project without docker, follow these steps:

1. Make sure you have both jdk14 and maven installed.
2. Clone the project into your local first.
```
> git clone https://github.com/mvadood/zendesk-coding-challenge.git zendesk
```
3. cd into the cloned directory
```
> cd zendesk
```
4.  Run a maven build.
```
> mvn clean install
```
5. cd into the `target` directory and run the jar file.
```
> cd target
> java -jar coding-challenge-1.0-SNAPSHOT.jar
```
6. The rest of the steps are the same as mentioned on the [Quckstart](#quickstart) section.
```
> shell:> load
> shell:> search user _id "2"
> shell:> quit
```
## How it all works
The code of four main packages:
* `model`: contains all the classes modeling different entities such as the `user` and also request and response entities that are used to get the user input and return the results to them.
* `processor`: Contains the class building up the index and handling search requests.
* `repository`: Contains a simple implementations of an im-memory [inverted index]([https://www.geeksforgeeks.org/inverted-index/](https://www.geeksforgeeks.org/inverted-index/)) which aims to provide `O(1)` eaxcr match search capabilities.
* `view`: Contains classes needed to interpret and validate user input

The inverted index was implemented using simple Java Maps. The whole application is based on [Spring boot]([https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)). [Spring Boot Shell]([https://projects.spring.io/spring-shell/](https://projects.spring.io/spring-shell/)) takes care of exposing the methods through shell commands.

## Some notes
1. With the help of [logback]([http://logback.qos.ch/](http://logback.qos.ch/)), all logs are emitted into files residing in a directory called logs that is created automatically upon the start of the app.
2. With the help of [Gson]([https://github.com/google/gson](https://github.com/google/gson))'s stream reader, json files are not loaded into the memory all at once. However, with the current implementation, the whole reverse index does reside in memory. Had I more time, I would implement a disk based index based on [Apache Lucene]([https://lucene.apache.org/](https://lucene.apache.org/)) that could eliminate completely the memory limitation and also give more search capabilities.
3. A simple stress test is also included where an arbitrary number of tickets can be generated all at once (with the help of [Mockneat]([https://github.com/nomemory/mockneat](https://github.com/nomemory/mockneat))) and given to the shell. This can be used to see where the breaking point of the app is.
To do that, go to `src/test/java/com/zendesk/stress/StressTests.java`, tweak `numTickets` and do `mvn test` to run it.
4. To search for empty values, use `search entity_name field_name ""`
5. This problem could be resolved with more simplicity and probably less tech involved. Anyway, Using different frameworks such as spring were more used to serve as a showcase to what can be achieved in the real world and in a production environment.
## Assumptions
1. Users, tickets and organizations all have unique ids.
2. All three files need to be provided. (If only one is provided, the other two ones will be loaded from the default files)
3. Finding an entity based on one of its array fields can be done by searching for one of the elements in that array and not the whole array. For example, if `tags={a,b}`, user can do `search X tags a` or `search X tags b` and not `search X tags [a,b]`.
## Limitations
1. For date search, the user can choose between providing a datetime string in the same format used in the sample files or provide an epoch timestamp in seconds.
i.e. A time similar to `2016-07-31T02:37:50 -10:00` or a timestamp similar to `1586150527`
2. Memory limits. Please refer to point 2 on [some notes](#some-notes)