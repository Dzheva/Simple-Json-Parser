# Simple-Json-Parser

The **Simple-Json-Parser** is a tool that parses JSON files from any directory on your PC. It extracts data based on specified JSON nodes (attributes), processes the files, and generates detailed statistics for each attribute. The results are saved in an XML file for easy review and further analysis.

## Main Requirements
- **File Handling**: Avoid loading entire files into Java RAM, considering the potentially large number and size of files.
- **Parallel Parsing**: Use a thread pool to process files (each file in its own thread) and compare performance across 1, 2, 4, and 8 threads.

## Example Files

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

**Output XML file (statistics by brandName):**

```xml
<statistics>
  <item>
    <value>Samsung</value>
    <count>2880</count>
  </item>
  <item>
    <value>Lenovo</value>
    <count>2544</count>
  </item>
</statistics>
```

## How to Run

1. **Start the Application**:  
   Launch the program by executing the **Main** class.

2. **Provide Input**:  
   When prompted, enter the following:
   - **a)** Path to the folder containing the JSON files.
   - **b)** The attribute name to parse from the JSON files.
   - **c)** The number of threads to use for parallel processing.

3. **View the Results**:  
   After processing, the program will generate an XML file with the parsed statistics, saved in the `resources/xml_statistics` folder.

4. **Test with Sample Data**:  
   Test the program using sample JSON files located in the `resources/json_files` folder.

## My Results with `resources/json_files` by Attribute - `brandName`

| Number of Threads | Execution Time (ms) |
|-------------------|---------------------|
| 1                 | 1193                |
| 2                 | 790                 |
| 4                 | 598                 |
| 8                 | 494                 |

