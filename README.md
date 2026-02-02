# Banking System - Hibernate Sample

This project demonstrates a banking system using Hibernate ORM with Oracle database.

## Project Structure

```
HibernateSample/
├── pom.xml                          # Maven configuration
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── banking/
│       │           ├── entity/      # Entity classes (mapped to database tables)
│       │           │   ├── RefNotificationChannel.java
│       │           │   ├── KycProfile.java
│       │           │   ├── Customer.java
│       │           │   ├── Account.java
│       │           │   ├── BankTransaction.java
│       │           │   └── AccountSubscription.java
│       │           ├── util/
│       │           │   └── HibernateUtil.java
│       │           └── App.java     # Main application
│       └── resources/
│           └── hibernate.cfg.xml    # Hibernate configuration
```

## Database Schema

The application maps to the following Oracle database tables:

1. **ref_notification_channels** - Reference data for notification channels (SMS, Email, WhatsApp)
2. **kyc_profiles** - KYC profile information with PAN numbers
3. **customers** - Customer details with one-to-one relationship to KYC profiles
4. **accounts** - Bank accounts with optimistic locking and many-to-one relationship to customers
5. **bank_transactions** - Transaction ledger with many-to-one relationship to accounts
6. **account_subscriptions** - Join table for many-to-many relationship between accounts and notification channels

## Entity Relationships

- **Customer ↔ KycProfile**: One-to-One
- **Account ↔ Customer**: Many-to-One
- **BankTransaction ↔ Account**: Many-to-One
- **Account ↔ RefNotificationChannel**: Many-to-Many (through account_subscriptions)

## Features

- JPA/Hibernate annotations for ORM mapping
- Optimistic locking on Account entity using @Version
- Proper handling of relationships (One-to-One, One-to-Many, Many-to-Many)
- SessionFactory management through HibernateUtil
- Oracle database connectivity

## How to Build

```bash
mvn clean compile
```

## How to Run

```bash
mvn exec:java -Dexec.mainClass="com.banking.App"
```

## Database Configuration

Update the following properties in `src/main/resources/hibernate.cfg.xml`:
- hibernate.connection.url
- hibernate.connection.username
- hibernate.connection.password
- hibernate.default_schema

## Requirements

- JDK 1.8 or higher
- Maven 3.x
- Oracle Database with the required schema created
- Oracle JDBC driver (ojdbc6)

## Sample Data Required

Before running the application, ensure the following sample data exists in your database:

```sql
-- Notification Channels
INSERT INTO ref_notification_channels (channel_name, monthly_cost) VALUES ('SMS_ALERT', 1.00);
INSERT INTO ref_notification_channels (channel_name, monthly_cost) VALUES ('EMAIL_STMT', 0.00);
INSERT INTO ref_notification_channels (channel_name, monthly_cost) VALUES ('WHATSAPP_OTP', 2.50);
```

## Main Application (App.java)

The App.java demonstrates:
1. Retrieving all notification channels
2. Retrieving all KYC profiles
3. Retrieving all customers with their KYC details
4. Retrieving all accounts with subscriptions
5. Retrieving all bank transactions ordered by date
