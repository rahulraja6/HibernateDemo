# Hibernate CRUD and Mapping Examples

This directory contains comprehensive examples demonstrating Hibernate ORM operations and relationship mappings.

## Example Files

### 1. CRUDExamples.java
Complete demonstration of Create, Read, Update, and Delete operations.

**Run with:**
```bash
mvn exec:java -Dexec.mainClass="com.banking.examples.CRUDExamples"
```

**Features:**
- **CREATE**: Insert new records for all entities
- **READ**: Query data using HQL with various techniques
- **UPDATE**: Modify existing records and relationships
- **DELETE**: Remove records with proper handling

### 2. MappingExamples.java
Demonstrates different types of relationship mappings in Hibernate.

**Run with:**
```bash
mvn exec:java -Dexec.mainClass="com.banking.examples.MappingExamples"
```

**Mapping Types Covered:**

#### One-to-One Mapping
- **Customer ↔ KycProfile**
- Each customer has exactly one KYC profile
- Demonstrates bidirectional one-to-one relationship

#### One-to-Many Mapping
- **Customer → Multiple Accounts**
- One customer can have many accounts
- Shows how to navigate from parent to children

#### Many-to-One Mapping
- **Multiple Transactions → One Account**
- Many transactions belong to one account
- Demonstrates the inverse side of one-to-many

#### Many-to-Many Mapping
- **Accounts ↔ Notification Channels**
- Accounts can subscribe to multiple channels
- Channels can have multiple account subscribers
- Uses join table: `account_subscriptions`

### 3. QueryExamples.java
Advanced querying techniques in Hibernate.

**Run with:**
```bash
mvn exec:java -Dexec.mainClass="com.banking.examples.QueryExamples"
```

**Query Types:**
- **HQL Queries**: Basic to advanced HQL examples
- **Named Parameters**: Safe parameter binding
- **JOIN Queries**: Inner, outer, and fetch joins
- **Aggregate Queries**: COUNT, SUM, AVG, MIN, MAX, GROUP BY

## Entity Relationships

```
Customer (1) ←→ (1) KycProfile
    ↓
    | (1 to Many)
    ↓
Account (Many) → (1) Customer
    ↓
    | (1 to Many)
    ↓
BankTransaction (Many) → (1) Account

Account (Many) ←→ (Many) RefNotificationChannel
         (via account_subscriptions)
```

## Quick Start

1. **Ensure sequences are created** in your Oracle database:
   ```bash
   sqlplus username/password@database < create_sequences.sql
   ```

2. **Build the project:**
   ```bash
   mvn clean compile
   ```

3. **Run any example:**
   ```bash
   # CRUD operations
   mvn exec:java -Dexec.mainClass="com.banking.examples.CRUDExamples"
   
   # Relationship mappings
   mvn exec:java -Dexec.mainClass="com.banking.examples.MappingExamples"
   
   # Query examples
   mvn exec:java -Dexec.mainClass="com.banking.examples.QueryExamples"
   ```

## Key Concepts Demonstrated

### Transaction Management
```java
Session session = HibernateUtil.getSessionFactory().openSession();
Transaction transaction = session.beginTransaction();

try {
    // Your database operations
    session.save(entity);
    transaction.commit();
} catch (Exception e) {
    transaction.rollback();
} finally {
    session.close();
}
```

### Lazy Loading
Relationships are loaded lazily by default. Access them within an active session:
```java
Customer customer = session.get(Customer.class, 1L);
KycProfile kyc = customer.getKycProfile(); // Loaded when accessed
```

### Cascade Operations
Configure cascade behavior in entity annotations:
```java
@OneToMany(cascade = CascadeType.ALL)
private Set<Account> accounts;
```

### Fetch Strategies
Use FETCH JOIN to avoid N+1 query problem:
```java
Query<Customer> query = session.createQuery(
    "FROM Customer c LEFT JOIN FETCH c.kycProfile", Customer.class);
```

## Common Patterns

### Creating with Relationships
```java
// Create parent
Customer customer = new Customer("John Doe", "john@example.com");
session.save(customer);

// Create child and associate
Account account = new Account("ACC123", new BigDecimal("1000"));
account.setCustomer(customer);
session.save(account);
```

### Querying with Joins
```java
Query<Object[]> query = session.createQuery(
    "SELECT a.accountNumber, c.fullName " +
    "FROM Account a INNER JOIN a.customer c", Object[].class);
```

### Many-to-Many Management
```java
Account account = session.get(Account.class, 1L);
RefNotificationChannel channel = session.get(RefNotificationChannel.class, 1L);

// Add relationship
account.getSubscriptions().add(channel);
session.update(account);

// Remove relationship
account.getSubscriptions().remove(channel);
session.update(account);
```

## Troubleshooting

### LazyInitializationException
- **Cause**: Accessing lazy-loaded property outside session
- **Solution**: Use FETCH JOIN or access within session

### Sequence Not Found
- **Cause**: Oracle sequences not created
- **Solution**: Run `create_sequences.sql`

### Could not execute statement
- **Cause**: Constraint violation or invalid data
- **Solution**: Check foreign key relationships and required fields

## Learn More

- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [JPA Annotations Guide](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html)
- [HQL Reference](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#hql)
