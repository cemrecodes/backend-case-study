# backend-case-study
Backend Case Study for Carla

### Compensation Data API

This API provides access to compensation data with various filtering and sorting options.
Works on localhost:8080 when run locally.

#### Endpoints

1. **List Compensation Data**
   - **Description:** Retrieves a list of compensation data records.
   - **HTTP Method:** GET
   - **Query Parameters:**
     - `min_salary`: Minimum salary threshold (optional)
     - `max_salary`: Maximum salary threshold (optional)
     - `currency`: Currency of salary (optional)
     - `location`: Location of the employee (optional)
     - `industry`: Industry of the employee (optional)
     - `job_title`: Job title of the employee (optional)
     - `sortBy`: Field(s) to sort by (optional)
   - **Example Request:**
     ```
     GET http://localhost:8080/compensation_data?min_salary=50000&max_salary=100000&sortBy=salary
     ```
   - **Example Response:**
     ```json
     [
       {
         "jobTitle": "Software Engineer",
         "industry": "Technology",
         "location": "San Francisco, CA",
         "salary": 90000,
         "currency": "USD"
       },
       {
         "jobTitle": "Marketing Manager",
         "industry": "Marketing",
         "location": "New York, NY",
         "salary": 75000,
         "currency": "USD"
       }
     ]
     ```

2. **Fetch Single Record**
   - **Description:** Retrieves a single compensation data record by index.
   - **HTTP Method:** GET
   - **Path Parameter:**
     - `index`: Index of the record to fetch
   - **Query Parameters:**
     - `fields`: Fields to include in the response (optional)
   - **Example Request:**
     ```
     GET http://localhost:8080/compensation_data/0?fields=jobTitle,salary
     ```
   - **Example Response:**
     ```json
     {
       "jobTitle": "Software Engineer",
       "salary": 90000
     }
     ```

3. **Stretch Goals**
   - **Sparse Fieldset:** You can specify which fields to include in the response by using the `fields` query parameter with GET method with record index.
   - **Normalization:** The JSON response is normalized into a uniform schema, ensuring consistency across records.
   - **Serialize Multiple Data Sets:** The API supports serialization of more than one compensation data set.

#### Response Format
     ```json
    "jobtitle": "Senior Backend Developer",
    "location": "London, UK",
    "industry": "Technology",
    "currency": "GBP",
    "salary": 20000
    }
     ```
##### Screenshots

Screenshots from Postman testing.

**List Compensation Data**
![image](https://github.com/cemrecodes/backend-case-study/assets/83655146/7941255b-b85a-4d5f-8eff-5859f33e12da)

**Fetch Single Record**
![image](https://github.com/cemrecodes/backend-case-study/assets/83655146/79fd9ce3-595f-45b3-a5ea-78fd4984eecb)


