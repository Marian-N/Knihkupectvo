alter table CUSTOMER
rename to Customers;
alter table Customers add column CustomerAddress varchar(255) not null;
alter table Customers add column CustomerZip varchar(15) not null;
alter table Customers add column CustomerCity varchar(50) not null;
alter table Customers add column CustomerState varchar(50) not null;

