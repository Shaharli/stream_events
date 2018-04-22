# stream_events

Simple stream processing service that consumes executable which spits out an infinite stream of lines of event data encoded in JSON.

Exposes stats about event type and data count in HTTP interface.


Use /events/type  -> get map representing count of events by event type

Use /events/data -> get map representing count of words encountered in the data field of the events

TODO:
- Implementing a listener for consuming file output
- Testing the processing data methods by feeding various input
- Presenting http output in a more readable UI 
