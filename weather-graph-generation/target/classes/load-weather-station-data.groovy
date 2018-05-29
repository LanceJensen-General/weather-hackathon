// You can use this file to load the weather station nearest neighbors graph from the Gremlin Console
//
// My gremlin install is /Users/lj556b/tools/apache-tinkerpop-gremlin-console-3.3.2/bin/gremlin.sh - i /Users/lj556b/Documents/workspace-sts-3.8.4.RELEASE/weather-skills-test/weather-graph-generation/src/main/resources/load-weather-station-data.groovy
//  
// To execute use the console command ":load load-weather-station-data.groovy"
//

conf = new BaseConfiguration()
conf.setProperty("gremlin.tinkergraph.vertexIdManager","LONG")
conf.setProperty("gremlin.tinkergraph.edgeIdManager","LONG")
graph = TinkerGraph.open(conf)

// Change the path below to point to wherever you put the graphml file
graph.io(graphml()).readGraph('/Users/lj556b/Documents/workspace-sts-3.8.4.RELEASE/weather-skills-test/weather-graph-generation/weather_stations.graphml')

g=graph.traversal()
:set max-iteration 1000     
