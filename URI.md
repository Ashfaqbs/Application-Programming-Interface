### 1. **URI (Uniform Resource Identifier)**  
A **URI** is a broad concept used to identify a resource on the web. It can be further divided into **URL** (Uniform Resource Locator) and **URN** (Uniform Resource Name).

### 2. **URL (Uniform Resource Locator)**  
A **URL** is a specific type of URI that **not only identifies** a resource but also **provides the means to locate it** (e.g., the protocol, domain, and path).

**Example of a URL**:
```
https://www.example.com/products?id=123
```
- **Protocol**: `https` (tells the browser how to communicate with the server)
- **Domain**: `www.example.com` (the domain name of the server)
- **Path**: `/products` (location of the resource on the server)
- **Query Parameters**: `?id=123` (optional parameters to modify or filter the request)

A **URL** includes all the information needed to locate the resource on the internet.

### 3. **URN (Uniform Resource Name)**  
A **URN** is another type of URI, but unlike a URL, it **does not provide information on how to locate the resource**. Instead, a URN is used to **uniquely identify** a resource by name, typically within a specific namespace.

**Example of a URN**:
```
urn:isbn:0451450523
```
- This URN identifies a specific book by its **ISBN** number. However, it **does not** tell we where to find that book or how to retrieve it; it's just a unique name that identifies the book within the ISBN namespace.

### **How are URL and URN Types of URI?**

Both **URLs** and **URNs** are types of **URIs** because they **identify** a resource. However, the key difference lies in their purpose:

- **URLs** provide both identification **and location** of the resource.
- **URNs** only provide **unique identification**, without any locational information.

So, a **URI** can be either a URL (which tells we where to find the resource) or a URN (which just uniquely identifies the resource, but doesn't tell we how to find it).

### **Summary:**
- **URI**: A general identifier for a resource. It can be a URL or a URN.
- **URL**: A type of URI that identifies **and locates** a resource on the web.
- **URN**: A type of URI that only **identifies** a resource by name but doesn't provide location details.
