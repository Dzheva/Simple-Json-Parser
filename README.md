# Simple-Json-Parser
The "Simple-Json-Parser" project is a program for processing JSON files with information about orders and generating statistics on the specified attributes.

# Main Entity
**Order**

productType - product type

brandName - brand name

price - product price

clientName - client name

# Example files

**Input JSON file:**

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
   },
   ...
]

**Output XML file (statistics):**
<statistics>
     <attributeCounts>
         <entry>
             <key>Samsung</key>
             <value>5</value>
         </entry>
         <entry>
             <key>Lenovo</key>
             <value>3</value>
         </entry>
         <entry>
             <key>JBL</key>
             <value>2</value>
         </entry>
         <entry>
             <key>Bose</key>
             <value>1</value>
         </entry>
      ...
     </attributeCounts>
</statistics>

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
