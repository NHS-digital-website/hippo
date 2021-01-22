Use this endpoint to poll for the outcome of an updated patient record.
When updating resources the request is dealt with in an asynchronous manner.
Once the initial request is accepted a `202` response will be returned with a `Content-Location` header, use this location to poll for the outcome.
It will be in the form `/Polling/<message id>`.
