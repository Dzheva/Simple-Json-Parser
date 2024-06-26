# Simple-Json-Parser
The "Simple-Json-Parser" project is a program for processing JSON files with information about orders and generating statistics on the specified attributes.

# Classes
1. **App**:

   - **Description**: This class launches the application into operation.

3. **Order**:

   - **Description**: This class is the main entity and contains information about the product, brand, price and customer.
        
        - **Attributes**:
          - `productType`: product type of the order.
          - `brandName`: product brand.
          - `price`: order price.
          - `clientName`: name of the client who made the order.

5. **XmlStatisticWriter**:

   - **Description**: This class is designed for convenient recording of statistics in XML format, with preliminary DESC sorting by the number of occurrences.

7. **MyParser**:

   - **Description**: This class is responsible for parsing JSON files, extracting order information, creating statistics based on the specified attributes, and generating XML files with statistics.

# Example files

**Input JSON file:**

```json
[
{
"productType": "Laptop",
"brandName": "HP",
"price": 1200,
"clientName": "John Doe"
},
{
"productType": "Smartphone",
"brandName": "Samsung",
"price": 800,
"clientName": "Jane Smith"
}
]
```

**Output XML file (statistics by product brand):**
```xml
<statistics>
<attributeCounts>
<entry>
<key>HP</key>
<value>1</value>
</entry>
<entry>
<key>Samsung</key>
<value>1</value>
</entry>
</attributeCounts>
</statistics>
```

# Results of experiments with different numbers of threads.
For optimal processing of large volumes of data, experiments were carried out with the number of threads during parallel file processing.
The results show that increasing the number of threads reduces the program execution time.

**Results**
| Number of threads | Execution time (ms)| 
|-------------|-------------|
|  1    |  740  |
|  2    |  10   | 
|  4    |  8    | 
|  8    |  9    | 
