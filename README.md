This is a fairly generic message sending framework with a pluggable abstraction for new message systems. 
There are currently two message system implementations SQS/SNS. There is also a Persistence decoratior implementation, which offers recilience against network issues and crashes, when you need to ensure that you domain change are persisted AND an outgoing message is delivered.

The framework uses spring boot autoconfiguration system, to initialize the framework.
