## HTTP status codes are standard response codes given by web servers in response to requests made to them. They help identify the status of the request and inform the client about the success, failure, or further action needed. Here's a comprehensive list of HTTP status codes along with explanations:

### 1xx: Informational
100 Continue: Indicates that the initial part of a request has been received and the client should continue with the rest of the request.
101 Switching Protocols: Sent in response to an Upgrade request header from the client, and indicates the protocol the server is switching to.
102 Processing (WebDAV): Indicates that the server has received and is processing the request, but no response is available yet.

### 2xx: Success
200 OK: The request was successful, and the response body contains the representation requested.
201 Created: The request has been fulfilled, and a new resource has been created.
202 Accepted: The request has been accepted for processing, but the processing is not complete.
203 Non-Authoritative Information: The request was successful but the enclosed payload has been modified from that of the origin server's 200 OK response by a transforming proxy.
204 No Content: The server successfully processed the request, but is not returning any content.
205 Reset Content: The server successfully processed the request, but is not returning any content, and requires that the requester reset the document view.
206 Partial Content: The server is delivering only part of the resource due to a range header sent by the client.

### 3xx: Redirection
300 Multiple Choices: Indicates multiple options for the resource from which the client may choose.
301 Moved Permanently: This and all future requests should be directed to the given URI.
302 Found: Tells the client to look at another URL. It has been superseded by 303 and 307 but is still used by some web frameworks.
303 See Other: The response to the request can be found under another URI using a GET method.
304 Not Modified: Indicates that the resource has not been modified since the version specified by the request headers.
305 Use Proxy: The requested resource must be accessed through the proxy given by the Location field.
307 Temporary Redirect: The request should be repeated with another URI, but future requests should still use the original URI.
308 Permanent Redirect: The request and all future requests should be repeated using another URI.

### 4xx: Client Errors
400 Bad Request: The server could not understand the request due to invalid syntax.
401 Unauthorized: The client must authenticate itself to get the requested response.
402 Payment Required: Reserved for future use. Intended for use in digital payment systems.
403 Forbidden: The client does not have access rights to the content.
404 Not Found: The server can not find the requested resource.
405 Method Not Allowed: The request method is known by the server but is not supported by the target resource.
406 Not Acceptable: The server cannot produce a response matching the list of acceptable values defined in the request's headers.
407 Proxy Authentication Required: The client must first authenticate itself with the proxy.
408 Request Timeout: The server would like to shut down this unused connection.
409 Conflict: The request conflicts with the current state of the server.
410 Gone: The resource requested is no longer available and will not be available again.
411 Length Required: The request did not specify the length of its content, which is required by the requested resource.
412 Precondition Failed: The server does not meet one of the preconditions that the requester put on the request header fields.
413 Payload Too Large: The request is larger than the server is willing or able to process.
414 URI Too Long: The URI provided was too long for the server to process.
415 Unsupported Media Type: The media format of the requested data is not supported by the server.
416 Range Not Satisfiable: The range specified by the Range header field in the request cannot be fulfilled.
417 Expectation Failed: The server cannot meet the requirements of the Expect request-header field.
418 I'm a teapot: Defined in 1998 as an April Fools' joke in RFC 2324, Hyper Text Coffee Pot Control Protocol.
421 Misdirected Request: The request was directed at a server that is not able to produce a response.
422 Unprocessable Entity (WebDAV): The request was well-formed but was unable to be followed due to semantic errors.
423 Locked (WebDAV): The resource that is being accessed is locked.
424 Failed Dependency (WebDAV): The request failed due to failure of a previous request.
425 Too Early: Indicates that the server is unwilling to risk processing a request that might be replayed.
426 Upgrade Required: The client should switch to a different protocol such as TLS/1.0.
428 Precondition Required: The origin server requires the request to be conditional.
429 Too Many Requests: The user has sent too many requests in a given amount of time ("rate limiting").
431 Request Header Fields Too Large: The server is unwilling to process the request because its header fields are too large.
451 Unavailable For Legal Reasons: The user requested a resource that cannot be legally provided, such as a web page censored by a government.

### 5xx: Server Errors
500 Internal Server Error: The server has encountered a situation it doesn't know how to handle.
501 Not Implemented: The request method is not supported by the server and cannot be handled.
502 Bad Gateway: The server, while acting as a gateway or proxy, received an invalid response from the upstream server.
503 Service Unavailable: The server is not ready to handle the request.
504 Gateway Timeout: The server, while acting as a gateway or proxy, did not get a response in time from the upstream server.
505 HTTP Version Not Supported: The HTTP version used in the request is not supported by the server.
506 Variant Also Negotiates: The server has an internal configuration error.
507 Insufficient Storage (WebDAV): The server is unable to store the representation needed to complete the request.
508 Loop Detected (WebDAV): The server detected an infinite loop while processing a request.
510 Not Extended: Further extensions to the request are required for the server to fulfill it.
511 Network Authentication Required: The client needs to authenticate to gain network access.

### Usage Examples
Here’s how you might use these status codes in a Spring Boot application:

Example: Returning a 200 OK Status


  @GetMapping("/products")
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return ResponseEntity.ok(products); // 200 OK
 }

 Example: Returning a 201 Created Status

  @PostMapping("/products")
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product createdProduct = productService.saveProduct(product);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(createdProduct.getId()).toUri();
    return ResponseEntity.created(location).body(createdProduct); // 201 Created
 }


 Example: Returning a 404 Not Found Status

 @GetMapping("/products/{id}")
 public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    Product product = productService.getProductById(id);
    if (product == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
    }
    return ResponseEntity.ok(product); // 200 OK
 }


Example: Returning a 400 Bad Request Status



@PostMapping("/products")
public ResponseEntity<String> createProduct(@RequestBody Product product) {
    if (product.getPrice() <= 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Price must be greater than 0"); // 400 Bad Request
    }
    Product createdProduct = productService.saveProduct(product);
    return ResponseEntity.ok("Product created successfully"); // 200 OK
}



Example: Returning a 500 Internal Server Error Status


@GetMapping("/products")
public ResponseEntity<List<Product>> getAllProducts() {
    try {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products); // 200 OK
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
    }
 }





