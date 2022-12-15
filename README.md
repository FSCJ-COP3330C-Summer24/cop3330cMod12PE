# cop3330cMod12GPA

You may use a DBMS of your choice to complete this assignment.

Create an application which uses JDBC to create a Pet Store database with an Inventory table.

The table must contain the following fields with the specified data types:

    ProductID (alpha-numeric, 6 characters, primary key, not null)  
    Category (integer, not null)  
    WholesaleCost (money -- use decimal(10,4), not null)  
    RetailCost (money -- use decimal(10,4), not null)  
    InStock (integer, not null)  
    Description (alpha-numeric, 80 characters)  

Insert two records with product data of your choosing (I used "Dog Collars" and "10lb Dry Cat Food").

After inserting the records, execute a query against the table and display the resulting data.

Once the data is displayed, delete the table and drop the database.
