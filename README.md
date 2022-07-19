***Direct Follower Extraction***

**How to run**

    mvn clean install
    java -jar target/direct-followers-1.0.jar    

**Notes**

The matrix table has some hardcoded formatting, could be tweaked
to be dynamic depending on how long the activity strings are.

In case of single event in traces or no matching traces nothing is returned or printed. Perhaps a default empty matrix
would be nicer and clearer in this case.

I use TreeSet for creating Traces because it has good insertion sort and performance. But it risks losing duplicated events. Could
be addressed by implementing my own collection with insertion sort. As far as I found there's no other out of the box
library provided by stdlib.

Matrix string output is not tested, I skipped it because it's quite tricky to setup because and create an
accurate test string to test against. For production code I would definitely have done this though.