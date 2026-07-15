package com.ngo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;

/*
 * Database Initializer
 * Drops and recreates all tables, then seeds demo data
 */
@Component
public class DBInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("[DBInitializer] Starting database initialization...");
        try (Connection con = DBUtil.getConnection(); Statement st = con.createStatement()) {

            // Disable FK checks for clean drop
            st.execute("SET FOREIGN_KEY_CHECKS=0");

            // Drop all tables in reverse FK order
            st.execute("DROP TABLE IF EXISTS beneficiary_donations");
            st.execute("DROP TABLE IF EXISTS beneficiary_allocations");
            st.execute("DROP TABLE IF EXISTS beneficiary_requests");
            st.execute("DROP TABLE IF EXISTS notifications");
            st.execute("DROP TABLE IF EXISTS reports");
            st.execute("DROP TABLE IF EXISTS delivery_tracking");
            st.execute("DROP TABLE IF EXISTS audit_logs");
            st.execute("DROP TABLE IF EXISTS fund_expenditures");
            st.execute("DROP TABLE IF EXISTS ngo_inventory");
            st.execute("DROP TABLE IF EXISTS material_donations");
            st.execute("DROP TABLE IF EXISTS material_requests");
            st.execute("DROP TABLE IF EXISTS donations");
            st.execute("DROP TABLE IF EXISTS fund_requests");
            st.execute("DROP TABLE IF EXISTS beneficiaries");
            st.execute("DROP TABLE IF EXISTS users");

            st.execute("SET FOREIGN_KEY_CHECKS=1");

            // Create tables
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(100), " +
                "email VARCHAR(100) UNIQUE, " +
                "password VARCHAR(100), " +
                "role VARCHAR(20), " +
                "created_date DATE)");

            st.execute("CREATE TABLE IF NOT EXISTS beneficiaries (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "user_id INT, " +
                "ngo_id INT, " +
                "verification_status VARCHAR(20) DEFAULT 'PENDING', " +
                "contact_details VARCHAR(100), " +
                "address TEXT, " +
                "family_details TEXT, " +
                "income_info VARCHAR(100), " +
                "FOREIGN KEY(user_id) REFERENCES users(id), " +
                "FOREIGN KEY(ngo_id) REFERENCES users(id))");

            st.execute("CREATE TABLE IF NOT EXISTS fund_requests (" +
                "request_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "ngo_id INT, " +
                "title VARCHAR(200), " +
                "description TEXT, " +
                "category VARCHAR(50), " +
                "target_amount DOUBLE DEFAULT 0, " +
                "collected_amount DOUBLE DEFAULT 0, " +
                "status VARCHAR(20) DEFAULT 'OPEN', " +
                "created_date DATE, " +
                "FOREIGN KEY(ngo_id) REFERENCES users(id))");

            st.execute("CREATE TABLE IF NOT EXISTS donations (" +
                "donation_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "donor_id INT, " +
                "request_id INT, " +
                "amount DOUBLE, " +
                "payment_status VARCHAR(20) DEFAULT 'SUCCESS', " +
                "donation_status VARCHAR(20) DEFAULT 'COMPLETED', " +
                "donation_date DATE, " +
                "FOREIGN KEY(donor_id) REFERENCES users(id), " +
                "FOREIGN KEY(request_id) REFERENCES fund_requests(request_id))");

            st.execute("CREATE TABLE IF NOT EXISTS material_requests (" +
                "request_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "ngo_id INT, " +
                "item_name VARCHAR(200), " +
                "category VARCHAR(50), " +
                "description TEXT, " +
                "quantity_needed INT, " +
                "quantity_received INT DEFAULT 0, " +
                "delivery_location VARCHAR(200), " +
                "deadline DATE, " +
                "status VARCHAR(20) DEFAULT 'OPEN', " +
                "created_date DATE, " +
                "FOREIGN KEY(ngo_id) REFERENCES users(id))");

            st.execute("CREATE TABLE IF NOT EXISTS material_donations (" +
                "donation_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "donor_id INT, " +
                "request_id INT, " +
                "item_name VARCHAR(200), " +
                "quantity INT, " +
                "damaged_quantity INT DEFAULT 0, " +
                "courier_name VARCHAR(100), " +
                "tracking_number VARCHAR(100), " +
                "expected_delivery_date DATE, " +
                "delivery_status VARCHAR(30) DEFAULT 'NOT_STARTED', " +
                "receipt_status VARCHAR(20) DEFAULT 'PENDING', " +
                "donation_date DATE, " +
                "FOREIGN KEY(donor_id) REFERENCES users(id), " +
                "FOREIGN KEY(request_id) REFERENCES material_requests(request_id))");

            st.execute("CREATE TABLE IF NOT EXISTS fund_expenditures (" +
                "expenditure_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "ngo_id INT, " +
                "request_id INT, " +
                "amount DOUBLE, " +
                "description TEXT, " +
                "proof_document VARCHAR(255), " +
                "spent_date DATE, " +
                "status VARCHAR(20) DEFAULT 'PENDING', " +
                "FOREIGN KEY(ngo_id) REFERENCES users(id), " +
                "FOREIGN KEY(request_id) REFERENCES fund_requests(request_id))");

            st.execute("CREATE TABLE IF NOT EXISTS ngo_inventory (" +
                "inventory_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "ngo_id INT, " +
                "item_name VARCHAR(200), " +
                "category VARCHAR(50), " +
                "available_qty INT DEFAULT 0, " +
                "reserved_qty INT DEFAULT 0, " +
                "distributed_qty INT DEFAULT 0, " +
                "damaged_qty INT DEFAULT 0, " +
                "FOREIGN KEY(ngo_id) REFERENCES users(id))");

            st.execute("CREATE TABLE IF NOT EXISTS beneficiary_requests (" +
                "request_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "beneficiary_id INT, " +
                "assistance_category VARCHAR(50), " +
                "item_name VARCHAR(200), " +
                "requested_amount DOUBLE, " +
                "requested_quantity INT, " +
                "reason TEXT, " +
                "urgency VARCHAR(20) DEFAULT 'NORMAL', " +
                "status VARCHAR(30) DEFAULT 'PENDING', " +
                "created_date DATE, " +
                "FOREIGN KEY(beneficiary_id) REFERENCES beneficiaries(id))");

            st.execute("CREATE TABLE IF NOT EXISTS beneficiary_allocations (" +
                "allocation_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "ngo_id INT, " +
                "beneficiary_request_id INT, " +
                "allocation_type VARCHAR(20), " +
                "allocated_amount DOUBLE, " +
                "allocated_quantity INT, " +
                "distribution_status VARCHAR(30) DEFAULT 'ASSIGNED', " +
                "otp_pin VARCHAR(20), " +
                "allocation_date DATE, " +
                "FOREIGN KEY(ngo_id) REFERENCES users(id), " +
                "FOREIGN KEY(beneficiary_request_id) REFERENCES beneficiary_requests(request_id))");

            st.execute("CREATE TABLE IF NOT EXISTS audit_logs (" +
                "log_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "admin_id INT, " +
                "action VARCHAR(100), " +
                "details TEXT, " +
                "target_id INT, " +
                "target_type VARCHAR(50), " +
                "created_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(admin_id) REFERENCES users(id))");

            st.execute("CREATE TABLE IF NOT EXISTS delivery_tracking (" +
                "tracking_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "donation_id INT, " +
                "donation_type VARCHAR(20), " +
                "status VARCHAR(30), " +
                "notes TEXT, " +
                "updated_date DATETIME DEFAULT CURRENT_TIMESTAMP)");

            st.execute("CREATE TABLE IF NOT EXISTS beneficiary_donations (" +
                "donation_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "donor_id INT, " +
                "beneficiary_id INT, " +
                "amount DOUBLE, " +
                "purpose TEXT, " +
                "donation_date DATE, " +
                "FOREIGN KEY(donor_id) REFERENCES users(id), " +
                "FOREIGN KEY(beneficiary_id) REFERENCES beneficiaries(id))");

            st.execute("CREATE TABLE IF NOT EXISTS notifications (" +
                "notification_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "user_id INT, " +
                "title VARCHAR(200), " +
                "message TEXT, " +
                "is_read BOOLEAN DEFAULT FALSE, " +
                "created_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES users(id))");

            st.execute("CREATE TABLE IF NOT EXISTS reports (" +
                "report_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "ngo_id INT, " +
                "title VARCHAR(200), " +
                "content TEXT, " +
                "report_type VARCHAR(50), " +
                "created_date DATE, " +
                "FOREIGN KEY(ngo_id) REFERENCES users(id))");

            System.out.println("[DBInitializer] All tables created.");

            // Seed Users
            st.execute("INSERT INTO users (id,name,email,password,role,created_date) VALUES " +
                "(1,'System Admin','admin@donation.org','admin123','ADMIN','2026-07-01')," +
                "(2,'Hope Foundation','ngo1@gmail.com','123','NGO','2026-07-02')," +
                "(3,'Care India','ngo2@gmail.com','123','NGO','2026-07-02')," +
                "(4,'Relief Trust','ngo3@gmail.com','123','NGO','2026-07-02')," +
                "(5,'Rahul Kumar','donor1@gmail.com','123','DONOR','2026-07-03')," +
                "(6,'Priya Sharma','donor2@gmail.com','123','DONOR','2026-07-03')," +
                "(7,'Amit Singh','donor3@gmail.com','123','DONOR','2026-07-03')," +
                "(8,'Neha Gupta','donor4@gmail.com','123','DONOR','2026-07-03')," +
                "(9,'Vikram Patel','donor5@gmail.com','123','DONOR','2026-07-03')," +
                "(10,'John Doe','ben1@gmail.com','123','BENEFICIARY','2026-07-04')," +
                "(11,'Mary Jane','ben2@gmail.com','123','BENEFICIARY','2026-07-04')," +
                "(12,'Suresh Kumar','ben3@gmail.com','123','BENEFICIARY','2026-07-04')," +
                "(13,'Lakshmi Devi','ben4@gmail.com','123','BENEFICIARY','2026-07-04')," +
                "(14,'Ravi Shankar','ben5@gmail.com','123','BENEFICIARY','2026-07-04')," +
                "(15,'Anita Roy','ben6@gmail.com','123','BENEFICIARY','2026-07-04')," +
                "(16,'Mohan Das','ben7@gmail.com','123','BENEFICIARY','2026-07-04')," +
                "(17,'Ram Prasad','ben8@gmail.com','123','BENEFICIARY','2026-07-04')");

            // Seed Beneficiaries
            st.execute("INSERT INTO beneficiaries (id,user_id,ngo_id,verification_status,contact_details,address,family_details,income_info) VALUES " +
                "(1,10,2,'VERIFIED','9876543210','Delhi, India','4 members','Below poverty line')," +
                "(2,11,2,'VERIFIED','9876543211','Mumbai, India','3 members','Daily wage worker')," +
                "(3,12,3,'PENDING','9876543212','Chennai, India','5 members','Unemployed')," +
                "(4,13,3,'VERIFIED','9876543213','Kolkata, India','2 members','BPL card holder')," +
                "(5,14,4,'PENDING','9876543214','Hyderabad, India','6 members','Small farmer')," +
                "(6,15,4,'VERIFIED','9876543215','Pune, India','4 members','Widow, no income')," +
                "(7,16,2,'REJECTED','9876543216','Jaipur, India','3 members','Low income')," +
                "(8,17,3,'VERIFIED','9876543217','Lucknow, India','5 members','Below poverty line')");

            // Seed Fund Requests (10)
            st.execute("INSERT INTO fund_requests (request_id,ngo_id,title,description,category,target_amount,collected_amount,status,created_date) VALUES " +
                "(1,2,'Flood Relief Fund','Emergency fund for flood victims in Kerala','Disaster',100000,75000,'OPEN','2026-07-05')," +
                "(2,2,'Education Scholarship','Fund for underprivileged students in Delhi','Education',50000,50000,'COMPLETED','2026-07-06')," +
                "(3,3,'Medical Aid Campaign','Medicines and treatment for poor patients','Medical',80000,45000,'OPEN','2026-07-07')," +
                "(4,3,'Food Drive 2026','Monthly food supply for 100 families','Food',30000,30000,'COMPLETED','2026-07-08')," +
                "(5,4,'Winter Relief','Blankets and warm clothes for homeless','Disaster',40000,20000,'OPEN','2026-07-09')," +
                "(6,4,'School Supplies','Notebooks and stationery for rural schools','Education',25000,10000,'OPEN','2026-07-10')," +
                "(7,2,'COVID Relief','Medical support for COVID affected families','Medical',60000,60000,'CLOSED','2026-07-05')," +
                "(8,3,'Drought Relief','Water and food for drought affected villages','Disaster',70000,35000,'OPEN','2026-07-11')," +
                "(9,4,'Skill Training','Vocational training for unemployed youth','Other',45000,15000,'OPEN','2026-07-12')," +
                "(10,2,'Child Nutrition','Nutrition program for malnourished children','Food',55000,55000,'COMPLETED','2026-07-06')");

            // Seed Material Requests (10)
            st.execute("INSERT INTO material_requests (request_id,ngo_id,item_name,category,description,quantity_needed,quantity_received,delivery_location,deadline,status,created_date) VALUES " +
                "(1,2,'Blankets','Clothing','Winter blankets for homeless shelter',200,150,'Hope Foundation Warehouse, Delhi','2026-08-01','OPEN','2026-07-05')," +
                "(2,2,'Rice (50kg bags)','Food','Food supply for monthly distribution',100,100,'Hope Foundation Storage','2026-07-31','COMPLETED','2026-07-06')," +
                "(3,3,'School Notebooks','Education','Notebooks for rural school children',500,200,'Care India School Center','2026-08-15','OPEN','2026-07-07')," +
                "(4,3,'Medicines Kit','Medical','Basic medicine kits for health camp',150,150,'Care India Medical Center','2026-07-25','COMPLETED','2026-07-08')," +
                "(5,4,'Warm Clothes','Clothing','Winter jackets and sweaters',300,100,'Relief Trust Depot','2026-08-10','OPEN','2026-07-09')," +
                "(6,4,'Cooking Oil','Food','Cooking oil 5L cans for families',200,0,'Relief Trust Store','2026-08-20','OPEN','2026-07-10')," +
                "(7,2,'Water Bottles','Other','Clean drinking water bottles',1000,500,'Hope Foundation Camp','2026-07-28','OPEN','2026-07-11')," +
                "(8,3,'Sanitary Kits','Medical','Hygiene kits for women and girls',250,250,'Care India Center','2026-07-20','COMPLETED','2026-07-12')," +
                "(9,4,'Stationery Set','Education','Pens, pencils, erasers for students',400,100,'Relief Trust School','2026-08-25','OPEN','2026-07-13')," +
                "(10,2,'First Aid Kits','Medical','First aid kits for flood relief',100,50,'Hope Foundation Field','2026-07-30','OPEN','2026-07-14')");

            // Seed Donations (25)
            st.execute("INSERT INTO donations (donation_id,donor_id,request_id,amount,payment_status,donation_status,donation_date) VALUES " +
                "(1,5,1,5000,'SUCCESS','COMPLETED','2026-07-06')," +
                "(2,5,2,10000,'SUCCESS','COMPLETED','2026-07-07')," +
                "(3,6,1,8000,'SUCCESS','COMPLETED','2026-07-07')," +
                "(4,6,3,7500,'SUCCESS','COMPLETED','2026-07-08')," +
                "(5,7,1,3000,'SUCCESS','COMPLETED','2026-07-08')," +
                "(6,7,4,5000,'SUCCESS','COMPLETED','2026-07-09')," +
                "(7,8,5,2500,'SUCCESS','COMPLETED','2026-07-09')," +
                "(8,8,6,1500,'SUCCESS','COMPLETED','2026-07-10')," +
                "(9,9,3,12000,'SUCCESS','COMPLETED','2026-07-10')," +
                "(10,9,7,15000,'SUCCESS','COMPLETED','2026-07-11')," +
                "(11,5,8,8000,'SUCCESS','COMPLETED','2026-07-11')," +
                "(12,6,9,3000,'SUCCESS','COMPLETED','2026-07-12')," +
                "(13,7,10,20000,'SUCCESS','COMPLETED','2026-07-12')," +
                "(14,8,2,25000,'SUCCESS','COMPLETED','2026-07-13')," +
                "(15,9,4,25000,'SUCCESS','COMPLETED','2026-07-13')," +
                "(16,5,5,5000,'SUCCESS','COMPLETED','2026-07-05')," +
                "(17,6,6,3500,'SUCCESS','COMPLETED','2026-07-06')," +
                "(18,7,1,4500,'SUCCESS','COMPLETED','2026-07-06')," +
                "(19,8,3,8500,'SUCCESS','COMPLETED','2026-07-07')," +
                "(20,9,8,7000,'SUCCESS','COMPLETED','2026-07-07')," +
                "(21,5,9,4000,'SUCCESS','COMPLETED','2026-07-08')," +
                "(22,6,10,15000,'SUCCESS','COMPLETED','2026-07-08')," +
                "(23,7,5,5000,'SUCCESS','COMPLETED','2026-07-09')," +
                "(24,8,6,500,'SUCCESS','COMPLETED','2026-07-09')," +
                "(25,9,1,6000,'SUCCESS','COMPLETED','2026-07-10')");

            // Seed Material Donations (15)
            st.execute("INSERT INTO material_donations (donation_id,donor_id,request_id,item_name,quantity,damaged_quantity,courier_name,tracking_number,expected_delivery_date,delivery_status,receipt_status,donation_date) VALUES " +
                "(1,5,1,'Blankets',50,0,'BlueDart','BD123456','2026-07-20','DELIVERED','RECEIVED','2026-07-10')," +
                "(2,6,1,'Blankets',30,2,'DTDC','DT789012','2026-07-22','DELIVERED','RECEIVED','2026-07-11')," +
                "(3,7,3,'School Notebooks',100,0,'India Post','IP345678','2026-07-25','IN_TRANSIT','PENDING','2026-07-12')," +
                "(4,8,5,'Warm Clothes',50,5,'FedEx','FX901234','2026-07-28','IN_TRANSIT','PENDING','2026-07-12')," +
                "(5,9,6,'Cooking Oil',40,0,'BlueDart','BD567890','2026-07-30','NOT_STARTED','PENDING','2026-07-13')," +
                "(6,5,7,'Water Bottles',200,0,'DTDC','DT234567','2026-07-18','DELIVERED','RECEIVED','2026-07-10')," +
                "(7,6,9,'Stationery Set',80,0,'India Post','IP678901','2026-08-05','NOT_STARTED','PENDING','2026-07-13')," +
                "(8,7,10,'First Aid Kits',25,1,'BlueDart','BD012345','2026-07-26','IN_TRANSIT','PENDING','2026-07-13')," +
                "(9,8,3,'School Notebooks',150,0,'DTDC','DT456789','2026-08-01','NOT_STARTED','PENDING','2026-07-14')," +
                "(10,9,1,'Blankets',70,3,'FedEx','FX345678','2026-07-24','DELIVERED','RECEIVED','2026-07-11')," +
                "(11,5,5,'Warm Clothes',80,0,'BlueDart','BD789012','2026-08-05','NOT_STARTED','PENDING','2026-07-14')," +
                "(12,6,7,'Water Bottles',300,0,'India Post','IP901234','2026-07-22','IN_TRANSIT','PENDING','2026-07-12')," +
                "(13,7,6,'Cooking Oil',60,0,'DTDC','DT123456','2026-08-02','NOT_STARTED','PENDING','2026-07-14')," +
                "(14,8,9,'Stationery Set',120,5,'BlueDart','BD678901','2026-08-10','NOT_STARTED','PENDING','2026-07-14')," +
                "(15,9,10,'First Aid Kits',25,0,'FedEx','FX234567','2026-07-28','IN_TRANSIT','PENDING','2026-07-13')");

            // Seed NGO Inventory (10)
            st.execute("INSERT INTO ngo_inventory (inventory_id,ngo_id,item_name,category,available_qty,reserved_qty,distributed_qty,damaged_qty) VALUES " +
                "(1,2,'Blankets','Clothing',150,20,80,5)," +
                "(2,2,'Rice 50kg','Food',80,10,50,2)," +
                "(3,2,'First Aid Kits','Medical',45,5,30,2)," +
                "(4,2,'Water Bottles','Other',400,50,200,10)," +
                "(5,3,'School Notebooks','Education',180,20,120,5)," +
                "(6,3,'Medicines Kit','Medical',120,15,80,3)," +
                "(7,3,'Sanitary Kits','Medical',200,10,150,5)," +
                "(8,4,'Warm Clothes','Clothing',80,10,30,5)," +
                "(9,4,'Cooking Oil 5L','Food',120,20,60,0)," +
                "(10,4,'Stationery Set','Education',80,15,40,5)");

            // Seed Beneficiary Requests (10)
            st.execute("INSERT INTO beneficiary_requests (request_id,beneficiary_id,assistance_category,item_name,requested_amount,requested_quantity,reason,urgency,status,created_date) VALUES " +
                "(1,1,'Food','Rice and Dal',0,20,'Family of 4, no income for 2 months','HIGH','APPROVED','2026-07-08')," +
                "(2,2,'Medical','Blood pressure medicines',2000,0,'Elderly person needs monthly medicine','NORMAL','APPROVED','2026-07-09')," +
                "(3,3,'Education','School books and fees',5000,0,'Children cannot attend school without fees','HIGH','PENDING','2026-07-10')," +
                "(4,4,'Clothing','Winter clothes',0,10,'No warm clothes for winter season','NORMAL','APPROVED','2026-07-10')," +
                "(5,5,'Food','Ration kit',0,15,'Drought affected family needs food','CRITICAL','PENDING','2026-07-11')," +
                "(6,6,'Financial','Emergency cash support',3000,0,'Medical emergency in family','CRITICAL','APPROVED','2026-07-11')," +
                "(7,1,'Medical','Diabetes medication',1500,0,'Chronic patient needs medicines','HIGH','PENDING','2026-07-12')," +
                "(8,2,'Education','School uniform',0,5,'Children need uniform to attend school','NORMAL','PENDING','2026-07-12')," +
                "(9,4,'Food','Cooking oil and spices',0,10,'Basic cooking supplies needed','NORMAL','APPROVED','2026-07-13')," +
                "(10,6,'Clothing','Sarees and clothes',0,8,'Flood washed away all clothes','HIGH','PENDING','2026-07-13')");

            // Seed Beneficiary Allocations (10)
            st.execute("INSERT INTO beneficiary_allocations (allocation_id,ngo_id,beneficiary_request_id,allocation_type,allocated_amount,allocated_quantity,distribution_status,otp_pin,allocation_date) VALUES " +
                "(1,2,1,'MATERIAL',0,20,'DELIVERED','1234','2026-07-09')," +
                "(2,2,2,'FINANCIAL',2000,0,'DELIVERED','5678','2026-07-10')," +
                "(3,3,3,'FINANCIAL',5000,0,'ASSIGNED','9012','2026-07-11')," +
                "(4,3,4,'MATERIAL',0,10,'DELIVERED','3456','2026-07-11')," +
                "(5,4,5,'MATERIAL',0,15,'ASSIGNED','7890','2026-07-12')," +
                "(6,4,6,'FINANCIAL',3000,0,'DELIVERED','2345','2026-07-12')," +
                "(7,2,7,'FINANCIAL',1500,0,'ASSIGNED','6789','2026-07-13')," +
                "(8,2,8,'MATERIAL',0,5,'ASSIGNED','0123','2026-07-13')," +
                "(9,3,9,'MATERIAL',0,10,'DELIVERED','4567','2026-07-14')," +
                "(10,4,10,'MATERIAL',0,8,'ASSIGNED','8901','2026-07-14')");

            // Seed Fund Expenditures (10)
            st.execute("INSERT INTO fund_expenditures (expenditure_id,ngo_id,request_id,amount,description,proof_document,spent_date,status) VALUES " +
                "(1,2,1,25000,'Purchased food and supplies for flood victims','receipt_001.pdf','2026-07-10','APPROVED')," +
                "(2,2,2,20000,'Paid scholarship to 20 students','scholarship_proof.pdf','2026-07-11','APPROVED')," +
                "(3,3,3,15000,'Bought medicines for health camp','medicine_bill.pdf','2026-07-12','APPROVED')," +
                "(4,3,4,10000,'Food distribution to 50 families','food_receipt.pdf','2026-07-12','APPROVED')," +
                "(5,4,5,8000,'Purchased blankets from market','blanket_invoice.pdf','2026-07-13','PENDING')," +
                "(6,4,6,5000,'Bought school supplies from store','stationery_bill.pdf','2026-07-13','PENDING')," +
                "(7,2,7,30000,'COVID medicine and oxygen supply','covid_receipts.pdf','2026-07-09','APPROVED')," +
                "(8,3,8,12000,'Water tanker and food for drought area','drought_proof.pdf','2026-07-14','PENDING')," +
                "(9,4,9,8000,'Training center rent and materials','training_receipt.pdf','2026-07-14','PENDING')," +
                "(10,2,10,25000,'Nutrition supplements for children','nutrition_bill.pdf','2026-07-11','APPROVED')");

            // Seed Audit Logs (10)
            st.execute("INSERT INTO audit_logs (log_id,admin_id,action,details,target_id,target_type,created_date) VALUES " +
                "(1,1,'USER_VERIFIED','Admin verified beneficiary user John Doe',1,'BENEFICIARY','2026-07-09 10:00:00')," +
                "(2,1,'EXPENDITURE_APPROVED','Admin approved fund expenditure of 25000',1,'EXPENDITURE','2026-07-10 11:00:00')," +
                "(3,1,'EXPENDITURE_APPROVED','Admin approved scholarship expenditure',2,'EXPENDITURE','2026-07-11 09:30:00')," +
                "(4,1,'USER_VERIFIED','Admin verified beneficiary Mary Jane',2,'BENEFICIARY','2026-07-09 14:00:00')," +
                "(5,1,'EXPENDITURE_APPROVED','Admin approved medicine expenditure',3,'EXPENDITURE','2026-07-12 10:00:00')," +
                "(6,1,'USER_REJECTED','Admin rejected beneficiary Mohan Das',7,'BENEFICIARY','2026-07-10 15:00:00')," +
                "(7,1,'EXPENDITURE_APPROVED','Admin approved food distribution expenditure',4,'EXPENDITURE','2026-07-12 11:00:00')," +
                "(8,1,'EXPENDITURE_APPROVED','Admin approved COVID relief expenditure',7,'EXPENDITURE','2026-07-09 16:00:00')," +
                "(9,1,'USER_VERIFIED','Admin verified beneficiary Lakshmi Devi',4,'BENEFICIARY','2026-07-10 09:00:00')," +
                "(10,1,'EXPENDITURE_APPROVED','Admin approved nutrition program expenditure',10,'EXPENDITURE','2026-07-11 10:00:00')");

            // Seed Delivery Tracking (10)
            st.execute("INSERT INTO delivery_tracking (tracking_id,donation_id,donation_type,status,notes,updated_date) VALUES " +
                "(1,1,'MATERIAL','DELIVERED','Package delivered successfully to Hope Foundation','2026-07-20 14:00:00')," +
                "(2,2,'MATERIAL','DELIVERED','Delivered with 2 damaged items reported','2026-07-22 16:00:00')," +
                "(3,3,'MATERIAL','IN_TRANSIT','Package in transit, expected 25 July','2026-07-18 10:00:00')," +
                "(4,4,'MATERIAL','IN_TRANSIT','Shipment picked up, en route','2026-07-19 11:00:00')," +
                "(5,5,'MATERIAL','NOT_STARTED','Awaiting courier pickup','2026-07-13 09:00:00')," +
                "(6,6,'MATERIAL','DELIVERED','Water bottles delivered on time','2026-07-18 15:00:00')," +
                "(7,7,'MATERIAL','NOT_STARTED','Courier not yet assigned','2026-07-13 10:00:00')," +
                "(8,8,'MATERIAL','IN_TRANSIT','Package in transit via BlueDart','2026-07-20 12:00:00')," +
                "(9,10,'MATERIAL','DELIVERED','70 blankets delivered, 3 damaged','2026-07-24 13:00:00')," +
                "(10,12,'MATERIAL','IN_TRANSIT','300 water bottles in transit','2026-07-21 11:00:00')");

            System.out.println("[DBInitializer] All seed data inserted successfully.");

        } catch (Exception e) {
            System.err.println("[DBInitializer] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
