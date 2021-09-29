#UC 1
DROP database payroll_service;
CREATE DATABASE payroll_service;
USE payroll_service;

SELECT DATABASE();

CREATE TABLE employee_payroll (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    salary DOUBLE NOT NULL,
    start DATE NOT NULL,
    PRIMARY KEY (id)
);

#UC 3
INSERT INTO employee_payroll(name,salary,start) VALUES
		('Bill',1000000.00,'2018-01-03'),
        ('Terisa',2000000.00,'2019-11-29'),
        ('charlie',300000.00,'2020-05-21');
      


#UC 6
ALTER TABLE employee_payroll add gender char(1) after name;

UPDATE employee_payroll 
SET gender = 'F'
WHERE name = 'Terisa';

UPDATE employee_payroll 
SET gender = 'M'
WHERE name = 'Bill' OR name = 'Charlie';
    
UPDATE employee_payroll 
SET salary = 300000.00
WHERE name = 'Terisa';
    

#UC 8
ALTER TABLE employee_payroll add phone_number char(10) after name;
ALTER TABLE employee_payroll add address varchar(50) after phone_number;
ALTER TABLE employee_payroll add department varchar(10)  after address;
ALTER TABLE employee_payroll ALTER address set default 'ABC';


#UC 9
ALTER TABLE employee_payroll RENAME COLUMN salary TO basic_pay;
ALTER TABLE employee_payroll ADD deductions DOUBLE  AFTER basic_pay;
ALTER TABLE employee_payroll ADD taxable_pay DOUBLE  AFTER deductions;
ALTER TABLE employee_payroll ADD tax Double  AFTER taxable_pay;
ALTER TABLE employee_payroll ADD net_pay Double  AFTER tax;


UPDATE employee_payroll 
SET 
    phone_number = '9898787856',
    address = 'ghj',
    department = 'Marketting',
    deductions = '10000.00',
    taxable_pay = '2000.00',
    tax = '1000.00',
    net_pay = '3000000.00'
WHERE id = 1;

UPDATE employee_payroll 
SET 
    phone_number = '9778907856',
    address = 'ABC',
    deductions = '1000000.00',
    taxable_pay = '20000000.00',
    tax = '5000000.00',
    net_pay = '1500000.00'
WHERE
    id = 2;

UPDATE employee_payroll 
SET 
    phone_number = '9778907856',
    address = 'ttt',
    department = 'IT',
    deductions = '20000.00',
    taxable_pay = '1000.00',
    tax = '3000.00',
    net_pay = '2000000.00'
WHERE
    id = 3;


#UC 10
UPDATE employee_payroll 
SET department = 'Sales'
WHERE name = 'Terisa';

INSERT INTO employee_payroll(name,phone_number,department,gender,basic_pay,deductions,taxable_pay,tax,net_pay,start)
VALUES ('Terisa','9778907856','Marketting','F',3000000.00,1000000.00,20000000.00,5000000.00,1500000.00,'2019-11-29');

        


SELECT *
FROM employee_payroll
WHERE start BETWEEN '2018-01-02' AND '2019-11-30';

#UC 11
ALTER TABLE employee_payroll
DROP COLUMN address,
DROP COLUMN department,
DROP COLUMN deductions,
DROP COLUMN taxable_pay,
DROP COLUMN tax,
DROP COLUMN net_pay;


ALTER TABLE employee_payroll RENAME TO employee;
ALTER TABLE employee RENAME COLUMN basic_pay TO salary;

Delete from employee where id=4;

CREATE TABLE company 
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO company(name)
VALUES ('Reliance'),('Xiaomi');

ALTER TABLE employee add company_id INT UNSIGNED NOT NULL after start;
ALTER TABLE employee add is_active boolean  default true;


UPDATE employee 
SET company_id = 1
WHERE id=1;

UPDATE employee 
SET company_id = 2
WHERE id=2;

UPDATE employee 
SET company_id = 1
WHERE id=3;

CREATE TABLE department 
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO department(name)
VALUES ('Marketting'),('Sales'),('IT');

CREATE TABLE payroll 
(
    id INT UNSIGNED NOT NULL,
    basic_pay DOUBLE NOT NULL,
    deductions DOUBLE NOT NULL,
    taxable_pay DOUBLE NOT NULL,
    tax DOUBLE NOT NULL,
    net_pay DOUBLE NOT NULL,
	FOREIGN KEY (id) REFERENCES employee(id) on delete cascade
);

INSERT INTO payroll(id,basic_pay,deductions,taxable_pay,tax,net_pay)
VALUES (1,1000000,10000.00,2000.00,1000.00,3000000.00)
	  ,(2,3000000.00,1000000.00,20000000.00,5000000.00,1500000.00)
      ,(3,1000000,20000.00,1000.00,3000.00,2000000.00);

CREATE TABLE address 
(
	id INT UNSIGNED NOT NULL ,
    house_number VARCHAR(5) NOT NULL,
	street VARCHAR(20) NOT NULL,
	city VARCHAR(20) NOT NULL,
    state VARCHAR(20) NOT NULL,
    zip VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES employee(id) on delete cascade

);

INSERT INTO address(id,house_number,street ,city,state,zip)
VALUES(1,21,'AAA','Bangalore','Karnataka',12345)
	,(2,1,'tr','Mysore','Karnataka',45333)
	,(3,33,'green','Bangalore','Karnataka',23455);


CREATE TABLE employee_department 
(
    employee_id INT UNSIGNED NOT NULL,
    department_id INT UNSIGNED NOT NULL,
	FOREIGN KEY (department_id) REFERENCES department(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id)

);

INSERT INTO employee_department(employee_id,department_id)
VALUES (1,2),(2,1),(2,2),(3,3);




