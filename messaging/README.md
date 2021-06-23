This is an extendable messaging framework to publish events to message systems. There are currently two supported implementations SNS and SQS, but 
the framework should be generic enough to support most messaging paradigms.  

The framework consists of the following data classes:
* Destination: a type and a target. The type controls which messaging system (SNS/SQS), the target is an identifier that is converted to an API specific location (QueueURL/ArnTopic) later.
* Header: a header is metadata which is sent along with the event. In SNS this can be used to filter messages.
* Message: When an event is sent it is converted to a Message. This class is not important for users of the framework, only developers that want to implement support for additional messaging systems.

The framework consist of the following abstractions 
* EventPublisherFactory: Can create an EventPublisher for given destination. This is a Spring bean that should be wired into other beans.
* EventPublisher: Can send an event to a single destination. Not a Spring bean, should be created during a LifeCycle:start to comply with best practices for lifecycle, since it is possible 
that the framework want to do IO to verify that the destination actually exist, before the application is started.

The framework contains the following extension points, with a sensible default implementations for application to configure
* HeaderExtractor: When converting an event to a Message, this class is responsible for producing the headers, from the event payload.
* JsonConverter: The framework encodes events as Json, this class controls how.

The framework contains two abstractions that pluggable implementation must implement when supporting new message systems
* MesaageRouter: Handles the routing of messages for a given destination type (and adapter for the underlying messaging API)
* MessageRouterFactory: Creates the MessageRouter. These are automatically discovered by Spring when the framework starts.

For each implementation of a MessageRouter, there will typically be two additional abstractions, to allow customization of the adaption of the specific message API 
* A RequestBuilder: responsible for constructing the request that the router sends
* A (destination-target) Resolver: Resolve the more abstract destination-target to an API specific location, like a URL or a Topic 

In addition to supporting different messaging systems, the framework offers a persistence wrapper,to solve the following scenario:
If your system receives a HTTP request that modifies a domain entity and generates a message, you will need to update the database AND send a message, 
but how do you ensure that both things occur. If you update the database, there is a chance the message is not sent, and if you send the message first there 
is a chance the database is not updated. Regardless of which ordering you choose, the client calling the http endpoint will need to make request until 
both operations succeed, to ensure that the two resources (DB and Queue) is in a consistent state.   

The persistence extension allows you to solve this problem. It does this by inserting the message into a database inside the same transaction that modifies the 
domain entity, this ensures a single unit of work. Immediately after the message is inserted the framework will try to send it, if this fails, 
the framework has a re-send mechanism driven by a timer, which will try to deliver the message again until successful.

The persistence framework offers the following application abstractions, with sensible default implementation
* ResendCalculator: Calculates when a message is eligible for resending, this allows you to create backoff policies
* Dispatcher: Controls if the how messages are dispatched to the decorated messaging system. The default is asynchronous, so the calling code only waits for 
the transaction to finish, but not for the message to be sent, since this is 'guaranteed' to happen. 
* MessageRepository: a repository where the messages are stored, comes with a generic Jdbc implementation which should work for all databases, as long as 
the data types used in the database can be implicitly converted by the JdbcDriver.
* UndeliveredMessageLifecycleManager: Controls the lifecycle of the resend mechanism. If you run multiple instances of the same microservice you probably 
don't want all instances to run the resend logic, this lets you control when it is started. 
 


 
 

  
  