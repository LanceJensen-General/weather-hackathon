# AT&T Big Data SWE Test

## Introduction
The stated goal of the test is to "see your code, coding style, problem solving, creativity and critical thinking skills". Implicit in that is delivery as well in a short window 1 1/2 days.  

With this in mind I tried to research about what could be done with weather data. I found a well known effect called the ["Urban Heat Island"](https://scholar.google.com/scholar?hl=en&as_sdt=0%2C44&q=Urban+Heat+Island+effect&oq=) effect. Due to a number of conditions, cities are consistently warmer than their rule counterparts by as much as 8 degrees C. Other papers talked about the wash out effect of rain and how it muted or eliminated this effect. One last paper mentioned temperature gradients and that the center of the city was always the hottest. My study is to use the Urban Heat Island effect across temperature data to locate city centers in the US. This is easily verified and is a good example of feature engineering from a data set.

## Plan
Phase 1: Build an ETL tool that allows me to format explore the data. Make it reusable and command-line.  Coding is less important than delivery as doing a good coding job would consume all my time without doing any analysis so compromises just like in life have to be made. Along the way hopefully I can show skills but also flexiblity in execution to meet goals...
Phase 2: Data preparation
    a. Find all the US Stations with complete data on states to rule out bouys.
    b. Join the station data to the weather data.
    c. For each day 12:00 temperature reading where the previous day did not have precipitation to avoid the washing effect
Phase 3: Create a graphml export of the that contains the temp,lat, and long on the vertexes. The graph edges include the 10 nearest neighbors and the distance to that neighbor.
    e. If all goes well upto this point I should be able to update the graph in gremlin to contain the temperature gradient between the vertexes. Using a hill climber algorithm of maximizing the gradient will lead to the local maximum which should be as close to a city center as the weather data will allow.
Phase 4: Loading the data to a tinker graph and following the edge with the maximum tempreture gradient to the highest temp, in theory yielding a city center.
Phase 5: Finish packaging the modules as executables and complete the documentation.
    
## Zigging and Zagging
My plan was too aggressive for the 2 days I had to work on it. I ran into many unforseen issues such as duplicate stations, stations USAF-WBAN-<YYYY>.gz files not existing in the repo directories, special characters that where incompatable with graphml. I completed phases 1,2, and 3 to various degrees and hope to finish Phase 4 before the interview for demo purposes.

## Building

    git clone https://github.com/LanceJensen-General/weather-hackathon.git
    mvn clean install -DskipTests=true # I probably have broken tests as this was hacked together...
    
## Running

Each main method has a reflective parser.  Look at the projects input configuration pojo and use flags to initalize it:

Example java -jar weather-etl -weatherPullConfiguration --pullFile ftp://ftp.ncdc.noaa.gov/pub/data/noaa/isd-history.txt --outputDirectory /tmp/weatherDownloadDirectory

Or you can look at the unit tests and run it from there...

## Summary

I did not accomplish all my goals and I would like to make the code quality better with more documentation, better application design instead organic cowboy coding "design".

Accomplishments:

1.  Reflective Parser - I planned to make all the code reusable components and the reflective argument parser was core to that strategy. It was too much and I should have avoided it.  The positive of doing it allowed me to demonstrate skills with reflection and some coding ingenuity but it was a poor use of my time otherwise costing me code quality throughout the project.
2.  ETL application - Since I could not use spark I had to roll my own ETL and ftp processing tools. In hindsight I should have just sed/awk/paste/cut/grep my way to the data I needed. I would have found the issues with the data faster without having to debug/write code and could have focused solely on graph translation.
3. Graph translation application - is a success and I was able to translate station and temperature gradient routes between stations to a graph and load it into tinker graph.
4. Algorithm - Wrote a quad tree to implement finding nearest neighbors through recursive bin packing and dividing/resizing. Not my best work probably 7/10 quality but actually building it in this short of time was an accomplishment.

Copyright Lance Jensen all rights reserved