This module is intended to make testing easier.

It replaces the EventPublisherFactory, so it creates TestMessageRouter for all destinations, which means published messages will end up in 
a list in memory instead of routing them to the actual message system.  
The TestMessageRouter can also configured to throw exceptions, if you want to verify that the code behaves correctly when messages cannot be 
delivered to the message system; 
