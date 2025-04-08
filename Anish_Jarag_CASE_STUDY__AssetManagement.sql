create database assetmanagement;
use assetmanagement;

create table employees (
    employee_id int auto_increment primary key,
    name varchar(25) not null,
    department varchar(20),
    email varchar(100) unique not null,
    password varchar(255) not null
);

create table assets (
    asset_id int primary key auto_increment,
    name varchar(25) not null,
    type varchar(25) not null,
    serial_number varchar(50) unique not null,
    purchase_date date,
    location varchar(30),
    status enum('in_use', 'decommissioned', 'under_maintenance'),
    owner_id int,
    foreign key (owner_id) references employees(employee_id) on delete set null
);

create table maintenance_records (
    maintenance_id int primary key auto_increment,
    asset_id int,
    maintenance_date date not null,
    description varchar(50),
    cost decimal(9,2),
    foreign key (asset_id) references assets(asset_id) on delete cascade
);

create table asset_allocations (
    allocation_id int primary key auto_increment,
    asset_id int,
    employee_id int,
    allocation_date datetime not null,
    return_date datetime,
    foreign key (asset_id) references assets(asset_id) on delete cascade,
    foreign key (employee_id) references employees(employee_id) on delete cascade
);

create table reservations (
    reservation_id int primary key auto_increment,
    asset_id int,
    employee_id int,
    reservation_date date not null,
    start_date date not null,
    end_date date not null,
    status enum('pending', 'approved', 'canceled') not null,
    foreign key (asset_id) references assets(asset_id) on delete cascade,
    foreign key (employee_id) references employees(employee_id) on delete cascade
);

-- Employees  
insert into employees (name, department, email, password) values  
('Anish Jarag', 'IT', 'anish@gmail.com', 'pass1'),  
('Aditya More', 'HR', 'aditya@gmail.com', 'pass2'),  
('Siddharth Patil', 'Finance', 'siddharth@gmail.com', 'pass3'),  
('Tanmay Kulkarni', 'IT', 'tanmay@gmail.com', 'pass4'),  
('Pranav Deshmukh', 'Operations', 'pranav@gmail.com', 'pass5');  

insert into employees (name, department, email, password) values  
('Rohit Jadhav', 'Admin', 'rohit@gmail.com', 'pass6'),  
('Omkar Pawar', 'IT', 'omkar@gmail.com', 'pass7'),  
('Tejas Ghadge', 'HR', 'tejas@gmail.com', 'pass8'),  
('Saurabh Khot', 'Finance', 'saurabh@gmail.com', 'pass9'),  
('Yash Ghorpade', 'Operations', 'yash@gmail.com', 'pass10');  

select * from employees;

-- Assets  
insert into assets (name, type, serial_number, purchase_date, location, status, owner_id) values  
('MacBook Pro', 'Laptop', 'SN1011', '2023-01-15', 'Office A', 'in_use', 1),  
('Dell XPS', 'Laptop', 'SN1012', '2023-02-20', 'Office B', 'under_maintenance', 2),  
('Canon Printer', 'Printer', 'SN2011', '2022-08-10', 'Office C', 'in_use', 3),  
('Epson Scanner', 'Scanner', 'SN3011', '2022-05-12', 'Office D', 'decommissioned', 4),  
('BenQ Projector', 'Projector', 'SN4011', '2021-11-05', 'Conference Room', 'in_use', 5);  

insert into assets (name, type, serial_number, purchase_date, location, status, owner_id) values  
('TP-Link Router', 'Networking', 'SN5011', '2023-06-30', 'Server Room', 'in_use', 6),  
('LG UltraFine Monitor', 'Monitor', 'SN6011', '2022-07-14', 'Office A', 'under_maintenance', 7),  
('iPad Pro', 'Tablet', 'SN7011', '2023-04-25', 'Office B', 'in_use', 8),  
('HP Server', 'Server', 'SN8011', '2021-09-18', 'Data Center', 'decommissioned', 9),  
('Sony DSLR', 'Camera', 'SN9011', '2023-03-22', 'Security Room', 'in_use', 10);  

select * from assets;

-- Maintenance Records  
insert into maintenance_records (asset_id, maintenance_date, description, cost) values  
(2, '2024-01-05', 'Battery Replacement', 120.00),  
(3, '2023-12-18', 'Toner Refill', 30.50),  
(7, '2024-02-14', 'Screen Replacement', 200.00),  
(4, '2023-11-10', 'General Maintenance', 50.00),  
(5, '2023-10-25', 'Firmware Update', 0.00);  

insert into maintenance_records (asset_id, maintenance_date, description, cost) values  
(6, '2023-08-12', 'Power Supply Fix', 85.75),  
(8, '2023-09-30', 'Battery Replacement', 100.00),  
(9, '2023-07-05', 'Hard Drive Swap', 250.00),  
(10, '2023-06-15', 'Lens Cleaning', 15.00),  
(1, '2023-05-22', 'Keyboard Replacement', 45.00); 

select * from maintenance_records; 

-- Asset Allocations  
insert into asset_allocations (asset_id, employee_id, allocation_date, return_date) values  
(1, 1, '2024-03-01', null),  
(2, 2, '2024-02-28', null),  
(3, 3, '2024-02-20', null),  
(4, 4, '2024-02-15', '2024-03-10'),  
(5, 5, '2024-02-10', null);  

insert into asset_allocations (asset_id, employee_id, allocation_date, return_date) values  
(6, 6, '2024-01-25', '2024-02-28'),  
(7, 7, '2024-01-15', null),  
(8, 8, '2024-01-05', null),  
(9, 9, '2023-12-20', '2024-01-10'),  
(10, 10, '2023-12-10', null);  

select * from asset_allocations;

-- Reservations  
insert into reservations (asset_id, employee_id, reservation_date, start_date, end_date, status) values  
(1, 1, '2024-02-01', '2024-02-05', '2024-02-10', 'approved'),  
(2, 2, '2024-02-10', '2024-02-15', '2024-02-20', 'pending'),  
(3, 3, '2024-02-20', '2024-02-25', '2024-03-01', 'approved'),  
(4, 4, '2024-01-30', '2024-02-02', '2024-02-07', 'canceled'),  
(5, 5, '2024-01-20', '2024-01-25', '2024-01-30', 'approved');  

insert into reservations (asset_id, employee_id, reservation_date, start_date, end_date, status) values  
(6, 6, '2024-01-10', '2024-01-15', '2024-01-20', 'pending'),  
(7, 7, '2024-01-05', '2024-01-10', '2024-01-15', 'approved'),  
(8, 8, '2023-12-30', '2024-01-05', '2024-01-12', 'canceled'),  
(9, 9, '2023-12-20', '2023-12-25', '2024-01-01', 'approved'),  
(10, 10, '2023-12-10', '2023-12-15', '2023-12-22', 'pending');  

select * from reservations;


