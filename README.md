# pom-analysis

The project contains some urtilities to analyse poms based on the presence or absence of plugins, dependencies etc.

The API is very simple (`Filters` provided predicates on files (i.e. `Predicate<File>`)).
The analysis is done by parsing the poms into doms (XML trees), and then querying those trees with xquery. 
